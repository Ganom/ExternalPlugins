package net.runelite.client.plugins.externals.oneclick;

import com.google.inject.Injector;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.plugins.externals.oneclick.clickables.Clickable;
import net.runelite.client.plugins.externals.oneclick.clickables.misc.Custom;
import net.runelite.client.plugins.externals.oneclick.clickables.misc.TroubleBrewing;
import net.runelite.client.plugins.externals.oneclick.clickables.misc.Vorkath;
import net.runelite.client.plugins.externals.oneclick.clickables.skilling.Birdhouses;
import net.runelite.client.plugins.externals.oneclick.clickables.skilling.Bones;
import net.runelite.client.plugins.externals.oneclick.clickables.skilling.Firemaking;
import net.runelite.client.plugins.externals.oneclick.clickables.skilling.Karams;
import net.runelite.client.plugins.externals.oneclick.clickables.skilling.TitheFarm;
import net.runelite.client.plugins.externals.oneclick.clickables.misc.Healers;
import net.runelite.client.plugins.externals.oneclick.clickables.skilling.Tiaras;

@Getter
@AllArgsConstructor
public enum Method
{
	BA_HEALERS(Healers.class),
	BIRDHOUSES(Birdhouses.class),
	BONES(Bones.class),
	CUSTOM(Custom.class),
	KARAMBWANS(Karams.class),
	LOG_BURNING(Firemaking.class),
	TIARA(Tiaras.class),
	TITHE_FARM(TitheFarm.class),
	TROUBLE_BREWING(TroubleBrewing.class),
	VORKATH(Vorkath.class),
	;

	private final Class<? extends Clickable> clazz;

	public Clickable createInstance(Injector injector)
	{
		return injector.getInstance(clazz);
	}
}
