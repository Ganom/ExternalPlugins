/*
 * Copyright (c) 2019-2020, ganom <https://github.com/Ganom>
 * All rights reserved.
 * Licensed under GPL3, see LICENSE for the full scope.
 */
package net.runelite.client.plugins.externals.anon;

import com.google.inject.Inject;
import com.google.inject.Provides;
import java.text.NumberFormat;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.events.BeforeRender;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.OverheadTextChanged;
import net.runelite.api.widgets.Widget;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.Subscribe;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.rs.api.RSClient;
import net.runelite.rs.api.RSWidget;
import org.pf4j.Extension;

@Slf4j
@Extension
@PluginDescriptor(
	name = "Anonymizer",
	description = "Hides your rsn and others.",
	type = PluginType.MISCELLANEOUS
)
public class Anonymizer extends Plugin
{
	private static final String ANON = "------------";
	@Inject
	private Client client;

	@Inject
	private AnonConfig config;

	private final NumberFormat format = NumberFormat.getInstance();
	private String name = "";

	@Provides
	AnonConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(AnonConfig.class);
	}

	@Override
	public void startUp()
	{
		format.setGroupingUsed(true);
	}

	@Override
	public void shutDown()
	{
	}

	@Subscribe
	public void onOverheadTextChanged(OverheadTextChanged event)
	{
		String oh = event.getOverheadText();

		if (!oh.contains(name))
		{
			return;
		}

		Actor actor = event.getActor();

		if (actor != null)
		{
			String text = oh.replace(name, ANON);
			actor.setOverheadText(text);
		}
	}

	@Subscribe
	public void onGameTick(GameTick event)
	{
		Player p = client.getLocalPlayer();

		if (p == null)
		{
			return;
		}

		name = p.getName();

		if (config.hideXp())
		{
			Widget xp = client.getWidget(122, 9);

			if (xp != null && xp.getText() != null && !xp.isHidden())
			{
				xp.setText("123,456,789");
				xp.revalidate();
			}
		}
	}

	@Subscribe
	public void onBeforeRender(BeforeRender event)
	{
		if (!config.hideRsn() || client == null || client.getGameState() != GameState.LOGGED_IN || name.isBlank() || name.isEmpty())
		{
			return;
		}

		RSClient rsClient = (RSClient) client;

		if (rsClient == null || rsClient.getWidgets() == null)
		{
			return;
		}

		for (RSWidget[] parent : rsClient.getWidgets())
		{
			if (parent == null)
			{
				continue;
			}

			for (RSWidget widget : parent)
			{
				parseWidget(widget);

				if (widget.getDynamicChildren() != null && widget.getDynamicChildren().length > 0)
				{
					Widget[] dynamicChildren = widget.getDynamicChildren();
					for (int i = 0; i < dynamicChildren.length; i++)
					{
						Widget dynamicChild = dynamicChildren[i];

						if (dynamicChild.getId() == 10616890 && dynamicChild.getText().contains(name))
						{
							parseWidget(dynamicChild);
							try
							{
								Widget text = dynamicChildren[i + 1];
								text.setOriginalX(80);
								text.revalidate();
							}
							catch (Exception ignored)
							{
							}
						}
					}
				}

				if (widget.getStaticChildren() != null && widget.getStaticChildren().length > 0)
				{
					for (Widget staticChild : widget.getStaticChildren())
					{
						parseWidget(staticChild);
					}
				}

				if (widget.getNestedChildren() != null && widget.getNestedChildren().length > 0)
				{
					for (Widget nestedChild : widget.getNestedChildren())
					{
						parseWidget(nestedChild);
					}
				}
			}
		}
	}

	private void parseWidget(Widget widget)
	{
		if (widget == null || widget.isHidden() || widget.isSelfHidden())
		{
			return;
		}

		if (widget.getText() != null && widget.getText().contains(name))
		{
			String text = widget.getText().replace(name, ANON);
			widget.setText(text);
			widget.revalidate();
		}
	}
}
