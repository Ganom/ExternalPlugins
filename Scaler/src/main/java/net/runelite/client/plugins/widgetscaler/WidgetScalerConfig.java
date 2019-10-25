/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.widgetscaler;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("WidgetScalerConfig")
public interface WidgetScalerConfig extends Config
{
	@ConfigItem(
		position = 0,
		keyName = "widgetSize",
		name = "Widget Size",
		description = "Set widget sizing for cape."
	)
	default int widgetSizeCape()
	{
		return 72;
	}

	@ConfigItem(
		position = 1,
		keyName = "xOffsetCape",
		name = "X Offset - Cape",
		description = "X Offset for Cape."
	)
	default int xOffsetCape()
	{
		return 1;
	}

	@ConfigItem(
		position = 2,
		keyName = "yOffsetCape",
		name = "Y Offset - Cape",
		description = "Y Offset for Cape."
	)
	default int yOffsetCape()
	{
		return 5;
	}

	@ConfigItem(
		position = 3,
		keyName = "widgetSizeRing",
		name = "Widget Size Ring",
		description = "Set widget sizing for ring."
	)
	default int widgetSizeRing()
	{
		return 72;
	}

	@ConfigItem(
		position = 4,
		keyName = "xOffsetRing",
		name = "X Offset - Ring",
		description = "X Offset for Ring."
	)
	default int xOffsetRing()
	{
		return 115;
	}

	@ConfigItem(
		position = 5,
		keyName = "yOffsetRing",
		name = "Y Offset - Ring",
		description = "Y Offset for Ring."
	)
	default int yOffsetRing()
	{
		return 125;
	}
}
