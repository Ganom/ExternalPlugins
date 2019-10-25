/*
 * Copyright (c) 2019, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.widgetscaler;

import com.google.inject.Provides;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.events.GameTick;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;

@PluginDescriptor(
	name = "Widget Scaler",
	description = "Widget Scaling",
	type = PluginType.EXTERNAL
)

@Slf4j
public class WidgetScaler extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private WidgetScalerConfig config;
	@Inject
	private EventBus eventBus;

	@Provides
	WidgetScalerConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(WidgetScalerConfig.class);
	}

	@Override
	protected void startUp()
	{
		eventBus.subscribe(GameTick.class, this, this::onGameTick);
	}

	@Override
	protected void shutDown()
	{
		eventBus.unregister(this);
	}

	private void onGameTick(GameTick event)
	{
		Widget ring = client.getWidget(WidgetInfo.EQUIPMENT_RING);

		if (ring != null)
		{
			widgetHandler(ring, config.widgetSizeRing(), config.xOffsetRing(), config.yOffsetRing());
		}

		Widget cape = client.getWidget(WidgetInfo.EQUIPMENT_CAPE);

		if (cape != null)
		{
			widgetHandler(cape, config.widgetSizeCape(), config.xOffsetCape(), config.yOffsetCape());
		}
	}

	private void widgetHandler(Widget widget, int size, int offsetX, int offsetY)
	{
		Widget[] ringChildren = widget.getChildren();

		for (Widget widgets : ringChildren)
		{
			widgets.setOriginalWidth(size);
			widgets.setOriginalHeight(size);
			widgets.revalidate();
		}

		widget.setOriginalWidth(size);
		widget.setOriginalHeight(size);
		widget.setOriginalX(offsetX);
		widget.setOriginalY(offsetY);
		widget.revalidate();
	}
}
