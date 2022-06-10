package net.runelite.client.plugins.externals.oneclick.config;

import com.google.inject.Injector;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.plugins.externals.oneclick.clickables.Clickable;
import net.runelite.client.plugins.externals.oneclick.clickables.custom.CustomInventory;

@Getter
@AllArgsConstructor
public enum Custom
{
	CUSTOM_INV(CustomInventory.class),
	;

	private final Class<? extends Clickable> clazz;

	public static List<Clickable> createInstances(Set<Custom> set, Injector injector)
	{
		return set.stream()
			.filter(Objects::nonNull)
			.map(skilling -> injector.getInstance(skilling.getClazz()))
			.collect(Collectors.toList());
	}
}
