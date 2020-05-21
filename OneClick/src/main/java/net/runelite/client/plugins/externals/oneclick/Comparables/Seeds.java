package net.runelite.client.plugins.externals.oneclick.Comparables;

import com.google.common.collect.ImmutableSet;
import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.client.plugins.externals.oneclick.OneClickPlugin;

import java.util.Set;

public class Seeds implements ClickComparable {
	
	private static final Set<Integer> SEED_SET = ImmutableSet.of(
			ItemID.GOLOVANOVA_SEED, ItemID.BOLOGANO_SEED, ItemID.LOGAVANO_SEED
	);
	private static final Set<Integer> WATERING_CANS = ImmutableSet.of(
			ItemID.WATERING_CAN1, ItemID.WATERING_CAN2, ItemID.WATERING_CAN3, ItemID.WATERING_CAN4,
			ItemID.WATERING_CAN5, ItemID.WATERING_CAN6, ItemID.WATERING_CAN7, ItemID.WATERING_CAN8,
			ItemID.GRICOLLERS_CAN
	);
	
	private static final Set<String> TITHE_PATCHES = ImmutableSet.of(
			"<col=ffff>Tithe patch"
	);
	
	private static final Set TITHE_PATCHES_WATER = ImmutableSet.of(
			"<col=ffff>Logavano seedling", "<col=ffff>Bologano seedling", "<col=ffff>Golovanova seedling",
			"<col=ffff>Logavano plant", "<col=ffff>Bologano plant", "<col=ffff>Golovanova plant"
	);
	
	private static final Set TITHE_WATER_BARREL = ImmutableSet.of(
			"<col=ffff>Water Barrel"
	);
	
	@Override
	public boolean isEntryValid(MenuEntry event) {
		return event.getOpcode() == MenuOpcode.EXAMINE_OBJECT.getId() &&
				TITHE_PATCHES.contains(event.getTarget()) ||
				event.getOpcode() == MenuOpcode.GAME_OBJECT_SECOND_OPTION.getId() &&
						TITHE_PATCHES_WATER.contains(event.getTarget()) ||
				event.getOpcode() == MenuOpcode.EXAMINE_OBJECT.getId() &&
						TITHE_WATER_BARREL.contains(event.getTarget());
	}
	
	@Override
	public void modifyEntry(OneClickPlugin plugin, MenuEntry event) {
		if (TITHE_PATCHES.contains(event.getTarget())) {
			if (plugin.findItem(SEED_SET).getLeft() == -1) {
				return;
			}
			event.setOption("Use");
			event.setTarget("<col=ff9040>Seed<col=ffffff> -> " + plugin.getTargetMap().get(event.getIdentifier()));
			event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
			event.setForceLeftClick(true);
			
		} else if (TITHE_PATCHES_WATER.contains(event.getTarget())) {
			if (plugin.findItem(WATERING_CANS).getLeft() == -1) {
				return;
			}
			event.setOption("Use");
			event.setTarget("<col=ff9040>Watering can<col=ffffff> -> " + plugin.getTargetMap().get(event.getIdentifier()));
			event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
			event.setForceLeftClick(true);
			
		} else if (TITHE_WATER_BARREL.contains(event.getTarget())) {
			if (plugin.findItem(WATERING_CANS).getLeft() == -1) {
				return;
			}
			event.setOption("Use");
			event.setTarget("<col=ff9040>Watering can<col=ffffff> -> " + plugin.getTargetMap().get(event.getIdentifier()));
			event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
			event.setForceLeftClick(true);
		}
	}
	
	@Override
	public boolean isClickValid(MenuEntry event) {
		return event.getOpcode() == MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId() &&
				event.getTarget().contains("<col=ff9040>Seed<col=ffffff> -> ") ||
				event.getTarget().contains("<col=ff9040>Watering can<col=ffffff> -> ") ||
				event.getTarget().contains("<col=ff9040>Watering can<col=ffffff> -> ") &&
				event.getTarget().contains("<col=ffff>Water Barrel");
	}
	
	@Override
	public void modifyClick(OneClickPlugin plugin, MenuEntry event) {
		if (event.getTarget().contains("<col=ff9040>Seed<col=ffffff> -> ")) {
			if (plugin.updateSelectedItem(SEED_SET))
				event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
			
		} else if (event.getTarget().contains("<col=ff9040>Watering can<col=ffffff> -> ") &&
				plugin.updateSelectedItem(WATERING_CANS)) {
			event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
			
		} else if (event.getTarget().contains("<col=ffff>Water Barrel") &&
			plugin.updateSelectedItem(WATERING_CANS)) {
		event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
	}
	}
}