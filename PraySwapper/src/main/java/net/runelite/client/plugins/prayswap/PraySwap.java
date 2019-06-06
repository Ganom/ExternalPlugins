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

package net.runelite.client.plugins.prayswap;

import com.google.inject.Provides;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import lombok.Getter;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.events.ConfigChanged;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.callback.ClientThread;
import net.runelite.client.chat.ChatMessageManager;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.game.ItemManager;
import net.runelite.client.input.KeyListener;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.prayswap.utils.Tab;
import net.runelite.client.plugins.prayswap.utils.TabUtils;
import net.runelite.client.plugins.stretchedmode.StretchedModeConfig;
import net.runelite.client.ui.overlay.OverlayManager;

@PluginDescriptor(
	name = "PraySwap",
	description = "Use plugin in PvP situations for best results",
	tags = {"highlight", "pvp", "overlay", "players"},
	type = PluginType.EXTERNAL
)

public class PraySwap extends Plugin implements KeyListener
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
	private PraySwapConfig config;
	@Inject
	private ClientThread clientThread;
	@Getter
	private Widget widget;
	@Inject
	private ConfigManager externalConfig;
	@Inject
	private TabUtils tabUtils;

	private ExecutorService executorService = Executors.newFixedThreadPool(1);
	private double scalingfactor;
	private Flexo flexo;

	@Provides
	PraySwapConfig getConfig(ConfigManager manager)
	{
		return (PraySwapConfig) manager.getConfig(PraySwapConfig.class);
	}

	protected void startUp()
	{
		keyManager.registerKeyListener(this);
		scalingfactor = externalConfig.getConfig(StretchedModeConfig.class).scalingFactor();
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
		keyManager.unregisterKeyListener(this);
	}

	@Subscribe
	public void onConfigChanged(ConfigChanged event)
	{
		if (event.getGroup().equals("stretchedmode"))
		{
			scalingfactor = externalConfig.getConfig(StretchedModeConfig.class).scalingFactor();
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		if (shouldProtPray())
		{
			executePrayer(WidgetInfo.PRAYER_PROTECT_ITEM);
		}
	}

	@Override
	public void keyTyped(KeyEvent e)
	{
	}

	@Override
	public void keyReleased(KeyEvent e)
	{
	}

	@Override
	public void keyPressed(KeyEvent e)
	{
		int MageHotkey = config.hotkeyMage().getKeyCode();
		int RangeHotkey = config.hotkeyRange().getKeyCode();
		int MeleeHotkey = config.hotkeyMelee().getKeyCode();
		int AuguryHotkey = config.hotkeyAugury().getKeyCode();
		int RigourHotkey = config.hotkeyRigour().getKeyCode();
		int PietyHotkey = config.hotkeyPiety().getKeyCode();
		int SmiteHotkey = config.hotkeySmite().getKeyCode();
		if (e.getKeyCode() == MageHotkey)
		{
			executePrayer(WidgetInfo.PRAYER_PROTECT_FROM_MAGIC);
		}
		if (e.getKeyCode() == RangeHotkey)
		{
			executePrayer(WidgetInfo.PRAYER_PROTECT_FROM_MISSILES);
		}
		if (e.getKeyCode() == MeleeHotkey)
		{
			executePrayer(WidgetInfo.PRAYER_PROTECT_FROM_MELEE);
		}
		if (e.getKeyCode() == AuguryHotkey)
		{
			executePrayer(WidgetInfo.PRAYER_AUGURY);
		}
		if (e.getKeyCode() == RigourHotkey)
		{
			executePrayer(WidgetInfo.PRAYER_RIGOUR);
		}
		if (e.getKeyCode() == PietyHotkey)
		{
			executePrayer(WidgetInfo.PRAYER_PIETY);
		}
		if (e.getKeyCode() == SmiteHotkey)
		{
			executePrayer(WidgetInfo.PRAYER_SMITE);
		}
	}

	private boolean shouldProtPray()
	{
		return config.protectItem() && !client.isPrayerActive(Prayer.PROTECT_ITEM) && client.getBoostedSkillLevel(Skill.PRAYER) >= 1;
	}

	private void executePrayer(WidgetInfo prayer)
	{
		Widget pray = client.getWidget(prayer);
		executorService.submit(() -> clickPrayer(pray));
	}

	private void clickPrayer(Widget prayer)
	{
		if (client.getWidget(WidgetInfo.PRAYER_PROTECT_FROM_MELEE).isHidden())
		{
			flexo.keyPress(tabUtils.getTabHotkey(Tab.PRAYER));
		}
		if (prayer != null)
		{
			handleSwitch(prayer.getBounds());
		}
	}

	private void handleSwitch(Rectangle rectangle)
	{
		Point cp = getClickPoint(rectangle);
		if (cp.getX() >= 1)
		{
			switch (config.actionType())
			{
				case FLEXO:
					flexo.mouseMove(cp.getX(), cp.getY());
					flexo.mousePressAndRelease(1);
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
					break;
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
			double scale = 1 + (scalingfactor / 100);

			MouseEvent mousePressed =
				new MouseEvent(this.client.getCanvas(), 501, System.currentTimeMillis(), 0, (int) (this.client.getMouseCanvasPosition().getX() * scale), (int) (this.client.getMouseCanvasPosition().getY() * scale), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mousePressed);
			MouseEvent mouseReleased =
				new MouseEvent(this.client.getCanvas(), 502, System.currentTimeMillis(), 0, (int) (this.client.getMouseCanvasPosition().getX() * scale), (int) (this.client.getMouseCanvasPosition().getY() * scale), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mouseReleased);
			MouseEvent mouseClicked =
				new MouseEvent(this.client.getCanvas(), 500, System.currentTimeMillis(), 0, (int) (this.client.getMouseCanvasPosition().getX() * scale), (int) (this.client.getMouseCanvasPosition().getY() * scale), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mouseClicked);
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

	private Point getClickPoint(Rectangle rect)
	{
		if (client.isStretchedEnabled())
		{
			int rand = (Math.random() <= 0.5) ? 1 : 2;
			int x = (int) (rect.getX() + (rand * 3) + rect.getWidth() / 2);
			int y = (int) (rect.getY() + (rand * 3) + rect.getHeight() / 2);
			double scale = 1 + (scalingfactor / 100);
			return new Point((int) (x * scale), (int) (y * scale));
		}
		else
		{
			int rand = (Math.random() <= 0.5) ? 1 : 2;
			int x = (int) (rect.getX() + (rand * 3) + rect.getWidth() / 2);
			int y = (int) (rect.getY() + (rand * 3) + rect.getHeight() / 2);
			return new Point(x, y);
		}
	}
}
