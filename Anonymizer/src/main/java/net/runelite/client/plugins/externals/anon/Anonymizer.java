package net.runelite.client.plugins.externals.anon;

import com.google.inject.Inject;
import com.google.inject.Provides;
import java.text.NumberFormat;
import java.util.Random;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Client;
import net.runelite.api.GameState;
import net.runelite.api.Player;
import net.runelite.api.events.BeforeRender;
import net.runelite.api.events.GameTick;
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
				int ran = (int) (Math.random() * 600000000);
				xp.setText(format.format(ran));
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

			for (RSWidget child : parent)
			{
				if (child == null || child.isHidden() || child.isSelfHidden() || child.getText() == null)
				{
					continue;
				}

				if (child.getText().contains(name))
				{
					String SALTCHARS = "abcdefghijklmnopqrstuvwxyz0123456789";
					StringBuilder salty = new StringBuilder();
					Random rando = new Random();
					while (salty.length() < 12)
					{
						int index = (int) (rando.nextFloat() * SALTCHARS.length());
						salty.append(SALTCHARS.charAt(index));
					}
					String saltStr = salty.toString();
					String t = child.getText().replace(name, saltStr);
					child.setText(t);
					child.revalidate();
				}
			}
		}
	}
}
