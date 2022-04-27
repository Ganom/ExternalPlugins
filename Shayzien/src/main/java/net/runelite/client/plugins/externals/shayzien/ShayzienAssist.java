package net.runelite.client.plugins.externals.shayzien;

import com.google.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.MenuAction;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.ui.overlay.OverlayManager;
import org.pf4j.Extension;

@PluginDescriptor(
	name = "Shayzien Assist",
	tags = {"oneclick", "medic", "medpack", "ganom"}
)
@Extension
public class ShayzienAssist extends Plugin
{
	@Inject
	private Client client;

	@Inject
	private InfirmaryOverlay overlay;

	@Inject
	private OverlayManager overlayManager;

	private final List<ItemData> inventory = new ArrayList<>();
	@Getter
	private int regionId = -1;

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		regionId = client.getLocalPlayer().getWorldLocation() == null ? -1 :
			client.getLocalPlayer().getWorldLocation().getRegionID();
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (event.getType() != MenuAction.EXAMINE_NPC.getId() ||
			!event.getTarget().contains("Wounded soldier") ||
			event.isForceLeftClick() ||
			findItem(13382) == null)
		{
			return;
		}
		client.createMenuEntry(client.getMenuOptionCount())
			.setOption("Heal")
			.setTarget(event.getTarget())
			.setType(MenuAction.WIDGET_TARGET_ON_NPC)
			.setIdentifier(event.getIdentifier())
			.setParam0(event.getActionParam0())
			.setParam1(event.getActionParam1())
			.setForceLeftClick(true);
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (!event.getMenuOption().equals("Heal") || !event.getMenuTarget().contains("Wounded soldier"))
		{
			return;
		}
		updateSelectedItem(13382);
	}

	@Subscribe
	public void onItemContainerChanged(ItemContainerChanged event)
	{
		if (event.getContainerId() != InventoryID.INVENTORY.getId() || event.getItemContainer() == null)
		{
			return;
		}

		inventory.clear();

		var containerItems = event.getItemContainer().getItems();

		for (int index = 0; index < containerItems.length; index++)
		{
			var item = containerItems[index];

			if (item.getId() == -1 || item.getId() == 6512 || item.getQuantity() < 1)
			{
				continue;
			}

			var def = client.getItemComposition(item.getId());
			inventory.add(new ItemData(item.getId(), item.getQuantity(), index, def.getName(), def));
		}
	}

	public void updateSelectedItem(int id)
	{
		updateSelectedItem(List.of(id));
	}

	public void updateSelectedItem(Collection<Integer> ids)
	{
		if (client == null)
		{
			return;
		}
		ItemData pair = findItem(ids);
		if (pair == null)
		{
			return;
		}
		setSelectedWidget(pair);
	}

	public ItemData findItem(int id)
	{
		return findItem(List.of(id));
	}

	public ItemData findItem(Collection<Integer> ids)
	{
		return inventory
			.stream()
			.filter(i -> ids.contains(i.getId()))
			.max(Comparator.comparingInt(ItemData::getIndex))
			.orElse(null);
	}

	public void setSelectedWidget(ItemData item)
	{
		client.setSelectedSpellWidget(WidgetInfo.INVENTORY.getId());
		client.setSelectedSpellChildIndex(item.getIndex());
		client.setSelectedSpellItemId(item.getId());
		client.setSpellSelected(true);
	}
}
