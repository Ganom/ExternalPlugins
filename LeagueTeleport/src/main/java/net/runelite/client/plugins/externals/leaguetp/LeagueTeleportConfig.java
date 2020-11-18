package net.runelite.client.plugins.externals.leaguetp;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.Units;
import net.runelite.client.ui.overlay.components.ComponentOrientation;

@ConfigGroup("LeagueTeleportConfig")
public interface LeagueTeleportConfig extends Config
{
	@ConfigItem(
		keyName = "scale",
		name = "Scale",
		description = "Scaling percent for the images",
		position = 0
	)
	@Units(Units.PERCENT)
	default int scale()
	{
		return 200;
	}

	@ConfigItem(
		keyName = "style",
		name = "Style",
		description = "",
		position = 1
	)
	default Style style()
	{
		return Style.BADGES;
	}

	@ConfigItem(
		keyName = "align",
		name = "Alignment",
		description = "",
		position = 3
	)
	default ComponentOrientation align()
	{
		return ComponentOrientation.VERTICAL;
	}
}
