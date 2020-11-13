package net.runelite.client.plugins.externals.oneclick.comparables.misc;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

public class Seeds extends ClickCompare
{
	private static final Set<Integer> SEED_SET = ImmutableSet.of(
		ItemID.GOLOVANOVA_SEED, ItemID.BOLOGANO_SEED, ItemID.LOGAVANO_SEED
	);
	private static final Set<Integer> WATERING_CANS = ImmutableSet.of(
		ItemID.WATERING_CAN, ItemID.WATERING_CAN1, ItemID.WATERING_CAN2, ItemID.WATERING_CAN3, ItemID.WATERING_CAN4,
		ItemID.WATERING_CAN5, ItemID.WATERING_CAN6, ItemID.WATERING_CAN7, ItemID.GRICOLLERS_CAN
	);

	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == MenuOpcode.EXAMINE_OBJECT.getId() &&
			event.getTarget().toLowerCase().contains("tithe");
	}

	@Override
	public void modifyEntry(MenuEntry event)
	{
		if (findItem(SEED_SET).getLeft() == -1)
		{
			return;
		}

		MenuEntry e = event.clone();
		e.setOption("Use");
		e.setTarget("<col=ff9040>Seed<col=ffffff> -> " + event.getTarget());
		e.setForceLeftClick(true);
		insert(e);
	}

	@Override
	public boolean isClickValid(MenuEntry event)
	{
		return event.getOpcode() == MenuOpcode.EXAMINE_OBJECT.getId() && (
			(event.getTarget().contains("<col=ff9040>Watering can<col=ffffff> -> ") &&
				event.getTarget().toLowerCase().contains("water barrel")) ||
				(event.getTarget().contains("<col=ff9040>Seed<col=ffffff> -> ") &&
					event.getTarget().toLowerCase().contains("tithe"))
		);
	}

	@Override
	public void modifyClick(MenuEntry event)
	{
		if (event.getTarget().toLowerCase().contains("tithe"))
		{
			if (updateSelectedItem(SEED_SET))
			{
				event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
			}
		}
		else if (event.getTarget().toLowerCase().contains("water barrel"))
		{
			if (updateSelectedItem(WATERING_CANS))
			{
				event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
			}
		}
	}
}
