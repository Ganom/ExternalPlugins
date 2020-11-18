package net.runelite.client.plugins.externals.leaguetp;

import com.google.inject.Provides;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.events.ConfigChanged;
import net.runelite.client.game.SpriteManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.ui.overlay.tooltip.TooltipManager;
import org.pf4j.Extension;

@PluginDescriptor(
	name = "League Teleporter",
	type = PluginType.MISCELLANEOUS,
	description = "Adds widgets directly to screen so you can teleport without navigating 500 menus."
)
@Slf4j
@Extension
public class LeagueTeleport extends Plugin
{
	private static final MenuEntry LEAGUES = new MenuEntry("Leagues", "", 1, MenuOpcode.CC_OP.getId(), -1, WidgetInfo.RESIZABLE_VIEWPORT_QUESTS_TAB.getId(), false);
	private static final MenuEntry VIEW_AREAS = new MenuEntry("View Areas", "", 1, MenuOpcode.CC_OP.getId(), -1, 42991644, false);
	private static final MenuEntry VIEW = new MenuEntry("View", "<col=ff981f>Area</col>", 1, MenuOpcode.CC_OP.getId(), -1, 33554457, false);
	private static final MenuEntry TELEPORT_TO = new MenuEntry("Teleport to", "<col=ff981f>Area</col>", 1, MenuOpcode.CC_OP.getId(), -1, WidgetInfo.TRAILBLAZER_AREA_TELEPORT.getId(), false);

	@Inject
	private Client client;

	@Inject
	private ClientThread clientThread;

	@Inject
	private LeagueTeleportConfig config;

	@Inject
	private OverlayManager overlayManager;

	@Inject
	private SpriteManager spriteManager;

	@Inject
	private TooltipManager tooltipManager;

	private final List<Teleport> unlocked = new ArrayList<>();
	private SelectorOverlay overlay;
	private boolean tick;
	private boolean ignore;
	private int fail;

	@Provides
	LeagueTeleportConfig providePlusConfig(ConfigManager configManager)
	{
		return configManager.getConfig(LeagueTeleportConfig.class);
	}

	@Override
	public void startUp()
	{
		overlay = new SelectorOverlay(this, client, config, spriteManager, tooltipManager);
		overlayManager.add(overlay);
		if (client.getGameState() == GameState.LOGGED_IN && !unlocked.isEmpty())
		{
			overlay.refresh(unlocked);
		}
	}

	@Override
	public void shutDown()
	{
		overlayManager.remove(overlay);
		overlay = null;
		tick = false;
		ignore = false;
		fail = 0;
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (!event.getGroup().equals("LeagueTeleportConfig"))
		{
			return;
		}

		overlay.refresh(unlocked);
	}

	@Subscribe
	public void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (ignore)
		{
			return;
		}

		if (overlay.getHoveredComponent() != null)
		{
			ignore = true;
			event.consume();
			event.setMenuEntry(LEAGUES);
			invoke(VIEW_AREAS);
			VIEW.setParam1(overlay.getHoveredComponent().getTeleport().getWidgetId());
			tick = true;
			invoke(VIEW);
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		updateUnlocks();

		if (!tick)
		{
			return;
		}

		Widget w = client.getWidget(512, 1);

		if (w == null || w.isHidden())
		{
			fail++;
			if (fail > 3)
			{
				fail = 0;
				tick = false;
				ignore = false;
				log.info("Failed, reset.");
			}
			return;
		}

		w.setHidden(true);
		w.revalidate();
		invoke(TELEPORT_TO);
		tick = false;
		ignore = false;
	}

	private void updateUnlocks()
	{
		Widget w = client.getWidget(512, 8);

		if (w == null)
		{
			return;
		}

		Widget[] widgets = w.getStaticChildren();
		List<Teleport> teleports = new ArrayList<>();

		for (Widget widget : widgets)
		{
			Teleport tp = Teleport.getAreaBySprite(widget.getSpriteId());
			if (tp != null)
			{
				teleports.add(tp);
			}
		}

		if (teleports.size() > 0 && teleports.size() != unlocked.size())
		{
			unlocked.clear();
			unlocked.addAll(teleports);
			overlay.refresh(unlocked);
		}
	}

	private void invoke(MenuEntry e)
	{
		clientThread.invoke(() -> client.invokeMenuAction(e.getOption(), e.getTarget(), e.getIdentifier(), e.getOpcode(), e.getParam0(), e.getParam1()));
	}
}
