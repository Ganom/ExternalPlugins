package net.runelite.client.plugins.externals.oneclick.comparables.skilling;

import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

public class Seaweed extends ClickCompare
{

	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == MenuAction.EXAMINE_OBJECT.getId() && !event.isForceLeftClick() &&
				event.getTarget().toLowerCase().contains("fire");
	}

	@Override
	public void modifyEntry(MenuEntry event)
	{
		if (findItem(ItemID.GIANT_SEAWEED).getLeft() == -1)
		{
			return;
		}

		MenuEntry e = event.clone();
		e.setOption("Use");
		e.setTarget("<col=ff9040>Hops seed<col=ffffff> -> " + getTargetMap().get(event.getIdentifier()));
		e.setOpcode(MenuAction.ITEM_USE_ON_GAME_OBJECT.getId());
		e.setForceLeftClick(true);
		insert(e);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		return event.getMenuAction() == MenuAction.ITEM_USE_ON_GAME_OBJECT &&
				event.getMenuTarget().contains("<col=ff9040>Hops seed<col=ffffff> -> ");
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{
		updateSelectedItem(ItemID.GIANT_SEAWEED);
	}

	@Override
	public void backupEntryModify(MenuEntry e)
	{
		if (findItem(ItemID.GIANT_SEAWEED).getLeft() == -1)
		{
			return;
		}

		e.setOption("Use");
		e.setTarget("<col=ff9040>Hops seed<col=ffffff> -> " + getTargetMap().get(e.getIdentifier()));
		e.setOpcode(MenuAction.ITEM_USE_ON_GAME_OBJECT.getId());
		e.setForceLeftClick(true);
	}
}
