package net.runelite.client.plugins.externals.oneclick.Comparables;

import com.google.common.collect.ImmutableSet;
import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.client.plugins.externals.oneclick.OneClickPlugin;

import java.util.Set;

public class Farming implements ClickComparable
{
	private static final Set<Integer> HERB_SEEDS = ImmutableSet.of(
			ItemID.RANARR_SEED
	);
	
	private static final Set<Integer> COMPOST = ImmutableSet.of(
			ItemID.BOTTOMLESS_COMPOST_BUCKET_22997, ItemID.SUPERCOMPOST, ItemID.ULTRACOMPOST
	);
	
	private static final Set<String> FARM_PATCHES = ImmutableSet.of(
			"<col=ffff>Herb patch"
	);
	
	private static final Set<String> FARM_PATCHES_COMPOST = ImmutableSet.of(
			"<col=ffff>Herbs"
	);
	
	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == MenuOpcode.GAME_OBJECT_SECOND_OPTION.getId() &&
				FARM_PATCHES.contains(event.getTarget()) ||
				event.getOpcode() == MenuOpcode.GAME_OBJECT_SECOND_OPTION.getId() &&
						FARM_PATCHES_COMPOST.contains(event.getTarget());
	}
	
	@Override
	public void modifyEntry(OneClickPlugin plugin, MenuEntry event)
	{
		if (FARM_PATCHES.contains(event.getTarget()))
		{
			if (plugin.findItem(HERB_SEEDS).getLeft() == -1)
			{
				return;
			}
			event.setOption("Use");
			event.setTarget("<col=ff9040>Herb seed<col=ffffff> -> " + plugin.getTargetMap().get(event.getIdentifier()));
			event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
			event.setForceLeftClick(true);
			
		} else if (FARM_PATCHES_COMPOST.contains(event.getTarget()))
		{
			if (plugin.findItem(COMPOST).getLeft() == -1)
			{
				return;
			}
			event.setOption("Use");
			event.setTarget("<col=ff9040>Compost<col=ffffff> -> " + plugin.getTargetMap().get(event.getIdentifier()));
			event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
			event.setForceLeftClick(true);
		}
	}
	
	@Override
	public boolean isClickValid(MenuEntry event)
	{
		return (event.getOpcode() == MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId() &&
				event.getTarget().contains("<col=ff9040>Herb seed<col=ffffff> -> ") ||
				event.getTarget().contains("<col=ff9040>Compost<col=ffffff> -> "));
	}
	
	@Override
	public void modifyClick(OneClickPlugin plugin, MenuEntry event)
	{
		if (event.getTarget().contains("<col=ff9040>Herb seed<col=ffffff> -> "))
		{
			if (plugin.updateSelectedItem(HERB_SEEDS))
				event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
			
		} else if (event.getTarget().contains("<col=ff9040>Compost<col=ffffff> -> ") &&
				plugin.updateSelectedItem(COMPOST))
		{
			event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
		}
	}
}
