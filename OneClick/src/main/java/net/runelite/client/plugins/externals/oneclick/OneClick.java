package net.runelite.client.plugins.externals.oneclick;

import com.google.common.base.Splitter;
import com.google.inject.Inject;
import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.externals.oneclick.clickables.Clickable;
import net.runelite.client.plugins.externals.oneclick.clickables.misc.Healers;
import net.runelite.client.plugins.externals.oneclick.pojos.CustomItem;
import net.runelite.client.plugins.externals.oneclick.pojos.ItemData;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "One Click",
	description = "OP One Click methods.",
	tags = "ganom"
)
public class OneClick extends Plugin
{
	private static final int BA_CALL_LISTEN = 7;
	private static final int BA_HEALER_GROUP_ID = 488;

	@Inject
	private Client client;
	@Inject
	private ClientThread clientThread;
	@Inject
	private OneClickConfig config;

	@Getter
	private final List<ItemData> inventory = new ArrayList<>();
	@Getter
	private final List<CustomItem> items = new ArrayList<>();
	private final List<Clickable> clickable = new ArrayList<>();
	@Setter
	private boolean tick;
	@Getter
	private String roleText = "";


	@Provides
	OneClickConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(OneClickConfig.class);
	}

	@Override
	protected void startUp()
	{
		updateConfig();
	}

	@Override
	protected void shutDown()
	{
		clickable.clear();
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (tick)
		{
			tick = false;
		}

		updateBarbarianAssaultRoleCallText();
	}

	@Subscribe
	public void onMenuEntryAdded(MenuEntryAdded event)
	{
		for (Clickable method : clickable)
		{
			if (method.isValidEntry(event))
			{
				//Don't allow more than 2 methods to modify same event.
				break;
			}
		}
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (tick)
		{
			event.consume();
			return;
		}

		for (Clickable method : clickable)
		{
			if (method.isValidClick(event))
			{
				//Don't allow more than 2 methods to modify same event.
				break;
			}
		}
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

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals("oneclick"))
		{
			return;
		}
		updateConfig();
	}

	private void updateBarbarianAssaultRoleCallText()
	{
		if (clickable.stream().noneMatch(c -> c instanceof Healers))
		{
			return;
		}

		Widget widget = client.getWidget(BA_HEALER_GROUP_ID, BA_CALL_LISTEN);

		if (widget == null || widget.getText() == null)
		{
			roleText = "";
			return;
		}

		roleText = widget.getText().trim();
	}

	private void updateConfig()
	{
		clickable.clear();
		clientThread.invoke(() ->
		{
			convertStringToCustomItemMap();
			config.getOneClickMethods()
				.stream()
				.filter(Objects::nonNull)
				.map(m -> m.createInstance(injector))
				.forEach(clickable::add);
		});
	}

	@SuppressWarnings("UnstableApiUsage")
	private void convertStringToCustomItemMap()
	{
		items.clear();
		Splitter.on("\n")
			.omitEmptyStrings()
			.trimResults()
			.splitToStream(config.getCustomIds())
			.map((string) -> CustomItem.from(client, string))
			.filter(Objects::nonNull)
			.forEach(items::add);
	}
}
