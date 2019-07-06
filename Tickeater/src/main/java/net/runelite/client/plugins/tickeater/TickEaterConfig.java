package net.runelite.client.plugins.tickeater;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("tickeaterconfig")
public interface TickEaterConfig extends Config
{
	@ConfigItem(
		position = 0,
		keyName = "projectiles",
		name = "Projectiles to Tick-Eat",
		description = "Put in the IDs seperate with a comma."
	)
	default String projectiles()
	{
		return "0";
	}

	@ConfigItem(
		position = 1,
		keyName = "animations",
		name = "Animations to Tick-Eat",
		description = "Put in the IDs seperate with a comma."
	)
	default String animations()
	{
		return "0";
	}

	@ConfigItem(
		position = 2,
		keyName = "food",
		name = "Food to Tick-Eat with",
		description = "Put in the IDs seperate with a comma."
	)
	default String food()
	{
		return "0";
	}
}
