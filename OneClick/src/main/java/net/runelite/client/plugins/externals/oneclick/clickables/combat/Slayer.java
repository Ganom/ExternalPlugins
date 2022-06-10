package net.runelite.client.plugins.externals.oneclick.clickables.combat;

import com.google.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.MenuAction;
import net.runelite.api.NPC;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.client.game.NPCManager;
import net.runelite.client.plugins.externals.oneclick.clickables.Clickable;
import net.runelite.client.plugins.externals.oneclick.pojos.TaskWeakness;

@Slf4j
public class Slayer extends Clickable
{
	@Inject
	private NPCManager npcManager;

	@Override
	public boolean isValidEntry(MenuEntryAdded event)
	{
		if (event.getType() != MenuAction.EXAMINE_NPC.getId() || event.isForceLeftClick())
		{
			return false;
		}

		var weakness = TaskWeakness.getWeakness(event.getTarget());

		if (weakness == null || findItemWithIds(weakness.getItemIds()) == null)
		{
			return false;
		}

		var npc = findWeakNpc(event.getIdentifier(), weakness);

		if (npc == null)
		{
			return false;
		}

		client.createMenuEntry(client.getMenuOptionCount())
			.setOption("Hit")
			.setTarget("<col=ff9040>Weakness</col><col=ffffff> -> " + event.getTarget())
			.setIdentifier(npc.getIndex())
			.setType(MenuAction.WIDGET_TARGET_ON_NPC)
			.setParam0(0)
			.setParam1(0)
			.setForceLeftClick(true);
		return true;
	}

	@Override
	public boolean isValidClick(MenuOptionClicked event)
	{
		if (!event.getMenuOption().equals("Hit") || event.getMenuAction() != MenuAction.WIDGET_TARGET_ON_NPC)
		{
			return false;
		}
		var weakness = TaskWeakness.getWeakness(event.getMenuTarget());
		if (weakness == null)
		{
			log.error("How did we get a null weakness after validating a hit menu option that is custom made?!?!");
			return false;
		}
		return updateSelectedItem(weakness.getItemIds());
	}

	/**
	 * Shamelessly yoinked from Slayer plugin.
	 */
	private int calculateHealth(NPC target)
	{
		if (target == null || target.getName() == null)
		{
			return -1;
		}

		final int healthScale = target.getHealthScale();
		final int healthRatio = target.getHealthRatio();
		final Integer maxHealth = npcManager.getHealth(target.getId());

		if (healthRatio < 0 || healthScale <= 0 || maxHealth == null)
		{
			return -1;
		}

		return (int) ((maxHealth * healthRatio / healthScale) + 0.5f);
	}

	private NPC findWeakNpc(int index, TaskWeakness weakness)
	{
		var npc = client.getCachedNPCs()[index];
		var health = calculateHealth(npc);
		if (health < 0 || health > weakness.getThreshold())
		{
			return null;
		}
		return npc;
	}
}
