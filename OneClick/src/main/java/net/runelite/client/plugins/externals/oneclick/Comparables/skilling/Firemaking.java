package net.runelite.client.plugins.externals.oneclick.comparables.skilling;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import static net.runelite.api.MenuAction.ITEM_USE_ON_WIDGET_ITEM;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

@Slf4j
public class Firemaking extends ClickCompare
{
	private static final Set<Integer> LOG_ID = ImmutableSet.of(
		ItemID.LOGS, ItemID.OAK_LOGS, ItemID.WILLOW_LOGS, ItemID.TEAK_LOGS,
		ItemID.MAPLE_LOGS, ItemID.MAHOGANY_LOGS, ItemID.YEW_LOGS, ItemID.MAGIC_LOGS,
		ItemID.REDWOOD_LOGS
	);

	@Override
	public boolean isEntryValid(MenuEntryAdded event)
	{
		return event.getOpcode() == MenuAction.ITEM_USE.getId() && !event.isForceLeftClick() &&
			LOG_ID.contains(event.getIdentifier());
	}

	@Override
	public void modifyEntry(MenuEntryAdded event)
	{
		if (client == null || findItem(ItemID.TINDERBOX).getLeft() == -1 || event.isForceLeftClick())
		{
			return;
		}

		client.createMenuEntry(-1)
			.setOption(event.getOption())
			.setTarget("<col=ff9040>Tinderbox<col=ffffff> -> " + getTargetMap().get(event.getIdentifier()))
			.setType(ITEM_USE_ON_WIDGET_ITEM)
			.setIdentifier(event.getIdentifier())
			.setParam0(event.getActionParam0())
			.setParam1(event.getActionParam1())
			.setForceLeftClick(true);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		return event.getMenuTarget().contains("<col=ff9040>Tinderbox<col=ffffff> -> ") && updateSelectedItem(ItemID.TINDERBOX);
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{

	}

	@Override
	public void backupEntryModify(MenuEntry e)
	{
		if (findItem(ItemID.TINDERBOX).getLeft() == -1 || e.isForceLeftClick())
		{
			return;
		}

		e.setTarget("<col=ff9040>Tinderbox<col=ffffff> -> " + getTargetMap().get(e.getIdentifier()));
		e.setForceLeftClick(true);
	}
}
