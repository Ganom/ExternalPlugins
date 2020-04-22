/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.anon;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigTitleSection;
import net.runelite.client.config.Title;

@ConfigGroup("AnonConfig")
public interface AnonConfig extends Config
{
	@ConfigTitleSection(
		position = 0,
		keyName = "anonConfig",
		name = "Anonymizer Config",
		description = ""
	)
	default Title anonConfig()
	{
		return new Title();
	}

	@ConfigItem(
		keyName = "hideXp",
		name = "Randomize XP Tracker",
		description = "Only works for canvas xp tracker.",
		position = 1,
		titleSection = "anonConfig"
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
		titleSection = "anonConfig"
	)
	default boolean hideRsn()
	{
		return true;
	}

	@ConfigItem(
		keyName = "fixedName",
		name = "Fixed RSN",
		description = "One a random salted name is generated, it will always be used.",
		position = 3,
		titleSection = "anonConfig"
	)
	default boolean fixedName()
	{
		return false;
	}
}
