/*
 * Copyright (c) 2019, Ganom <https://github.com/ganom>
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
package net.runelite.client.plugins.leftclickpk;

import com.google.inject.Provides;
import java.awt.Color;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import javax.inject.Inject;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Actor;
import net.runelite.api.ChatMessageType;
import net.runelite.api.Client;
import net.runelite.api.InventoryID;
import net.runelite.api.Item;
import net.runelite.api.ItemContainer;
import net.runelite.api.NPC;
import net.runelite.api.Player;
import net.runelite.api.Prayer;
import net.runelite.api.Skill;
import net.runelite.api.Varbits;
import net.runelite.api.WorldType;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.ChatMessage;
import net.runelite.api.events.GameTick;
import net.runelite.api.events.ItemContainerChanged;
import net.runelite.api.events.LocalPlayerDeath;
import net.runelite.api.events.MenuEntryAdded;
import net.runelite.api.events.MenuOptionClicked;
import net.runelite.api.events.PlayerDespawned;
import net.runelite.api.events.PlayerSpawned;
import net.runelite.api.events.SpotAnimationChanged;
import net.runelite.api.util.Text;
import net.runelite.api.widgets.Widget;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.eventbus.EventBus;
import net.runelite.client.plugins.Plugin;
import net.runelite.client.plugins.PluginDescriptor;
import net.runelite.client.plugins.PluginType;
import net.runelite.client.plugins.multiindicators.MapLocations;
import net.runelite.client.util.ColorUtil;
import net.runelite.client.util.PvPUtil;

@PluginDescriptor(
	name = "Left Click PK",
	description = "Who needs to learn pking when you can just left click?",
	type = PluginType.EXTERNAL
)
@Slf4j
public class LeftClickPK extends Plugin
{
	@Inject
	private Client client;
	@Inject
	private EventBus eventBus;
	@Inject
	private LeftClickConfig config;

	private final Map<Integer, Victim> victimMap = new HashMap<>();
	private final Map<Integer, Victim> victimMapCache = new HashMap<>();

	private boolean maging;
	private float hue;
	private boolean reverse;

	@Provides
	LeftClickConfig getConfig(ConfigManager configManager)
	{
		return configManager.getConfig(LeftClickConfig.class);
	}

	@Override
	public void startUp()
	{
		eventBus.subscribe(MenuOptionClicked.class, this, this::onMenuOptionClicked);
		eventBus.subscribe(ItemContainerChanged.class, this, this::onItemContainerChanged);
		eventBus.subscribe(MenuEntryAdded.class, this, this::onMenuEntryAdded);
		eventBus.subscribe(LocalPlayerDeath.class, this, this::onLocalPlayerDeath);
		eventBus.subscribe(PlayerDespawned.class, this, this::onPlayerDespawned);
		eventBus.subscribe(PlayerSpawned.class, this, this::onPlayerSpawned);
		eventBus.subscribe(ChatMessage.class, this, this::onChatMessage);
		eventBus.subscribe(SpotAnimationChanged.class, this, this::onSpotAnimationChanged);
		eventBus.subscribe(GameTick.class, this, this::onGameTick);
	}

	@Override
	public void shutDown()
	{
		eventBus.unregister(this);
	}

	private void onGameTick(GameTick tickEvent)
	{
		victimMap.values().forEach(this::update);
	}

	private void onMenuEntryAdded(MenuEntryAdded event)
	{
		if (event.getOption().equalsIgnoreCase("attack") && victimMap.containsKey(event.getIdentifier()) && maging)
		{
			final String name = Text.standardize(event.getTarget(), true);
			final Victim victim = victimMap.getOrDefault(event.getIdentifier(), null);

			if (victim == null || !PvPUtil.isAttackable(client, (Player) victim.getActor())
				|| client.isFriended(name, false) || client.isClanMember(name))
			{
				return;
			}

			event.setWasModified(true);
			setSpell(victim);
			final String rainbow = rainbow(Text.removeTags(client.getSelectedSpellName()));
			event.getMenuEntry().setOption("Left Click " + rainbow + " -> ");
		}
	}

	private void onMenuOptionClicked(MenuOptionClicked event)
	{
		if (event.getOption().contains("Left Click"))
		{
			event.getMenuEntry().setOpcode(15);
			event.getMenuEntry().setParam0(0);
			event.getMenuEntry().setParam1(0);
		}
	}

	private void onItemContainerChanged(ItemContainerChanged event)
	{
		final ItemContainer ic = event.getItemContainer();

		if (client.getItemContainer(InventoryID.EQUIPMENT) != ic)
		{
			return;
		}

		maging = false;

		for (Item item : ic.getItems())
		{
			final String name = client.getItemDefinition(item.getId()).getName().toLowerCase();
			if (name.contains("staff") || name.contains("wand"))
			{
				maging = true;
				break;
			}
		}
	}

	private void onSpotAnimationChanged(SpotAnimationChanged event)
	{
		final Actor actor = event.getActor();

		if (actor == null)
		{
			return;
		}

		final Victim victim;

		if (actor instanceof Player)
		{
			victim = victimMap.getOrDefault(((Player) actor).getPlayerId(), null);
		}
		else if (actor instanceof NPC)
		{
			victim = victimMap.getOrDefault(((NPC) actor).getIndex(), null);
		}
		else
		{
			return;
		}

		if (victim == null)
		{
			return;
		}

		final int oldGraphic = victim.getSpotAnimLastTick();
		final int newGraphic = actor.getSpotAnimation();

		if (oldGraphic == newGraphic)
		{
			return;
		}

		final PlayerSpellEffect effect = PlayerSpellEffect.getFromSpotAnim(newGraphic);

		if (effect == PlayerSpellEffect.NONE)
		{
			return;
		}

		final int currentTick = client.getTickCount();

		if (victim.getTimerMap().containsKey(effect.getType()) || victim.getImmunityMap().containsKey(effect.getType()))
		{
			return;
		}

		int length = effect.getTimerLengthTicks();

		if (effect.isHalvable() && (victim.getLastPrayer() != null && victim.getLastPrayer() == Prayer.PROTECT_FROM_MAGIC))
		{
			length /= 2;
		}

		victim.getTimerMap().put(effect.getType(), currentTick + length);
	}

	private void onChatMessage(ChatMessage event)
	{
		if (event.getType() != ChatMessageType.GAMEMESSAGE
			|| !event.getMessage().contains("Your Tele Block has been removed")
			|| client.getLocalPlayer() == null)
		{
			return;
		}

		final Victim victim = victimMap.get(client.getLocalPlayer().getPlayerId());
		victim.getTimerMap().remove(TimerType.TELEBLOCK);
		victim.getImmunityMap().put(TimerType.TELEBLOCK, TimerType.TELEBLOCK.getImmunityTime());
	}

	private void onLocalPlayerDeath(LocalPlayerDeath event)
	{
		final Player localPlayer = client.getLocalPlayer();

		if (localPlayer == null)
		{
			return;
		}

		victimMap.get(localPlayer.getPlayerId()).getTimerMap().clear();
	}

	private void onPlayerDespawned(PlayerDespawned event)
	{
		final int id = event.getPlayer().getPlayerId();
		if (victimMap.containsKey(id))
		{
			final Victim vic = victimMap.get(id);
			victimMapCache.put(id, vic);
			victimMap.remove(id);
		}
	}

	private void onPlayerSpawned(PlayerSpawned event)
	{
		final int id = event.getPlayer().getPlayerId();
		if (victimMapCache.containsKey(id))
		{
			final Victim vic = victimMapCache.get(id);
			victimMap.put(id, vic);
			victimMapCache.remove(id);
		}
		else
		{
			victimMap.put(id, new Victim(event.getActor()));
		}
	}

	private void update(Victim victim)
	{
		if (victim.getActor() == null)
		{
			return;
		}
		victim.timerToImmunity(client.getTickCount());
		victim.update();
		if (victim.getTimerMap().containsKey(TimerType.TELEBLOCK))
		{
			final WorldPoint actorLoc = victim.getActor().getWorldLocation();
			final EnumSet<WorldType> worldTypes = client.getWorldType();

			if (!WorldType.isAllPvpWorld(worldTypes) && (actorLoc.getY() < 3525 || PvPUtil.getWildernessLevelFrom(actorLoc) <= 0))
			{
				victim.getTimerMap().remove(TimerType.TELEBLOCK);
				victim.getImmunityMap().put(TimerType.TELEBLOCK, TimerType.TELEBLOCK.getImmunityTime());
			}
			else if (WorldType.isPvpWorld(worldTypes) &&
				MapLocations.getPvpSafeZones(actorLoc.getPlane()).contains(actorLoc.getX(), actorLoc.getY()))
			{
				victim.getTimerMap().remove(TimerType.TELEBLOCK);
				victim.getImmunityMap().put(TimerType.TELEBLOCK, TimerType.TELEBLOCK.getImmunityTime());
			}
			else if (WorldType.isDeadmanWorld(worldTypes) &&
				MapLocations.getDeadmanSafeZones(actorLoc.getPlane()).contains(actorLoc.getX(), actorLoc.getY()))
			{
				victim.getTimerMap().remove(TimerType.TELEBLOCK);
				victim.getImmunityMap().put(TimerType.TELEBLOCK, TimerType.TELEBLOCK.getImmunityTime());
			}
		}
	}

	private void setSpell(Victim victim)
	{
		final int spellBookVar = client.getVar(Varbits.SPELLBOOK);
		final int mageLevel = client.getBoostedSkillLevel(Skill.MAGIC);
		final String spell = client.getSelectedSpellName() == null ? "null" : client.getSelectedSpellName();

		switch (spellBookVar)
		{
			case 0:
				if (!config.enableTbEntangle() ||
					((victim.getTimerMap().containsKey(TimerType.FREEZE) || victim.getTimerMap().containsKey(TimerType.TELEBLOCK)) &&
						(victim.getImmunityMap().containsKey(TimerType.FREEZE) || victim.getImmunityMap().containsKey(TimerType.TELEBLOCK))))
				{
					if (mageLevel >= 95)
					{
						if (!spell.contains("Fire Surge"))
						{
							setSelectSpell(WidgetInfo.SPELL_FIRE_SURGE);
						}
					}
					else if (mageLevel >= 75)
					{
						if (!spell.contains("Fire Wave"))
						{
							setSelectSpell(WidgetInfo.SPELL_FIRE_WAVE);
						}
					}
					else if (mageLevel >= 59)
					{
						if (!spell.contains("Fire Blast"))
						{
							setSelectSpell(WidgetInfo.SPELL_FIRE_BLAST);
						}
					}
				}
				else if (!victim.getTimerMap().containsKey(TimerType.TELEBLOCK) && !victim.getImmunityMap().containsKey(TimerType.TELEBLOCK) && mageLevel >= 85)
				{
					if (!spell.contains("Tele Block"))
					{
						setSelectSpell(WidgetInfo.SPELL_TELE_BLOCK);
					}
				}
				else if (!victim.getTimerMap().containsKey(TimerType.FREEZE) && !victim.getImmunityMap().containsKey(TimerType.FREEZE))
				{
					if (mageLevel >= 79)
					{
						if (!spell.contains("Entangle"))
						{
							setSelectSpell(WidgetInfo.SPELL_ENTANGLE);
						}
					}
					else if (mageLevel >= 50)
					{
						if (!spell.contains("Snare"))
						{
							setSelectSpell(WidgetInfo.SPELL_SNARE);
						}
					}
				}
				break;
			case 1:
				if (config.enableBlood() && (victim.getTimerMap().containsKey(TimerType.FREEZE) || victim.getImmunityMap().containsKey(TimerType.FREEZE)))
				{
					if (mageLevel >= 92)
					{
						if (!spell.contains("Blood Barrage"))
						{
							setSelectSpell(WidgetInfo.SPELL_BLOOD_BARRAGE);
						}
					}
					else if (mageLevel >= 80)
					{
						if (!spell.contains("Blood Blitz"))
						{
							setSelectSpell(WidgetInfo.SPELL_BLOOD_BLITZ);
						}
					}
				}
				else
				{
					if (mageLevel >= 94)
					{
						if (!spell.contains("Ice Barrage"))
						{
							setSelectSpell(WidgetInfo.SPELL_ICE_BARRAGE);
						}
					}
					else if (mageLevel >= 82)
					{
						if (!spell.contains("Ice Blitz"))
						{
							setSelectSpell(WidgetInfo.SPELL_ICE_BLITZ);
						}
					}
				}
				break;
			default:
				break;
		}
	}

	private void setSelectSpell(WidgetInfo info)
	{
		Widget widget = client.getWidget(info);
		client.setSelectedSpellName(widget.getName());
		client.setSelectedSpellWidget(widget.getId());
		client.setSelectedSpellChildIndex(-1);
	}

	private String rainbow(String string)
	{
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < string.length(); i++)
		{
			String post = String.valueOf(string.charAt(i));
			sb.append(ColorUtil.prependColorTag(post, getHue()));
		}
		return sb.toString();
	}

	private Color getHue()
	{
		if (reverse)
		{
			hue -= 0.5f;
			if (hue <= 0f)
			{
				hue = 0f;
			}
		}
		else
		{
			hue += 0.5f;
			if (hue >= 360f)
			{
				hue = 360f;
			}
		}
		if (hue == 360f)
		{
			reverse = true;
		}
		else if (hue == 0f)
		{
			reverse = false;
		}
		return Color.decode(hsvToRgb(hue, 100, 100));
	}

	/**
	 * @param H 0-360
	 * @param S 0-100
	 * @param V 0-100
	 * @return color in hex string
	 */
	private static String hsvToRgb(float H, float S, float V)
	{

		float R, G, B;

		H /= 360f;
		S /= 100f;
		V /= 100f;

		if (S == 0)
		{
			R = V * 255;
			G = V * 255;
			B = V * 255;
		}
		else
		{
			float var_h = H * 6;
			if (var_h == 6)
			{
				var_h = 0; // H must be < 1
			}
			int var_i = (int) Math.floor((double) var_h); // Or ... var_i =
			// floor( var_h )
			float var_1 = V * (1 - S);
			float var_2 = V * (1 - S * (var_h - var_i));
			float var_3 = V * (1 - S * (1 - (var_h - var_i)));

			float var_r;
			float var_g;
			float var_b;
			if (var_i == 0)
			{
				var_r = V;
				var_g = var_3;
				var_b = var_1;
			}
			else if (var_i == 1)
			{
				var_r = var_2;
				var_g = V;
				var_b = var_1;
			}
			else if (var_i == 2)
			{
				var_r = var_1;
				var_g = V;
				var_b = var_3;
			}
			else if (var_i == 3)
			{
				var_r = var_1;
				var_g = var_2;
				var_b = V;
			}
			else if (var_i == 4)
			{
				var_r = var_3;
				var_g = var_1;
				var_b = V;
			}
			else
			{
				var_r = V;
				var_g = var_1;
				var_b = var_2;
			}

			R = var_r * 255; // RGB results from 0 to 255
			G = var_g * 255;
			B = var_b * 255;
		}

		String rs = Integer.toHexString((int) (R));
		String gs = Integer.toHexString((int) (G));
		String bs = Integer.toHexString((int) (B));

		if (rs.length() == 1)
		{
			rs = "0" + rs;
		}
		if (gs.length() == 1)
		{
			gs = "0" + gs;
		}
		if (bs.length() == 1)
		{
			bs = "0" + bs;
		}
		return "#" + rs + gs + bs;
	}
}