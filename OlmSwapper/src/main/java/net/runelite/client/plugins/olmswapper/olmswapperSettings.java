package net.runelite.client.plugins.olmswapper;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.plugins.olmswapper.utils.ActionType;

@ConfigGroup(value = "olmswapper")
public interface olmswapperSettings
	extends Config
{
	@ConfigItem(
		position = 23,
		keyName = "actionType",
		name = "Action Type",
		description = "Flexo is smooth mouse, MouseEvents is ghost mouse."
	)
	default ActionType actionType()
	{
		return ActionType.FLEXO;
	}

	@ConfigItem(
		position = 24,
		keyName = "randLow",
		name = "Minimum Delay",
		description = "For MouseEvents"
	)
	default int randLow()
	{
		return 70;
	}

	@ConfigItem(
		position = 25,
		keyName = "randLower",
		name = "Maximum Delay",
		description = "For MouseEvents"
	)
	default int randHigh()
	{
		return 80;
	}
}

