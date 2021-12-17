package net.runelite.client.plugins.externals.oneclick.comparables.skilling;

import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

public class Tiara extends ClickCompare
{
	@Override
	public boolean isEntryValid(MenuEntryAdded event)
	{
		return event.getOpcode() == MenuAction.GAME_OBJECT_FIRST_OPTION.getId() && !event.isForceLeftClick() &&
			event.getOption().equals("Craft-rune") &&
			event.getTarget().equals("<col=ffff>Altar");
	}

	@Override
	public void modifyEntry(MenuEntryAdded event)
	{
		if (client == null || findItem(ItemID.TIARA).getLeft() == -1)
		{
			return;
		}

		client.createMenuEntry(-1)
			.setOption("Use")
			.setTarget("<col=ff9040>Tiara<col=ffffff> -> <col=ffff>Altar")
			.setType(MenuAction.ITEM_USE_ON_GAME_OBJECT)
			.setIdentifier(event.getIdentifier())
			.setParam0(event.getActionParam0())
			.setParam1(event.getActionParam1())
			.setForceLeftClick(true);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		return event.getMenuTarget().equals("<col=ff9040>Tiara<col=ffffff> -> <col=ffff>Altar") && updateSelectedItem(ItemID.TIARA);
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{

	}

	@Override
	public void backupEntryModify(MenuEntry e)
	{
		if (findItem(ItemID.TIARA).getLeft() == -1)
		{
			return;
		}
		e.setOption("Use");
		e.setTarget("<col=ff9040>Tiara<col=ffffff> -> <col=ffff>Altar");
		e.setForceLeftClick(true);
	}
}
