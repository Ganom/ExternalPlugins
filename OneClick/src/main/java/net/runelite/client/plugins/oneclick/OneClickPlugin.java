package net.runelite.client.plugins.oneclick;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Provides;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.inject.Inject;
import net.runelite.api.AnimationID;
import net.runelite.api.Client;
import net.runelite.api.DynamicObject;
import net.runelite.api.Entity;
import net.runelite.api.GameObject;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import static net.runelite.api.ObjectID.DWARF_MULTICANNON;
import net.runelite.api.Player;
import net.runelite.api.Skill;
import net.runelite.api.SpriteID;
import net.runelite.api.Varbits;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.GameObjectSpawned;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;

@PluginDescriptor(
	name = "One Click",
	description = "Osbuddy in runelite+",
	enabledByDefault = false,
	type = PluginType.EXTERNAL
)
public class OneClickPlugin extends Plugin
{
	private static final Set<Integer> DART_TIPS = ImmutableSet.of(ItemID.BRONZE_DART_TIP, ItemID.IRON_DART_TIP,
		ItemID.STEEL_DART_TIP, ItemID.MITHRIL_DART_TIP, ItemID.ADAMANT_DART_TIP, ItemID.RUNE_DART_TIP, ItemID.DRAGON_DART_TIP, ItemID.UNFINISHED_BROAD_BOLTS
	);
	private static final Set<Integer> LOG_ID = ImmutableSet.of(ItemID.LOGS, ItemID.OAK_LOGS, ItemID.WILLOW_LOGS, ItemID.TEAK_LOGS,
		ItemID.MAPLE_LOGS, ItemID.MAHOGANY_LOGS, ItemID.YEW_LOGS, ItemID.MAGIC_LOGS, ItemID.REDWOOD_LOGS
	);
	private static final Set<Integer> HOPS_SEED = ImmutableSet.of(ItemID.BARLEY_SEED, ItemID.HAMMERSTONE_SEED,
		ItemID.ASGARNIAN_SEED, ItemID.JUTE_SEED, ItemID.YANILLIAN_SEED, ItemID.KRANDORIAN_SEED, ItemID.WILDBLOOD_SEED
	);
	private static final Set<Integer> HERBS = ImmutableSet.of(ItemID.GUAM_LEAF, ItemID.MARRENTILL, ItemID.TARROMIN, ItemID.HARRALANDER
	);
	private static final Set<Integer> BONE_SET = ImmutableSet.of(ItemID.BONES, ItemID.WOLF_BONE, ItemID.BURNT_BONES, ItemID.MONKEY_BONES,
		ItemID.BAT_BONES, ItemID.JOGRE_BONE, ItemID.BIG_BONES, ItemID.ZOGRE_BONE, ItemID.SHAIKAHAN_BONES, ItemID.BABYDRAGON_BONES,
		ItemID.WYRM_BONES, ItemID.DRAGON_BONES, ItemID.DRAKE_BONES, ItemID.FAYRG_BONES, ItemID.LAVA_DRAGON_BONES, ItemID.RAURG_BONES,
		ItemID.HYDRA_BONES, ItemID.DAGANNOTH_BONES, ItemID.OURG_BONES, ItemID.SUPERIOR_DRAGON_BONES, ItemID.WYVERN_BONES
	);
	private static final Set<String> BIRD_HOUSES_NAMES = ImmutableSet.of("<col=ffff>Bird house (empty)", "<col=ffff>Oak birdhouse (empty)",
		"<col=ffff>Willow birdhouse (empty)", "<col=ffff>Teak birdhouse (empty)", "<col=ffff>Maple birdhouse (empty)", "<col=ffff>Mahogany birdhouse (empty)",
		"<col=ffff>Yew birdhouse (empty)", "<col=ffff>Magic birdhouse (empty)", "<col=ffff>Redwood birdhouse (empty)"
	);
	private static final Set<Integer> CUTTABLE_FISHES = ImmutableSet.of(ItemID.SACRED_EEL, ItemID.LEAPING_TROUT, ItemID.LEAPING_SALMON, ItemID.LEAPING_STURGEON
	);

	@Inject
	private Client client;
	@Inject
	private OneClickConfig config;
	@Inject
	private EventBus eventBus;

	private boolean tick = false;
	private final Map<Integer, String> targetMap = new HashMap<>();
	private AlchItem alchItem;
	private GameObject cannon;
	private boolean cannonFiring = false;
	private int prevCannonAnimation = 514;
	private boolean darts;
	private boolean lightLogs;
	private boolean birdhouses;
	private boolean herbTar;
	private boolean earthRuneAltar;
	private boolean highAlch;
	private boolean dwarfCannon;
	private boolean bones;
	private boolean karambwan;
	private boolean darkEssence;
	private boolean cutFish;

	@Provides
	OneClickConfig provideConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OneClickConfig.class);
	}

	@Override
	protected void startUp()
	{
		addSubscriptions();
		updateConfig();
	}

	@Override
	protected void shutDown()
	{
		eventBus.unregister(this);
	}

	private void addSubscriptions()
	{
		eventBus.subscribe(MenuEntryAdded.class, this, this::onMenuEntryAdded);
		eventBus.subscribe(MenuOptionClicked.class, this, this::onMenuOptionClicked);
		eventBus.subscribe(GameTick.class, this, this::onGameTick);
		eventBus.subscribe(ConfigChanged.class, this, this::onConfigChanged);
		eventBus.subscribe(MenuOpened.class, this, this::onMenuOpened);
		eventBus.subscribe(GameObjectSpawned.class, this, this::onGameObjectSpawned);
		eventBus.subscribe(ChatMessage.class, this, this::onChatMessage);
	}

	private void updateConfig()
	{
		darts = config.getDarts();
		lightLogs = config.getLightLogs();
		birdhouses = config.getBirdHouses();
		herbTar = config.getHerbTar();
		earthRuneAltar = config.getEarthRuneAltar();
		highAlch = config.getHighAlch();
		dwarfCannon = config.getDwarfMultiCannon();
		bones = config.getBones();
		karambwan = config.getKarambwan();
		darkEssence = config.getDarkEssence();
		cutFish = config.getCutFish();
	}

	private void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals("oneclick"))
		{
			updateConfig();
		}
	}

	private void onChatMessage(ChatMessage event)
	{
		if (event.getMessage().equals("You pick up the cannon. It's really heavy."))
		{
			cannonFiring = false;
			cannon = null;
		}
	}

	private void onGameObjectSpawned(GameObjectSpawned event)
	{
		final GameObject gameObject = event.getGameObject();
		final Player localPlayer = client.getLocalPlayer();
		if (gameObject.getId() == DWARF_MULTICANNON && cannon == null &&
			localPlayer.getWorldLocation().distanceTo(gameObject.getWorldLocation()) <= 2 &&
			localPlayer.getAnimation() == AnimationID.BURYING_BONES)
		{
			cannon = gameObject;
		}
	}

	private void onGameTick(GameTick event)
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

	private void onMenuOpened(MenuOpened event)
	{
		final MenuEntry firstEntry = event.getFirstEntry();

		if (firstEntry == null)
		{
			return;
		}

		final int widgetId = firstEntry.getParam1();

		if (widgetId == WidgetInfo.INVENTORY.getId() && highAlch)
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

			final MenuEntry SetHighAlchItem = new MenuEntry();
			final boolean set = alchItem != null && alchItem.getId() == firstEntry.getIdentifier();
			SetHighAlchItem.setOption(set ? "Unset" : "Set");
			SetHighAlchItem.setTarget("<col=00ff00>High Alchemy Item <col=ffffff> -> " + firstEntry.getTarget());
			SetHighAlchItem.setIdentifier(set ? -1 : firstEntry.getIdentifier());
			SetHighAlchItem.setOpcode(MenuOpcode.RUNELITE.getId());
			SetHighAlchItem.setParam1(widgetId);
			SetHighAlchItem.setForceLeftClick(false);
			menuList[1] = SetHighAlchItem;
			event.setMenuEntries(menuList);
			event.setModified(true);
		}
	}


	private void onMenuEntryAdded(MenuEntryAdded event)
	{
		final int id = event.getIdentifier();
		targetMap.put(id, event.getMenuEntry().getTarget());

		if (darts && event.getType() == MenuOpcode.ITEM_USE.getId() && DART_TIPS.contains(id))
		{
			if (findItem(ItemID.FEATHER) == -1)
			{
				return;
			}
			event.getMenuEntry().setTarget("<col=ff9040>Feather<col=ffffff> -> " + targetMap.get(id));
			event.setWasModified(true);
		}
		else if (lightLogs && event.getType() == MenuOpcode.ITEM_USE.getId() && LOG_ID.contains(id))
		{
			if (findItem(ItemID.TINDERBOX) == -1)
			{
				return;
			}
			event.getMenuEntry().setTarget("<col=ff9040>Tinderbox<col=ffffff> -> " + targetMap.get(id));
			event.setWasModified(true);
		}
		else if (darkEssence && event.getType() == MenuOpcode.ITEM_USE.getId() && id == ItemID.CHISEL)
		{
			if (findItem(ItemID.DARK_ESSENCE_BLOCK) == -1)
			{
				return;
			}
			event.getMenuEntry().setTarget("<col=ff9040>Chisel<col=ffffff> -> <col=ff9040>Dark essence block");
			event.setWasModified(true);
		}
		else if (birdhouses && event.getType() == MenuOpcode.GAME_OBJECT_SECOND_OPTION.getId() &&
			BIRD_HOUSES_NAMES.contains(event.getTarget()))
		{
			if (findItem(HOPS_SEED) == null)
			{
				return;
			}
			event.getMenuEntry().setOption("Use");
			event.getMenuEntry().setTarget("<col=ff9040>Hops seed<col=ffffff> -> " + targetMap.get(id));
			event.getMenuEntry().setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
			event.setWasModified(true);
		}
		else if (herbTar && event.getType() == MenuOpcode.ITEM_USE.getId() && HERBS.contains(id))
		{
			if (findItem(ItemID.SWAMP_TAR) == -1 || findItem(ItemID.PESTLE_AND_MORTAR) == -1)
			{
				return;
			}
			event.getMenuEntry().setTarget("<col=ff9040>Swamp tar<col=ffffff> -> " + targetMap.get(id));
			event.setWasModified(true);
		}
		else if (earthRuneAltar && event.getType() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
			event.getOption().equals("Craft-rune") && event.getTarget().equals("<col=ffff>Altar"))
		{
			if (findItem(ItemID.EARTH_RUNE) == -1)
			{
				return;
			}
			event.getMenuEntry().setOption("Use");
			event.getMenuEntry().setTarget("<col=ff9040>Earth rune<col=ffffff> -> <col=ffff>Altar");
			event.setWasModified(true);
		}
		else if (highAlch && event.getType() == MenuOpcode.WIDGET_TYPE_2.getId() && alchItem != null &&
			event.getOption().equals("Cast") && event.getTarget().equals("<col=00ff00>High Level Alchemy</col>"))
		{
			event.getMenuEntry().setOption("Cast");
			event.getMenuEntry().setTarget("<col=00ff00>High Level Alchemy</col><col=ffffff> -> " + alchItem.getName());
			event.setWasModified(true);
		}
		else if (dwarfCannon && cannonFiring && event.getIdentifier() == DWARF_MULTICANNON &&
			event.getType() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId())
		{
			if (findItem(ItemID.CANNONBALL) == -1)
			{
				return;
			}
			event.getMenuEntry().setOption("Use");
			event.getMenuEntry().setTarget("<col=ff9040>Cannonball<col=ffffff> -> <col=ffff>Dwarf multicannon");
			event.setWasModified(true);
		}
		else if (bones && event.getType() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
			event.getOption().toLowerCase().contains("pray") && event.getTarget().toLowerCase().contains("altar"))
		{
			if (findItem(BONE_SET) == null)
			{
				return;
			}
			event.getMenuEntry().setOption("Use");
			event.getMenuEntry().setTarget("<col=ff9040>Bones<col=ffffff> -> " + event.getTarget());
			event.setWasModified(true);
		}
		else if (karambwan && event.getType() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() && event.getOption().equals("Cook"))
		{
			if (findItem(ItemID.RAW_KARAMBWAN) == -1)
			{
				return;
			}
			event.getMenuEntry().setOption("Use");
			event.getMenuEntry().setTarget("<col=ff9040>Raw karambwan<col=ffffff> -> " + event.getMenuEntry().getTarget());
			event.setWasModified(true);
		}
		else if (cutFish && event.getType() == MenuOpcode.ITEM_USE.getId() && CUTTABLE_FISHES.contains(id))
		{
			if (findItem(ItemID.KNIFE) == -1)
			{
				return;
			}
			event.getMenuEntry().setTarget("<col=ff9040>Knife<col=ffffff> -> " + targetMap.get(id));
			event.setWasModified(true);
		}
	}

	private void onMenuOptionClicked(MenuOptionClicked event)
	{
		final MenuEntry entry = event.getMenuEntry();
		if (tick)
		{
			event.consume();
		}
		else if (darts && event.getOpcode() == MenuOpcode.ITEM_USE.getId() &&
			event.getTarget().contains("<col=ff9040>Feather<col=ffffff> -> "))
		{
			entry.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET_ITEM.getId());
			client.setSelectedItemWidget(WidgetInfo.INVENTORY.getId());
			client.setSelectedItemSlot(findItem(ItemID.FEATHER));
			client.setSelectedItemID(ItemID.FEATHER);
		}
		else if (lightLogs && event.getOpcode() == MenuOpcode.ITEM_USE.getId() &&
			event.getTarget().contains("<col=ff9040>Tinderbox<col=ffffff> -> "))
		{
			entry.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET_ITEM.getId());
			client.setSelectedItemWidget(WidgetInfo.INVENTORY.getId());
			client.setSelectedItemSlot(findItem(ItemID.TINDERBOX));
			client.setSelectedItemID(ItemID.TINDERBOX);
		}
		else if (darkEssence && event.getOpcode() == MenuOpcode.ITEM_USE.getId() &&
			event.getTarget().contains("<col=ff9040>Chisel<col=ffffff> ->"))
		{
			if (findItem(ItemID.DARK_ESSENCE_BLOCK) != -1)
			{
				entry.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET_ITEM.getId());
				client.setSelectedItemWidget(WidgetInfo.INVENTORY.getId());
				client.setSelectedItemSlot(findItem(ItemID.DARK_ESSENCE_BLOCK));
				client.setSelectedItemID(ItemID.DARK_ESSENCE_BLOCK);
			}
		}
		else if (birdhouses && event.getOpcode() == MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId() &&
			event.getTarget().contains("<col=ff9040>Hops seed<col=ffffff> -> "))
		{
			final int[] seedLoc = findItem(HOPS_SEED);
			if (seedLoc == null || seedLoc.length < 2)
			{
				return;
			}
			client.setSelectedItemWidget(WidgetInfo.INVENTORY.getId());
			client.setSelectedItemSlot(seedLoc[0]);
			client.setSelectedItemID(seedLoc[1]);
		}
		else if (herbTar && event.getOpcode() == MenuOpcode.ITEM_USE.getId() &&
			event.getTarget().contains("<col=ff9040>Swamp tar<col=ffffff> -> "))
		{
			entry.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET_ITEM.getId());
			client.setSelectedItemWidget(WidgetInfo.INVENTORY.getId());
			client.setSelectedItemSlot(findItem(ItemID.SWAMP_TAR));
			client.setSelectedItemID(ItemID.SWAMP_TAR);
		}
		else if (earthRuneAltar && event.getOpcode() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
			event.getTarget().equals("<col=ff9040>Earth rune<col=ffffff> -> <col=ffff>Altar"))
		{
			entry.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
			client.setSelectedItemWidget(WidgetInfo.INVENTORY.getId());
			client.setSelectedItemSlot(findItem(ItemID.EARTH_RUNE));
			client.setSelectedItemID(ItemID.EARTH_RUNE);
		}
		else if (highAlch && event.getOpcode() == MenuOpcode.WIDGET_TYPE_2.getId() && event.getOption().equals("Cast") &&
			event.getTarget().contains("<col=00ff00>High Level Alchemy</col><col=ffffff> -> "))
		{
			entry.setIdentifier(alchItem.getId());
			entry.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET.getId());
			entry.setParam0(findItem(alchItem.getId()));
			entry.setParam1(9764864);
			client.setSelectedSpellName("<col=00ff00>High Level Alchemy</col><col=ffffff>");
			client.setSelectedSpellWidget(WidgetInfo.SPELL_HIGH_LEVEL_ALCHEMY.getId());
		}
		else if (highAlch && entry.getOpcode() == MenuOpcode.RUNELITE.getId() && entry.getIdentifier() == -1)
		{
			alchItem = null;
		}
		else if (highAlch && entry.getOpcode() == MenuOpcode.RUNELITE.getId())
		{
			final String itemName = entry.getTarget().split("<col=00ff00>High Alchemy Item <col=ffffff> -> ")[1];
			alchItem = new AlchItem(itemName, entry.getIdentifier());
		}
		else if (dwarfCannon && cannonFiring && entry.getIdentifier() == DWARF_MULTICANNON &&
			entry.getOpcode() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId())
		{
			entry.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
			client.setSelectedItemWidget(WidgetInfo.INVENTORY.getId());
			client.setSelectedItemSlot(findItem(ItemID.CANNONBALL));
			client.setSelectedItemID(ItemID.CANNONBALL);
		}
		else if (bones && entry.getOpcode() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() && entry.getTarget().contains("<col=ff9040>Bones<col=ffffff> -> ") &&
			event.getTarget().toLowerCase().contains("altar"))
		{
			final int[] bonesLoc = findItem(BONE_SET);

			if (bonesLoc == null || bonesLoc.length < 2)
			{
				return;
			}

			event.getMenuEntry().setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
			client.setSelectedItemWidget(WidgetInfo.INVENTORY.getId());
			client.setSelectedItemSlot(bonesLoc[0]);
			client.setSelectedItemID(bonesLoc[1]);
		}
		else if (karambwan && entry.getOpcode() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
			entry.getTarget().contains("<col=ff9040>Raw karambwan<col=ffffff> -> "))
		{
			event.getMenuEntry().setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
			client.setSelectedItemWidget(WidgetInfo.INVENTORY.getId());
			client.setSelectedItemSlot(findItem(ItemID.RAW_KARAMBWAN));
			client.setSelectedItemID(ItemID.RAW_KARAMBWAN);
			tick = true;
		}
		else if (cutFish && event.getOpcode() == MenuOpcode.ITEM_USE.getId() &&
			event.getTarget().contains("<col=ff9040>Knife<col=ffffff> -> "))
		{
			entry.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET_ITEM.getId());
			client.setSelectedItemWidget(WidgetInfo.INVENTORY.getId());
			client.setSelectedItemSlot(findItem(ItemID.KNIFE));
			client.setSelectedItemID(ItemID.KNIFE);
		}
	}

	private int findItem(int itemID)
	{
		final ItemContainer itemContainer = client.getItemContainer(InventoryID.INVENTORY);

		if (itemContainer == null)
		{
			return -1;
		}

		final Item[] items = itemContainer.getItems();
		for (int slot = items.length - 1; slot >= 0; slot--)
		{
			final Item item = items[slot];
			if (item != null && itemID == item.getId())
			{
				return slot;
			}
		}
		return -1;
	}

	private int[] findItem(Collection<Integer> itemIDs)
	{
		final ItemContainer itemContainer = client.getItemContainer(InventoryID.INVENTORY);

		if (itemContainer == null)
		{
			return null;
		}

		final Item[] items = itemContainer.getItems();
		for (int slot = items.length - 1; slot >= 0; slot--)
		{
			final Item item = items[slot];
			if (item != null && itemIDs.contains(item.getId()))
			{
				return new int[]{slot, item.getId()};
			}
		}
		return null;
	}

}