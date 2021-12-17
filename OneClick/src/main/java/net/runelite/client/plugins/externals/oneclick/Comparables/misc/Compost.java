package net.runelite.client.plugins.externals.oneclick.comparables.misc;

import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import static net.runelite.api.MenuAction.ITEM_USE_ON_WIDGET_ITEM;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

public class Compost extends ClickCompare
{
	@Override
	public boolean isEntryValid(MenuEntryAdded event)
	{
		return event.getOpcode() == MenuAction.ITEM_USE.getId() && !event.isForceLeftClick() &&
			event.getIdentifier() == ItemID.COMPOST;
	}

	@Override
	public void modifyEntry(MenuEntryAdded event)
	{
		if (client == null || findItem(ItemID.COMPOST).getLeft() == -1)
		{
			return;
		}

		client.createMenuEntry(-1)
			.setOption(event.getOption())
			.setTarget("<col=ff9040>Saltpetre<col=ffffff> -> " + getTargetMap().get(event.getIdentifier()))
			.setType(ITEM_USE_ON_WIDGET_ITEM)
			.setIdentifier(event.getIdentifier())
			.setParam0(event.getActionParam0())
			.setParam1(event.getActionParam1())
			.setForceLeftClick(true);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		return event.getId() == ItemID.COMPOST && updateSelectedItem(ItemID.SALTPETRE);
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{
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
