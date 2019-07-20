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
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import net.runelite.api.Client;
import net.runelite.api.Point;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.VarClientInt;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.Keybind;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.flexo.Flexo;
import net.runelite.client.input.KeyListener;
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
	private TabUtils tabUtils;
	@Inject
	private EventBus eventBus;
	private BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);
	private ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1, 1, TimeUnit.SECONDS, queue,
		new ThreadPoolExecutor.DiscardPolicy());
	private Flexo flexo;
	private Keybind mageKey;
	private Keybind rangeKey;
	private Keybind meleeKey;
	private Keybind auguryKey;
	private Keybind rigourKey;
	private Keybind pietyKey;
	private Keybind smiteKey;
	private Keybind comboOneKey;
	private Keybind comboTwoKey;
	private Keybind comboThreeKey;

	@Provides
	PraySwapConfig getConfig(ConfigManager manager)
	{
		return (PraySwapConfig) manager.getConfig(PraySwapConfig.class);
	}

	protected void startUp()
	{
		eventBus.subscribe(GameTick.class, this, this::onGameTick);

		this.updateConfig();
		keyManager.registerKeyListener(mageKeyListener);
		keyManager.registerKeyListener(rangeKeyListener);
		keyManager.registerKeyListener(meleeKeyListener);
		keyManager.registerKeyListener(auguryKeyListener);
		keyManager.registerKeyListener(rigourKeyListener);
		keyManager.registerKeyListener(pietyKeyListener);
		keyManager.registerKeyListener(smiteKeyListener);
		keyManager.registerKeyListener(comboOneKeyListener);
		keyManager.registerKeyListener(comboTwoKeyListener);
		keyManager.registerKeyListener(comboThreeKeyListener);

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
		keyManager.unregisterKeyListener(mageKeyListener);
		keyManager.unregisterKeyListener(rangeKeyListener);
		keyManager.unregisterKeyListener(meleeKeyListener);
		keyManager.unregisterKeyListener(auguryKeyListener);
		keyManager.unregisterKeyListener(rigourKeyListener);
		keyManager.unregisterKeyListener(pietyKeyListener);
		keyManager.unregisterKeyListener(smiteKeyListener);
		keyManager.unregisterKeyListener(comboOneKeyListener);
		keyManager.unregisterKeyListener(comboTwoKeyListener);
		keyManager.unregisterKeyListener(comboThreeKeyListener);
		eventBus.unregister(this);
	}

	private void onGameTick(GameTick event)
	{
		if (shouldProtPray())
		{
			clickPrayer(Prayer.PROTECT_ITEM, true);
		}
	}

	private final HotkeyListener mageKeyListener = new HotkeyListener(() -> this.mageKey)
	{
		@Override
		public void hotkeyPressed()
		{
			executorService.submit(() -> clickPrayer(Prayer.PROTECT_FROM_MAGIC, true));
		}
	};

	private final HotkeyListener rangeKeyListener = new HotkeyListener(() -> this.rangeKey)
	{
		@Override
		public void hotkeyPressed()
		{
			executorService.submit(() -> clickPrayer(Prayer.PROTECT_FROM_MISSILES, true));
		}
	};

	private final HotkeyListener meleeKeyListener = new HotkeyListener(() -> this.meleeKey)
	{
		@Override
		public void hotkeyPressed()
		{
			executorService.submit(() -> clickPrayer(Prayer.PROTECT_FROM_MELEE, true));
		}
	};

	private final HotkeyListener auguryKeyListener = new HotkeyListener(() -> this.auguryKey)
	{
		@Override
		public void hotkeyPressed()
		{
			if (config.lowLevel())
			{
				executorService.submit(() -> clickPrayer(Prayer.MYSTIC_MIGHT, true));
			}
			else
			{
				executorService.submit(() -> clickPrayer(Prayer.AUGURY, true));
			}
		}
	};

	private final HotkeyListener rigourKeyListener = new HotkeyListener(() -> this.rigourKey)
	{
		@Override
		public void hotkeyPressed()
		{
			if (config.lowLevel())
			{
				executorService.submit(() -> clickPrayer(Prayer.EAGLE_EYE, true));
			}
			else
			{
				executorService.submit(() -> clickPrayer(Prayer.RIGOUR, true));
			}
		}
	};

	private final HotkeyListener pietyKeyListener = new HotkeyListener(() -> this.pietyKey)
	{
		@Override
		public void hotkeyPressed()
		{
			if (config.lowLevel())
			{
				executorService.submit(() -> {
					clickPrayer(Prayer.STEEL_SKIN, false);
					clickPrayer(Prayer.ULTIMATE_STRENGTH, false);
					clickPrayer(Prayer.INCREDIBLE_REFLEXES, true);
				});
			}
			else
			{
				executorService.submit(() -> clickPrayer(Prayer.PIETY, true));
			}
		}
	};

	private final HotkeyListener smiteKeyListener = new HotkeyListener(() -> this.smiteKey)
	{
		@Override
		public void hotkeyPressed()
		{
			executorService.submit(() -> clickPrayer(Prayer.SMITE, true));
		}
	};

	private final HotkeyListener comboOneKeyListener = new HotkeyListener(() -> this.comboOneKey)
	{
		@Override
		public void hotkeyPressed()
		{
			executorService.submit(() -> {
				clickPrayer(config.comboOnePrayerOne().getPrayer(), false);
				clickPrayer(config.comboOnePrayerTwo().getPrayer(), true);
			});
		}
	};

	private final HotkeyListener comboTwoKeyListener = new HotkeyListener(() -> this.comboTwoKey)
	{
		@Override
		public void hotkeyPressed()
		{
			executorService.submit(() -> {
				clickPrayer(config.comboTwoPrayerOne().getPrayer(), false);
				clickPrayer(config.comboTwoPrayerTwo().getPrayer(), true);
			});
		}
	};

	private final HotkeyListener comboThreeKeyListener = new HotkeyListener(() -> this.comboThreeKey)
	{
		@Override
		public void hotkeyPressed()
		{
			executorService.submit(() -> {
				clickPrayer(config.comboThreePrayerOne().getPrayer(), false);
				clickPrayer(config.comboThreePrayerTwo().getPrayer(), true);
			});
		}
	};

	private void updateConfig()
	{
		this.mageKey = config.hotkeyMage();
		this.rangeKey = config.hotkeyRange();
		this.meleeKey = config.hotkeyMelee();
		this.auguryKey = config.hotkeyAugury();
		this.rigourKey = config.hotkeyRigour();
		this.pietyKey = config.hotkeyPiety();
		this.smiteKey = config.hotkeySmite();
		this.comboOneKey = config.comboOne();
		this.comboTwoKey = config.comboTwo();
		this.comboThreeKey = config.comboThree();
	}

	private boolean shouldProtPray()
	{
		return config.protectItem() && !client.isPrayerActive(Prayer.PROTECT_ITEM) && client.getBoostedSkillLevel(Skill.PRAYER) >= 1;
	}

	private void clickPrayer(Prayer prayer, boolean swapBack)
	{
		if (prayer != null)
		{
			Widget widget = client.getWidget(prayer.getWidgetInfo());

			if (widget != null)
			{
				if (client.getVar(VarClientInt.PLAYER_INVENTORY_OPENED) != 5)
				{
					flexo.keyPress(tabUtils.getTabHotkey(Tab.PRAYER));
				}
				handleSwitch(widget.getBounds(), swapBack);
			}
		}
	}

	private void handleSwitch(Rectangle rectangle, boolean swapBack)
	{
		Point cp = getClickPoint(rectangle);
		if (cp.getX() >= 1 && cp.getY() >= 1)
		{
			switch (config.actionType())
			{
				case FLEXO:
					flexo.mouseMove(cp.getX(), cp.getY());
					this.delay();
					this.leftClickAndRelease();
					flexo.delay(10);
					if (swapBack && config.backToInventory())
					{
						flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
					}
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
					if (swapBack && config.backToInventory())
					{
						flexo.keyPress(tabUtils.getTabHotkey(Tab.INVENTORY));
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
		Point p = this.client.getMouseCanvasPosition();
		if (p.getX() != x || p.getY() != y)
		{
			this.moveMouse(x, y);
		}
		this.leftClickAndRelease();
	}

	private void leftClickAndRelease()
	{
		if (client.isStretchedEnabled())
		{
			double scalingfactor = configManager.getConfig(StretchedModeConfig.class).scalingFactor();
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
			MouseEvent mousePressed = new MouseEvent(this.client.getCanvas(), 501, System.currentTimeMillis(), 0, this.client.getMouseCanvasPosition().getX(), this.client.getMouseCanvasPosition().getY(), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mousePressed);
			MouseEvent mouseReleased = new MouseEvent(this.client.getCanvas(), 502, System.currentTimeMillis(), 0, this.client.getMouseCanvasPosition().getX(), this.client.getMouseCanvasPosition().getY(), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mouseReleased);
			MouseEvent mouseClicked = new MouseEvent(this.client.getCanvas(), 500, System.currentTimeMillis(), 0, this.client.getMouseCanvasPosition().getX(), this.client.getMouseCanvasPosition().getY(), 1, false, 1);
			this.client.getCanvas().dispatchEvent(mouseClicked);
		}
	}

	private void delay()
	{
		try
		{
			Thread.sleep(getMillis());
		}
		catch (InterruptedException e)
		{
			e.printStackTrace();
		}
	}

	private Point getClickPoint(Rectangle rect)
	{
		double scalingfactor = configManager.getConfig(StretchedModeConfig.class).scalingFactor();

		int rand = (Math.random() <= 0.5) ? 1 : 2;
		int x = (int) (rect.getX() + (rand * 3) + rect.getWidth() / 2);
		int y = (int) (rect.getY() + (rand * 3) + rect.getHeight() / 2);

		double scale = 1 + (scalingfactor / 100);

		if (client.isStretchedEnabled())
		{
			return new Point((int) (x * scale), (int) (y * scale));
		}

		return new Point(x, y);
	}
}
