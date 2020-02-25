/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.customswapper;

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
		keyName = "config",
		name = "Config",
		description = "",
		position = 0
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
		position = 1
	)
	default boolean swapBack()
	{
		return true;
	}

	@ConfigItem(
		keyName = "enablePrayCheck",
		name = "Active Prayer Check",
		description = "Enabling this will make it so you can't toggle prayers if they're on.",
		titleSection = "config",
		position = 2
	)
	default boolean enablePrayCheck()
	{
		return true;
	}

	@ConfigItem(
		keyName = "randLow",
		name = "Minimum MS Delay",
		description = "Dont set this too high.",
		titleSection = "config",
		position = 3
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
		position = 4
	)
	default int randHigh()
	{
		return 80;
	}

	@ConfigTitleSection(
		position = 1,
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

	@ConfigItem(
		keyName = "customSwapNine",
		name = "Swap Set: Nine",
		description = "",
		position = 9,
		titleSection = "mainConfig"
	)
	default String customSwapNine()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapTen",
		name = "Swap Set: Ten",
		description = "",
		position = 10,
		titleSection = "mainConfig"
	)
	default String customSwapTen()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapEleven",
		name = "Swap Set: Eleven",
		description = "",
		position = 11,
		titleSection = "mainConfig"
	)
	default String customSwapEleven()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapTwelve",
		name = "Swap Set: Twelve",
		description = "",
		position = 12,
		titleSection = "mainConfig"
	)
	default String customSwapTwelve()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapThirteen",
		name = "Swap Set: Thirteen",
		description = "",
		position = 13,
		titleSection = "mainConfig"
	)
	default String customSwapThirteen()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapFourteen",
		name = "Swap Set: Fourteen",
		description = "",
		position = 14,
		titleSection = "mainConfig"
	)
	default String customSwapFourteen()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapFifteen",
		name = "Swap Set: Fifteen",
		description = "",
		position = 15,
		titleSection = "mainConfig"
	)
	default String customSwapFifteen()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapSixteen",
		name = "Swap Set: Sixteen",
		description = "",
		position = 16,
		titleSection = "mainConfig"
	)
	default String customSwapSixteen()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapSeventeen",
		name = "Swap Set: Seventeen",
		description = "",
		position = 17,
		titleSection = "mainConfig"
	)
	default String customSwapSeventeen()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapEighteen",
		name = "Swap Set: Eighteen",
		description = "",
		position = 18,
		titleSection = "mainConfig"
	)
	default String customSwapEighteen()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapNineteen",
		name = "Swap Set: Nineteen",
		description = "",
		position = 19,
		titleSection = "mainConfig"
	)
	default String customSwapNineteen()
	{
		return "";
	}

	@ConfigItem(
		keyName = "customSwapTwenty",
		name = "Swap Set: Twenty",
		description = "",
		position = 20,
		titleSection = "mainConfig"
	)
	default String customSwapTwenty()
	{
		return "";
	}

	@ConfigTitleSection(
		position = 21,
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
		position = 22,
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
		position = 23,
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
		position = 24,
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
		position = 25,
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
		position = 26,
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
		position = 27,
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
		position = 28,
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
		position = 29,
		titleSection = "hotkeys"
	)
	default Keybind customEight()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customNine",
		name = "Execute Nine",
		description = "",
		position = 30,
		titleSection = "hotkeys"
	)
	default Keybind customNine()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customTen",
		name = "Execute Ten",
		description = "",
		position = 31,
		titleSection = "hotkeys"
	)
	default Keybind customTen()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customEleven",
		name = "Execute Eleven",
		description = "",
		position = 32,
		titleSection = "hotkeys"
	)
	default Keybind customEleven()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customTwelve",
		name = "Execute Twelve",
		description = "",
		position = 33,
		titleSection = "hotkeys"
	)
	default Keybind customTwelve()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customThirteen",
		name = "Execute Thirteen",
		description = "",
		position = 34,
		titleSection = "hotkeys"
	)
	default Keybind customThirteen()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customFourteen",
		name = "Execute Fourteen",
		description = "",
		position = 35,
		titleSection = "hotkeys"
	)
	default Keybind customFourteen()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customFifteen",
		name = "Execute Fifteen",
		description = "",
		position = 36,
		titleSection = "hotkeys"
	)
	default Keybind customFifteen()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customSixteen",
		name = "Execute Sixteen",
		description = "",
		position = 37,
		titleSection = "hotkeys"
	)
	default Keybind customSixteen()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customSeventeen",
		name = "Execute Seventeen",
		description = "",
		position = 38,
		titleSection = "hotkeys"
	)
	default Keybind customSeventeen()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customEighteen",
		name = "Execute Eighteen",
		description = "",
		position = 39,
		titleSection = "hotkeys"
	)
	default Keybind customEighteen()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customNineteen",
		name = "Execute Nineteen",
		description = "",
		position = 40,
		titleSection = "hotkeys"
	)
	default Keybind customNineteen()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "customTwenty",
		name = "Execute Twenty",
		description = "",
		position = 41,
		titleSection = "hotkeys"
	)
	default Keybind customTwenty()
	{
		return Keybind.NOT_SET;
	}
}
