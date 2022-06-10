package net.runelite.client.plugins.externals.oneclick.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.runelite.client.plugins.externals.oneclick.clickables.skilling.farming.utils.FarmingPlot;

@Getter
@AllArgsConstructor
public enum Compostable
{
	ALLOTMENT(FarmingPlot.ALLOTMENT),
	BELLADONNA(FarmingPlot.BELLADONNA),
	BUSH(FarmingPlot.BUSH),
	CACTUS(FarmingPlot.CACTUS),
	CALQUAT(FarmingPlot.CALQUAT),
	CELASTRUS(FarmingPlot.CELASTRUS),
	FLOWER(FarmingPlot.FLOWER),
	FRUIT_TREE(FarmingPlot.FRUIT_TREE),
	HARDWOOD_TREE(FarmingPlot.HARDWOOD_TREE),
	HERB(FarmingPlot.HERB),
	HOPS(FarmingPlot.HOPS),
	MUSHROOM(FarmingPlot.MUSHROOM),
	REDWOOD(FarmingPlot.REDWOOD),
	SEAWEED(FarmingPlot.SEAWEED),
	SPIRIT_TREE(FarmingPlot.SPIRIT_TREE),
	TREE(FarmingPlot.TREE),
	;

	private final FarmingPlot plot;
}
