/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.autoclicker;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigTitleSection;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.Range;
import net.runelite.client.config.Title;

@ConfigGroup("autoClickerConfig")
public interface AutoClickConfig extends Config
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
		keyName = "toggle",
		name = "Toggle",
		description = "Toggles the clicker.",
		position = 0,
		titleSection = "mainConfig"
	)
	default Keybind toggle()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "miniDelay",
		name = "Absolute Delay Min",
		description = "",
		position = 3,
		titleSection = "mainConfig"
	)
	default int min()
	{
		return 120;
	}

	@ConfigItem(
		keyName = "maxiDelay",
		name = "Absolute Delay Max",
		description = "",
		position = 4,
		titleSection = "mainConfig"
	)
	default int max()
	{
		return 240;
	}

	@ConfigItem(
		keyName = "target",
		name = "Delay Target",
		description = "",
		position = 5,
		titleSection = "mainConfig"
	)
	default int target()
	{
		return 180;
	}

	@ConfigItem(
		keyName = "deviation",
		name = "Delay Deviation",
		description = "",
		position = 6,
		titleSection = "mainConfig"
	)
	default int deviation()
	{
		return 10;
	}

	@ConfigItem(
		keyName = "weightedDistribution",
		name = "Weighted Distribution",
		description = "Shifts the random distribution towards the lower end at the target, otherwise it will be an even distribution",
		position = 7,
		titleSection = "mainConfig"
	)
	default boolean weightedDistribution()
	{
		return false;
	}

	@ConfigTitleSection(
		position = 1,
		keyName = "helperConfig",
		name = "Helper Config",
		description = ""
	)
	default Title helperConfig()
	{
		return new Title();
	}

	@ConfigItem(
		keyName = "autoDisable",
		name = "Auto Disable at low HP",
		description = "Automatically disables the clicker when you get to low hp.",
		position = 0,
		titleSection = "helperConfig"
	)
	default boolean autoDisableHp()
	{
		return false;
	}

	@ConfigItem(
		keyName = "autoDisableInv",
		name = "Disable when Inv is full",
		description = "Automatically disables the clicker when your inventory is full..",
		position = 1,
		titleSection = "helperConfig"
	)
	default boolean autoDisableInv()
	{
		return false;
	}

	@Range(
		min = 5,
		max = 98
	)
	@ConfigItem(
		keyName = "hpThreshold",
		name = "Hp Threshold",
		description = "The hp in which the plugin will auto disable.",
		position = 2,
		hidden = true,
		unhide = "autoDisable",
		titleSection = "helperConfig"
	)
	default int hpThreshold()
	{
		return 200;
	}

	@ConfigItem(
		keyName = "flash",
		name = "Flash on Low HP",
		description = "Your Screen flashes when you get to low hp.",
		position = 3,
		hidden = true,
		unhide = "autoDisable",
		titleSection = "helperConfig"
	)
	default boolean flash()
	{
		return false;
	}
}
