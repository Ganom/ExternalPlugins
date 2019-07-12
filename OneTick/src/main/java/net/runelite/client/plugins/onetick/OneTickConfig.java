package net.runelite.client.plugins.onetick;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Keybind;
import net.runelite.client.plugins.onetick.utils.ActionType;

@ConfigGroup("OneTick")
public interface OneTickConfig extends Config
{
	@ConfigItem(
		position = 1,
		keyName = "oneTick",
		name = "One Tick",
		description = "Does 1 inventory of selected bones"
	)
	default Keybind oneTick()
	{
		return Keybind.NOT_SET;
	}

	@ConfigItem(
		keyName = "boneId",
		name = "Bone ID",
		description = "to 1 tick",
		position = 2
	)
	default String boneId()
	{
		return "22780, 22786";
	}

	@ConfigItem(
		position = 14,
		keyName = "actionType",
		name = "Action Type",
		description = "Flexo is smooth mouse, MouseEvents is ghost mouse",
		parent = "configs"
	)
	default ActionType actionType()
	{
		return ActionType.FLEXO;
	}

	@ConfigItem(
		keyName = "randLow",
		name = "Minimum Delay",
		description = "For MouseEvents only.",
		position = 15,
		parent = "configs"
	)
	default int randLow()
	{
		return 70;
	}

	@ConfigItem(
		keyName = "randLower",
		name = "Maximum Delay",
		description = "For MouseEvents only.",
		position = 16,
		parent = "configs"
	)
	default int randHigh()
	{
		return 80;
	}
}
