package net.runelite.client.plugins.externals.oneclick.comparables.misc;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

public class Herbtar extends ClickCompare
{
	private static final Set<Integer> HERBS = ImmutableSet.of(
		ItemID.GUAM_LEAF, ItemID.MARRENTILL, ItemID.TARROMIN, ItemID.HARRALANDER
	);

	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == MenuAction.ITEM_USE.getId() && !event.isForceLeftClick() &&
			HERBS.contains(event.getIdentifier());
	}

	@Override
	public void modifyEntry(MenuEntry event)
	{
		if (findItem(ItemID.SWAMP_TAR).getLeft() == -1 ||
			findItem(ItemID.PESTLE_AND_MORTAR).getLeft() == -1
		)
		{
			return;
		}
		MenuEntry e = event.clone();
		e.setTarget("<col=ff9040>Swamp tar<col=ffffff> -> " + getTargetMap().get(event.getIdentifier()));
		e.setForceLeftClick(true);
		insert(e);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		return event.getMenuAction() == MenuAction.ITEM_USE &&
			event.getMenuTarget().contains("<col=ff9040>Swamp tar<col=ffffff> -> ");
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{
		if (updateSelectedItem(ItemID.SWAMP_TAR))
		{
			event.setMenuAction(MenuAction.ITEM_USE_ON_WIDGET_ITEM);
		}
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
