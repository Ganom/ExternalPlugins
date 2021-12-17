package net.runelite.client.plugins.externals.oneclick.comparables.skilling;

import com.google.common.collect.ImmutableSet;
import java.util.Set;
import net.runelite.api.ItemID;
import net.runelite.api.MenuAction;
import net.runelite.api.MenuEntry;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.plugins.externals.oneclick.comparables.ClickCompare;

public class Bones extends ClickCompare
{
	private static final Set<Integer> BONE_SET = ImmutableSet.of(
		ItemID.BONES, ItemID.WOLF_BONE, ItemID.BURNT_BONES, ItemID.MONKEY_BONES, ItemID.BAT_BONES,
		ItemID.JOGRE_BONE, ItemID.BIG_BONES, ItemID.ZOGRE_BONE, ItemID.SHAIKAHAN_BONES, ItemID.BABYDRAGON_BONES,
		ItemID.WYRM_BONES, ItemID.DRAGON_BONES, ItemID.DRAKE_BONES, ItemID.FAYRG_BONES, ItemID.LAVA_DRAGON_BONES,
		ItemID.RAURG_BONES, ItemID.HYDRA_BONES, ItemID.DAGANNOTH_BONES, ItemID.OURG_BONES, ItemID.SUPERIOR_DRAGON_BONES,
		ItemID.WYVERN_BONES
	);

	@Override
	public boolean isEntryValid(MenuEntryAdded event)
	{
		return event.getOpcode() == MenuAction.GAME_OBJECT_FIRST_OPTION.getId() && !event.isForceLeftClick() &&
			event.getOption().toLowerCase().contains("pray") &&
			event.getTarget().toLowerCase().contains("altar");
	}

	@Override
	public void modifyEntry(MenuEntryAdded event)
	{
		if (client == null || findItem(BONE_SET).getLeft() == -1)
		{
			return;
		}

		client.createMenuEntry(-1)
			.setOption("Use")
			.setTarget("<col=ff9040>Bones<col=ffffff> -> " + getTargetMap().get(event.getIdentifier()))
			.setType(MenuAction.ITEM_USE_ON_GAME_OBJECT)
			.setIdentifier(event.getIdentifier())
			.setParam0(event.getActionParam0())
			.setParam1(event.getActionParam1())
			.setForceLeftClick(true);
	}

	@Override
	public boolean isClickValid(MenuOptionClicked event)
	{
		return event.getMenuTarget().contains("<col=ff9040>Bones<col=ffffff> -> ") &&
			event.getMenuTarget().toLowerCase().contains("altar") && updateSelectedItem(BONE_SET);
	}

	@Override
	public void modifyClick(MenuOptionClicked event)
	{

	}

	@Override
	public void backupEntryModify(MenuEntry e)
	{
		if (findItem(BONE_SET).getLeft() == -1)
		{
			return;
		}
		e.setOption("Use");
		e.setTarget("<col=ff9040>Bones<col=ffffff> -> " + getTargetMap().get(e.getIdentifier()));
		e.setForceLeftClick(true);
	}
}
