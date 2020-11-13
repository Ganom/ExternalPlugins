package net.runelite.client.plugins.externals.oneclick.comparables.skilling;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

public class Birdhouses extends ClickCompare
{
	private static final Set<Integer> HOPS_SEED = ImmutableSet.of(
		ItemID.BARLEY_SEED, ItemID.HAMMERSTONE_SEED, ItemID.ASGARNIAN_SEED,
		ItemID.JUTE_SEED, ItemID.YANILLIAN_SEED, ItemID.KRANDORIAN_SEED, ItemID.WILDBLOOD_SEED
	);

	private static final Set<String> BIRD_HOUSES_NAMES = ImmutableSet.of(
		"<col=ffff>Bird house (empty)", "<col=ffff>Oak birdhouse (empty)", "<col=ffff>Willow birdhouse (empty)",
		"<col=ffff>Teak birdhouse (empty)", "<col=ffff>Maple birdhouse (empty)", "<col=ffff>Mahogany birdhouse (empty)",
		"<col=ffff>Yew birdhouse (empty)", "<col=ffff>Magic birdhouse (empty)", "<col=ffff>Redwood birdhouse (empty)"
	);

	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == MenuOpcode.GAME_OBJECT_SECOND_OPTION.getId() &&
			BIRD_HOUSES_NAMES.contains(event.getTarget());
	}

	@Override
	public void modifyEntry(MenuEntry event)
	{
		if (findItem(HOPS_SEED).getLeft() == -1)
		{
			return;
		}

		MenuEntry e = event.clone();
		e.setOption("Use");
		e.setTarget("<col=ff9040>Hops seed<col=ffffff> -> " + getTargetMap().get(event.getIdentifier()));
		e.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
		e.setForceLeftClick(true);
		insert(e);
	}

	@Override
	public boolean isClickValid(MenuEntry event)
	{
		return event.getOpcode() == MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId() &&
			event.getTarget().contains("<col=ff9040>Hops seed<col=ffffff> -> ");
	}

	@Override
	public void modifyClick(MenuEntry event)
	{
		updateSelectedItem(HOPS_SEED);
	}
}
