package net.runelite.client.plugins.externals.oneclick.Comparables;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.runelite.api.ItemID;
import net.runelite.api.MenuEntry;
import net.runelite.api.MenuOpcode;
import net.runelite.client.plugins.externals.oneclick.OneClickPlugin;

public class Bones implements ClickComparable
{
	private static final Set<Integer> BONE_SET = ImmutableSet.of(
		ItemID.BONES, ItemID.WOLF_BONE, ItemID.BURNT_BONES, ItemID.MONKEY_BONES, ItemID.BAT_BONES,
		ItemID.JOGRE_BONE, ItemID.BIG_BONES, ItemID.ZOGRE_BONE, ItemID.SHAIKAHAN_BONES, ItemID.BABYDRAGON_BONES,
		ItemID.WYRM_BONES, ItemID.DRAGON_BONES, ItemID.DRAKE_BONES, ItemID.FAYRG_BONES, ItemID.LAVA_DRAGON_BONES,
		ItemID.RAURG_BONES, ItemID.HYDRA_BONES, ItemID.DAGANNOTH_BONES, ItemID.OURG_BONES, ItemID.SUPERIOR_DRAGON_BONES,
		ItemID.WYVERN_BONES
	);

	@Override
	public boolean isEntryValid(MenuEntry event)
	{
		return event.getOpcode() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
			event.getOption().toLowerCase().contains("pray") &&
			event.getTarget().toLowerCase().contains("altar");
	}

	@Override
	public void modifyEntry(OneClickPlugin plugin, MenuEntry event)
	{
		if (plugin.findItem(BONE_SET).getLeft() == -1)
		{
			return;
		}
		event.setOption("Use");
		event.setTarget("<col=ff9040>Bones<col=ffffff> -> " + event.getTarget());
		event.setForceLeftClick(true);
	}

	@Override
	public boolean isClickValid(MenuEntry event)
	{
		return event.getOpcode() == MenuOpcode.GAME_OBJECT_FIRST_OPTION.getId() &&
			event.getTarget().contains("<col=ff9040>Bones<col=ffffff> -> ") &&
			event.getTarget().toLowerCase().contains("altar");
	}

	@Override
	public void modifyClick(OneClickPlugin plugin, MenuEntry event)
	{
		if (plugin.updateSelectedItem(BONE_SET))
		{
			event.setOpcode(MenuOpcode.ITEM_USE_ON_GAME_OBJECT.getId());
		}
	}
}
