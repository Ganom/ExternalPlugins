package net.runelite.client.plugins.externals.oneclick.comparables.skilling;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

public class Birdhouses extends ClickCompare
{
	private static final Set<Integer> HOPS_SEED = ImmutableSet.of(
		ItemID.BARLEY_SEED, ItemID.HAMMERSTONE_SEED, ItemID.ASGARNIAN_SEED,
		ItemID.JUTE_SEED, ItemID.YANILLIAN_SEED, ItemID.KRANDORIAN_SEED, ItemID.WILDBLOOD_SEED
	);

	private static final Set<String> BIRD_HOUSES_NAMES = ImmutableSet.of(
		"<col=ffff>Birdhouse (empty)", "<col=ffff>Oak birdhouse (empty)", "<col=ffff>Willow birdhouse (empty)",
		"<col=ffff>Teak birdhouse (empty)", "<col=ffff>Maple birdhouse (empty)", "<col=ffff>Mahogany birdhouse (empty)",
		"<col=ffff>Yew birdhouse (empty)", "<col=ffff>Magic birdhouse (empty)", "<col=ffff>Redwood birdhouse (empty)"
	);

	@Override
	public boolean isEntryValid(MenuEntryAdded event)
	{
		return event.getType() == MenuAction.GAME_OBJECT_SECOND_OPTION.getId() && !event.isForceLeftClick() &&
			BIRD_HOUSES_NAMES.contains(event.getTarget());
	}

	@Override
	public void modifyEntry(MenuEntryAdded event)
	{
		if (client == null || findItem(HOPS_SEED).getLeft() == -1)
		{
			return;
		}

		client.createMenuEntry(-1)
			.setOption("Use")
			.setTarget("<col=ff9040>Hops seed<col=ffffff> -> " + getTargetMap().get(event.getIdentifier()))
			.setType(MenuAction.ITEM_USE_ON_GAME_OBJECT)
			.setIdentifier(event.getIdentifier())
			.setParam0(event.getActionParam0())
			.setParam1(event.getActionParam1())
			.setForceLeftClick(true);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		return event.getMenuTarget().contains("<col=ff9040>Hops seed<col=ffffff> -> ") && updateSelectedItem(HOPS_SEED);
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{
	}

	@Override
	public void backupEntryModify(MenuEntry e)
	{
		if (findItem(HOPS_SEED).getLeft() == -1)
		{
			return;
		}

		e.setOption("Use");
		e.setTarget("<col=ff9040>Hops seed<col=ffffff> -> " + getTargetMap().get(e.getIdentifier()));
		e.setOpcode(MenuAction.ITEM_USE_ON_GAME_OBJECT.getId());
		e.setForceLeftClick(true);
	}
}
