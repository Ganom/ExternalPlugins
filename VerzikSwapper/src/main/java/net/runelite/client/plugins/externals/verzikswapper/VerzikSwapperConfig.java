/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * Copyright (c) 2019-2020, andrewterra <https://github.com/andrewterra>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.verzikswapper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;

@ConfigGroup(value = "VerzikSwapper")
public interface VerzikSwapperConfig extends Config
{

	@ConfigItem(
		keyName = "testRange",
		name = "Test Range Projectile",
		description = "",
		position = 0,
		titleSection = "hotkeys"
	)
	default Keybind testRange()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "testMage",
		name = "Test Mage Projectile",
		description = "",
		position = 1,
		titleSection = "hotkeys"
	)
	default Keybind testMage()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		position = 2,
		keyName = "randLow",
		name = "Minimum Delay",
		description = "For MouseEvents"
	)
	default int randLow()
	{
		return 70;
	}

	@ConfigItem(
		position = 3,
		keyName = "randLower",
		name = "Maximum Delay",
		description = "For MouseEvents"
	)
	default int randHigh()
	{
		return 80;
	}
}

