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

import com.l2jserver.gameserver.instancemanager.ClanEntryManager;
import com.l2jserver.gameserver.model.ClanPrivilege;
import com.l2jserver.gameserver.model.L2Clan;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.clan.entry.PledgeRecruitInfo;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * @author Sdw
 */
public class RequestPledgeRecruitBoardAccess extends L2GameClientPacket
{
	private static final String _C__D0_D5_REQUESTPLEDGERECRUITBOARDACCESS = "[C] D0;D5 RequestPledgeRecruitBoardAccess";
	
	private int _applyType;
	private int _karma;
	private String _information;
	private String _datailedInformation;
	
	@Override
	protected void readImpl()
	{
		_applyType = readD();
		_karma = readD();
		_information = readS();
		_datailedInformation = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		
		if (activeChar == null)
		{
			return;
		}
		
		final L2Clan clan = activeChar.getClan();
		
		if (clan == null)
		{
			activeChar.sendPacket(SystemMessageId.ONLY_THE_CLAN_LEADER_OR_SOMEONE_WITH_RANK_MANAGEMENT_AUTHORITY_MAY_REGISTER_THE_CLAN);
			return;
		}
		
		if (!activeChar.hasClanPrivilege(ClanPrivilege.CL_MANAGE_RANKS))
		{
			activeChar.sendPacket(SystemMessageId.ONLY_THE_CLAN_LEADER_OR_SOMEONE_WITH_RANK_MANAGEMENT_AUTHORITY_MAY_REGISTER_THE_CLAN);
			return;
		}
		
		final PledgeRecruitInfo pledgeRecruitInfo = new PledgeRecruitInfo(clan.getId(), _karma, _information, _datailedInformation);
		
		switch (_applyType)
		{
			case 0: // remove
			{
				ClanEntryManager.getInstance().removeFromClanList(clan.getId());
				break;
			}
			case 1: // add
			{
				if (ClanEntryManager.getInstance().addToClanList(clan.getId(), pledgeRecruitInfo))
				{
					activeChar.sendPacket(SystemMessageId.ENTRY_APPLICATION_COMPLETE_USE_ENTRY_APPLICATION_INFO_TO_CHECK_OR_CANCEL_YOUR_APPLICATION_APPLICATION_IS_AUTOMATICALLY_CANCELLED_AFTER_30_DAYS_IF_YOU_CANCEL_APPLICATION_YOU_CANNOT_APPLY_AGAIN_FOR_5_MINUTES);
				}
				else
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_MAY_APPLY_FOR_ENTRY_AFTER_S1_MINUTE_S_DUE_TO_CANCELLING_YOUR_APPLICATION);
					sm.addLong(ClanEntryManager.getInstance().getClanLockTime(clan.getId()));
					activeChar.sendPacket(sm);
				}
				break;
			}
			case 2: // update
			{
				if (ClanEntryManager.getInstance().updateClanList(clan.getId(), pledgeRecruitInfo))
				{
					activeChar.sendPacket(SystemMessageId.ENTRY_APPLICATION_COMPLETE_USE_ENTRY_APPLICATION_INFO_TO_CHECK_OR_CANCEL_YOUR_APPLICATION_APPLICATION_IS_AUTOMATICALLY_CANCELLED_AFTER_30_DAYS_IF_YOU_CANCEL_APPLICATION_YOU_CANNOT_APPLY_AGAIN_FOR_5_MINUTES);
				}
				else
				{
					SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_MAY_APPLY_FOR_ENTRY_AFTER_S1_MINUTE_S_DUE_TO_CANCELLING_YOUR_APPLICATION);
					sm.addLong(ClanEntryManager.getInstance().getClanLockTime(clan.getId()));
					activeChar.sendPacket(sm);
				}
				break;
			}
		}
	}
	
	@Override
	public String getType()
	{
		return _C__D0_D5_REQUESTPLEDGERECRUITBOARDACCESS;
	}
}
