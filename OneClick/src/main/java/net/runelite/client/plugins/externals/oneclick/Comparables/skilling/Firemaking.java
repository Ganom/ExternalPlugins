package net.runelite.client.plugins.externals.oneclick.comparables.skilling;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
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
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == MenuAction.ITEM_USE.getId() && !event.isForceLeftClick() &&
			LOG_ID.contains(event.getIdentifier());
	}

	@Override
	public void modifyEntry(MenuEntry event)
	{
		if (findItem(ItemID.TINDERBOX).getLeft() == -1 || event.isForceLeftClick())
		{
			return;
		}
		MenuEntry e = event.clone();
		e.setTarget("<col=ff9040>Tinderbox<col=ffffff> -> " + getTargetMap().get(event.getIdentifier()));
		e.setForceLeftClick(true);
		insert(e);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		return event.getMenuAction() == MenuAction.ITEM_USE &&
			event.getMenuTarget().contains("<col=ff9040>Tinderbox<col=ffffff> -> ");
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{
		if (updateSelectedItem(ItemID.TINDERBOX))
		{
			event.setMenuAction(MenuAction.ITEM_USE_ON_WIDGET_ITEM);
		}
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
