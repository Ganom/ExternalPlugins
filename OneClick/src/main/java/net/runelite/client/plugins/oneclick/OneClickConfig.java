package net.runelite.client.plugins.oneclick;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("oneclick")
public interface OneClickConfig extends Config
{
	@ConfigItem(
			keyName = "1 Click Menu",
			name = "1 Click Menu",
			description = "Choose from menu your desired 1 click option"
	)
	default Types getType()
	{
		return Types.NONE;
	}

	@ConfigItem(
			keyName = "isUsingImbue",
			name = "Use Magic Imbue",
			description = "If you are using lava runes, and want to use magic imbue, enable this."
	)
	default boolean isUsingImbue()
	{
		return false;
	}

}