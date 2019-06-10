/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this
 *    list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package net.runelite.client.plugins.olmswapper;

import com.google.inject.Provides;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.Prayer;
import net.runelite.api.Projectile;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ProjectileMoved;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.olmswapper.utils.Tab;
import net.runelite.client.plugins.olmswapper.utils.TabUtils;
import net.runelite.client.plugins.stretchedmode.StretchedModeConfig;
import net.runelite.client.ui.overlay.OverlayManager;


@PluginDescriptor(
	name = "Olm Pray Swapper",
	description = "Automatically swaps prayers for CoX",
	tags = {"prayer", "olm", "bot", "swap"},
	type = PluginType.EXTERNAL
)

@Slf4j
public class olmswapper extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private ItemManager itemManager;
	@Inject
	private ChatMessageManager chatMessageManager;
	@Inject
	private KeyManager keyManager;
	@Inject
	private olmswapperSettings config;
	@Inject
	private ConfigManager externalConfig;
	@Inject
	private ClientThread clientThread;
	@Inject
	private TabUtils tabUtils;
	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	private boolean magic;
	private boolean range;
	private double scalingFactor;
	private Flexo flexo;


	@Provides
	olmswapperSettings getConfig(ConfigManager manager)
	{
		return (olmswapperSettings) manager.getConfig(olmswapperSettings.class);
	}

	private Point getClickPoint(Rectangle2D rect)
	{
		if (client.isStretchedEnabled())
		{
			int rand = (Math.random() <= 0.5) ? 1 : 2;
			int x = (int) (rect.getX() + rand + rect.getWidth() / 2);
			int y = (int) (rect.getY() + rand + rect.getHeight() / 2);
			double scale = 1 + ((double) scalingFactor / 100);
			return new Point((int) (x * scale), (int) (y * scale));
		}
		else
		{
			int rand = (Math.random() <= 0.5) ? 1 : 2;
			int x = (int) (rect.getX() + rand + rect.getWidth() / 2);
			int y = (int) (rect.getY() + rand + rect.getHeight() / 2);
			return new Point(x, y);
		}
	}

	protected void startUp()
	{
		scalingFactor = externalConfig.getConfig(StretchedModeConfig.class).scalingFactor();
		Flexo.client = client;
		executorService.submit(() -> {
			flexo = null;
			try
			{
				flexo = new Flexo();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		});
	}

	protected void shutDown()
	{
		flexo = null;
	}

	@Subscribe
	public void onChatMessage(ChatMessage event)
	{
		final String msg = event.getMessage();
		if (event.getType() == ChatMessageType.GAMEMESSAGE)
		{
			if (msg.toLowerCase().contains("aggression") && !client.isPrayerActive(Prayer.PROTECT_FROM_MELEE))
			{
				executeMeleePray();
			}
			if (msg.toLowerCase().contains("of magical power") && !client.isPrayerActive(Prayer.PROTECT_FROM_MAGIC))
			{
				executeMagePray();
			}
			if (msg.toLowerCase().contains("dexterity") && !client.isPrayerActive(Prayer.PROTECT_FROM_MISSILES))
			{
				executeRangedPray();
			}
		}
	}

	@Subscribe
	public void onProjectileMoved(ProjectileMoved event)
	{
		Projectile projectile = event.getProjectile();
		if (projectile.getId() == 1339 && !client.isPrayerActive(Prayer.PROTECT_FROM_MAGIC))
		{
			magic = true;
		}
		if (projectile.getId() == 1340 && !client.isPrayerActive(Prayer.PROTECT_FROM_MISSILES))
		{
			range = true;
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (magic)
		{
			executeMagePray();
			magic = false;
		}
		if (range)
		{
			executeRangedPray();
			range = false;
		}
	}

	private void executeMagePray()
	{
		Widget prayerMage = client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MAGIC);
		executorService.submit(() ->
			clickPrayer(prayerMage));
	}

	private void executeRangedPray()
	{
		Widget prayerRanged = client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MISSILES);
		executorService.submit(() ->
			clickPrayer(prayerRanged));
	}

	private void executeMeleePray()
	{
		Widget prayerMelee = client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MELEE);
		executorService.submit(() ->
			clickPrayer(prayerMelee));
	}


	private void clickPrayer(Widget prayer)
	{
		if (client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MELEE).isHidden())
		{
			flexo.keyPress(tabUtils.getTabHotkey(Tab.PRAYER));
		}
		if (prayer != null)
		{
			Rectangle bounds = prayer.getBounds();
			Point cp = getClickPoint(bounds);
			if (cp.getX() >= 1)
			{
				if (client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MELEE).isHidden())
				{
					return;
				}
				switch (config.actionType())
				{
					case FLEXO:
						flexo.mouseMove(cp.getX(), cp.getY());
						flexo.mousePressAndRelease(1);
						flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
						break;
					case MOUSEEVENTS:
						leftClick(cp.getX(), cp.getY());
						try
						{
							Thread.sleep(getMillis());
						}
						catch (InterruptedException e)
						{
							e.printStackTrace();
						}
						flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
						break;
				}
			}
		}
	}

	private long getMillis()
	{
		return (long) (Math.random() * config.randLow() + config.randHigh());
	}

	private void leftClick(int x, int y)
	{
		if (client.isStretchedEnabled())
		{
			Point p = this.client.getMouseCanvasPosition();
			if (p.getX() != x || p.getY() != y)
			{
				this.moveMouse(x, y);
			}
			double scale = 1 + ((double) scalingFactor / 100);

			MouseEvent mousePressed =
				new MouseEvent(this.client.getCanvas(), 501, System.currentTimeMillis(), 0, (int) (this.client.getMouseCanvasPosition().getX() * (double) scale), (int) (this.client.getMouseCanvasPosition().getY() * (double) scale), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mousePressed);
			System.out.println("Mouse Pressed:" + this.client.getMouseCanvasPosition());
			MouseEvent mouseReleased =
				new MouseEvent(this.client.getCanvas(), 502, System.currentTimeMillis(), 0, (int) (this.client.getMouseCanvasPosition().getX() * (double) scale), (int) (this.client.getMouseCanvasPosition().getY() * (double) scale), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mouseReleased);
			System.out.println("Mouse Released:" + this.client.getMouseCanvasPosition());
			MouseEvent mouseClicked =
				new MouseEvent(this.client.getCanvas(), 500, System.currentTimeMillis(), 0, (int) (this.client.getMouseCanvasPosition().getX() * (double) scale), (int) (this.client.getMouseCanvasPosition().getY() * (double) scale), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mouseClicked);
			System.out.println("Mouse Clicked:" + this.client.getMouseCanvasPosition());
		}
		if (!client.isStretchedEnabled())
		{
			Point p = this.client.getMouseCanvasPosition();
			if (p.getX() != x || p.getY() != y)
			{
				this.moveMouse(x, y);
			}
			MouseEvent mousePressed = new MouseEvent(this.client.getCanvas(), 501, System.currentTimeMillis(), 0, this.client.getMouseCanvasPosition().getX(), this.client.getMouseCanvasPosition().getY(), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mousePressed);
			MouseEvent mouseReleased = new MouseEvent(this.client.getCanvas(), 502, System.currentTimeMillis(), 0, this.client.getMouseCanvasPosition().getX(), this.client.getMouseCanvasPosition().getY(), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mouseReleased);
			MouseEvent mouseClicked = new MouseEvent(this.client.getCanvas(), 500, System.currentTimeMillis(), 0, this.client.getMouseCanvasPosition().getX(), this.client.getMouseCanvasPosition().getY(), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mouseClicked);
		}
	}

	private void moveMouse(int x, int y)
	{
		MouseEvent mouseEntered = new MouseEvent(this.client.getCanvas(), 504, System.currentTimeMillis(), 0, x, y, 0, false);
		this.client.getCanvas().dispatchEvent(mouseEntered);
		MouseEvent mouseExited = new MouseEvent(this.client.getCanvas(), 505, System.currentTimeMillis(), 0, x, y, 0, false);
		this.client.getCanvas().dispatchEvent(mouseExited);
		MouseEvent mouseMoved = new MouseEvent(this.client.getCanvas(), 503, System.currentTimeMillis(), 0, x, y, 0, false);
		this.client.getCanvas().dispatchEvent(mouseMoved);
	}
}
