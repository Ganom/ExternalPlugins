/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.customswapper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigTitleSection;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Title;

@ConfigGroup("customSwapper")
public interface CustomSwapperConfig extends Config
{
	@ConfigTitleSection(
		position = 0,
		keyName = "mainConfig",
		name = "Main Config",
		description = ""
	)
	default Title mainConfig()
	{
		return new Title();
	}

	@ConfigItem(
		keyName = "customSwapOne",
		name = "Swap Set: One",
		description = "",
		position = 1,
		titleSection = "mainConfig"
	)
	default String customSwapOne()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapTwo",
		name = "Swap Set: Two",
		description = "",
		position = 2,
		titleSection = "mainConfig"
	)
	default String customSwapTwo()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapThree",
		name = "Swap Set: Three",
		description = "",
		position = 3,
		titleSection = "mainConfig"
	)
	default String customSwapThree()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapFour",
		name = "Swap Set: Four",
		description = "",
		position = 4,
		titleSection = "mainConfig"
	)
	default String customSwapFour()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapFive",
		name = "Swap Set: Five",
		description = "",
		position = 5,
		titleSection = "mainConfig"
	)
	default String customSwapFive()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapSix",
		name = "Swap Set: Six",
		description = "",
		position = 6,
		titleSection = "mainConfig"
	)
	default String customSwapSix()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapSeven",
		name = "Swap Set: Seven",
		description = "",
		position = 7,
		titleSection = "mainConfig"
	)
	default String customSwapSeven()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapEight",
		name = "Swap Set: Eight",
		description = "",
		position = 8,
		titleSection = "mainConfig"
	)
	default String customSwapEight()
	{
		return "";
	}


	@ConfigTitleSection(
		position = 9,
		keyName = "hotkeys",
		name = "Hotkeys",
		description = ""
	)
	default Title hotkeys()
	{
		return new Title();
	}

	@ConfigItem(
		keyName = "customOne",
		name = "Execute One",
		description = "",
		position = 10,
		titleSection = "hotkeys"
	)
	default Keybind customOne()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customTwo",
		name = "Execute Two",
		description = "",
		position = 11,
		titleSection = "hotkeys"
	)
	default Keybind customTwo()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customThree",
		name = "Execute Three",
		description = "",
		position = 12,
		titleSection = "hotkeys"
	)
	default Keybind customThree()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customFour",
		name = "Execute Four",
		description = "",
		position = 13,
		titleSection = "hotkeys"
	)
	default Keybind customFour()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customFive",
		name = "Execute Five",
		description = "",
		position = 14,
		titleSection = "hotkeys"
	)
	default Keybind customFive()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customSix",
		name = "Execute Six",
		description = "",
		position = 15,
		titleSection = "hotkeys"
	)
	default Keybind customSix()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customSeven",
		name = "Execute Seven",
		description = "",
		position = 16,
		titleSection = "hotkeys"
	)
	default Keybind customSeven()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customEight",
		name = "Execute Eight",
		description = "",
		position = 17,
		titleSection = "hotkeys"
	)
	default Keybind customEight()
	{
		return Keybind.NOT_SET;
	}

	@ConfigTitleSection(
		keyName = "config",
		name = "Config",
		description = "",
		position = 18
	)
	default Title config()
	{
		return new Title();
	}

	@ConfigItem(
		keyName = "swapBack",
		name = "Swap back to inventory",
		description = "Once finished with a swap, should it swap back to inventory?",
		titleSection = "config",
		position = 19
	)
	default boolean swapBack()
	{
		return true;
	}

	@ConfigItem(
		keyName = "randLow",
		name = "Minimum MS Delay",
		description = "Dont set this too high.",
		titleSection = "config",
		position = 20
	)
	default int randLow()
	{
		return 70;
	}

	@ConfigItem(
		keyName = "randLower",
		name = "Maximum MS Delay",
		description = "Dont set this too high.",
		titleSection = "config",
		position = 21
	)
	default int randHigh()
	{
		return 80;
	}
}
