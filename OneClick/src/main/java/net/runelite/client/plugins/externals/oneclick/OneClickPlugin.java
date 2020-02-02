/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * Copyright (c) 2019, TomC <https://github.com/tomcylke>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.oneclick;

import com.google.common.base.Splitter;
import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.inject.Inject;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.api.Skill;
import net.runelite.api.SpriteID;
import net.runelite.api.Varbits;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.externals.oneclick.Comparables.Blank;
import net.runelite.client.plugins.externals.oneclick.Comparables.ClickComparable;
import org.apache.commons.lang3.tuple.Pair;

@PluginDescriptor(
	name = "One Click",
	description = "OP One Click methods.",
	enabledByDefault = false,
	type = PluginType.EXTERNAL
)
@SuppressWarnings("unused")
@Getter
@Slf4j
public class OneClickPlugin extends Plugin
{
	private static final String MAGIC_IMBUE_EXPIRED_MESSAGE = "Your Magic Imbue charge has ended.";
	private static final String MAGIC_IMBUE_MESSAGE = "You are charged to combine runes!";
	private static final Splitter NEWLINE_SPLITTER = Splitter
		.on("\n")
		.omitEmptyStrings()
		.trimResults();

	@Inject
	private Client client;

	@Inject
	private OneClickConfig config;

	private final Map<Integer, String> targetMap = new HashMap<>();
	private final Map<Integer, List<Integer>> customClickMap = new HashMap<>();

	private ClickComparable comparable = new Blank();
	private ClickItem clickItem;
	private Spells spellSelection = Spells.NONE;
	private Types type = Types.NONE;

	private boolean enableImbue;
	private boolean imbue;
	@Setter
	private boolean tick;

	@Provides
	OneClickConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OneClickConfig.class);
	}

	@Override
	protected void startUp()
	{
		type = config.getType();
		spellSelection = config.getSpells();
		enableImbue = config.isUsingImbue();
		comparable = type.getComparable();
		updateMap();
	}

	@Override
	protected void shutDown()
	{

	}

	@Subscribe
	public void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() == GameState.LOGGED_IN && imbue)
		{
			imbue = false;
		}
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals("oneclick"))
		{
			type = config.getType();
			spellSelection = config.getSpells();
			enableImbue = config.isUsingImbue();
			comparable = type.getComparable();
			updateMap();
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		switch (event.getMessage())
		{
			case MAGIC_IMBUE_MESSAGE:
				imbue = true;
				break;
			case MAGIC_IMBUE_EXPIRED_MESSAGE:
				imbue = false;
				break;
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		tick = false;
	}

	@Subscribe
	public void onMenuOpened(MenuOpened event)
	{
		final MenuEntry firstEntry = event.getFirstEntry();

		if (firstEntry == null)
		{
			return;
		}

		final int widgetId = firstEntry.getParam1();

		if (widgetId == WidgetInfo.INVENTORY.getId() && type == Types.SPELL)
		{

			final Widget spell = client.getWidget(spellSelection.getWidgetInfo());

			if (spell == null)
			{
				return;
			}

			switch (spellSelection)
			{
				case HIGH_ALCH:
					if (spell.getSpriteId() != SpriteID.SPELL_HIGH_LEVEL_ALCHEMY ||
						spell.getSpriteId() == SpriteID.SPELL_HIGH_LEVEL_ALCHEMY_DISABLED ||
						client.getBoostedSkillLevel(Skill.MAGIC) < 55 ||
						client.getVar(Varbits.SPELLBOOK) != 0)
					{
						clickItem = null;
						return;
					}
					break;
				case SUPERHEAT:
					if (spell.getSpriteId() != SpriteID.SPELL_SUPERHEAT_ITEM ||
						spell.getSpriteId() == SpriteID.SPELL_SUPERHEAT_ITEM_DISABLED ||
						client.getBoostedSkillLevel(Skill.MAGIC) < 43 ||
						client.getVar(Varbits.SPELLBOOK) != 0)
					{
						clickItem = null;
						return;
					}
					break;
				case ENCHANT_SAPPHIRE:
					if (spell.getSpriteId() != SpriteID.SPELL_LVL_1_ENCHANT ||
						spell.getSpriteId() == SpriteID.SPELL_LVL_1_ENCHANT_DISABLED ||
						client.getBoostedSkillLevel(Skill.MAGIC) < 7 ||
						client.getVar(Varbits.SPELLBOOK) != 0)
					{
						clickItem = null;
						return;
					}
					break;
				case ENCHANT_EMERALD:
					if (spell.getSpriteId() != SpriteID.SPELL_LVL_2_ENCHANT ||
						spell.getSpriteId() == SpriteID.SPELL_LVL_2_ENCHANT_DISABLED ||
						client.getBoostedSkillLevel(Skill.MAGIC) < 27 ||
						client.getVar(Varbits.SPELLBOOK) != 0)
					{
						clickItem = null;
						return;
					}
					break;
				case ENCHANT_RUBY:
					if (spell.getSpriteId() != SpriteID.SPELL_LVL_3_ENCHANT ||
						spell.getSpriteId() == SpriteID.SPELL_LVL_3_ENCHANT_DISABLED ||
						client.getBoostedSkillLevel(Skill.MAGIC) < 49 ||
						client.getVar(Varbits.SPELLBOOK) != 0)
					{
						clickItem = null;
						return;
					}
					break;
				case ENCHANT_DIAMOND:
					if (spell.getSpriteId() != SpriteID.SPELL_LVL_4_ENCHANT ||
						spell.getSpriteId() == SpriteID.SPELL_LVL_4_ENCHANT_DISABLED ||
						client.getBoostedSkillLevel(Skill.MAGIC) < 57 ||
						client.getVar(Varbits.SPELLBOOK) != 0)
					{
						clickItem = null;
						return;
					}
					break;
				case ENCHANT_DRAGONSTONE:
					if (spell.getSpriteId() != SpriteID.SPELL_LVL_5_ENCHANT ||
						spell.getSpriteId() == SpriteID.SPELL_LVL_5_ENCHANT_DISABLED ||
						client.getBoostedSkillLevel(Skill.MAGIC) < 68 ||
						client.getVar(Varbits.SPELLBOOK) != 0)
					{
						clickItem = null;
						return;
					}
					break;
				case ENCHANT_ONYX:
					if (spell.getSpriteId() != SpriteID.SPELL_LVL_6_ENCHANT ||
						spell.getSpriteId() == SpriteID.SPELL_LVL_6_ENCHANT_DISABLED ||
						client.getBoostedSkillLevel(Skill.MAGIC) < 87 ||
						client.getVar(Varbits.SPELLBOOK) != 0)
					{
						clickItem = null;
						return;
					}
					break;
				case ENCHANT_ZENYTE:
					if (spell.getSpriteId() != SpriteID.SPELL_LVL_7_ENCHANT ||
						spell.getSpriteId() == SpriteID.SPELL_LVL_7_ENCHANT_DISABLED ||
						client.getBoostedSkillLevel(Skill.MAGIC) < 93 ||
						client.getVar(Varbits.SPELLBOOK) != 0)
					{
						clickItem = null;
						return;
					}
					break;
				default:
					clickItem = null;
					break;

			}

			final int itemId = firstEntry.getIdentifier();

			if (itemId == -1)
			{
				return;
			}

			final MenuEntry[] menuList = new MenuEntry[event.getMenuEntries().length + 1];

			for (int i = event.getMenuEntries().length - 1; i >= 0; i--)
			{
				if (i == 0)
				{
					menuList[i] = event.getMenuEntries()[i];
				}
				else
				{
					menuList[i + 1] = event.getMenuEntries()[i];
				}
			}

			final MenuEntry setTargetItem = new MenuEntry();
			final boolean set = clickItem != null && clickItem.getId() == firstEntry.getIdentifier();
			setTargetItem.setOption(set ? "Unset" : "Set");

			switch (spellSelection)
			{
				case HIGH_ALCH:
					setTargetItem.setTarget("<col=00ff00>High Alchemy Item <col=ffffff> -> " + firstEntry.getTarget());
					break;
				case SUPERHEAT:
					setTargetItem.setTarget("<col=00ff00>Superheat Item <col=ffffff> -> " + firstEntry.getTarget());
					break;
				case ENCHANT_SAPPHIRE:
					setTargetItem.setTarget("<col=00ff00>Lvl-1 Enchant <col=ffffff> -> " + firstEntry.getTarget());
					break;
				case ENCHANT_EMERALD:
					setTargetItem.setTarget("<col=00ff00>Lvl-2 Enchant <col=ffffff> -> " + firstEntry.getTarget());
					break;
				case ENCHANT_RUBY:
					setTargetItem.setTarget("<col=00ff00>Lvl-3 Enchant <col=ffffff> -> " + firstEntry.getTarget());
					break;
				case ENCHANT_DIAMOND:
					setTargetItem.setTarget("<col=00ff00>Lvl-4 Enchant <col=ffffff> -> " + firstEntry.getTarget());
					break;
				case ENCHANT_DRAGONSTONE:
					setTargetItem.setTarget("<col=00ff00>Lvl-5 Enchant <col=ffffff> -> " + firstEntry.getTarget());
					break;
				case ENCHANT_ONYX:
					setTargetItem.setTarget("<col=00ff00>Lvl-6 Enchant <col=ffffff> -> " + firstEntry.getTarget());
					break;
				case ENCHANT_ZENYTE:
					setTargetItem.setTarget("<col=00ff00>Lvl-7 Enchant <col=ffffff> -> " + firstEntry.getTarget());
					break;
			}

			setTargetItem.setIdentifier(set ? -1 : firstEntry.getIdentifier());
			setTargetItem.setOpcode(MenuOpcode.RUNELITE.getId());
			setTargetItem.setParam1(widgetId);
			setTargetItem.setForceLeftClick(false);
			menuList[1] = setTargetItem;
			event.setMenuEntries(menuList);
			event.setModified();
		}
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		final int id = event.getIdentifier();
		targetMap.put(id, event.getTarget());

		if (config.customInvSwap() && customClickMap.getOrDefault(id, null) != null)
		{
			if (event.getOpcode() == MenuOpcode.ITEM_USE.getId() && customClickMap.containsKey(id))
			{
				int item = findItem(customClickMap.get(id)).getLeft();
				if (item == -1)
				{
					return;
				}
				final String name = client.getItemDefinition(item).getName();
				event.setTarget("<col=ff9040>" + name + "<col=ffffff> -> " + targetMap.get(id));
				event.setForceLeftClick(true);
				event.setModified();
				return;
			}
		}

		/*Todo: abstract spells.*/
		if (type == Types.SPELL)
		{
			switch (spellSelection)
			{
				case HIGH_ALCH:
					if (event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
						clickItem != null &&
						event.getOption().equals("Cast") &&
						event.getTarget().equals("<col=00ff00>High Level Alchemy</col>")
					)
					{
						event.setOption("Cast");
						event.setTarget("<col=00ff00>High Level Alchemy</col><col=ffffff> -> " + clickItem.getName());
						event.setForceLeftClick(true);
						event.setModified();
					}
					break;
				case SUPERHEAT:
					if (event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
						clickItem != null &&
						event.getOption().equals("Cast") &&
						event.getTarget().equals("<col=00ff00>Superheat Item</col>")
					)
					{
						event.setOption("Cast");
						event.setTarget("<col=00ff00>Superheat Item</col><col=ffffff> -> " + clickItem.getName());
						event.setForceLeftClick(true);
						event.setModified();
					}
					break;
				case ENCHANT_SAPPHIRE:
					if (event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
						clickItem != null &&
						event.getOption().equals("Cast") &&
						event.getTarget().equals("<col=00ff00>Lvl-1 Enchant</col>")
					)
					{
						event.setOption("Cast");
						event.setTarget("<col=00ff00>Lvl-1 Enchant</col><col=ffffff> -> " + clickItem.getName());
						event.setForceLeftClick(true);
						event.setModified();
					}
					break;
				case ENCHANT_EMERALD:
					if (event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
						clickItem != null &&
						event.getOption().equals("Cast") &&
						event.getTarget().equals("<col=00ff00>Lvl-2 Enchant</col>")
					)
					{
						event.setOption("Cast");
						event.setTarget("<col=00ff00>Lvl-2 Enchant</col><col=ffffff> -> " + clickItem.getName());
						event.setForceLeftClick(true);
						event.setModified();
					}
					break;
				case ENCHANT_RUBY:
					if (event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
						clickItem != null &&
						event.getOption().equals("Cast") &&
						event.getTarget().equals("<col=00ff00>Lvl-3 Enchant</col>")
					)
					{
						event.setOption("Cast");
						event.setTarget("<col=00ff00>Lvl-3 Enchant</col><col=ffffff> -> " + clickItem.getName());
						event.setForceLeftClick(true);
						event.setModified();
					}
					break;
				case ENCHANT_DIAMOND:
					if (event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
						clickItem != null &&
						event.getOption().equals("Cast") &&
						event.getTarget().equals("<col=00ff00>Lvl-4 Enchant</col>")
					)
					{
						event.setOption("Cast");
						event.setTarget("<col=00ff00>Lvl-4 Enchant</col><col=ffffff> -> " + clickItem.getName());
						event.setForceLeftClick(true);
						event.setModified();
					}
					break;
				case ENCHANT_DRAGONSTONE:
					if (event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
						clickItem != null &&
						event.getOption().equals("Cast") &&
						event.getTarget().equals("<col=00ff00>Lvl-5 Enchant</col>")
					)
					{
						event.setOption("Cast");
						event.setTarget("<col=00ff00>Lvl-5 Enchant</col><col=ffffff> -> " + clickItem.getName());
						event.setForceLeftClick(true);
						event.setModified();
					}
					break;
				case ENCHANT_ONYX:
					if (event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
						clickItem != null &&
						event.getOption().equals("Cast") &&
						event.getTarget().equals("<col=00ff00>Lvl-6 Enchant</col>")
					)
					{
						event.setOption("Cast");
						event.setTarget("<col=00ff00>Lvl-6 Enchant</col><col=ffffff> -> " + clickItem.getName());
						event.setForceLeftClick(true);
						event.setModified();
					}
					break;
				case ENCHANT_ZENYTE:
					if (event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
						clickItem != null &&
						event.getOption().equals("Cast") &&
						event.getTarget().equals("<col=00ff00>Lvl-7 Enchant</col>")
					)
					{
						event.setOption("Cast");
						event.setTarget("<col=00ff00>Lvl-7 Enchant</col><col=ffffff> -> " + clickItem.getName());
						event.setForceLeftClick(true);
						event.setModified();
					}
					break;
			}
		}
		else
		{
			if (comparable == null)
			{
				log.error("This should not be possible.");
				throw new AssertionError();
			}

			if (comparable.isEntryValid(event))
			{
				comparable.modifyEntry(this, event);
				event.setModified();
			}
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (tick)
		{
			event.consume();
			return;
		}

		if (event.getTarget() == null)
		{
			return;
		}

		if (config.customInvSwap() &&
			event.getOpcode() == MenuOpcode.ITEM_USE.getId() &&
			customClickMap.containsKey(event.getIdentifier()) &&
			updateSelectedItem(customClickMap.get(event.getIdentifier()))
		)
		{
			event.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET_ITEM.getId());
			return;
		}

		/*Todo: abstract spells.*/
		if (type == Types.SPELL)
		{
			switch (spellSelection)
			{
				case HIGH_ALCH:
					if (event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
						event.getOption().equals("Cast") &&
						event.getTarget().contains("<col=00ff00>High Level Alchemy</col><col=ffffff> -> ")
					)
					{
						final Pair<Integer, Integer> pair = findItem(clickItem.getId());

						if (pair.getLeft() != -1)
						{
							event.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET.getId());
							event.setIdentifier(pair.getLeft());
							event.setParam0(pair.getRight());
							event.setParam1(WidgetInfo.INVENTORY.getId());
							client.setSelectedSpellName("<col=00ff00>High Level Alchemy</col><col=ffffff>");
							client.setSelectedSpellWidget(WidgetInfo.SPELL_HIGH_LEVEL_ALCHEMY.getId());
						}
					}
					else if (event.getOpcode() == MenuOpcode.RUNELITE.getId() && event.getIdentifier() == -1)
					{
						clickItem = null;
					}
					else if (spellSelection == Spells.HIGH_ALCH && event.getOpcode() == MenuOpcode.RUNELITE.getId())
					{
						final String itemName = event.getTarget().split("<col=00ff00>High Alchemy Item <col=ffffff> -> ")[1];
						clickItem = new ClickItem(itemName, event.getIdentifier());
					}
					break;
				case SUPERHEAT:
					if (event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
						event.getOption().equals("Cast") &&
						event.getTarget().contains("<col=00ff00>Superheat Item</col><col=ffffff> -> ")
					)
					{
						final Pair<Integer, Integer> pair = findItem(clickItem.getId());
						if (pair.getLeft() != -1)
						{
							event.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET.getId());
							event.setIdentifier(pair.getLeft());
							event.setParam0(pair.getRight());
							event.setParam1(WidgetInfo.INVENTORY.getId());
							client.setSelectedSpellName("<col=00ff00>Superheat Item</col><col=ffffff>");
							client.setSelectedSpellWidget(WidgetInfo.SPELL_SUPERHEAT_ITEM.getId());
						}
					}
					else if (event.getOpcode() == MenuOpcode.RUNELITE.getId() && event.getIdentifier() == -1)
					{
						clickItem = null;
					}
					else if (spellSelection == Spells.SUPERHEAT && event.getOpcode() == MenuOpcode.RUNELITE.getId())
					{
						final String itemName = event.getTarget().split("<col=00ff00>Superheat Item <col=ffffff> -> ")[1];
						clickItem = new ClickItem(itemName, event.getIdentifier());
					}
					break;
				case ENCHANT_SAPPHIRE:
					if (event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
						event.getOption().equals("Cast") &&
						event.getTarget().contains("<col=00ff00>Lvl-1 Enchant</col><col=ffffff> -> ")
					)
					{
						final Pair<Integer, Integer> pair = findItem(clickItem.getId());
						if (pair.getLeft() != -1)
						{
							event.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET.getId());
							event.setIdentifier(pair.getLeft());
							event.setParam0(pair.getRight());
							event.setParam1(WidgetInfo.INVENTORY.getId());
							client.setSelectedSpellName("<col=00ff00>Lvl-1 Enchant</col><col=ffffff>");
							client.setSelectedSpellWidget(WidgetInfo.SPELL_LVL_1_ENCHANT.getId());
						}
					}
					else if (event.getOpcode() == MenuOpcode.RUNELITE.getId() &&
						event.getIdentifier() == -1
					)
					{
						clickItem = null;
					}
					else if (spellSelection == Spells.ENCHANT_SAPPHIRE &&
						event.getOpcode() == MenuOpcode.RUNELITE.getId()
					)
					{
						final String itemName = event.getTarget().split("<col=00ff00>Lvl-1 Enchant <col=ffffff> -> ")[1];
						clickItem = new ClickItem(itemName, event.getIdentifier());
					}
					break;
				case ENCHANT_RUBY:
					if (event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
						event.getOption().equals("Cast") &&
						event.getTarget().contains("<col=00ff00>Lvl-2 Enchant</col><col=ffffff> -> ")
					)
					{
						final Pair<Integer, Integer> pair = findItem(clickItem.getId());
						if (pair.getLeft() != -1)
						{
							event.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET.getId());
							event.setIdentifier(pair.getLeft());
							event.setParam0(pair.getRight());
							event.setParam1(WidgetInfo.INVENTORY.getId());
							client.setSelectedSpellName("<col=00ff00>Lvl-2 Enchant</col><col=ffffff>");
							client.setSelectedSpellWidget(WidgetInfo.SPELL_LVL_2_ENCHANT.getId());
						}
					}
					else if (event.getOpcode() == MenuOpcode.RUNELITE.getId() &&
						event.getIdentifier() == -1
					)
					{
						clickItem = null;
					}
					else if (spellSelection == Spells.ENCHANT_RUBY &&
						event.getOpcode() == MenuOpcode.RUNELITE.getId()
					)
					{
						final String itemName = event.getTarget().split("<col=00ff00>Lvl-2 Enchant <col=ffffff> -> ")[1];
						clickItem = new ClickItem(itemName, event.getIdentifier());
					}
					break;
				case ENCHANT_EMERALD:
					if (event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
						event.getOption().equals("Cast") &&
						event.getTarget().contains("<col=00ff00>Lvl-3 Enchant</col><col=ffffff> -> ")
					)
					{
						final Pair<Integer, Integer> pair = findItem(clickItem.getId());
						if (pair.getLeft() != -1)
						{
							event.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET.getId());
							event.setIdentifier(pair.getLeft());
							event.setParam0(pair.getRight());
							event.setParam1(WidgetInfo.INVENTORY.getId());
							client.setSelectedSpellName("<col=00ff00>Lvl-3 Enchant</col><col=ffffff>");
							client.setSelectedSpellWidget(WidgetInfo.SPELL_LVL_3_ENCHANT.getId());
						}
					}
					else if (event.getOpcode() == MenuOpcode.RUNELITE.getId() &&
						event.getIdentifier() == -1
					)
					{
						clickItem = null;
					}
					else if (spellSelection == Spells.ENCHANT_EMERALD &&
						event.getOpcode() == MenuOpcode.RUNELITE.getId()
					)
					{
						final String itemName = event.getTarget().split("<col=00ff00>Lvl-3 Enchant <col=ffffff> -> ")[1];
						clickItem = new ClickItem(itemName, event.getIdentifier());
					}
					break;
				case ENCHANT_DIAMOND:
					if (event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
						event.getOption().equals("Cast") &&
						event.getTarget().contains("<col=00ff00>Lvl-4 Enchant</col><col=ffffff> -> ")
					)
					{
						final Pair<Integer, Integer> pair = findItem(clickItem.getId());
						if (pair.getLeft() != -1)
						{
							event.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET.getId());
							event.setIdentifier(pair.getLeft());
							event.setParam0(pair.getRight());
							event.setParam1(WidgetInfo.INVENTORY.getId());
							client.setSelectedSpellName("<col=00ff00>Lvl-4 Enchant</col><col=ffffff>");
							client.setSelectedSpellWidget(WidgetInfo.SPELL_LVL_4_ENCHANT.getId());
						}
					}
					else if (event.getOpcode() == MenuOpcode.RUNELITE.getId() &&
						event.getIdentifier() == -1
					)
					{
						clickItem = null;
					}
					else if (spellSelection == Spells.ENCHANT_DIAMOND &&
						event.getOpcode() == MenuOpcode.RUNELITE.getId()
					)
					{
						final String itemName = event.getTarget().split("<col=00ff00>Lvl-4 Enchant <col=ffffff> -> ")[1];
						clickItem = new ClickItem(itemName, event.getIdentifier());
					}
					break;
				case ENCHANT_DRAGONSTONE:
					if (event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
						event.getOption().equals("Cast") &&
						event.getTarget().contains("<col=00ff00>Lvl-5 Enchant</col><col=ffffff> -> ")
					)
					{
						final Pair<Integer, Integer> pair = findItem(clickItem.getId());
						if (pair.getLeft() != -1)
						{
							event.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET.getId());
							event.setIdentifier(pair.getLeft());
							event.setParam0(pair.getRight());
							event.setParam1(WidgetInfo.INVENTORY.getId());
							client.setSelectedSpellName("<col=00ff00>Lvl-5 Enchant</col><col=ffffff>");
							client.setSelectedSpellWidget(WidgetInfo.SPELL_LVL_5_ENCHANT.getId());
						}
					}
					else if (event.getOpcode() == MenuOpcode.RUNELITE.getId() &&
						event.getIdentifier() == -1
					)
					{
						clickItem = null;
					}
					else if (spellSelection == Spells.ENCHANT_DRAGONSTONE &&
						event.getOpcode() == MenuOpcode.RUNELITE.getId()
					)
					{
						final String itemName = event.getTarget().split("<col=00ff00>Lvl-5 Enchant <col=ffffff> -> ")[1];
						clickItem = new ClickItem(itemName, event.getIdentifier());
					}
					break;
				case ENCHANT_ONYX:
					if (event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
						event.getOption().equals("Cast") &&
						event.getTarget().contains("<col=00ff00>Lvl-6 Enchant</col><col=ffffff> -> ")
					)
					{
						final Pair<Integer, Integer> pair = findItem(clickItem.getId());
						if (pair.getLeft() != -1)
						{
							event.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET.getId());
							event.setIdentifier(pair.getLeft());
							event.setParam0(pair.getRight());
							event.setParam1(WidgetInfo.INVENTORY.getId());
							client.setSelectedSpellName("<col=00ff00>Lvl-6 Enchant</col><col=ffffff>");
							client.setSelectedSpellWidget(WidgetInfo.SPELL_LVL_6_ENCHANT.getId());
						}
					}
					else if (event.getOpcode() == MenuOpcode.RUNELITE.getId() &&
						event.getIdentifier() == -1
					)
					{
						clickItem = null;
					}
					else if (spellSelection == Spells.ENCHANT_ONYX &&
						event.getOpcode() == MenuOpcode.RUNELITE.getId()
					)
					{
						final String itemName = event.getTarget().split("<col=00ff00>Lvl-6 Enchant <col=ffffff> -> ")[1];
						clickItem = new ClickItem(itemName, event.getIdentifier());
					}
					break;
				case ENCHANT_ZENYTE:
					if (event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() &&
						event.getOption().equals("Cast") &&
						event.getTarget().contains("<col=00ff00>Lvl-7 Enchant</col><col=ffffff> -> ")
					)
					{
						final Pair<Integer, Integer> pair = findItem(clickItem.getId());
						if (pair.getLeft() != -1)
						{
							event.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET.getId());
							event.setIdentifier(pair.getLeft());
							event.setParam0(pair.getRight());
							event.setParam1(WidgetInfo.INVENTORY.getId());
							client.setSelectedSpellName("<col=00ff00>Lvl-7 Enchant</col><col=ffffff>");
							client.setSelectedSpellWidget(WidgetInfo.SPELL_LVL_7_ENCHANT.getId());
						}
					}
					else if (event.getOpcode() == MenuOpcode.RUNELITE.getId() &&
						event.getIdentifier() == -1
					)
					{
						clickItem = null;
					}
					else if (spellSelection == Spells.ENCHANT_ZENYTE &&
						event.getOpcode() == MenuOpcode.RUNELITE.getId()
					)
					{
						final String itemName = event.getTarget().split("<col=00ff00>Lvl-7 Enchant <col=ffffff> -> ")[1];
						clickItem = new ClickItem(itemName, event.getIdentifier());
					}
					break;
			}
		}
		else
		{
			// Hacky solution for mixin changes.

			if (comparable == null)
			{
				log.error("This should not be possible.");
				throw new AssertionError();
			}

			if (comparable.isEntryValid(event))
			{
				comparable.modifyEntry(this, event);

				if (comparable.isClickValid(event))
				{
					comparable.modifyClick(this, event);
				}
			}
		}
	}

	public boolean updateSelectedItem(int id)
	{
		final Pair<Integer, Integer> pair = findItem(id);
		if (pair.getLeft() != -1)
		{
			client.setSelectedItemWidget(WidgetInfo.INVENTORY.getId());
			client.setSelectedItemSlot(pair.getRight());
			client.setSelectedItemID(pair.getLeft());
			return true;
		}
		return false;
	}

	public boolean updateSelectedItem(Collection<Integer> ids)
	{
		final Pair<Integer, Integer> pair = findItem(ids);
		if (pair.getLeft() != -1)
		{
			client.setSelectedItemWidget(WidgetInfo.INVENTORY.getId());
			client.setSelectedItemSlot(pair.getRight());
			client.setSelectedItemID(pair.getLeft());
			return true;
		}
		return false;
	}

	public Pair<Integer, Integer> findItem(int id)
	{
		final Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		final List<WidgetItem> itemList = (List<WidgetItem>) inventoryWidget.getWidgetItems();

		for (int i = itemList.size() - 1; i >= 0; i--)
		{
			final WidgetItem item = itemList.get(i);
			if (item.getId() == id)
			{
				return Pair.of(item.getId(), item.getIndex());
			}
		}

		return Pair.of(-1, -1);
	}

	public Pair<Integer, Integer> findItem(Collection<Integer> ids)
	{
		final Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		final List<WidgetItem> itemList = (List<WidgetItem>) inventoryWidget.getWidgetItems();

		for (int i = itemList.size() - 1; i >= 0; i--)
		{
			final WidgetItem item = itemList.get(i);
			if (ids.contains(item.getId()))
			{
				return Pair.of(item.getId(), item.getIndex());
			}
		}

		return Pair.of(-1, -1);
	}

	private void updateMap()
	{
		final Iterable<String> tmp = NEWLINE_SPLITTER.split(config.swaps());

		for (String s : tmp)
		{
			if (s.startsWith("//"))
			{
				continue;
			}

			String[] split = s.split(":");

			try
			{
				int oneClickThat = Integer.parseInt(split[0]);
				int withThis = Integer.parseInt(split[1]);
				if (customClickMap.containsKey(oneClickThat))
				{
					customClickMap.get(oneClickThat).add(withThis);
					continue;
				}
				customClickMap.put(oneClickThat, new ArrayList<>(withThis));
			}
			catch (Exception e)
			{
				return;
			}
		}
	}
}
