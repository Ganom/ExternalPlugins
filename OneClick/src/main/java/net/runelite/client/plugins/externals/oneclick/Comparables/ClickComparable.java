package net.runelite.client.plugins.externals.oneclick.Comparables;

import net.runelite.api.MenuEntry;
import net.runelite.client.plugins.externals.oneclick.OneClickPlugin;

public interface ClickComparable
{
	boolean isEntryValid(MenuEntry event);

	void modifyEntry(OneClickPlugin plugin, MenuEntry event);

	boolean isClickValid(MenuEntry event);

	void modifyClick(OneClickPlugin plugin, MenuEntry event);
}
