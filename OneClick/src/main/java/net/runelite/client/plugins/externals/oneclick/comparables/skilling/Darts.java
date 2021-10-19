package net.runelite.client.plugins.externals.oneclick.comparables.skilling;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

public class Darts extends ClickCompare
{
	private static final Set<Integer> BOLTS = ImmutableSet.of(
		ItemID.BRONZE_BOLTS_UNF, ItemID.IRON_BOLTS_UNF, ItemID.STEEL_BOLTS_UNF,
		ItemID.MITHRIL_BOLTS_UNF, ItemID.ADAMANT_BOLTSUNF, ItemID.RUNITE_BOLTS_UNF,
		ItemID.DRAGON_BOLTS_UNF, ItemID.UNFINISHED_BROAD_BOLTS
	);
	private static final Set<Integer> DART_TIPS = ImmutableSet.of(
		ItemID.BRONZE_DART_TIP, ItemID.IRON_DART_TIP, ItemID.STEEL_DART_TIP,
		ItemID.MITHRIL_DART_TIP, ItemID.ADAMANT_DART_TIP, ItemID.RUNE_DART_TIP,
		ItemID.AMETHYST_DART_TIP, ItemID.DRAGON_DART_TIP
	);

	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == MenuAction.ITEM_USE.getId() && !event.isForceLeftClick() &&
			(DART_TIPS.contains(event.getIdentifier()) || BOLTS.contains(event.getIdentifier()));
	}

	@Override
	public void modifyEntry(MenuEntry event)
	{
		if (findItem(ItemID.FEATHER).getLeft() == -1)
		{
			return;
		}

		MenuEntry e = event.clone();
		e.setTarget("<col=ff9040>Feather<col=ffffff> -> " + getTargetMap().get(event.getIdentifier()));
		e.setForceLeftClick(true);
		insert(e);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		return event.getMenuAction() == MenuAction.ITEM_USE &&
			event.getMenuTarget().contains("<col=ff9040>Feather<col=ffffff> -> ");
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{
		if (updateSelectedItem(ItemID.FEATHER))
		{
			event.setMenuAction(MenuAction.ITEM_USE_ON_WIDGET_ITEM);
		}
	}

	@Override
	public void backupEntryModify(MenuEntry e)
	{
		if (findItem(ItemID.FEATHER).getLeft() == -1)
		{
			return;
		}

		e.setTarget("<col=ff9040>Feather<col=ffffff> -> " + getTargetMap().get(e.getIdentifier()));
		e.setForceLeftClick(true);
	}
}
