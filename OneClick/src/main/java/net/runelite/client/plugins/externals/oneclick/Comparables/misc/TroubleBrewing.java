package net.runelite.client.plugins.externals.oneclick.comparables.misc;

import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

public class TroubleBrewing extends ClickCompare
{
	@Override
	public boolean isEntryValid(MenuEntryAdded event)
	{
		return event.getType() == MenuAction.EXAMINE_OBJECT.getId() && !event.isForceLeftClick() &&
			event.getTarget().toLowerCase().contains("hopper");
	}

	@Override
	public void modifyEntry(MenuEntryAdded event)
	{
		if (client == null || findItem(1929).getLeft() == -1)
		{
			return;
		}

		client.createMenuEntry(-1)
			.setOption("Use")
			.setTarget("<col=ff9040>Water Bucket<col=ffffff> -> " + getTargetMap().get(event.getIdentifier()))
			.setType(MenuAction.ITEM_USE_ON_GAME_OBJECT)
			.setIdentifier(event.getIdentifier())
			.setParam0(event.getActionParam0())
			.setParam1(event.getActionParam1())
			.setForceLeftClick(true);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		if (event.getMenuTarget().contains("<col=ff9040>Water Bucket<col=ffffff> -> "))
		{
			return updateSelectedItem(1929);
		}
		return false;
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{

	}

	@Override
	public void backupEntryModify(MenuEntry e)
	{

	}
}
