package net.runelite.client.plugins.externals.oneclick.Comparables;

import net.runelite.api.MenuEntry;
import net.runelite.client.plugins.externals.oneclick.OneClickPlugin;

public class Blank implements ClickComparable
{
	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return false;
	}

	@Override
	public void modifyEntry(OneClickPlugin plugin, MenuEntry event)
	{

	}

	@Override
	public boolean isClickValid(MenuEntry event)
	{
		return false;
	}

	@Override
	public void modifyClick(OneClickPlugin plugin, MenuEntry event)
	{

	}
}
