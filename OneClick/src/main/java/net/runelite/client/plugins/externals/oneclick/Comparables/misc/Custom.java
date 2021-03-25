package net.runelite.client.plugins.externals.oneclick.comparables.misc;

import com.google.common.base.Splitter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

@Slf4j
public class Custom extends ClickCompare
{
	private static final Splitter NEWLINE_SPLITTER = Splitter
		.on("\n")
		.omitEmptyStrings()
		.trimResults();
	private final Map<Integer, List<Integer>> customClickMap = new HashMap<>();

	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		if (client == null)
		{
			return false;
		}

		int id = event.getIdentifier();

		if (customClickMap.get(id) != null &&
			event.getMenuAction() == MenuAction.ITEM_USE &&
			customClickMap.containsKey(id)
		)
		{
			int item = findItem(customClickMap.get(id)).getLeft();
			return item != -1;
		}
		return false;
	}

	@Override
	public void modifyEntry(MenuEntry event)
	{
		if (client == null || event.isForceLeftClick())
		{
			return;
		}
		int id = event.getIdentifier();
		int item = findItem(customClickMap.get(id)).getLeft();
		final String name = client.getItemComposition(item).getName();
		MenuEntry e = event.clone();
		e.setTarget("<col=ff9040>" + name + "<col=ffffff> -> " + getTargetMap().get(id));
		e.setForceLeftClick(true);
		insert(e);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		return event.getMenuAction() == MenuAction.ITEM_USE &&
			customClickMap.containsKey(event.getId());
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{
		if (updateSelectedItem(customClickMap.get(event.getId())))
		{
			event.setMenuAction(MenuAction.ITEM_USE_ON_WIDGET_ITEM);
		}
	}

	@Override
	public void backupEntryModify(MenuEntry e)
	{
		if (client == null || e.isForceLeftClick())
		{
			return;
		}
		int id = e.getIdentifier();
		int item = findItem(customClickMap.get(id)).getLeft();
		final String name = client.getItemComposition(item).getName();
		e.setTarget("<col=ff9040>" + name + "<col=ffffff> -> " + getTargetMap().get(id));
		e.setForceLeftClick(true);
	}

	public void updateMap(String swaps)
	{
		final Iterable<String> tmp = NEWLINE_SPLITTER.split(swaps);

		for (String s : tmp)
		{
			if (s.startsWith("//"))
			{
				continue;
			}

			String[] split = s.split(":");

			try
			{
				int oneClickThat = Integer.parseInt(split[0]);
				int withThis = Integer.parseInt(split[1]);
				if (customClickMap.containsKey(oneClickThat))
				{
					customClickMap.get(oneClickThat).add(withThis);
					continue;
				}
				customClickMap.put(oneClickThat, new ArrayList<>(withThis));
			}
			catch (Exception e)
			{
				log.error("Error: ", e);
				return;
			}
		}
	}
}
