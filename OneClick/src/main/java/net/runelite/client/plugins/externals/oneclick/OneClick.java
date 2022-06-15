package net.runelite.client.plugins.externals.oneclick;

import com.google.common.base.Splitter;
import com.google.inject.Inject;
import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.KeyCode;
import net.runelite.api.MenuAction;
import static net.runelite.api.MenuAction.CC_OP_LOW_PRIORITY;
import net.runelite.api.Skill;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOpened;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.events.XpDropEvent;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.externals.oneclick.clickables.Clickable;
import net.runelite.client.plugins.externals.oneclick.clickables.minigames.Healers;
import net.runelite.client.plugins.externals.oneclick.config.Combat;
import net.runelite.client.plugins.externals.oneclick.config.Custom;
import net.runelite.client.plugins.externals.oneclick.config.Minigame;
import net.runelite.client.plugins.externals.oneclick.config.Skilling;
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
	private static final String MAGIC_IMBUE_EXPIRED_MESSAGE = "Your Magic Imbue charge has ended.";
	private static final String MAGIC_IMBUE_MESSAGE = "You are charged to combine runes!";
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
	@Getter
	private final Set<Integer> highAlchs = new HashSet<>();
	@Setter
	private boolean tick;
	@Getter
	private String roleText = "";
	@Getter
	private boolean imbued;
	@Getter
	private int highAlchTicks;

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
		imbued = false;
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (highAlchTicks > 0)
		{
			highAlchTicks--;
		}
		if (tick)
		{
			tick = false;
		}

		updateBarbarianAssaultRoleCallText();
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		switch (event.getMessage())
		{
			case MAGIC_IMBUE_MESSAGE:
				imbued = true;
				break;
			case MAGIC_IMBUE_EXPIRED_MESSAGE:
				imbued = false;
				break;
		}
	}

	@Subscribe
	public void onXpDrop(XpDropEvent event)
	{
		if (event.getExp() != 65 || event.getSkill() != Skill.MAGIC)
		{
			return;
		}
		highAlchTicks = 4;
	}

	@Subscribe
	public void onMenuOpened(MenuOpened event)
	{
		if (!config.getSkillingOneClicks().contains(Skilling.HIGH_ALCH) || !client.isKeyPressed(KeyCode.KC_SHIFT))
		{
			return;
		}

		boolean hasExamineItemEntry = Arrays.stream(event.getMenuEntries())
			.filter(e -> !e.isForceLeftClick())
			.filter(e -> e.getOption().equals("Examine"))
			.anyMatch(e -> e.getType() == CC_OP_LOW_PRIORITY);

		if (!hasExamineItemEntry)
		{
			return;
		}

		var item = inventory.stream()
			.filter(itemData -> itemData.getIndex() == event.getFirstEntry().getParam0())
			.findFirst()
			.orElse(null);

		if (item == null)
		{
			return;
		}

		if (highAlchs.contains(item.getId()))
		{
			client.createMenuEntry(client.getMenuOptionCount())
				.setOption("Remove")
				.setTarget("<col=ff0000>Alch Target</col><col=ffffff> -> <col=ff9040>" + item.getName() + "</col>")
				.setType(MenuAction.RUNELITE_HIGH_PRIORITY)
				.onClick(e -> highAlchs.remove(item.getId()));
			return;
		}

		client.createMenuEntry(client.getMenuOptionCount())
			.setOption("Select")
			.setTarget("<col=00ff00>Alch Target</col><col=ffffff> -> <col=ff9040>" + item.getName() + "</col>")
			.setType(MenuAction.RUNELITE_HIGH_PRIORITY)
			.onClick(e -> highAlchs.add(item.getId()));
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

	private void updateConfig()
	{
		clickable.clear();
		clientThread.invoke(() ->
		{
			convertStringToCustomItemMap();
			clickable.addAll(Combat.createInstances(config.getCombatOneClicks(), injector));
			clickable.addAll(Custom.createInstances(config.getCustomOneClicks(), injector));
			clickable.addAll(Minigame.createInstances(config.getMinigameOneClicks(), injector));
			clickable.addAll(Skilling.createInstances(config.getSkillingOneClicks(), injector));
		});
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
