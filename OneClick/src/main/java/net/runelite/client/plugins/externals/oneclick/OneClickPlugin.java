/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
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
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import static net.runelite.api.MenuOpcode.MENU_ACTION_DEPRIORITIZE_OFFSET;
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
import org.pf4j.Extension;

@Extension
@PluginDescriptor(
	name = "One Click",
	description = "OP One Click methods.",
	type = PluginType.UTILITY
)
@SuppressWarnings("unused")
@Getter
@Setter
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
	@Setter(AccessLevel.NONE)
	private Client client;

	@Inject
	@Setter(AccessLevel.NONE)
	private OneClickConfig config;

	private final Map<Integer, String> targetMap = new HashMap<>();
	private final Map<Integer, List<Integer>> customClickMap = new HashMap<>();

	private ClickComparable comparable = new Blank();
	private ClickItem clickItem = null;
	private Spells spellSelection = Spells.NONE;
	private Types type = Types.NONE;
	private String roleText = "";

	private boolean enableImbue;
	private boolean imbue;
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
		updateMap();
		if (type == Types.SPELL)
		{
			comparable = spellSelection.getComparable();
		}
		else
		{
			comparable = type.getComparable();
		}
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
			updateMap();
			if (type == Types.SPELL)
			{
				comparable = spellSelection.getComparable();
			}
			else
			{
				comparable = type.getComparable();
			}
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

		if (type == Types.BA_HEALER)
		{
			Widget widget = client.getWidget(WidgetInfo.BA_HEAL_LISTEN_TEXT);

			if (widget != null && widget.getText() != null)
			{
				roleText = widget.getText().trim();
			}
			else
			{
				roleText = "";
			}
		}
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

		switch (type)
		{
			case SEED_SET:
			case BA_HEALER:
				if (event.getOpcode() == MenuOpcode.WALK.getId())
				{
					MenuEntry menuEntry = client.getLeftClickMenuEntry();
					menuEntry.setOpcode(MenuOpcode.WALK.getId() + MENU_ACTION_DEPRIORITIZE_OFFSET);
					client.setLeftClickMenuEntry(menuEntry);
				}
				break;
			default:
				break;
		}

		if (comparable == null)
		{
			log.error("This should not be possible.");
			throw new AssertionError();
		}

		if (type == Types.SPELL && clickItem == null)
		{
			return;
		}

		if (comparable.isEntryValid(event))
		{
			comparable.modifyEntry(this, event);
			event.setModified();
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

		if (comparable == null)
		{
			log.error("This should not be possible.");
			throw new AssertionError();
		}

		if (comparable.isEntryValid(event))
		{
			comparable.modifyEntry(this, event);
		}

		if (comparable.isClickValid(event))
		{
			comparable.modifyClick(this, event);
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
