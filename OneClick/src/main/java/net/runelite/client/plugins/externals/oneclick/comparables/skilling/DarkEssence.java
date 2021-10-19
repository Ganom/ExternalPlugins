package net.runelite.client.plugins.externals.oneclick.comparables.skilling;

import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

public class DarkEssence extends ClickCompare
{
	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == MenuAction.ITEM_USE.getId() && !event.isForceLeftClick() &&
			event.getIdentifier() == ItemID.CHISEL;
	}

	@Override
	public void modifyEntry(MenuEntry event)
	{
		if (findItem(ItemID.DARK_ESSENCE_BLOCK).getLeft() == -1)
		{
			return;
		}

		MenuEntry e = event.clone();
		e.setTarget("<col=ff9040>Chisel<col=ffffff> -> <col=ff9040>Dark essence block");
		e.setForceLeftClick(true);
		insert(e);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		return event.getMenuAction() == MenuAction.ITEM_USE &&
			event.getMenuTarget().contains("<col=ff9040>Chisel<col=ffffff> ->");
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{
		if (updateSelectedItem(ItemID.DARK_ESSENCE_BLOCK))
		{
			event.setMenuAction(MenuAction.ITEM_USE_ON_WIDGET_ITEM);
		}
	}

	@Override
	public void backupEntryModify(MenuEntry e)
	{
		if (findItem(ItemID.DARK_ESSENCE_BLOCK).getLeft() == -1)
		{
			return;
		}

		e.setTarget("<col=ff9040>Chisel<col=ffffff> -> <col=ff9040>Dark essence block");
		e.setForceLeftClick(true);
	}
}
