/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.basicbossswapper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.config.Keybind;

@ConfigGroup("BasicBossSwapperConfig")
public interface BasicBossSwapperConfig extends Config
{
	@ConfigSection(
			position = 0,
			keyName = "botConfig",
			name = "Bot Config",
			description = ""
	)
	String mainConfig = "Bot Config";

	@ConfigItem(
			keyName = "toggle",
			name = "Toggle",
			description = "",
			position = 1,
			section = mainConfig
	)
	default Keybind toggle()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
			position = 2,
			keyName = "randLow",
			name = "Minimum Delay",
			description = "",
			section = mainConfig
	)
	default int randLow()
	{
		return 70;
	}

	@ConfigItem(
			position = 3,
			keyName = "randLower",
			name = "Maximum Delay",
			description = "",
			section = mainConfig
	)
	default int randHigh()
	{
		return 80;
	}

	@ConfigSection(
			position = 1,
			keyName = "olmConfig",
			name = "Olm Config",
			description = ""
	)
	String olmConfig = "Olm Config";

	@ConfigItem(
			position = 0,
			keyName = "swapAutos",
			name = "Swap on Auto Attacks",
			description = "This will swap prayers for olms auto attacks as-well.",
			section = olmConfig
	)
	default boolean swapAutos()
	{
		return false;
	}

	@ConfigSection(
			position = 2,
			keyName = "nyloConfig",
			name = "Nylo Config",
			description = ""
	)
	String nyloConfig = "Nylo Config";

	@ConfigItem(
			position = 1,
			keyName = "mage",
			name = "Mage Gearswap",
			description = "Mage Gearswap Item Ids",
			section = nyloConfig
	)
	default String mage()
	{
		return "11663,22323,21795,12002";
	}

	@ConfigItem(
			position = 2,
			keyName = "range",
			name = "Range Gearswap",
			description = "Range Gearswap Item Ids",
			section = nyloConfig
	)
	default String range()
	{
		return "11664,12926,22109,19547";
	}

	@ConfigItem(
			position = 3,
			keyName = "melee",
			name = "Melee Gearswap",
			description = "Melee Gearswap Item Ids",
			section = nyloConfig
	)
	default String melee()
	{
		return "11665,12006,6570,19553,12954";
	}
}
