package net.runelite.client.plugins.externals.oneclick.pojos;

import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import static net.runelite.api.ItemID.AIR_RUNE;
import static net.runelite.api.ItemID.EARTH_RUNE;
import static net.runelite.api.ItemID.FIRE_RUNE;
import static net.runelite.api.ItemID.WATER_RUNE;

@Getter
@AllArgsConstructor
public enum ElementalAltar
{
	AIR_ALTAR(11339, Set.of(EARTH_RUNE, WATER_RUNE, FIRE_RUNE)),
	WATER_ALTAR(10827, Set.of(EARTH_RUNE, FIRE_RUNE, AIR_RUNE)),
	FIRE_ALTAR(10315, Set.of(EARTH_RUNE, WATER_RUNE, AIR_RUNE)),
	EARTH_ALTAR(10571, Set.of(WATER_RUNE, FIRE_RUNE, AIR_RUNE));

	private final int regionId;
	private final Set<Integer> validComboRunes;

	public static ElementalAltar fromRegion(int regionId)
	{
		for (ElementalAltar value : values())
		{
			if (value.getRegionId() == regionId)
			{
				return value;
			}
		}
		return null;
	}
}
