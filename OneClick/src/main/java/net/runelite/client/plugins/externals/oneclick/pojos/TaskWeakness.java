package net.runelite.client.plugins.externals.oneclick.pojos;

import java.util.Arrays;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.api.ItemID;

@Getter
@AllArgsConstructor
public enum TaskWeakness
{
	GARGALONS(Set.of(ItemID.ROCK_HAMMER, ItemID.GRANITE_HAMMER, ItemID.ROCK_THROWNHAMMER), Set.of("gargoyle"), 9),
	GUARDIANS(Set.of(ItemID.ROCK_HAMMER, ItemID.GRANITE_HAMMER, ItemID.ROCK_THROWNHAMMER), Set.of("dawn", "dusk"), 10),
	FUNGI(Set.of(
		ItemID.FUNGICIDE_SPRAY_1, ItemID.FUNGICIDE_SPRAY_2, ItemID.FUNGICIDE_SPRAY_3,
		ItemID.FUNGICIDE_SPRAY_4, ItemID.FUNGICIDE_SPRAY_5, ItemID.FUNGICIDE_SPRAY_6,
		ItemID.FUNGICIDE_SPRAY_7, ItemID.FUNGICIDE_SPRAY_8, ItemID.FUNGICIDE_SPRAY_9,
		ItemID.FUNGICIDE_SPRAY_10
	), Set.of("zygomite"), 7),
	ROCKSLUGS(Set.of(ItemID.BAG_OF_SALT), Set.of("rockslug"), 4),
	LIZARD(Set.of(ItemID.ICE_COOLER), Set.of("lizard"), 4),
	;

	private final Set<Integer> itemIds;
	private final Set<String> names;
	private final int threshold;

	public static TaskWeakness getWeakness(String target)
	{
		return Arrays.stream(values())
			.filter(value -> value.getNames()
				.stream()
				.anyMatch(name -> target.toLowerCase().contains(name)))
			.findFirst()
			.orElse(null);
	}
}
