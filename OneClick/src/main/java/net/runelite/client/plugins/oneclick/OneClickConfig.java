/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * Copyright (c) 2019, TomC <https://github.com/tomcylke>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.oneclick;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("oneclick")
public interface OneClickConfig extends Config
{
	@ConfigItem(
		keyName = "Type",
		name = "Type",
		description = "Select which one click method you would like to use."
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
