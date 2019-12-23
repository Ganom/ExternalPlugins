/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.customswapper.utils;

import javax.inject.Inject;
import net.runelite.api.Client;

public class TabUtils
{
	private final Client client;

	@Inject
	public TabUtils(final Client client)
	{
		this.client = client;
	}

	public int getTabHotkey(Tab tab)
	{
		final int var = client.getVar(tab.getVarbit());
		final int offset = 111;

		switch (var)
		{
			case 1:
			case 2:
			case 3:
			case 4:
			case 5:
			case 6:
			case 7:
			case 8:
			case 9:
			case 10:
			case 11:
			case 12:
				return var + offset;
			case 13:
				return 27;
			default:
				return -1;
		}
	}
}