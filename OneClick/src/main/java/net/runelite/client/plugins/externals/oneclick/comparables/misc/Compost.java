package net.runelite.client.plugins.externals.oneclick.comparables.misc;

import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

public class Compost extends ClickCompare
{
	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == MenuAction.ITEM_USE.getId() && !event.isForceLeftClick() &&
			event.getIdentifier() == ItemID.COMPOST;
	}

	@Override
	public void modifyEntry(MenuEntry event)
	{
		if (findItem(ItemID.COMPOST).getLeft() == -1)
		{
			return;
		}

		MenuEntry e = event.clone();
		e.setTarget("<col=ff9040>Saltpetre<col=ffffff> -> " + getTargetMap().get(event.getIdentifier()));
		e.setForceLeftClick(true);
		insert(e);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		return event.getMenuAction() == MenuAction.ITEM_USE &&
			event.getId() == ItemID.COMPOST;
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{
		if (updateSelectedItem(ItemID.SALTPETRE))
		{
			event.setMenuAction(MenuAction.ITEM_USE_ON_WIDGET_ITEM);
		}
	}

	@Override
	public void backupEntryModify(MenuEntry e)
	{
		if (findItem(ItemID.COMPOST).getLeft() == -1)
		{
			return;
		}

		e.setTarget("<col=ff9040>Saltpetre<col=ffffff> -> " + getTargetMap().get(e.getIdentifier()));
		e.setForceLeftClick(true);
	}
}
