/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.anon;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;

@ConfigGroup("AnonConfig")
public interface AnonConfig extends Config
{
	@ConfigSection(
		position = 0,
		keyName = "anonConfig",
		name = "Anonymizer Config",
		description = ""
	)
	String anonConfig = "Anonymizer Config";

	@ConfigItem(
		keyName = "hideXp",
		name = "Randomize XP Tracker",
		description = "Only works for canvas xp tracker.",
		position = 1,
		section = anonConfig
	)
	default boolean hideXp()
	{
		return true;
	}

	@ConfigItem(
		keyName = "hideRsn",
		name = "Randomize RSN",
		description = "This should work for every single widget in the game.",
		position = 2,
		section = anonConfig
	)
	default boolean hideRsn()
	{
		return true;
	}
}
