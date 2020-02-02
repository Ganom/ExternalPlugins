package net.runelite.client.plugins.externals.oneclick.Comparables;

import net.runelite.api.MenuEntry;
import net.runelite.client.plugins.externals.oneclick.OneClickPlugin;

public interface ClickComparable
{
	boolean isEntryValid(MenuEntry entry);

	void modifyEntry(OneClickPlugin plugin, MenuEntry entry);

	boolean isClickValid(MenuEntry entry);

	void modifyClick(OneClickPlugin plugin, MenuEntry entry);
}
