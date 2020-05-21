package net.runelite.client.plugins.externals.oneclick.Comparables;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.client.plugins.externals.oneclick.OneClickPlugin;

@Slf4j
public class Healer implements ClickComparable
{
	private static final ImmutableMap<String, Integer> ITEMS = ImmutableMap.<String, Integer>builder()
		.put("Pois. Worms", ItemID.POISONED_WORMS)
		.put("Pois. Tofu", ItemID.POISONED_TOFU)
		.put("Pois. Meat", ItemID.POISONED_MEAT)
		.build();

	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == 1003 && event.getTarget().contains("Penance Healer");
	}

	@Override
	public void modifyEntry(OneClickPlugin plugin, MenuEntry event)
	{
		if (plugin.getRoleText() == null ||
			plugin.getRoleText().isBlank() ||
			plugin.getRoleText().isEmpty() ||
			plugin.getRoleText().equals("- - -"))
		{
			return;
		}

		int id = ITEMS.getOrDefault(plugin.getRoleText(), -1);

		if (id == -1)
		{
			log.error("This shouldn't be possible, bad string: {}", plugin.getRoleText());
			return;
		}

		event.setOption("Use");
		event.setTarget("<col=ff9040>Food<col=ffffff> -> <col=ffff00>Penance Healer");
		event.setForceLeftClick(true);
	}

	@Override
	public boolean isClickValid(MenuEntry event)
	{
		return event.getTarget().equalsIgnoreCase("<col=ff9040>Food<col=ffffff> -> <col=ffff00>Penance Healer");
	}

	@Override
	public void modifyClick(OneClickPlugin plugin, MenuEntry event)
	{
		if (event.getTarget().equalsIgnoreCase("<col=ff9040>Food<col=ffffff> -> <col=ffff00>Penance Healer"))
		{
			if (plugin.updateSelectedItem(ITEMS.getOrDefault(plugin.getRoleText(), -1)))
			{
				event.setOpcode(MenuOpcode.ITEM_USE_ON_NPC.getId());
			}
		}
	}
}
