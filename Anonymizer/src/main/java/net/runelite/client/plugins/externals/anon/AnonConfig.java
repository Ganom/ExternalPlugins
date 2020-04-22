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
		position = 0,
		titleSection = "anonConfig"
	)
	default boolean hideXp()
	{
		return true;
	}

	@ConfigItem(
		keyName = "hideXp",
		name = "Randomize RSN",
		description = "This should work for every single widget in the game.",
		position = 0,
		titleSection = "anonConfig"
	)
	default boolean hideRsn()
	{
		return true;
	}
}
