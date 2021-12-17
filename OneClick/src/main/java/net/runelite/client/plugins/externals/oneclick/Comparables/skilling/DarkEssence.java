package net.runelite.client.plugins.externals.oneclick.comparables.skilling;

import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

public class DarkEssence extends ClickCompare
{
	@Override
	public boolean isEntryValid(MenuEntryAdded event)
	{
		return event.getOpcode() == MenuAction.ITEM_USE.getId() && !event.isForceLeftClick() &&
			event.getIdentifier() == ItemID.CHISEL;
	}

	@Override
	public void modifyEntry(MenuEntryAdded event)
	{
		if (client == null || findItem(ItemID.DARK_ESSENCE_BLOCK).getLeft() == -1)
		{
			return;
		}

		client.createMenuEntry(-1)
			.setOption(event.getOption())
			.setTarget("<col=ff9040>Chisel<col=ffffff> -> <col=ff9040>Dark essence block")
			.setType(MenuAction.ITEM_USE_ON_WIDGET_ITEM)
			.setIdentifier(event.getIdentifier())
			.setParam0(event.getActionParam0())
			.setParam1(event.getActionParam1())
			.setForceLeftClick(true);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		return event.getMenuTarget().contains("<col=ff9040>Chisel<col=ffffff> ->") && updateSelectedItem(ItemID.DARK_ESSENCE_BLOCK);
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{
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
