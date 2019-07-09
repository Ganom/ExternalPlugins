package net.runelite.client.plugins.olmswapper;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value = "OlmSwapper")
public interface OlmSwapperConfig extends Config
{
	@ConfigItem(
		position = 0,
		keyName = "swapAutos",
		name = "Swap on Auto Attacks",
		description = "This will swap prayers for olms auto attacks as-well."
	)
	default boolean swapAutos()
	{
		return false;
	}

	@ConfigItem(
		position = 1,
		keyName = "actionType",
		name = "Action Type",
		description = "Flexo is smooth mouse, MouseEvents is ghost mouse."
	)
	default ActionType actionType()
	{
		return ActionType.FLEXO;
	}

	@ConfigItem(
		position = 2,
		keyName = "randLow",
		name = "Minimum Delay",
		description = "For MouseEvents"
	)
	default int randLow()
	{
		return 70;
	}

	@ConfigItem(
		position = 3,
		keyName = "randLower",
		name = "Maximum Delay",
		description = "For MouseEvents"
	)
	default int randHigh()
	{
		return 80;
	}

	@Getter
	@AllArgsConstructor
	public enum ActionType
	{
		FLEXO("Flexo"),
		MOUSEEVENTS("MouseEvents");

		private String name;

		@Override
		public String toString()
		{
			return getName();
		}
	}
}

