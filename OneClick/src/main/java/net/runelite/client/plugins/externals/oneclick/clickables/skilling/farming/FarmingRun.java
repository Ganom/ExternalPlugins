package net.runelite.client.plugins.externals.oneclick.clickables.skilling.farming;

import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.clickables.Clickable;
import static net.runelite.client.plugins.externals.oneclick.clickables.skilling.farming.utils.FarmingConstants.COMPOST_BUCKETS;
import net.runelite.client.plugins.externals.oneclick.clickables.skilling.farming.utils.FarmingPatch;
import net.runelite.client.plugins.externals.oneclick.clickables.skilling.farming.utils.FarmingPlot;
import net.runelite.client.plugins.externals.oneclick.clickables.skilling.farming.utils.FarmingRegion;
import net.runelite.client.plugins.externals.oneclick.clickables.skilling.farming.utils.FarmingWorld;
import net.runelite.client.util.Text;

@Slf4j
public class FarmingRun extends Clickable
{
	private static final FarmingWorld FARMING_WORLD = new FarmingWorld();

	@Override
	public boolean isValidEntry(MenuEntryAdded event)
	{
		if (!event.getOption().equals("Inspect") || event.isForceLeftClick())
		{
			return false;
		}
		Optional<FarmingPlot> plot = parseRegions(event);
		if (plot.isEmpty())
		{
			return false;
		}
		return createdMenuFromState(plot.get(), event);
	}

	@Override
	public boolean isValidClick(MenuOptionClicked event)
	{
		return false;
	}

	private Optional<FarmingPlot> parseRegions(MenuEntryAdded event)
	{
		var regions = FARMING_WORLD.getRegionsForLocation(client.getLocalPlayer().getWorldLocation());
		int varbit = getVarbitFromObjectId(event.getIdentifier());
		for (FarmingRegion region : regions)
		{
			for (FarmingPatch patch : region.getPatches())
			{
				if (varbit == patch.getVarbit())
				{
					return Optional.of(patch.getImplementation());
				}
			}
		}
		return Optional.empty();
	}

	private boolean createdMenuFromState(FarmingPlot plot, MenuEntryAdded event)
	{
		var varbit = getVarbitFromObjectId(event.getIdentifier());
		var state = plot.forVarbitValue(client.getVarbitValue(varbit));

		if (state == null)
		{
			return false;
		}

		switch (state.getCropState())
		{
			case SEEDABLE:
				var seed = findItemWithIds(plot.getSeeds());

				if (seed == null)
				{
					return false;
				}

				client.createMenuEntry(client.getMenuOptionCount())
					.setOption("Use")
					.setTarget("<col=ff9040>" + seed.getName() + "</col><col=ffffff> -> <col=ffff> Farming Patch")
					.setType(MenuAction.WIDGET_TARGET_ON_GAME_OBJECT)
					.setIdentifier(event.getIdentifier())
					.setParam0(event.getActionParam0())
					.setParam1(event.getActionParam1())
					.setForceLeftClick(true)
					.onClick((e) -> updateSelectedItem(plot.getSeeds()));
				return true;
			case GROWING:
				if (!plot.isCompostable() || config.getCompostablePlots().stream().noneMatch(compostable -> compostable.getPlot() == plot))
				{
					return false;
				}

				var compost = findItemWithIds(COMPOST_BUCKETS);

				if (compost == null)
				{
					return false;
				}

				client.createMenuEntry(client.getMenuOptionCount())
					.setOption("Use")
					.setTarget("<col=ff9040>" + compost.getName() + "</col><col=ffffff> -> <col=ffff> " + Text.titleCase(plot))
					.setType(MenuAction.WIDGET_TARGET_ON_GAME_OBJECT)
					.setIdentifier(event.getIdentifier())
					.setParam0(event.getActionParam0())
					.setParam1(event.getActionParam1())
					.setForceLeftClick(true)
					.onClick((e) -> updateSelectedItem(COMPOST_BUCKETS));
				return true;
			default:
				return false;
		}
	}
}
