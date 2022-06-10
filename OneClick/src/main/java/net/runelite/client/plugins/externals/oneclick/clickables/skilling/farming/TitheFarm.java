package net.runelite.client.plugins.externals.oneclick.clickables.skilling.farming;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import java.util.regex.Pattern;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.ObjectID;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.clickables.Clickable;

public class TitheFarm extends Clickable
{
	private static final Set<Integer> SEED_SET = ImmutableSet.of(
		ItemID.GOLOVANOVA_SEED, ItemID.BOLOGANO_SEED, ItemID.LOGAVANO_SEED
	);

	private static final Set<Integer> WATERING_CANS = ImmutableSet.of(
		ItemID.WATERING_CAN, ItemID.WATERING_CAN1, ItemID.WATERING_CAN2, ItemID.WATERING_CAN3, ItemID.WATERING_CAN4,
		ItemID.WATERING_CAN5, ItemID.WATERING_CAN6, ItemID.WATERING_CAN7, ItemID.GRICOLLERS_CAN
	);

	private static final Set<Integer> USEABLE_WATERING_CANS = ImmutableSet.of(
		ItemID.WATERING_CAN1, ItemID.WATERING_CAN2, ItemID.WATERING_CAN3, ItemID.WATERING_CAN4,
		ItemID.WATERING_CAN5, ItemID.WATERING_CAN6, ItemID.WATERING_CAN7, ItemID.WATERING_CAN8, ItemID.GRICOLLERS_CAN
	);

	private static final Set<Integer> PLANTS_FIRST_STAGE_UNWATERED = ImmutableSet.of(ObjectID.GOLOVANOVA_SEEDLING, ObjectID.BOLOGANO_SEEDLING, ObjectID.LOGAVANO_SEEDLING);

	private static final Set<Integer> PLANTS_FIRST_STAGE_WATERED = ImmutableSet.of(ObjectID.GOLOVANOVA_SEEDLING_27385, ObjectID.BOLOGANO_SEEDLING_27396, ObjectID.LOGAVANO_SEEDLING_27407);

	private static final Pattern WATERING_CAN_PATTERN = Pattern.compile("<col=ff9040>(Watering can(\\([1-7]\\))?(?!\\(8\\))|Gricoller's can)<col=ffffff> ->");

	@Override
	public boolean isValidEntry(MenuEntryAdded event)
	{
		if (event.getType() != MenuAction.EXAMINE_OBJECT.getId() ||
			event.isForceLeftClick())
		{
			return false;
		}

		if (event.getTarget().toLowerCase().contains("tithe") &&
			findItemWithIds(SEED_SET) != null)
		{
			client.createMenuEntry(client.getMenuOptionCount())
				.setOption("Use")
				.setTarget("<col=ff9040>Seed<col=ffffff> -> " + event.getTarget())
				.setType(MenuAction.WIDGET_TARGET_ON_GAME_OBJECT)
				.setIdentifier(event.getIdentifier())
				.setParam0(event.getActionParam0())
				.setParam1(event.getActionParam1())
				.setForceLeftClick(true);
		}
		else if (PLANTS_FIRST_STAGE_UNWATERED.contains(event.getIdentifier()) &&
			findItemWithIds(USEABLE_WATERING_CANS) != null)
		{
			client.createMenuEntry(client.getMenuOptionCount())
				.setOption("Use")
				.setTarget("<col=ff9040>Watering can<col=ffffff> -> " + event.getTarget())
				.setType(MenuAction.WIDGET_TARGET_ON_GAME_OBJECT)
				.setIdentifier(event.getIdentifier())
				.setParam0(event.getActionParam0())
				.setParam1(event.getActionParam1())
				.setForceLeftClick(true);
		}
		else if (PLANTS_FIRST_STAGE_WATERED.contains(event.getIdentifier()) &&
			findItem(ItemID.GRICOLLERS_FERTILISER) != null)
		{
			client.createMenuEntry(client.getMenuOptionCount())
				.setOption("Use")
				.setTarget("<col=ff9040>Fertilize<col=ffffff> -> " + event.getTarget())
				.setType(MenuAction.WIDGET_TARGET_ON_GAME_OBJECT)
				.setIdentifier(event.getIdentifier())
				.setParam0(event.getActionParam0())
				.setParam1(event.getActionParam1())
				.setForceLeftClick(true);
		}
		else if (event.getTarget().toLowerCase().contains("water barrel") &&
			findItemWithIds(WATERING_CANS) != null)
		{
			client.createMenuEntry(client.getMenuOptionCount())
				.setOption("Use")
				.setTarget("<col=ff9040>Watering can<col=ffffff> -> " + event.getTarget())
				.setType(MenuAction.WIDGET_TARGET_ON_GAME_OBJECT)
				.setIdentifier(event.getIdentifier())
				.setParam0(event.getActionParam0())
				.setParam1(event.getActionParam1())
				.setForceLeftClick(true);
		}
		return true;
	}

	@Override
	public boolean isValidClick(MenuOptionClicked event)
	{
		// Water barrel refilling and initial watering of saplings
		if (WATERING_CAN_PATTERN.matcher(event.getMenuTarget()).find())
		{
			if (PLANTS_FIRST_STAGE_UNWATERED.contains(event.getId()))
			{
				return updateSelectedItem(USEABLE_WATERING_CANS);
			}
			return updateSelectedItem(WATERING_CANS);
		}
		else if (event.getMenuTarget().contains("<col=ff9040>Fertilize<col=ffffff> -> "))
		{
			return updateSelectedItem(ItemID.GRICOLLERS_FERTILISER);
		}
		else if (event.getMenuTarget().contains("<col=ff9040>Seed<col=ffffff> -> ") &&
			event.getMenuTarget().toLowerCase().contains("tithe"))
		{
			return updateSelectedItem(SEED_SET);
		}
		return false;
	}
}
