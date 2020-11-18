package net.runelite.client.plugins.externals.leaguetp;

import java.util.Arrays;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Teleport
{
	MISTHALIN("Misthalin", 33554457, 2767, 2749, 2722),
	KARAMJA("Karamja", 33554459, 2768, 2750, 2723),
	DESERT("Desert", 33554460, 2770, 2752, 2727),
	MORYTANIA("Morytania", 33554461, 2771, 2753, 2730),
	ASGARNIA("Asgarnia", 33554462, 2769, 2751, 2725),
	KANDARIN("Kandarin", 33554463, 2773, 2755, 2726),
	FREMENNIK("Fremennik", 33554464, 2774, 2756, 2728),
	TIRANNWN("Tirannwn", 33554465, 2775, 2757, 2729),
	WILDERNESS("Wildy", 33554466, 2772, 2754, 2724);

	private final String tooltip;
	private final int widgetId;
	private final int textSpriteId;
	private final int bannerSpriteId;
	private final int unlockSpriteId;

	public static Teleport getAreaBySprite(int spriteId)
	{
		return Arrays.stream(Teleport.values())
			.filter(value -> value.getUnlockSpriteId() == spriteId)
			.findFirst()
			.orElse(null);
	}
}
