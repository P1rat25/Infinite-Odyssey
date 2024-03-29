/*
 * Copyright (C) 2004-2015 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package quests.Q10362_CertificationOfTheSeeker;

import java.util.HashMap;
import java.util.Map;

import quests.Q10361_RolesOfTheSeeker.Q10361_RolesOfTheSeeker;

import com.l2jserver.gameserver.model.actor.L2Npc;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.holders.ItemHolder;
import com.l2jserver.gameserver.model.quest.Quest;
import com.l2jserver.gameserver.model.quest.QuestState;
import com.l2jserver.gameserver.model.quest.State;
import com.l2jserver.gameserver.network.NpcStringId;
import com.l2jserver.gameserver.network.serverpackets.ExQuestNpcLogList;
import com.l2jserver.gameserver.network.serverpackets.ExShowScreenMessage;
import com.l2jserver.gameserver.util.Util;

/**
 * Certification of the Seeker (10362)
 * @author spider
 */
public class Q10362_CertificationOfTheSeeker extends Quest
{
	// NPCs
	private static final int CHESHA = 33449;
	private static final int NAGEL = 33450;
	// Monsters
	private static final int CRAWLER = 22991;
	private static final int STALKER = 22992;
	private static final Map<Integer, Integer> MOBS_REQUIRED = new HashMap<>();
	{
		MOBS_REQUIRED.put(CRAWLER, 5);
		MOBS_REQUIRED.put(STALKER, 10);
	}
	// Rewards
	private static final int ADENA_REWARD = 43000;
	private static final int EXP_REWARD = 50000;
	private static final int SP_REWARD = 12;
	private static final ItemHolder GLOVES = new ItemHolder(49, 1);
	private static final ItemHolder HEALING_POTIONS = new ItemHolder(1060, 50);
	// Others
	private static final int MIN_LEVEL = 10;
	private static final int MAX_LEVEL = 20;
	
	public Q10362_CertificationOfTheSeeker()
	{
		super(10362, Q10362_CertificationOfTheSeeker.class.getSimpleName(), "Certification of the Seeker");
		addStartNpc(CHESHA);
		addTalkId(CHESHA, NAGEL);
		addKillId(CRAWLER, STALKER);
		addCondLevel(MIN_LEVEL, MAX_LEVEL, "no_level.htm");
		addCondCompletedQuest(Q10361_RolesOfTheSeeker.class.getSimpleName(), "no_prequest.html");
	}
	
	@Override
	public String onAdvEvent(String event, L2Npc npc, L2PcInstance player)
	{
		final QuestState qs = getQuestState(player, false);
		if (qs == null)
		{
			return null;
		}
		
		String htmltext = null;
		switch (event)
		{
			case "33449-02.htm":
			{
				htmltext = event;
				break;
			}
			case "33449-03.htm": // start the quest
			{
				qs.startQuest();
				qs.set(Integer.toString(CRAWLER), 0);
				qs.set(Integer.toString(STALKER), 0);
				htmltext = event;
				break;
			}
			case "33450-02.html":
			{
				htmltext = event;
				break;
			}
			case "33450-03.html": // exit quest
			{
				giveAdena(player, ADENA_REWARD, true);
				addExpAndSp(player, EXP_REWARD, SP_REWARD);
				giveItems(player, GLOVES);
				giveItems(player, HEALING_POTIONS);
				qs.exitQuest(false, true);
				htmltext = event;
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onTalk(L2Npc npc, L2PcInstance player)
	{
		final QuestState qs = getQuestState(player, true);
		String htmltext = null;
		switch (qs.getState())
		{
			case State.CREATED:
			{
				htmltext = npc.getId() == CHESHA ? "33449-01.htm" : getNoQuestMsg(player);
				break;
			}
			case State.STARTED:
			{
				if (qs.isCond(1))
				{
					htmltext = npc.getId() == CHESHA ? "33449-04.html" : getNoQuestMsg(player);
				}
				else if (qs.isCond(2))
				{
					if (npc.getId() == CHESHA) // mobs defeated
					{
						htmltext = "33449-05.html";
						showOnScreenMsg(player, NpcStringId.USE_THE_YE_SAGIRA_TELEPORT_DEVICE_TO_GO_TO_EXPLORATION_AREA_2, ExShowScreenMessage.TOP_CENTER, 5000);
						qs.setCond(3);
						qs.unset(Integer.toString(CRAWLER));
						qs.unset(Integer.toString(STALKER));
					}
					else
					{
						htmltext = getNoQuestMsg(player);
					}
				}
				else
				{
					htmltext = npc.getId() == CHESHA ? "33449-06.html" : "33450-01.html";
				}
				break;
			}
			case State.COMPLETED:
			{
				htmltext = getAlreadyCompletedMsg(player);
				break;
			}
		}
		return htmltext;
	}
	
	@Override
	public String onKill(L2Npc npc, L2PcInstance killer, boolean isSummon)
	{
		final QuestState qs = getRandomPartyMemberState(killer, -1, 3, npc);
		if ((qs != null) && qs.isStarted() && qs.isCond(1) && (Util.checkIfInRange(1500, npc, qs.getPlayer(), false)))
		{
			int kills = 0;
			switch (npc.getId())
			{
				case CRAWLER:
				{
					kills = qs.getInt(Integer.toString(CRAWLER));
					kills++;
					qs.set(Integer.toString(CRAWLER), kills);
					break;
				}
				case STALKER:
				{
					kills = qs.getInt(Integer.toString(STALKER));
					kills++;
					qs.set(Integer.toString(STALKER), kills);
					break;
				}
			}
			
			ExQuestNpcLogList log = new ExQuestNpcLogList(getId());
			log.addNpc(CRAWLER, qs.getInt(Integer.toString(CRAWLER)));
			log.addNpc(STALKER, qs.getInt(Integer.toString(STALKER)));
			killer.sendPacket(log);
			
			if (((qs.getInt(Integer.toString(CRAWLER)) >= MOBS_REQUIRED.get(CRAWLER)) && (qs.getInt(Integer.toString(STALKER)) >= MOBS_REQUIRED.get(STALKER))))
			{
				qs.setCond(2);
			}
		}
		return super.onKill(npc, killer, isSummon);
	}
}
