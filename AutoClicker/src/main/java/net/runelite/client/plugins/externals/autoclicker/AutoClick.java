/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.autoclicker;

import com.google.inject.Provides;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Point;
import net.runelite.api.Skill;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.input.KeyManager;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.externals.utils.ExtUtils;
import net.runelite.client.ui.overlay.OverlayManager;
import net.runelite.client.util.HotkeyListener;
import org.pf4j.Extension;

@Extension
@PluginDescriptor(
	name = "Auto Clicker",
	enabledByDefault = false,
	type = PluginType.UTILITY
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

	@Inject
	private ExtUtils extUtils;

	private ExecutorService executorService;
	private Point point;
	private Random random;
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
		executorService = Executors.newSingleThreadExecutor();
		random = new Random();
	}

	@Override
	protected void shutDown()
	{
		overlayManager.remove(overlay);
		keyManager.unregisterKeyListener(hotkeyListener);
		executorService.shutdown();
		random = null;
	}

	private HotkeyListener hotkeyListener = new HotkeyListener(() -> config.toggle())
	{
		@Override
		public void hotkeyPressed()
		{
			run = !run;

			if (!run)
			{
				return;
			}

			point = client.getMouseCanvasPosition();

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

					extUtils.click(point);

					try
					{
						Thread.sleep(randomDelay());
					}
					catch (InterruptedException e)
					{
						e.printStackTrace();
					}
				}
			});
		}
	};

	private long randomDelay()
	{
		if (config.weightedDistribution())
		{
			/* generate a gaussian random (average at 0.0, std dev of 1.0)
			 * take the absolute value of it (if we don't, every negative value will be clamped at the minimum value)
			 * get the log base e of it to make it shifted towards the right side
			 * invert it to shift the distribution to the other end
			 * clamp it to min max, any values outside of range are set to min or max */
			return (long) clamp(
				(-Math.log(Math.abs(random.nextGaussian()))) * config.deviation() + config.target()
			);
		}
		else
		{
			/* generate a normal even distribution random */
			return (long) clamp(
				Math.round(random.nextGaussian() * config.deviation() + config.target())
			);
		}
	}

	private double clamp(double val)
	{
		return Math.max(config.min(), Math.min(config.max(), val));
	}

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
}
