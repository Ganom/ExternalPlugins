package net.runelite.client.plugins.externals.oneclick.clickables;

import com.google.inject.Inject;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.externals.oneclick.pojos.ItemData;
import net.runelite.client.plugins.externals.oneclick.OneClick;

@Slf4j
public abstract class Clickable
{
	@Inject
	protected Client client;
	@Inject
	protected OneClick plugin;

	public abstract boolean isValidEntry(MenuEntryAdded event);

	public abstract boolean isValidClick(MenuOptionClicked event);

	public boolean updateSelectedItem(int id)
	{
		return updateSelectedItem(List.of(id));
	}

	public boolean updateSelectedItem(Collection<Integer> ids)
	{
		if (client == null)
		{
			return false;
		}
		ItemData pair = findItem(ids);
		if (pair == null)
		{
			return false;
		}
		return setSelectedWidget(pair);
	}

	public ItemData findItem(int id)
	{
		return findItem(List.of(id));
	}

	public ItemData findItem(Collection<Integer> ids)
	{
		return plugin.getInventory()
			.stream()
			.filter(i -> ids.contains(i.getId()))
			.max(Comparator.comparingInt(ItemData::getIndex))
			.orElse(null);
	}

	public boolean setSelectedWidget(ItemData item)
	{
		client.setSelectedSpellWidget(WidgetInfo.INVENTORY.getId());
		client.setSelectedSpellChildIndex(item.getIndex());
		client.setSelectedSpellItemId(item.getId());
		client.setSpellSelected(true);
		return true;
	}

	public void setSelectSpell(WidgetInfo info)
	{
		Widget widget = client.getWidget(info);
		if (widget == null)
		{
			log.info("Unable to locate spell widget.");
			return;
		}
		client.setSelectedSpellName("<col=00ff00>" + widget.getName() + "</col>");
		client.setSelectedSpellWidget(widget.getId());
		client.setSelectedSpellChildIndex(-1);
	}
}
