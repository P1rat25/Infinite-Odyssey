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
package com.l2jserver.gameserver.network.clientpackets.adenadistribution;

import java.util.List;

import com.l2jserver.gameserver.model.L2CommandChannel;
import com.l2jserver.gameserver.model.L2Party;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.actor.request.AdenaDistributionRequest;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.l2jserver.gameserver.network.serverpackets.adenadistribution.ExDivideAdenaStart;

/**
 * @author Sdw
 */
public class RequestDivideAdenaStart extends L2GameClientPacket
{
	@Override
	protected void readImpl()
	{
		// Nothing to read
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance player = getClient().getActiveChar();
		if (player == null)
		{
			return;
		}
		
		final L2Party party = player.getParty();
		
		if (party == null)
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_PROCEED_AS_YOU_ARE_NOT_IN_AN_ALLIANCE_OR_PARTY);
			return;
		}
		
		final L2CommandChannel commandChannel = party.getCommandChannel();
		
		if ((commandChannel != null) && !commandChannel.isLeader(player))
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_PROCEED_AS_YOU_ARE_NOT_AN_ALLIANCE_LEADER_OR_PARTY_LEADER);
			return;
		}
		else if (!party.isLeader(player))
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_PROCEED_AS_YOU_ARE_NOT_A_PARTY_LEADER);
			return;
		}
		
		final List<L2PcInstance> targets = commandChannel != null ? commandChannel.getMembers() : party.getMembers();
		
		if (player.getAdena() < targets.size())
		{
			player.sendPacket(SystemMessageId.YOU_CANNOT_PROCEED_AS_THERE_IS_INSUFFICIENT_ADENA);
			return;
		}
		
		if (targets.stream().anyMatch(t -> t.hasRequest(AdenaDistributionRequest.class)))
		{
			// Handle that case ?
			return;
		}
		
		final int adenaObjectId = player.getInventory().getAdenaInstance().getObjectId();
		
		targets.forEach(t ->
		{
			t.sendPacket(SystemMessageId.ADENA_DISTRIBUTION_HAS_STARTED);
			t.addRequest(new AdenaDistributionRequest(t, player, targets, adenaObjectId, player.getAdena()));
		});
		
		player.sendPacket(ExDivideAdenaStart.STATIC_PACKET);
	}
	
	@Override
	public String getType()
	{
		return getClass().getSimpleName();
	}
}
