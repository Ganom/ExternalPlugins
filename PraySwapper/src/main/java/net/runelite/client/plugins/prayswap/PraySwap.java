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
import java.awt.AWTException;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Point;
import net.runelite.api.Prayer;
import static net.runelite.api.Prayer.*;
import net.runelite.api.VarClientInt;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.vars.InterfaceTab;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.prayswap.utils.Tab;
import net.runelite.client.plugins.prayswap.utils.TabUtils;
import net.runelite.client.plugins.stretchedmode.StretchedModeConfig;
import net.runelite.client.util.HotkeyListener;

@PluginDescriptor(
	name = "PraySwap",
	description = "Use plugin in PvP situations for best results",
	tags = {"highlight", "pvp", "overlay", "players"},
	type = PluginType.EXTERNAL
)

public class PraySwap extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private KeyManager keyManager;
	@Inject
	private PraySwapConfig config;
	@Inject
	private ConfigManager configManager;
	@Inject
	private EventBus eventBus;
	@Inject
	private TabUtils tabUtils;
	private final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);
	private final ThreadPoolExecutor executorService = new ThreadPoolExecutor(2, 2, 2, TimeUnit.SECONDS, queue, new ThreadPoolExecutor.DiscardPolicy());
	private Flexo flexo;

	@Provides
	PraySwapConfig getConfig(ConfigManager manager)
	{
		return manager.getConfig(PraySwapConfig.class);
	}

	@Override
	protected void startUp()
	{
		Flexo.client = client;
		executorService.submit(() ->
		{
			try
			{
				flexo = new Flexo();
			}
			catch (AWTException e)
			{
				e.printStackTrace();
			}
		});
		eventBus.subscribe(GameStateChanged.class, this, this::onGameStateChanged);
	}

	@Override
	protected void shutDown()
	{
		eventBus.unregister(this);
	}

	private void onGameStateChanged(GameStateChanged event)
	{
		if (event.getGameState() != GameState.LOGGED_IN)
		{
			keyManager.unregisterKeyListener(melee);
			keyManager.unregisterKeyListener(range);
			keyManager.unregisterKeyListener(mage);
			keyManager.unregisterKeyListener(augury);
			keyManager.unregisterKeyListener(rigour);
			keyManager.unregisterKeyListener(piety);
			keyManager.unregisterKeyListener(smite);
			keyManager.unregisterKeyListener(comboOne);
			keyManager.unregisterKeyListener(comboTwo);
			keyManager.unregisterKeyListener(comboThree);
			return;
		}
		keyManager.registerKeyListener(melee);
		keyManager.registerKeyListener(range);
		keyManager.registerKeyListener(mage);
		keyManager.registerKeyListener(augury);
		keyManager.registerKeyListener(rigour);
		keyManager.registerKeyListener(piety);
		keyManager.registerKeyListener(smite);
		keyManager.registerKeyListener(comboOne);
		keyManager.registerKeyListener(comboTwo);
		keyManager.registerKeyListener(comboThree);
	}

	private void clickPrayer(List<Prayer> prayers)
	{
		final List<Rectangle> toClick = new ArrayList<>();

		for (Prayer p : prayers)
		{
			final Widget widget = client.getWidget(p.getWidgetInfo());

			if (widget == null)
			{
				continue;
			}

			toClick.add(widget.getBounds());
		}

		if (toClick.isEmpty())
		{
			return;
		}

		executorService.submit(() ->
		{
			for (Rectangle rectangle : toClick)
			{
				System.out.println("Clicking Rectangle.");
				handleSwitch(rectangle);
			}
		});
	}

	private void handleSwitch(Rectangle rectangle)
	{
		if (client.getVar(VarClientInt.INTERFACE_TAB) != InterfaceTab.PRAYER.getId())
		{
			flexo.keyPress(tabUtils.getTabHotkey(Tab.PRAYER));
			flexo.delay(25);
		}
		final Point cp = getClickPoint(rectangle);
		if (cp.getX() >= 1 && cp.getY() >= 1)
		{
			switch (config.actionType())
			{
				case FLEXO:
					flexo.mouseMove(cp.getX(), cp.getY());
					flexo.mousePressAndRelease(1);
					flexo.delay(10);
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

	private void moveMouse(int x, int y)
	{
		MouseEvent mouseEntered = new MouseEvent(this.client.getCanvas(), 504, System.currentTimeMillis(), 0, x, y, 0, false);
		this.client.getCanvas().dispatchEvent(mouseEntered);
		MouseEvent mouseExited = new MouseEvent(this.client.getCanvas(), 505, System.currentTimeMillis(), 0, x, y, 0, false);
		this.client.getCanvas().dispatchEvent(mouseExited);
		MouseEvent mouseMoved = new MouseEvent(this.client.getCanvas(), 503, System.currentTimeMillis(), 0, x, y, 0, false);
		this.client.getCanvas().dispatchEvent(mouseMoved);
	}

	private void leftClick(int x, int y)
	{
		if (client.isStretchedEnabled())
		{
			double scalingfactor = configManager.getConfig(StretchedModeConfig.class).scalingFactor();
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
		else
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

	private Point getClickPoint(Rectangle rect)
	{
		double scalingFactor = configManager.getConfig(StretchedModeConfig.class).scalingFactor();

		int rand = (Math.random() <= 0.5) ? 1 : 2;
		int x = (int) (rect.getX() + (rand * 3) + rect.getWidth() / 2);
		int y = (int) (rect.getY() + (rand * 3) + rect.getHeight() / 2);

		double scale = 1 + (scalingFactor / 100);

		if (client.isStretchedEnabled())
		{
			return new Point((int) (x * scale), (int) (y * scale));
		}

		return new Point(x, y);
	}

	private final HotkeyListener melee = new HotkeyListener(() -> config.hotkeyMelee())
	{
		@Override
		public void hotkeyPressed()
		{
			clickPrayer(Collections.singletonList(PROTECT_FROM_MELEE));
		}
	};

	private final HotkeyListener range = new HotkeyListener(() -> config.hotkeyRange())
	{
		@Override
		public void hotkeyPressed()
		{
			clickPrayer(Collections.singletonList(PROTECT_FROM_MISSILES));
		}
	};
	private final HotkeyListener mage = new HotkeyListener(() -> config.hotkeyMage())
	{
		@Override
		public void hotkeyPressed()
		{
			clickPrayer(Collections.singletonList(PROTECT_FROM_MAGIC));
		}
	};
	private final HotkeyListener augury = new HotkeyListener(() -> config.hotkeyAugury())
	{
		@Override
		public void hotkeyPressed()
		{
			if (config.lowLevel())
			{
				clickPrayer(Collections.singletonList(MYSTIC_MIGHT));
				return;
			}
			clickPrayer(Collections.singletonList(AUGURY));
		}
	};
	private final HotkeyListener rigour = new HotkeyListener(() -> config.hotkeyRigour())
	{
		@Override
		public void hotkeyPressed()
		{
			if (config.lowLevel())
			{
				clickPrayer(Collections.singletonList(EAGLE_EYE));
				return;
			}
			clickPrayer(Collections.singletonList(RIGOUR));
		}
	};
	private final HotkeyListener piety = new HotkeyListener(() -> config.hotkeyPiety())
	{
		@Override
		public void hotkeyPressed()
		{
			if (config.lowLevel())
			{
				clickPrayer(Arrays.asList(STEEL_SKIN, ULTIMATE_STRENGTH, INCREDIBLE_REFLEXES));
				return;
			}
			clickPrayer(Collections.singletonList(PIETY));
		}
	};
	private final HotkeyListener smite = new HotkeyListener(() -> config.hotkeySmite())
	{
		@Override
		public void hotkeyPressed()
		{
			clickPrayer(Collections.singletonList(SMITE));
		}
	};
	private final HotkeyListener comboOne = new HotkeyListener(() -> config.comboOne())
	{
		@Override
		public void hotkeyPressed()
		{
			clickPrayer(Arrays.asList
				(config.comboOnePrayerOne().getPrayer(), config.comboOnePrayerTwo().getPrayer())
			);
		}
	};
	private final HotkeyListener comboTwo = new HotkeyListener(() -> config.comboTwo())
	{
		@Override
		public void hotkeyPressed()
		{
			clickPrayer(Arrays.asList(config.comboTwoPrayerOne().getPrayer(), config.comboTwoPrayerTwo().getPrayer()));
		}
	};
	private final HotkeyListener comboThree = new HotkeyListener(() -> config.comboThree())
	{
		@Override
		public void hotkeyPressed()
		{
			clickPrayer(Arrays.asList(config.comboThreePrayerOne().getPrayer(), config.comboThreePrayerTwo().getPrayer()));
		}
	};

}
