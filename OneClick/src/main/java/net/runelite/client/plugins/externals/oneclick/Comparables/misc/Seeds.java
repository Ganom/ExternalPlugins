package net.runelite.client.plugins.externals.oneclick.comparables.misc;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
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
	public boolean isEntryValid(MenuEntryAdded event)
	{
		return event.getType() == MenuAction.EXAMINE_OBJECT.getId() && !event.isForceLeftClick() &&
			event.getTarget().toLowerCase().contains("tithe");
	}

	@Override
	public void modifyEntry(MenuEntryAdded event)
	{
		if (client == null || findItem(SEED_SET).getLeft() == -1)
		{
			return;
		}

		client.createMenuEntry(-1)
			.setOption("Use")
			.setTarget("<col=ff9040>Seed<col=ffffff> -> " + getTargetMap().get(event.getIdentifier()))
			.setType(MenuAction.ITEM_USE_ON_GAME_OBJECT)
			.setIdentifier(event.getIdentifier())
			.setParam0(event.getActionParam0())
			.setParam1(event.getActionParam1())
			.setForceLeftClick(true);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		if (event.getMenuTarget().contains("<col=ff9040>Watering can<col=ffffff> -> ") &&
			event.getMenuTarget().toLowerCase().contains("water barrel"))
		{
			return updateSelectedItem(WATERING_CANS);
		}
		else if (event.getMenuTarget().contains("<col=ff9040>Seed<col=ffffff> -> ") &&
			event.getMenuTarget().toLowerCase().contains("tithe"))
		{
			return updateSelectedItem(SEED_SET);
		}
		return false;
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{

	}

	@Override
	public void backupEntryModify(MenuEntry e)
	{
		if (findItem(SEED_SET).getLeft() == -1)
		{
			return;
		}

		e.setOption("Use");
		e.setTarget("<col=ff9040>Seed<col=ffffff> -> " + getTargetMap().get(e.getIdentifier()));
		e.setForceLeftClick(true);
		insert(e);
	}
}
