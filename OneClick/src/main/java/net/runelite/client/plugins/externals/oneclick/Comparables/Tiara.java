package net.runelite.client.plugins.externals.oneclick.Comparables;

import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.client.plugins.externals.oneclick.OneClickPlugin;

public class Tiara implements ClickComparable
{
	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
			event.getOption().equals("Craft-rune") &&
			event.getTarget().equals("<col=ffff>Altar");
	}

	@Override
	public void modifyEntry(OneClickPlugin plugin, MenuEntry event)
	{
		if (plugin.findItem(ItemID.TIARA).getLeft() == -1)
		{
			return;
		}
		event.setOption("Use");
		event.setTarget("<col=ff9040>Tiara<col=ffffff> -> <col=ffff>Altar");
		event.setForceLeftClick(true);
	}

	@Override
	public boolean isClickValid(MenuEntry event)
	{
		return event.getOpcode() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
			event.getTarget().equals("<col=ff9040>Tiara<col=ffffff> -> <col=ffff>Altar");
	}

	@Override
	public void modifyClick(OneClickPlugin plugin, MenuEntry event)
	{
		if (plugin.updateSelectedItem(ItemID.TIARA))
		{
			event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
		}
	}
}
