package net.runelite.client.plugins.externals.oneclick.Comparables;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.client.plugins.externals.oneclick.OneClickPlugin;

public class Darts implements ClickComparable
{
	private static final Set<Integer> BOLTS = ImmutableSet.of(
		ItemID.BRONZE_BOLTS_UNF, ItemID.IRON_BOLTS_UNF, ItemID.STEEL_BOLTS_UNF,
		ItemID.MITHRIL_BOLTS_UNF, ItemID.ADAMANT_BOLTSUNF, ItemID.RUNITE_BOLTS_UNF,
		ItemID.DRAGON_BOLTS_UNF, ItemID.UNFINISHED_BROAD_BOLTS
	);
	private static final Set<Integer> DART_TIPS = ImmutableSet.of(
		ItemID.BRONZE_DART_TIP, ItemID.IRON_DART_TIP, ItemID.STEEL_DART_TIP,
		ItemID.MITHRIL_DART_TIP, ItemID.ADAMANT_DART_TIP, ItemID.RUNE_DART_TIP,
		ItemID.DRAGON_DART_TIP
	);

	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == MenuOpcode.ITEM_USE.getId() &&
			(DART_TIPS.contains(event.getIdentifier()) || BOLTS.contains(event.getIdentifier()));
	}

	@Override
	public void modifyEntry(OneClickPlugin plugin, MenuEntry event)
	{
		if (plugin.findItem(ItemID.FEATHER).getLeft() == -1)
		{
			return;
		}

		event.setTarget("<col=ff9040>Feather<col=ffffff> -> " + plugin.getTargetMap().get(event.getIdentifier()));
		event.setForceLeftClick(true);
	}

	@Override
	public boolean isClickValid(MenuEntry event)
	{
		return event.getOpcode() == MenuOpcode.ITEM_USE.getId() &&
			event.getTarget().contains("<col=ff9040>Feather<col=ffffff> -> ");
	}

	@Override
	public void modifyClick(OneClickPlugin plugin, MenuEntry event)
	{
		if (plugin.updateSelectedItem(ItemID.FEATHER))
		{
			event.setOpcode(MenuOpcode.ITEM_USE_ON_WIDGET_ITEM.getId());
		}
	}
}
