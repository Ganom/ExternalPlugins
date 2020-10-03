package net.runelite.client.plugins.externals.oneclick.comparables.skilling;

import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

public class Tiara extends ClickCompare
{
	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
			event.getOption().equals("Craft-rune") &&
			event.getTarget().equals("<col=ffff>Altar");
	}

	@Override
	public void modifyEntry(MenuEntry event)
	{
		if (findItem(ItemID.TIARA).getLeft() == -1)
		{
			return;
		}
		MenuEntry e = event.clone();
		e.setOption("Use");
		e.setTarget("<col=ff9040>Tiara<col=ffffff> -> <col=ffff>Altar");
		e.setForceLeftClick(true);
		insert(e);
	}

	@Override
	public boolean isClickValid(MenuEntry event)
	{
		return event.getOpcode() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
			event.getTarget().equals("<col=ff9040>Tiara<col=ffffff> -> <col=ffff>Altar");
	}

	@Override
	public void modifyClick(MenuEntry event)
	{
		if (updateSelectedItem(ItemID.TIARA))
		{
			event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
		}
	}
}
