package net.runelite.client.plugins.externals.oneclick.clickables.misc;

import java.util.Set;
import static net.runelite.api.ItemID.*;
import net.runelite.api.MenuAction;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.clickables.Clickable;

public class Herbs extends Clickable
{
	private static final Set<Integer> COMPOST_BUCKETS = Set.of(
		COMPOST, SUPERCOMPOST, ULTRACOMPOST, BOTTOMLESS_COMPOST_BUCKET,
		BOTTOMLESS_COMPOST_BUCKET_22997
	);
	private static final Set<Integer> SEEDS = Set.of(
		GUAM_SEED, MARRENTILL_SEED, TARROMIN_SEED, HARRALANDER_SEED,
		RANARR_SEED, TOADFLAX_SEED, IRIT_SEED, AVANTOE_SEED,
		KWUARM_SEED, SNAPDRAGON_SEED, CADANTINE_SEED, LANTADYME_SEED,
		DWARF_WEED_SEED, TORSTOL_SEED
	);
	private static final String COMPOST_STRING = "<col=ff9040>Compost Bucket</col><col=ffffff> -> <col=ffff>Herbs";
	private static final String SEED_STRING = "<col=ff9040>Seed</col><col=ffffff> -> <col=ffff>Patch";
	private static final Set<Integer> DISEASE_PROTECTED_REGIONS = Set.of(11325, 11321, 6967);

	@Override
	public boolean isValidEntry(MenuEntryAdded event)
	{
		if (!event.getOption().equals("Inspect") ||
			!event.getTarget().contains("Herb") ||
			event.isForceLeftClick())
		{
			return false;
		}
		var od = client.getObjectDefinition(event.getIdentifier());
		if (od == null)
		{
			return false;
		}
		var state = getGrowthStateFromVarbitValue(client.getVarbitValue(od.getVarbitId()));
		if (state == null)
		{
			return false;
		}
		return createMenuFromState(state, event);
	}

	@Override
	public boolean isValidClick(MenuOptionClicked event)
	{
		switch (event.getMenuTarget())
		{
			case COMPOST_STRING:
				return updateSelectedItem(COMPOST_BUCKETS);
			case SEED_STRING:
				return updateSelectedItem(SEEDS);
			default:
				return false;
		}
	}

	private boolean createMenuFromState(GrowthState state, MenuEntryAdded event)
	{
		switch (state)
		{
			case COMPOSTABLE:
			{
				if (!config.isCompostingDiseaseProtectedPatches() &&
					DISEASE_PROTECTED_REGIONS.contains(client.getLocalPlayer().getWorldLocation().getRegionID()))
				{
					return false;
				}
				var bucket = findItemWithIds(COMPOST_BUCKETS);
				if (bucket == null)
				{
					return false;
				}
				client.createMenuEntry(client.getMenuOptionCount())
					.setOption("Use")
					.setTarget(COMPOST_STRING)
					.setType(MenuAction.WIDGET_TARGET_ON_GAME_OBJECT)
					.setIdentifier(event.getIdentifier())
					.setParam0(event.getActionParam0())
					.setParam1(event.getActionParam1())
					.setForceLeftClick(true);
			}
			return true;
			case SEEDABLE:
			{
				var seed = findItemWithIds(SEEDS);
				if (seed == null)
				{
					return false;
				}
				client.createMenuEntry(client.getMenuOptionCount())
					.setOption("Use")
					.setTarget(SEED_STRING)
					.setType(MenuAction.WIDGET_TARGET_ON_GAME_OBJECT)
					.setIdentifier(event.getIdentifier())
					.setParam0(event.getActionParam0())
					.setParam1(event.getActionParam1())
					.setForceLeftClick(true);
			}
			return true;
			default:
				return false;
		}
	}

	/**
	 * Not sure if this needs to include below 3 or not. The herb patches
	 * I've seen only had a value of 3, but time tracking plugin
	 * shows below 3 being valid as well, so we'll use it here for the
	 * time being.
	 */
	public GrowthState getGrowthStateFromVarbitValue(int value)
	{
		if (value >= 0 && value <= 3)
		{
			return GrowthState.SEEDABLE;
		}
		if (value >= 4 && value <= 7)
		{
			return GrowthState.COMPOSTABLE;
		}
		if (value >= 11 && value <= 14)
		{
			return GrowthState.COMPOSTABLE;
		}
		if (value >= 18 && value <= 21)
		{
			return GrowthState.COMPOSTABLE;
		}
		if (value >= 25 && value <= 28)
		{
			return GrowthState.COMPOSTABLE;
		}
		if (value >= 32 && value <= 35)
		{
			return GrowthState.COMPOSTABLE;
		}
		if (value >= 39 && value <= 42)
		{
			return GrowthState.COMPOSTABLE;
		}
		if (value >= 46 && value <= 49)
		{
			return GrowthState.COMPOSTABLE;
		}
		if (value >= 53 && value <= 56)
		{
			return GrowthState.COMPOSTABLE;
		}
		if (value >= 68 && value <= 71)
		{
			return GrowthState.COMPOSTABLE;
		}
		if (value >= 75 && value <= 78)
		{
			return GrowthState.COMPOSTABLE;
		}
		if (value >= 82 && value <= 85)
		{
			return GrowthState.COMPOSTABLE;
		}
		if (value >= 89 && value <= 92)
		{
			return GrowthState.COMPOSTABLE;
		}
		if (value >= 96 && value <= 99)
		{
			return GrowthState.COMPOSTABLE;
		}
		if (value >= 103 && value <= 106)
		{
			return GrowthState.COMPOSTABLE;
		}
		return GrowthState.INVALID;
	}

	enum GrowthState
	{
		COMPOSTABLE,
		SEEDABLE,
		INVALID
	}
}
