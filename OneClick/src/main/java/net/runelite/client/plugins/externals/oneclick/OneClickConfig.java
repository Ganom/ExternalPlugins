package net.runelite.client.plugins.externals.oneclick;

import java.util.Set;
import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;
import net.runelite.client.config.ConfigSection;
import net.runelite.client.plugins.externals.oneclick.config.Combat;
import net.runelite.client.plugins.externals.oneclick.config.Compostable;
import net.runelite.client.plugins.externals.oneclick.config.Custom;
import net.runelite.client.plugins.externals.oneclick.config.Minigame;
import net.runelite.client.plugins.externals.oneclick.config.Skilling;

@ConfigGroup("oneclick")
public interface OneClickConfig extends Config
{
	@ConfigSection(
		position = 0,
		keyName = "oneClickConfig",
		name = "One Click Config",
		description = ""
	)
	String oneClickConfig = "One Click Config";

	@ConfigItem(
		position = 0,
		keyName = "getCustomOneClicks",
		name = "Custom",
		description = "",
		section = oneClickConfig
	)
	default Set<Custom> getCustomOneClicks()
	{
		return Set.of(Custom.values());
	}

	@ConfigItem(
		position = 1,
		keyName = "getCombatOneClicks",
		name = "Combat",
		description = "",
		section = oneClickConfig
	)
	default Set<Combat> getCombatOneClicks()
	{
		return Set.of(Combat.values());
	}

	@ConfigItem(
		position = 2,
		keyName = "getSkillingOneClicks",
		name = "Skilling",
		description = "",
		section = oneClickConfig
	)
	default Set<Skilling> getSkillingOneClicks()
	{
		return Set.of(Skilling.values());
	}

	@ConfigItem(
		position = 3,
		keyName = "getMinigameOneClicks",
		name = "Minigames",
		description = "",
		section = oneClickConfig
	)
	default Set<Minigame> getMinigameOneClicks()
	{
		return Set.of(Minigame.values());
	}

	@ConfigSection(
		position = 1,
		keyName = "farmRunConfig",
		name = "Farm Run Config",
		description = ""
	)
	String farmRunConfig = "Farm Run Config";

	@ConfigItem(
		position = 0,
		keyName = "getCompostablePlots",
		name = "Compost",
		description = "Select which plots you want to compost.",
		section = farmRunConfig
	)
	default Set<Compostable> getCompostablePlots()
	{
		return Set.of(Compostable.values());
	}

	@ConfigSection(
		position = 2,
		keyName = "runeConfig",
		name = "Rune Config",
		description = ""
	)
	String runeConfig = "Rune Config";

	@ConfigItem(
		position = 0,
		keyName = "isMagicImbueEnabled",
		name = "Magic Imbue",
		description = "Enable this to allow the plugin to magic imbue before clicking an altar.",
		section = runeConfig
	)
	default boolean isMagicImbueEnabled()
	{
		return true;
	}

	@ConfigSection(
		position = 3,
		keyName = "customConfig",
		name = "Custom Config",
		description = ""
	)
	String customConfig = "Custom Config";

	@ConfigItem(
		position = 0,
		keyName = "swaps",
		name = "Custom",
		description = "Format is as follows: UseThis:WithThis" +
			"<br>For example, 6032:13421. This will use saltpetre on compost.",
		section = customConfig
	)
	default String getCustomIds()
	{
		return "0:0";
	}
}
