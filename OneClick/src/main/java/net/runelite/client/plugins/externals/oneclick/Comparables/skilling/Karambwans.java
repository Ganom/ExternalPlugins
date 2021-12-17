package net.runelite.client.plugins.externals.oneclick.comparables.skilling;

import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

public class Karambwans extends ClickCompare
{
	@Override
	public boolean isEntryValid(MenuEntryAdded event)
	{
		return event.getOpcode() == MenuAction.GAME_OBJECT_FIRST_OPTION.getId() && !event.isForceLeftClick() &&
			event.getOption().equals("Cook");
	}

	@Override
	public void modifyEntry(MenuEntryAdded event)
	{
		if (client == null || findItem(ItemID.RAW_KARAMBWAN).getLeft() == -1)
		{
			return;
		}

		client.createMenuEntry(-1)
			.setOption("Use")
			.setTarget("<col=ff9040>Raw karambwan<col=ffffff> -> " + getTargetMap().get(event.getIdentifier()))
			.setType(MenuAction.ITEM_USE_ON_GAME_OBJECT)
			.setIdentifier(event.getIdentifier())
			.setParam0(event.getActionParam0())
			.setParam1(event.getActionParam1())
			.setForceLeftClick(true);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		return event.getMenuTarget().contains("<col=ff9040>Raw karambwan<col=ffffff> -> ") && updateSelectedItem(ItemID.RAW_KARAMBWAN);
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{
		if (plugin != null)
		{
			plugin.setTick(true);
		}
	}

	@Override
	public void backupEntryModify(MenuEntry e)
	{
		if (findItem(ItemID.RAW_KARAMBWAN).getLeft() == -1)
		{
			return;
		}
		e.setOption("Use");
		e.setTarget("<col=ff9040>Raw karambwan<col=ffffff> -> " + getTargetMap().get(e.getIdentifier()));
		e.setForceLeftClick(true);
		insert(e);
	}
}
