package net.runelite.client.plugins.externals.oneclick.Comparables;

import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.client.plugins.externals.oneclick.OneClickPlugin;

public class Karambwans implements ClickComparable
{
	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
			event.getOption().equals("Cook");
	}

	@Override
	public void modifyEntry(OneClickPlugin plugin, MenuEntry event)
	{

		if (plugin.findItem(ItemID.RAW_KARAMBWAN).getLeft() == -1)
		{
			return;
		}
		event.setOption("Use");
		event.setTarget("<col=ff9040>Raw karambwan<col=ffffff> -> " + event.getTarget());
		event.setForceLeftClick(true);
	}

	@Override
	public boolean isClickValid(MenuEntry event)
	{
		return event.getOpcode() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
			event.getTarget().contains("<col=ff9040>Raw karambwan<col=ffffff> -> ");
	}

	@Override
	public void modifyClick(OneClickPlugin plugin, MenuEntry event)
	{
		if (plugin.updateSelectedItem(ItemID.RAW_KARAMBWAN))
		{
			event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
			plugin.setTick(true);
		}
	}
}
