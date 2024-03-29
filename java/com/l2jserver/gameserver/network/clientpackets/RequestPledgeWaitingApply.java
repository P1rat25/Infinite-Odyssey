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

import com.l2jserver.gameserver.data.sql.impl.ClanTable;
import com.l2jserver.gameserver.enums.ClanEntryStatus;
import com.l2jserver.gameserver.instancemanager.ClanEntryManager;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.clan.entry.PledgeApplicantInfo;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.ExPledgeRecruitApplyInfo;
import com.l2jserver.gameserver.network.serverpackets.ExPledgeWaitingListAlarm;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Sdw
 */
public class RequestPledgeWaitingApply extends L2GameClientPacket
{
	private static final String _C__D0_D7_REQUESTPLEDGEWAITINGAPPLY = "[C] D0;D7 RequestPledgeWaitingApply";
	
	private int _karma;
	private int _clanId;
	private String _message;
	
	@Override
	protected void readImpl()
	{
		_karma = readD();
		_clanId = readD();
		_message = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		
		if ((activeChar == null) || (activeChar.getClan() != null))
		{
			return;
		}
		
		final L2Clan clan = ClanTable.getInstance().getClan(_clanId);
		
		if (clan == null)
		{
			return;
		}
		
		final PledgeApplicantInfo info = new PledgeApplicantInfo(activeChar.getObjectId(), activeChar.getName(), activeChar.getLevel(), _karma, _clanId, _message);
		
		if (ClanEntryManager.getInstance().addPlayerApplicationToClan(_clanId, info))
		{
			activeChar.sendPacket(new ExPledgeRecruitApplyInfo(ClanEntryStatus.WAITING));
			
			final L2PcInstance clanLeader = L2World.getInstance().getPlayer(clan.getLeaderId());
			
			if (clanLeader != null)
			{
				clanLeader.sendPacket(ExPledgeWaitingListAlarm.STATIC_PACKET);
			}
		}
		else
		{
			final SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_MAY_APPLY_FOR_ENTRY_AFTER_S1_MINUTE_S_DUE_TO_CANCELLING_YOUR_APPLICATION);
			sm.addLong(ClanEntryManager.getInstance().getPlayerLockTime(activeChar.getObjectId()));
			activeChar.sendPacket(sm);
		}
	}
	
	@Override
	public String getType()
	{
		return _C__D0_D7_REQUESTPLEDGEWAITINGAPPLY;
	}
	
}
