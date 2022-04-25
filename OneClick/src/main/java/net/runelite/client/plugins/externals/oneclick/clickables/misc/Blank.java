package net.runelite.client.plugins.externals.oneclick.clickables.misc;

import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.clickables.Clickable;

public class Blank extends Clickable
{
	@Override
	public boolean isValidEntry(MenuEntryAdded event)
	{
		return false;
	}

	@Override
	public boolean isValidClick(MenuOptionClicked event)
	{
		return false;
	}
}
