/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * Copyright (c) 2019, TomC <https://github.com/tomcylke>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.oneclick;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.AnimationID;
import net.runelite.api.Client;
import net.runelite.api.DynamicObject;
import net.runelite.api.Entity;
import net.runelite.api.GameObject;
import net.runelite.api.GameState;
import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import static net.runelite.api.MenuOpcode.MENU_ACTION_DEPRIORITIZE_OFFSET;
import static net.runelite.api.ObjectID.DWARF_MULTICANNON;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.SpriteID;
import net.runelite.api.Varbits;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import org.apache.commons.lang3.tuple.Pair;

@PluginDescriptor(
	name = "One Click",
	description = "OP One Click methods.",
	enabledByDefault = false,
	type = PluginType.EXTERNAL
)
@SuppressWarnings("unused")
public class OneClickPlugin extends Plugin
{
	private static final Set<Integer> BOLTS = ImmutableSet.of(
		ItemID.BRONZE_BOLTS_UNF, ItemID.IRON_BOLTS_UNF, ItemID.STEEL_BOLTS_UNF,
		ItemID.MITHRIL_BOLTS_UNF, ItemID.ADAMANT_BOLTSUNF, ItemID.RUNITE_BOLTS_UNF,
		ItemID.DRAGON_BOLTS_UNF, ItemID.UNFINISHED_BROAD_BOLTS
	);
	private static final Set<Integer> CANNONBALLS = ImmutableSet.of(
		ItemID.CANNONBALL, ItemID.GRANITE_CANNONBALL
	);
	private static final Set<Integer> DART_TIPS = ImmutableSet.of(
		ItemID.BRONZE_DART_TIP, ItemID.IRON_DART_TIP, ItemID.STEEL_DART_TIP,
		ItemID.MITHRIL_DART_TIP, ItemID.ADAMANT_DART_TIP, ItemID.RUNE_DART_TIP,
		ItemID.DRAGON_DART_TIP
	);
	private static final Set<Integer> LOG_ID = ImmutableSet.of(
		ItemID.LOGS, ItemID.OAK_LOGS, ItemID.WILLOW_LOGS, ItemID.TEAK_LOGS,
		ItemID.MAPLE_LOGS, ItemID.MAHOGANY_LOGS, ItemID.YEW_LOGS, ItemID.MAGIC_LOGS,
		ItemID.REDWOOD_LOGS
	);
	private static final Set<Integer> HOPS_SEED = ImmutableSet.of(
		ItemID.BARLEY_SEED, ItemID.HAMMERSTONE_SEED, ItemID.ASGARNIAN_SEED,
		ItemID.JUTE_SEED, ItemID.YANILLIAN_SEED, ItemID.KRANDORIAN_SEED, ItemID.WILDBLOOD_SEED
	);
	private static final Set<Integer> HERBS = ImmutableSet.of(
		ItemID.GUAM_LEAF, ItemID.MARRENTILL, ItemID.TARROMIN, ItemID.HARRALANDER
	);
	private static final Set<Integer> BONE_SET = ImmutableSet.of(
		ItemID.BONES, ItemID.WOLF_BONE, ItemID.BURNT_BONES, ItemID.MONKEY_BONES, ItemID.BAT_BONES,
		ItemID.JOGRE_BONE, ItemID.BIG_BONES, ItemID.ZOGRE_BONE, ItemID.SHAIKAHAN_BONES, ItemID.BABYDRAGON_BONES,
		ItemID.WYRM_BONES, ItemID.DRAGON_BONES, ItemID.DRAKE_BONES, ItemID.FAYRG_BONES, ItemID.LAVA_DRAGON_BONES,
		ItemID.RAURG_BONES, ItemID.HYDRA_BONES, ItemID.DAGANNOTH_BONES, ItemID.OURG_BONES, ItemID.SUPERIOR_DRAGON_BONES,
		ItemID.WYVERN_BONES
	);
	private static final Set<Integer> SEED_SET = ImmutableSet.of(
		ItemID.GOLOVANOVA_SEED, ItemID.BOLOGANO_SEED, ItemID.LOGAVANO_SEED
	);
	private static final Set<Integer> WATERING_CANS = ImmutableSet.of(
		ItemID.WATERING_CAN, ItemID.WATERING_CAN1, ItemID.WATERING_CAN2, ItemID.WATERING_CAN3, ItemID.WATERING_CAN4,
		ItemID.WATERING_CAN5, ItemID.WATERING_CAN6, ItemID.WATERING_CAN7, ItemID.GRICOLLERS_CAN
	);
	private static final Set<String> BIRD_HOUSES_NAMES = ImmutableSet.of(
		"<col=ffff>Bird house (empty)", "<col=ffff>Oak birdhouse (empty)", "<col=ffff>Willow birdhouse (empty)",
		"<col=ffff>Teak birdhouse (empty)", "<col=ffff>Maple birdhouse (empty)", "<col=ffff>Mahogany birdhouse (empty)",
		"<col=ffff>Yew birdhouse (empty)", "<col=ffff>Magic birdhouse (empty)", "<col=ffff>Redwood birdhouse (empty)"
	);
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
	@Inject
	private EventBus eventBus;

	private final Map<Integer, String> targetMap = new HashMap<>();
	private final Map<Integer, List<Integer>> customClickMap = new HashMap<>();

	private AlchItem alchItem;
	private GameObject cannon;
	private Types type = Types.NONE;
	private boolean cannonFiring;
	private boolean enableImbue;
	private boolean imbue;
	private boolean tick;
	private int prevCannonAnimation = 514;

	@Provides
	OneClickConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OneClickConfig.class);
	}

	@Override
	protected void startUp()
	{
		type = config.getType();
		enableImbue = config.isUsingImbue();
		updateMap();
	}

	@Override
	protected void shutDown()
	{
		eventBus.unregister(this);
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
			enableImbue = config.isUsingImbue();
			updateMap();
		}
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		switch (event.getMessage())
		{
			case "You pick up the cannon. It's really heavy.":
				cannonFiring = false;
				cannon = null;
				break;
			case MAGIC_IMBUE_MESSAGE:
				imbue = true;
				break;
			case MAGIC_IMBUE_EXPIRED_MESSAGE:
				imbue = false;
				break;
		}
	}

	@Subscribe
	public void onGameObjectSpawned(GameObjectSpawned event)
	{
		final GameObject gameObject = event.getGameObject();
		final Player localPlayer = client.getLocalPlayer();
		if (gameObject.getId() == DWARF_MULTICANNON && cannon == null && localPlayer != null &&
			localPlayer.getWorldLocation().distanceTo(gameObject.getWorldLocation()) <= 2 &&
			localPlayer.getAnimation() == AnimationID.BURYING_BONES)
		{
			cannon = gameObject;
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (cannon != null)
		{
			final Entity entity = cannon.getEntity();
			if (entity instanceof DynamicObject)
			{
				final int anim = ((DynamicObject) entity).getAnimationID();
				if (anim == 514 && prevCannonAnimation == 514)
				{
					cannonFiring = false;
				}
				else if (anim != prevCannonAnimation)
				{
					cannonFiring = true;
				}
				prevCannonAnimation = ((DynamicObject) entity).getAnimationID();
			}
		}
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

		if (widgetId == WidgetInfo.INVENTORY.getId() && type == Types.HIGH_ALCH)
		{
			final Widget spell = client.getWidget(WidgetInfo.SPELL_HIGH_LEVEL_ALCHEMY);

			if (spell == null)
			{
				return;
			}

			if (spell.getSpriteId() != SpriteID.SPELL_HIGH_LEVEL_ALCHEMY ||
				spell.getSpriteId() == SpriteID.SPELL_HIGH_LEVEL_ALCHEMY_DISABLED ||
				client.getBoostedSkillLevel(Skill.MAGIC) < 55 ||
				client.getVar(Varbits.SPELLBOOK) != 0)
			{
				alchItem = null;
				return;
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

			final MenuEntry setHighAlchItem = new MenuEntry();
			final boolean set = alchItem != null && alchItem.getId() == firstEntry.getIdentifier();
			setHighAlchItem.setOption(set ? "Unset" : "Set");
			setHighAlchItem.setTarget("<col=00ff00>High Alchemy Item <col=ffffff> -> " + firstEntry.getTarget());
			setHighAlchItem.setIdentifier(set ? -1 : firstEntry.getIdentifier());
			setHighAlchItem.setOpcode(MenuOpcode.RUNELITE.getId());
			setHighAlchItem.setParam1(widgetId);
			setHighAlchItem.setForceLeftClick(false);
			menuList[1] = setHighAlchItem;
			event.setMenuEntries(menuList);
			event.setModified();
		}
	}


	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		final int id = event.getIdentifier();
		final int opcode = event.getOpcode();
		targetMap.put(id, event.getTarget());

		if (config.customInvSwap() && customClickMap.getOrDefault(id, null) != null)
		{
			if (opcode == MenuOpcode.ITEM_USE.getId() && customClickMap.containsKey(id))
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
			case COMPOST:
			{
				if (opcode == MenuOpcode.ITEM_USE.getId() && id == ItemID.COMPOST)
				{
					if (findItem(ItemID.COMPOST).getLeft() == -1)
					{
						return;
					}
					event.setTarget("<col=ff9040>Saltpetre<col=ffffff> -> " + targetMap.get(id));
					event.setForceLeftClick(true);
					event.setModified();
				}
			}
			break;
			case BRUMA_ROOT:
			{
				if (opcode == MenuOpcode.ITEM_USE.getId() && id == ItemID.BRUMA_ROOT)
				{
					if (findItem(ItemID.BRUMA_ROOT).getLeft() == -1)
					{
						return;
					}
					event.setTarget("<col=ff9040>Knife<col=ffffff> -> " + targetMap.get(id));
					event.setForceLeftClick(true);
					event.setModified();
				}
			}
			break;
			case DARTS:
				if (opcode == MenuOpcode.ITEM_USE.getId() && (DART_TIPS.contains(id) || BOLTS.contains(id)))
				{
					if (findItem(ItemID.FEATHER).getLeft() == -1)
					{
						return;
					}
					event.setTarget("<col=ff9040>Feather<col=ffffff> -> " + targetMap.get(id));
					event.setForceLeftClick(true);
					event.setModified();
				}
				break;
			case FIREMAKING:
				if (opcode == MenuOpcode.ITEM_USE.getId() && LOG_ID.contains(id))
				{
					if (findItem(ItemID.TINDERBOX).getLeft() == -1)
					{
						return;
					}
					event.setTarget("<col=ff9040>Tinderbox<col=ffffff> -> " + targetMap.get(id));
					event.setForceLeftClick(true);
					event.setModified();
				}
				break;
			case BIRDHOUSES:
				if (opcode == MenuOpcode.GAME_OBJECT_SECOND_OPTION.getId() && BIRD_HOUSES_NAMES.contains(event.getTarget()))
				{
					if (findItem(HOPS_SEED).getLeft() == -1)
					{
						return;
					}
					event.setOption("Use");
					event.setTarget("<col=ff9040>Hops seed<col=ffffff> -> " + targetMap.get(id));
					event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
					event.setForceLeftClick(true);
					event.setModified();
				}
				break;
			case HERB_TAR:
				if (opcode == MenuOpcode.ITEM_USE.getId() && HERBS.contains(id))
				{
					if (findItem(ItemID.SWAMP_TAR).getLeft() == -1 || findItem(ItemID.PESTLE_AND_MORTAR).getLeft() == -1)
					{
						return;
					}
					event.setTarget("<col=ff9040>Swamp tar<col=ffffff> -> " + targetMap.get(id));
					event.setForceLeftClick(true);
					event.setModified();
				}
				break;
			case LAVA_RUNES:
				if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() && event.getOption().equals("Craft-rune") && event.getTarget().equals("<col=ffff>Altar"))
				{
					if (findItem(ItemID.EARTH_RUNE).getLeft() == -1)
					{
						return;
					}

					if (!imbue && enableImbue)
					{
						event.setOption("Use");
						event.setTarget("<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself");
						event.setForceLeftClick(true);
						event.setModified();
						return;
					}
					event.setOption("Use");
					event.setTarget("<col=ff9040>Earth rune<col=ffffff> -> <col=ffff>Altar");
					event.setForceLeftClick(true);
					event.setModified();
				}
				break;
			case STEAM_RUNES:
				if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
					event.getOption().equals("Craft-rune") &&
					event.getTarget().equals("<col=ffff>Altar"))
				{
					if (findItem(ItemID.WATER_RUNE).getLeft() == -1)
					{
						return;
					}

					if (!imbue && enableImbue)
					{
						event.setOption("Use");
						event.setTarget("<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself");
						event.setForceLeftClick(true);
						event.setModified();
						return;
					}
					event.setOption("Use");
					event.setTarget("<col=ff9040>Water rune<col=ffffff> -> <col=ffff>Altar");
					event.setForceLeftClick(true);
					event.setModified();
				}
				break;
			case SMOKE_RUNES:
				if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
					event.getOption().equals("Craft-rune") &&
					event.getTarget().equals("<col=ffff>Altar"))
				{
					if (findItem(ItemID.AIR_RUNE).getLeft() == -1)
					{
						return;
					}

					if (!imbue && enableImbue)
					{
						event.setOption("Use");
						event.setTarget("<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself");
						event.setForceLeftClick(true);
						event.setModified();
						return;
					}
					event.setOption("Use");
					event.setTarget("<col=ff9040>Air rune<col=ffffff> -> <col=ffff>Altar");
					event.setForceLeftClick(true);
					event.setModified();
				}
				break;
			case HIGH_ALCH:
				if (opcode == MenuOpcode.WIDGET_TYPE_2.getId() && alchItem != null && event.getOption().equals("Cast") && event.getTarget().equals("<col=00ff00>High Level Alchemy</col>"))
				{
					event.setOption("Cast");
					event.setTarget("<col=00ff00>High Level Alchemy</col><col=ffffff> -> " + alchItem.getName());
					event.setForceLeftClick(true);
					event.setModified();
				}
				break;
			case DWARF_CANNON:
				if (cannonFiring && event.getIdentifier() == DWARF_MULTICANNON && opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId())
				{
					if (findItem(CANNONBALLS).getLeft() == -1)
					{
						return;
					}
					event.setOption("Use");
					event.setTarget("<col=ff9040>Cannonball<col=ffffff> -> <col=ffff>Dwarf multicannon");
					event.setForceLeftClick(true);
					event.setModified();
				}
				break;
			case BONES:
				if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() && event.getOption().toLowerCase().contains("pray") && event.getTarget().toLowerCase().contains("altar"))
				{
					if (findItem(BONE_SET).getLeft() == -1)
					{
						return;
					}
					event.setOption("Use");
					event.setTarget("<col=ff9040>Bones<col=ffffff> -> " + event.getTarget());
					event.setForceLeftClick(true);
					event.setModified();
				}
				break;
			case SEED_SET:
				if (opcode == MenuOpcode.EXAMINE_OBJECT.getId() && event.getTarget().toLowerCase().contains("tithe"))
				{
					if (findItem(SEED_SET).getLeft() == -1)
					{
						return;
					}

					event.setOption("Use");
					event.setTarget("<col=ff9040>Seed<col=ffffff> -> " + event.getTarget());
					event.setForceLeftClick(true);
					event.setModified();
				}
				else if (opcode == MenuOpcode.EXAMINE_OBJECT.getId() && event.getTarget().toLowerCase().contains("water barrel"))
				{
					if (findItem(WATERING_CANS).getLeft() == -1)
					{
						return;
					}

					event.setOption("Use");
					event.setTarget("<col=ff9040>Watering can<col=ffffff> -> " + event.getTarget());
					event.setForceLeftClick(true);
					event.setModified();
				}
				else if (opcode == MenuOpcode.WALK.getId())
				{
					Widget titheWidget = client.getWidget(WidgetInfo.TITHE_FARM);
					if (titheWidget == null || titheWidget.isHidden())
					{
						return;
					}
					MenuEntry menuEntry = client.getLeftClickMenuEntry();
					menuEntry.setOpcode(MenuOpcode.WALK.getId() + MENU_ACTION_DEPRIORITIZE_OFFSET);
					client.setLeftClickMenuEntry(menuEntry);
				}
				break;
			case KARAMBWANS:
				if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() && event.getOption().equals("Cook"))
				{
					if (findItem(ItemID.RAW_KARAMBWAN).getLeft() == -1)
					{
						return;
					}
					event.setOption("Use");
					event.setTarget("<col=ff9040>Raw karambwan<col=ffffff> -> " + event.getTarget());
					event.setForceLeftClick(true);
					event.setModified();
				}
				break;
			case DARK_ESSENCE:
				if (opcode == MenuOpcode.ITEM_USE.getId() && id == ItemID.CHISEL)
				{
					if (findItem(ItemID.DARK_ESSENCE_BLOCK).getLeft() == -1)
					{
						return;
					}
					event.setTarget("<col=ff9040>Chisel<col=ffffff> -> <col=ff9040>Dark essence block");
					event.setForceLeftClick(true);
					event.setModified();
				}
				break;
			case TIARA:
				if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
					event.getOption().equals("Craft-rune") &&
					event.getTarget().equals("<col=ffff>Altar"))
				{
					if (findItem(ItemID.TIARA).getLeft() == -1)
					{
						return;
					}
					event.setOption("Use");
					event.setTarget("<col=ff9040>Tiara<col=ffffff> -> <col=ffff>Altar");
					event.setForceLeftClick(true);
					event.setModified();
				}
				break;
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		int opcode = event.getOpcode();
		int id = event.getIdentifier();

		if (tick)
		{
			event.consume();
			return;
		}

		if (event.getTarget() == null)
		{
			return;
		}

		switch (type)
		{
			case COMPOST:
			{
				if (opcode == MenuOpcode.ITEM_USE.getId() && id == ItemID.COMPOST)
				{
					if (findItem(ItemID.COMPOST).getLeft() == -1)
					{
						return;
					}
					event.setTarget("<col=ff9040>Saltpetre<col=ffffff> -> " + targetMap.get(id));
					event.setForceLeftClick(true);
				}
			}
			break;
			case BRUMA_ROOT:
			{
				if (opcode == MenuOpcode.ITEM_USE.getId() && id == ItemID.BRUMA_ROOT)
				{
					if (findItem(ItemID.BRUMA_ROOT).getLeft() == -1)
					{
						return;
					}
					event.setTarget("<col=ff9040>Knife<col=ffffff> -> " + targetMap.get(id));
					event.setForceLeftClick(true);
				}
			}
			break;
			case DARTS:
				if (opcode == MenuOpcode.ITEM_USE.getId() && (DART_TIPS.contains(id) || BOLTS.contains(id)))
				{
					if (findItem(ItemID.FEATHER).getLeft() == -1)
					{
						return;
					}
					event.setTarget("<col=ff9040>Feather<col=ffffff> -> " + targetMap.get(id));
					event.setForceLeftClick(true);
				}
				break;
			case FIREMAKING:
				if (opcode == MenuOpcode.ITEM_USE.getId() && LOG_ID.contains(id))
				{
					if (findItem(ItemID.TINDERBOX).getLeft() == -1)
					{
						return;
					}
					event.setTarget("<col=ff9040>Tinderbox<col=ffffff> -> " + targetMap.get(id));
					event.setForceLeftClick(true);
				}
				break;
			case BIRDHOUSES:
				if (opcode == MenuOpcode.GAME_OBJECT_SECOND_OPTION.getId() && BIRD_HOUSES_NAMES.contains(event.getTarget()))
				{
					if (findItem(HOPS_SEED).getLeft() == -1)
					{
						return;
					}
					event.setOption("Use");
					event.setTarget("<col=ff9040>Hops seed<col=ffffff> -> " + targetMap.get(id));
					event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
					event.setForceLeftClick(true);
				}
				break;
			case HERB_TAR:
				if (opcode == MenuOpcode.ITEM_USE.getId() && HERBS.contains(id))
				{
					if (findItem(ItemID.SWAMP_TAR).getLeft() == -1 || findItem(ItemID.PESTLE_AND_MORTAR).getLeft() == -1)
					{
						return;
					}
					event.setTarget("<col=ff9040>Swamp tar<col=ffffff> -> " + targetMap.get(id));
					event.setForceLeftClick(true);
				}
				break;
			case LAVA_RUNES:
				if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() && event.getOption().equals("Craft-rune") && event.getTarget().equals("<col=ffff>Altar"))
				{
					if (findItem(ItemID.EARTH_RUNE).getLeft() == -1)
					{
						return;
					}

					if (!imbue && enableImbue)
					{
						event.setOption("Use");
						event.setTarget("<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself");
						event.setForceLeftClick(true);
						return;
					}
					event.setOption("Use");
					event.setTarget("<col=ff9040>Earth rune<col=ffffff> -> <col=ffff>Altar");
					event.setForceLeftClick(true);
				}
				break;
			case STEAM_RUNES:
				if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
					event.getOption().equals("Craft-rune") &&
					event.getTarget().equals("<col=ffff>Altar"))
				{
					if (findItem(ItemID.WATER_RUNE).getLeft() == -1)
					{
						return;
					}

					if (!imbue && enableImbue)
					{
						event.setOption("Use");
						event.setTarget("<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself");
						event.setForceLeftClick(true);
						return;
					}
					event.setOption("Use");
					event.setTarget("<col=ff9040>Water rune<col=ffffff> -> <col=ffff>Altar");
					event.setForceLeftClick(true);
				}
				break;
			case SMOKE_RUNES:
				if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
					event.getOption().equals("Craft-rune") &&
					event.getTarget().equals("<col=ffff>Altar"))
				{
					if (findItem(ItemID.AIR_RUNE).getLeft() == -1)
					{
						return;
					}

					if (!imbue && enableImbue)
					{
						event.setOption("Use");
						event.setTarget("<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself");
						event.setForceLeftClick(true);
						return;
					}
					event.setOption("Use");
					event.setTarget("<col=ff9040>Air rune<col=ffffff> -> <col=ffff>Altar");
					event.setForceLeftClick(true);
				}
				break;
			case HIGH_ALCH:
				if (opcode == MenuOpcode.WIDGET_TYPE_2.getId() && alchItem != null && event.getOption().equals("Cast") && event.getTarget().equals("<col=00ff00>High Level Alchemy</col>"))
				{
					event.setOption("Cast");
					event.setTarget("<col=00ff00>High Level Alchemy</col><col=ffffff> -> " + alchItem.getName());
					event.setForceLeftClick(true);
				}
				break;
			case DWARF_CANNON:
				if (cannonFiring && event.getIdentifier() == DWARF_MULTICANNON && opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId())
				{
					if (findItem(CANNONBALLS).getLeft() == -1)
					{
						return;
					}
					event.setOption("Use");
					event.setTarget("<col=ff9040>Cannonball<col=ffffff> -> <col=ffff>Dwarf multicannon");
					event.setForceLeftClick(true);
				}
				break;
			case BONES:
				if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() && event.getOption().toLowerCase().contains("pray") && event.getTarget().toLowerCase().contains("altar"))
				{
					if (findItem(BONE_SET).getLeft() == -1)
					{
						return;
					}
					event.setOption("Use");
					event.setTarget("<col=ff9040>Bones<col=ffffff> -> " + event.getTarget());
					event.setForceLeftClick(true);
				}
				break;
			case SEED_SET:
				if (opcode == MenuOpcode.EXAMINE_OBJECT.getId() && event.getTarget().toLowerCase().contains("tithe"))
				{
					if (findItem(SEED_SET).getLeft() == -1)
					{
						return;
					}

					event.setOption("Use");
					event.setTarget("<col=ff9040>Seed<col=ffffff> -> " + event.getTarget());
					event.setForceLeftClick(true);
				}
				else if (opcode == MenuOpcode.EXAMINE_OBJECT.getId() && event.getTarget().toLowerCase().contains("water barrel"))
				{
					if (findItem(WATERING_CANS).getLeft() == -1)
					{
						return;
					}

					event.setOption("Use");
					event.setTarget("<col=ff9040>Watering can<col=ffffff> -> " + event.getTarget());
					event.setForceLeftClick(true);
				}
				else if (opcode == MenuOpcode.WALK.getId())
				{
					Widget titheWidget = client.getWidget(WidgetInfo.TITHE_FARM);
					if (titheWidget == null || titheWidget.isHidden())
					{
						return;
					}
					MenuEntry menuEntry = client.getLeftClickMenuEntry();
					menuEntry.setOpcode(MenuOpcode.WALK.getId() + MENU_ACTION_DEPRIORITIZE_OFFSET);
					client.setLeftClickMenuEntry(menuEntry);
				}
				break;
			case KARAMBWANS:
				if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() && event.getOption().equals("Cook"))
				{
					if (findItem(ItemID.RAW_KARAMBWAN).getLeft() == -1)
					{
						return;
					}
					event.setOption("Use");
					event.setTarget("<col=ff9040>Raw karambwan<col=ffffff> -> " + event.getTarget());
					event.setForceLeftClick(true);
				}
				break;
			case DARK_ESSENCE:
				if (opcode == MenuOpcode.ITEM_USE.getId() && id == ItemID.CHISEL)
				{
					if (findItem(ItemID.DARK_ESSENCE_BLOCK).getLeft() == -1)
					{
						return;
					}
					event.setTarget("<col=ff9040>Chisel<col=ffffff> -> <col=ff9040>Dark essence block");
					event.setForceLeftClick(true);
				}
				break;
			case TIARA:
				if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
					event.getOption().equals("Craft-rune") &&
					event.getTarget().equals("<col=ffff>Altar"))
				{
					if (findItem(ItemID.TIARA).getLeft() == -1)
					{
						return;
					}
					event.setOption("Use");
					event.setTarget("<col=ff9040>Tiara<col=ffffff> -> <col=ffff>Altar");
					event.setForceLeftClick(true);
				}
				break;
		}

		String target = event.getTarget();
		opcode = event.getOpcode();
		id = event.getIdentifier();

		if (config.customInvSwap())
		{
			if (opcode == MenuOpcode.ITEM_USE.getId() && customClickMap.containsKey(id))
			{
				if (updateSelectedItem(customClickMap.get(id)))
				{
					event.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET_ITEM.getId());
					return;
				}
			}
		}

		switch (type)
		{
			case COMPOST:
			{
				if (opcode == MenuOpcode.ITEM_USE.getId() && id == ItemID.COMPOST)
				{
					if (updateSelectedItem(ItemID.SALTPETRE))
					{
						event.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET_ITEM.getId());
					}
				}
			}
			break;
			case BRUMA_ROOT:
				if (opcode == MenuOpcode.ITEM_USE.getId() && target.contains("<col=ff9040>Knife<col=ffffff> -> "))
				{
					if (updateSelectedItem(ItemID.KNIFE))
					{
						event.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET_ITEM.getId());
					}
				}
				break;
			case DARTS:
				if (opcode == MenuOpcode.ITEM_USE.getId() && target.contains("<col=ff9040>Feather<col=ffffff> -> "))
				{
					if (updateSelectedItem(ItemID.FEATHER))
					{
						event.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET_ITEM.getId());
					}
				}
				break;
			case FIREMAKING:
				if (opcode == MenuOpcode.ITEM_USE.getId() && target.contains("<col=ff9040>Tinderbox<col=ffffff> -> "))
				{
					if (updateSelectedItem(ItemID.TINDERBOX))
					{
						event.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET_ITEM.getId());
					}
				}
				break;
			case BIRDHOUSES:
				if (opcode == MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId() && target.contains("<col=ff9040>Hops seed<col=ffffff> -> "))
				{
					updateSelectedItem(HOPS_SEED);
				}
				break;
			case HERB_TAR:
				if (opcode == MenuOpcode.ITEM_USE.getId() && target.contains("<col=ff9040>Swamp tar<col=ffffff> -> "))
				{
					if (updateSelectedItem(ItemID.SWAMP_TAR))
					{
						event.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET_ITEM.getId());
					}
				}
				break;
			case LAVA_RUNES:
				if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
					target.equals("<col=ff9040>Earth rune<col=ffffff> -> <col=ffff>Altar"))
				{
					if (updateSelectedItem(ItemID.EARTH_RUNE))
					{
						event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
					}
				}
				else if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
					target.equals("<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself"))
				{
					event.setIdentifier(1);
					event.setOpcode(MenuOpcode.CC_OP.getId());
					event.setParam0(-1);
					event.setParam1(WidgetInfo.SPELL_MAGIC_IMBUE.getId());
				}
				break;
			case STEAM_RUNES:
				if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
					target.equals("<col=ff9040>Water rune<col=ffffff> -> <col=ffff>Altar"))
				{
					if (updateSelectedItem(ItemID.WATER_RUNE))
					{
						event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
					}
				}
				else if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
					target.equals("<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself"))
				{
					event.setIdentifier(1);
					event.setOpcode(MenuOpcode.CC_OP.getId());
					event.setParam0(-1);
					event.setParam1(WidgetInfo.SPELL_MAGIC_IMBUE.getId());
				}
				break;
			case SMOKE_RUNES:
				if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
					target.equals("<col=ff9040>Air rune<col=ffffff> -> <col=ffff>Altar"))
				{
					if (updateSelectedItem(ItemID.AIR_RUNE))
					{
						event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
					}
				}
				else if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
					target.equals("<col=ff9040>Magic Imbue<col=ffffff> -> <col=ffff>Yourself"))
				{
					event.setIdentifier(1);
					event.setOpcode(MenuOpcode.CC_OP.getId());
					event.setParam0(-1);
					event.setParam1(WidgetInfo.SPELL_MAGIC_IMBUE.getId());
				}
				break;
			case HIGH_ALCH:
				if (opcode == MenuOpcode.WIDGET_TYPE_2.getId() && event.getOption().equals("Cast") && target.contains("<col=00ff00>High Level Alchemy</col><col=ffffff> -> "))
				{
					final Pair<Integer, Integer> pair = findItem(alchItem.getId());
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
				else if (opcode == MenuOpcode.RUNELITE.getId() && event.getIdentifier() == -1)
				{
					alchItem = null;
				}
				else if (type == Types.HIGH_ALCH && opcode == MenuOpcode.RUNELITE.getId())
				{
					final String itemName = event.getTarget().split("<col=00ff00>High Alchemy Item <col=ffffff> -> ")[1];
					alchItem = new AlchItem(itemName, event.getIdentifier());
				}
				break;
			case DWARF_CANNON:
				if (cannonFiring && event.getIdentifier() == DWARF_MULTICANNON && opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId())
				{
					if (updateSelectedItem(ItemID.CANNON_BALL))
					{
						event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
					}
				}
				break;
			case BONES:
				if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
					event.getTarget().contains("<col=ff9040>Bones<col=ffffff> -> ") && target.toLowerCase().contains("altar"))
				{
					if (updateSelectedItem(BONE_SET))
					{
						event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
					}
				}
				break;
			case SEED_SET:
				if (opcode == MenuOpcode.EXAMINE_OBJECT.getId() &&
					event.getTarget().contains("<col=ff9040>Seed<col=ffffff> -> ") && target.toLowerCase().contains("tithe"))
				{
					if (updateSelectedItem(SEED_SET))
					{
						event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
					}
				}
				else if (opcode == MenuOpcode.EXAMINE_OBJECT.getId() &&
					event.getTarget().contains("<col=ff9040>Watering can<col=ffffff> -> ") && target.toLowerCase().contains("water barrel"))
				{
					if (updateSelectedItem(WATERING_CANS))
					{
						event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
					}
				}
				break;
			case KARAMBWANS:
				if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() && event.getTarget().contains("<col=ff9040>Raw karambwan<col=ffffff> -> "))
				{
					if (updateSelectedItem(ItemID.RAW_KARAMBWAN))
					{
						event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
						tick = true;
					}
				}
				break;
			case DARK_ESSENCE:
				if (opcode == MenuOpcode.ITEM_USE.getId() && target.contains("<col=ff9040>Chisel<col=ffffff> ->"))
				{
					if (updateSelectedItem(ItemID.DARK_ESSENCE_BLOCK))
					{
						event.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET_ITEM.getId());
					}
				}
				break;
			case TIARA:
				if (opcode == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
					target.equals("<col=ff9040>Tiara<col=ffffff> -> <col=ffff>Altar"))
				{
					if (updateSelectedItem(ItemID.TIARA))
					{
						event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
					}
				}
				break;
		}
	}

	private boolean updateSelectedItem(int id)
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

	private boolean updateSelectedItem(Collection<Integer> ids)
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

	private Pair<Integer, Integer> findItem(int id)
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

	private Pair<Integer, Integer> findItem(Collection<Integer> ids)
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
