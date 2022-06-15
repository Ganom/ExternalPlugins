package net.runelite.client.plugins.externals.oneclick.clickables;

import com.google.inject.Inject;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.plugins.externals.oneclick.OneClick;
import net.runelite.client.plugins.externals.oneclick.OneClickConfig;
import net.runelite.client.plugins.externals.oneclick.pojos.ItemData;

@Slf4j
public abstract class Clickable
{
	@Inject
	protected Client client;
	@Inject
	protected OneClick plugin;
	@Inject
	protected OneClickConfig config;

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
		ItemData pair = findItemWithIds(ids);
		if (pair == null)
		{
			return false;
		}
		return setSelectedWidget(pair);
	}

	public ItemData findItem(int id)
	{
		return findItemWithIds(List.of(id));
	}

	public ItemData findItem(String itemName)
	{
		return findItemByName(List.of(itemName));
	}

	public ItemData findItemWithIds(Collection<Integer> ids)
	{
		return plugin.getInventory()
			.stream()
			.filter(i -> ids.contains(i.getId()))
			.max(Comparator.comparingInt(ItemData::getIndex))
			.orElse(null);
	}

	public ItemData findItemByName(Collection<String> itemNames)
	{
		Collection<String> lowercaseItemNames = itemNames.stream().map(String::toLowerCase).collect(Collectors.toList());
		return plugin.getInventory()
			.stream()
			.filter(i -> lowercaseItemNames.contains(i.getName()))
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

	protected int getVarbitFromObjectId(int id)
	{
		var objectDefinition = client.getObjectDefinition(id);
		if (objectDefinition == null)
		{
			return -1;
		}
		return objectDefinition.getVarbitId();
	}

	protected int getItemCount(int id)
	{
		return (int) plugin.getInventory()
			.stream()
			.filter(i -> id == i.getId())
			.count();
	}

	protected void refreshInventory()
	{
		client.runScript(6009, WidgetInfo.INVENTORY.getId(), 28, 1, 0);
	}
}
