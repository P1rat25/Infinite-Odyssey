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
package com.l2jserver.gameserver.network.clientpackets;

import java.util.concurrent.TimeUnit;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ExPledgeCount;
import com.l2jserver.gameserver.network.serverpackets.PledgeShowMemberListDelete;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * This class ...
 * @version $Revision: 1.3.2.1.2.3 $ $Date: 2005/03/27 15:29:30 $
 */
public final class RequestWithdrawalPledge extends L2GameClientPacket
{
	private static final String _C__28_REQUESTWITHDRAWALPLEDGE = "[C] 28 RequestWithdrawalPledge";
	
	@Override
	protected void readImpl()
	{
		// trigger
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		if (activeChar.getClan() == null)
		{
			activeChar.sendPacket(SystemMessageId.YOU_ARE_NOT_A_CLAN_MEMBER_AND_CANNOT_PERFORM_THIS_ACTION);
			return;
		}
		if (activeChar.isClanLeader())
		{
			activeChar.sendPacket(SystemMessageId.A_CLAN_LEADER_CANNOT_WITHDRAW_FROM_THEIR_OWN_CLAN);
			return;
		}
		if (activeChar.isInCombat())
		{
			activeChar.sendPacket(SystemMessageId.YOU_CANNOT_LEAVE_A_CLAN_WHILE_ENGAGED_IN_COMBAT);
			return;
		}
		
		L2Clan clan = activeChar.getClan();
		
		clan.removeClanMember(activeChar.getObjectId(), System.currentTimeMillis() + TimeUnit.DAYS.toMillis(Config.ALT_CLAN_JOIN_DAYS));
		
		SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.S1_HAS_WITHDRAWN_FROM_THE_CLAN);
		sm.addString(activeChar.getName());
		clan.broadcastToOnlineMembers(sm);
		
		// Remove the Player From the Member list
		clan.broadcastToOnlineMembers(new PledgeShowMemberListDelete(activeChar.getName()));
		clan.broadcastToOnlineMembers(new ExPledgeCount(clan));
		
		activeChar.sendPacket(SystemMessageId.YOU_HAVE_WITHDRAWN_FROM_THE_CLAN);
		activeChar.sendPacket(SystemMessageId.AFTER_LEAVING_OR_HAVING_BEEN_DISMISSED_FROM_A_CLAN_YOU_MUST_WAIT_AT_LEAST_A_DAY_BEFORE_JOINING_ANOTHER_CLAN);
	}
	
	@Override
	public String getType()
	{
		return _C__28_REQUESTWITHDRAWALPLEDGE;
	}
}
