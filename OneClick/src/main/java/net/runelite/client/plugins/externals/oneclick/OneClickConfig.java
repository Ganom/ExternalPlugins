package net.runelite.client.plugins.externals.oneclick;

import java.util.Set;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup("oneclick")
public interface OneClickConfig extends Config
{
	@ConfigItem(
		position = 1,
		keyName = "oneClicks",
		name = "One Clicks",
		description = "yert"
	)
	default Set<Method> getOneClickMethods()
	{
		return Set.of(

		);
	}

	@ConfigItem(
		position = 2,
		keyName = "swaps",
		name = "Custom",
		description = "Format is as follows: UseThis:WithThis" +
			"<br>For example, 6032:13421. This will use saltpetre on compost."
	)
	default String getCustomIds()
	{
		return "0:0";
	}

	@ConfigItem(
		position = 3,
		keyName = "isMagicImbueEnabled",
		name = "Magic Imbue",
		description = "Enable this to allow the plugin to magic imbue before clicking an altar."
	)
	default boolean isMagicImbueEnabled()
	{
		return true;
	}

	@ConfigItem(
		position = 4,
		keyName = "isCompostingDiseaseProtectedPatch",
		name = "Compost on Protected",
		description = "Disabling this will disable the compost one click on disease protected patches."
	)
	default boolean isCompostingDiseaseProtectedPatches()
	{
		return true;
	}
}
