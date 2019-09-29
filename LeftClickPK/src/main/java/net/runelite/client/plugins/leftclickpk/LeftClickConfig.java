package net.runelite.client.plugins.leftclickpk;

import net.runelite.client.config.Config;
import net.runelite.client.config.ConfigGroup;
import net.runelite.client.config.ConfigItem;

@ConfigGroup(value = "leftClickPK")
public interface LeftClickConfig extends Config
{
	@ConfigItem(
		position = 0,
		keyName = "enableTbEntangle",
		name = "Enable TB/Entangle",
		description = "This will cast tb until they're tb'd, then entangle until they're entangled."
	)
	default boolean enableTbEntangle()
	{
		return false;
	}

	@ConfigItem(
		position = 0,
		keyName = "enableBlood",
		name = "Enable Blood Barrage/Blitz",
		description = "If the player is frozen/immune to freeze it will cast blood barrage/blitz."
	)
	default boolean enableBlood()
	{
		return false;
	}
}

