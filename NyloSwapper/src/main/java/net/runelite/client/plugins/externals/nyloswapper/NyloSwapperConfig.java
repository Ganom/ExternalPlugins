/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.nyloswapper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigTitleSection;
import net.runelite.client.config.Title;

@ConfigGroup("nyloSwapper")

public interface NyloSwapperConfig extends Config
{
	@ConfigTitleSection(
		position = 0,
		keyName = "configs",
		name = "Configs",
		description = ""
	)
	default Title configs()
	{
		return new Title();
	}

	@ConfigItem(
		position = 1,
		keyName = "autoAttack",
		name = "Auto Attack",
		description = "Auto Attack after swaps. EXPERIMENTAL",
		titleSection = "configs"
	)
	default boolean autoAttack()
	{
		return true;
	}

	@ConfigItem(
		keyName = "backToInventory",
		name = "Swap back to Inventory",
		description = "After finishing a sequence, it will swap back to inventory if enabled.",
		position = 2,
		titleSection = "configs"
	)
	default boolean backToInventory()
	{
		return true;
	}

	@ConfigItem(
		keyName = "testing",
		name = "Test the features",
		description = "Type in chat 1 for mage swap, 2 for range, 3 for melee, 4 for ice barrage.",
		position = 3,
		titleSection = "configs"
	)
	default boolean testing()
	{
		return true;
	}

	@ConfigItem(
		position = 4,
		keyName = "mage",
		name = "Mage Gearswap",
		description = "Mage Gearswap Item Id's",
		titleSection = "configs"
	)
	default String mage()
	{
		return "11663,22323,21795,12002";
	}

	@ConfigItem(
		position = 5,
		keyName = "range",
		name = "Range Gearswap",
		description = "Range Gearswap Item Id's",
		titleSection = "configs"
	)
	default String range()
	{
		return "11664,12926,22109,19547";
	}

	@ConfigItem(
		position = 6,
		keyName = "melee",
		name = "Melee Gearswap",
		description = "Melee Gearswap Item Id's",
		titleSection = "configs"
	)
	default String melee()
	{
		return "11665,12006,6570,19553,12954";
	}

	@ConfigItem(
		keyName = "randLow",
		name = "Minimum Delay",
		description = "For MouseEvents only.",
		position = 8,
		titleSection = "configs"
	)
	default int randLow()
	{
		return 70;
	}

	@ConfigItem(
		keyName = "randLower",
		name = "Maximum Delay",
		description = "For MouseEvents only.",
		position = 9,
		titleSection = "configs"
	)
	default int randHigh()
	{
		return 80;
	}
}
