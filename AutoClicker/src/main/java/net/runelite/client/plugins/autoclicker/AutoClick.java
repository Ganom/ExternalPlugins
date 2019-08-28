/*
 * Copyright (c) 2019, Ganom <https://github.com/Ganom>
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
 *
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
package net.runelite.client.plugins.autoclicker;

import com.google.inject.Provides;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Skill;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;

@PluginDescriptor(
	name = "Auto Clicker",
	enabledByDefault = false,
	type = PluginType.EXTERNAL
)
@Slf4j
public class AutoClick extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private AutoClickConfig config;
	@Inject
	private AutoClickOverlay overlay;
	@Inject
	private OverlayManager overlayManager;
	@Inject
	private KeyManager keyManager;
	private final BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(1);
	private final ThreadPoolExecutor executorService = new ThreadPoolExecutor(1, 1,
		10, TimeUnit.SECONDS, queue, new ThreadPoolExecutor.DiscardPolicy());
	private boolean run;
	@Getter(AccessLevel.PACKAGE)
	@Setter(AccessLevel.PACKAGE)
	private boolean flash;

	@Provides
	AutoClickConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AutoClickConfig.class);
	}

	@Override
	protected void startUp()
	{
		overlayManager.add(overlay);
		keyManager.registerKeyListener(hotkeyListener);
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		keyManager.unregisterKeyListener(hotkeyListener);
	}

	private HotkeyListener hotkeyListener = new HotkeyListener(() -> config.hotkey())
	{
		@Override
		public void hotkeyPressed()
		{
			run = !run;
			executorService.submit(() ->
			{
				while (run)
				{
					if (client.getGameState() != GameState.LOGGED_IN)
					{
						run = false;
						break;
					}

					if (checkHitpoints() || checkInventory())
					{
						run = false;
						if (config.flash())
						{
							setFlash(true);
						}
						break;
					}

					simLeftClick();

					try
					{
						Thread.sleep(randomDelay(config.delayMin(), config.delayMax()));
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			});
		}
	};

	private boolean checkHitpoints()
	{
		if (!config.autoDisableHp())
		{
			return false;
		}
		return client.getBoostedSkillLevel(Skill.HITPOINTS) <= config.hpThreshold();
	}

	private boolean checkInventory()
	{
		if (!config.autoDisableInv())
		{
			return false;
		}
		final Widget inventoryWidget = client.getWidget(WidgetInfo.INVENTORY);
		return inventoryWidget.getWidgetItems().size() == 28;
	}

	private void simLeftClick()
	{
		try
		{
			Robot leftClk = new Robot();
			leftClk.mousePress(InputEvent.BUTTON1_MASK);
			leftClk.delay(randomDelay(21, 25));
			leftClk.mouseRelease(InputEvent.BUTTON1_MASK);
		}
		catch (AWTException e)
		{
			e.printStackTrace();
		}
	}

	private int randomDelay(int min, int max)
	{
		Random rand = new Random();
		int n = rand.nextInt(max) + 1;
		if (n < min)
		{
			n += min;
		}
		return n;
	}
}
