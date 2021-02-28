package net.runelite.client.plugins.externals.oneclick.comparables;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Setter;
import net.runelite.api.Client;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.api.widgets.WidgetItem;
import net.runelite.client.plugins.externals.oneclick.OneClickPlugin;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

public abstract class ClickCompare
{
	@Nullable
	@Setter
	protected Client client;

	@Nullable
	@Setter
	protected OneClickPlugin plugin;

	public abstract boolean isEntryValid(MenuEntry event);

	public abstract void modifyEntry(MenuEntry event);

	public abstract boolean isClickValid(MenuOptionClicked event);

	public abstract void modifyClick(MenuOptionClicked event);

	public abstract void backupEntryModify(MenuEntry e);

	protected Map<Integer, String> getTargetMap()
	{
		if (plugin == null)
		{
			return new HashMap<>();
		}
		return plugin.getTargetMap();
	}

	public void insert(MenuEntry e)
	{
		if (client == null)
		{
			return;
		}
		client.insertMenuItem(
			e.getOption(),
			e.getTarget(),
			e.getOpcode(),
			e.getIdentifier(),
			e.getParam0(),
			e.getParam1(),
			true
		);
	}

	public boolean updateSelectedItem(int id)
	{
		if (client == null)
		{
			return false;
		}
		final Pair<Integer, Integer> pair = findItem(id);
		if (pair.getLeft() != -1)
		{
			client.setSelectedItemWidget(WidgetInfo.INVENTORY.getId());
			client.setSelectedItemSlot(pair.getRight());
			client.setSelectedItemID(pair.getLeft());
			return true;
		}
		return false;
	}

	public boolean updateSelectedItem(Collection<Integer> ids)
	{
		if (client == null)
		{
			return false;
		}
		final Pair<Integer, Integer> pair = findItem(ids);
		if (pair.getLeft() != -1)
		{
			client.setSelectedItemWidget(WidgetInfo.INVENTORY.getId());
			client.setSelectedItemSlot(pair.getRight());
			client.setSelectedItemID(pair.getLeft());
			return true;
		}
		return false;
	}

	public Pair<Integer, Integer> findItem(int id)
	{
		if (client == null)
		{
			return Pair.of(-1, -1);
		}
		final Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		final List<WidgetItem> itemList = (List<WidgetItem>) inventoryWidget.getWidgetItems();

		for (int i = itemList.size() - 1; i >= 0; i--)
		{
			final WidgetItem item = itemList.get(i);
			if (item.getId() == id)
			{
				return Pair.of(item.getId(), item.getIndex());
			}
		}

		return Pair.of(-1, -1);
	}

	public Pair<Integer, Integer> findItem(Collection<Integer> ids)
	{
		if (client == null)
		{
			return Pair.of(-1, -1);
		}

		final Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		final List<WidgetItem> itemList = (List<WidgetItem>) inventoryWidget.getWidgetItems();

		for (int i = itemList.size() - 1; i >= 0; i--)
		{
			final WidgetItem item = itemList.get(i);
			if (ids.contains(item.getId()))
			{
				return Pair.of(item.getId(), item.getIndex());
			}
		}

		return Pair.of(-1, -1);
	}
}
