/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.olmswapper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value = "OlmSwapper")
public interface OlmSwapperConfig extends Config
{
	@ConfigItem(
		position = 0,
		keyName = "swapAutos",
		name = "Swap on Auto Attacks",
		description = "This will swap prayers for olms auto attacks as-well."
	)
	default boolean swapAutos()
	{
		return false;
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

