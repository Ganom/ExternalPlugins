package net.runelite.client.plugins.externals.oneclick.comparables.misc;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

public class Herbtar extends ClickCompare
{
	private static final Set<Integer> HERBS = ImmutableSet.of(
		ItemID.GUAM_LEAF, ItemID.MARRENTILL, ItemID.TARROMIN, ItemID.HARRALANDER
	);

	@Override
	public boolean isEntryValid(MenuEntryAdded event)
	{
		return event.getOpcode() == MenuAction.ITEM_USE.getId() && !event.isForceLeftClick() &&
			HERBS.contains(event.getIdentifier());
	}

	@Override
	public void modifyEntry(MenuEntryAdded event)
	{
		if (client == null ||
			findItem(ItemID.SWAMP_TAR).getLeft() == -1 ||
			findItem(ItemID.PESTLE_AND_MORTAR).getLeft() == -1
		)
		{
			return;
		}
		client.createMenuEntry(-1)
			.setOption(event.getOption())
			.setTarget("<col=ff9040>Swamp tar<col=ffffff> -> " + getTargetMap().get(event.getIdentifier()))
			.setType(MenuAction.ITEM_USE_ON_WIDGET_ITEM)
			.setIdentifier(event.getIdentifier())
			.setParam0(event.getActionParam0())
			.setParam1(event.getActionParam1())
			.setForceLeftClick(true);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		return event.getMenuTarget().contains("<col=ff9040>Swamp tar<col=ffffff> -> ") && updateSelectedItem(ItemID.SWAMP_TAR);
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{

	}

	@Override
	public void backupEntryModify(MenuEntry e)
	{
		if (findItem(ItemID.SWAMP_TAR).getLeft() == -1 ||
			findItem(ItemID.PESTLE_AND_MORTAR).getLeft() == -1
		)
		{
			return;
		}
		e.setTarget("<col=ff9040>Swamp tar<col=ffffff> -> " + getTargetMap().get(e.getIdentifier()));
		e.setForceLeftClick(true);
	}
}
