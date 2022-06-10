package net.runelite.client.plugins.externals.oneclick.config;

import com.google.inject.Injector;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.plugins.externals.oneclick.clickables.Clickable;
import net.runelite.client.plugins.externals.oneclick.clickables.combat.Slayer;
import net.runelite.client.plugins.externals.oneclick.clickables.combat.Vorkath;

@Getter
@AllArgsConstructor
public enum Combat
{
	SLAYER(Slayer.class),
	VORKATH(Vorkath.class),
	;

	private final Class<? extends Clickable> clazz;

	public static List<Clickable> createInstances(Set<Combat> set, Injector injector)
	{
		return set.stream()
			.filter(Objects::nonNull)
			.map(skilling -> injector.getInstance(skilling.getClazz()))
			.collect(Collectors.toList());
	}
}
