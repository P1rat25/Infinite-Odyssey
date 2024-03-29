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

import com.l2jserver.Config;
import com.l2jserver.gameserver.data.xml.impl.AdminData;
import com.l2jserver.gameserver.instancemanager.PetitionManager;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

/**
 * <p>
 * Format: (c) Sd
 * <ul>
 * <li>S: content</li>
 * <li>d: type</li>
 * </ul>
 * </p>
 * @author -Wooden-, TempyIncursion
 */
public final class RequestPetition extends L2GameClientPacket
{
	private static final String _C__89_RequestPetition = "[C] 89 RequestPetition";
	
	private String _content;
	private int _type; // 1 = on : 0 = off;
	
	@Override
	protected void readImpl()
	{
		_content = readS();
		_type = readD();
	}
	
	@Override
	protected void runImpl()
	{
		L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if (!AdminData.getInstance().isGmOnline(false))
		{
			activeChar.sendPacket(SystemMessageId.THERE_ARE_NO_GMS_CURRENTLY_VISIBLE_IN_THE_PUBLIC_LIST_AS_THEY_MAY_BE_PERFORMING_OTHER_FUNCTIONS_AT_THE_MOMENT);
			return;
		}
		
		if (!PetitionManager.getInstance().isPetitioningAllowed())
		{
			activeChar.sendPacket(SystemMessageId.THE_GAME_CLIENT_ENCOUNTERED_AN_ERROR_AND_WAS_UNABLE_TO_CONNECT_TO_THE_PETITION_SERVER);
			return;
		}
		
		if (PetitionManager.getInstance().isPlayerPetitionPending(activeChar))
		{
			activeChar.sendPacket(SystemMessageId.YOU_MAY_ONLY_SUBMIT_ONE_PETITION_ACTIVE_AT_A_TIME);
			return;
		}
		
		if (PetitionManager.getInstance().getPendingPetitionCount() == Config.MAX_PETITIONS_PENDING)
		{
			activeChar.sendPacket(SystemMessageId.THE_PETITION_SERVICE_IS_CURRENTLY_UNAVAILABLE_PLEASE_SEND_A_SUPPORT_TICKET_ON_HTTPS_LINEAGE2_CUSTHELP_COM);
			return;
		}
		
		int totalPetitions = PetitionManager.getInstance().getPlayerTotalPetitionCount(activeChar) + 1;
		
		if (totalPetitions > Config.MAX_PETITIONS_PER_PLAYER)
		{
			SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.WE_HAVE_RECEIVED_S1_PETITIONS_FROM_YOU_TODAY_AND_THAT_IS_THE_MAXIMUM_THAT_YOU_CAN_SUBMIT_IN_ONE_DAY_YOU_CANNOT_SUBMIT_ANY_MORE_PETITIONS);
			sm.addInt(totalPetitions);
			activeChar.sendPacket(sm);
			return;
		}
		
		if (_content.length() > 255)
		{
			activeChar.sendPacket(SystemMessageId.THE_PETITION_CAN_CONTAIN_UP_TO_800_CHARACTERS);
			return;
		}
		
		int petitionId = PetitionManager.getInstance().submitPetition(activeChar, _content, _type);
		
		SystemMessage sm = SystemMessage.getSystemMessage(SystemMessageId.YOUR_PETITION_APPLICATION_HAS_BEEN_ACCEPTED_NRECEIPT_NO_IS_S1);
		sm.addInt(petitionId);
		activeChar.sendPacket(sm);
		
		sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_SUBMITTED_S1_PETITION_S_NYOU_MAY_SUBMIT_S2_MORE_PETITION_S_TODAY);
		sm.addInt(totalPetitions);
		sm.addInt(Config.MAX_PETITIONS_PER_PLAYER - totalPetitions);
		activeChar.sendPacket(sm);
		
		sm = SystemMessage.getSystemMessage(SystemMessageId.THERE_ARE_S1_PETITIONS_CURRENTLY_ON_THE_WAITING_LIST);
		sm.addInt(PetitionManager.getInstance().getPendingPetitionCount());
		activeChar.sendPacket(sm);
	}
	
	@Override
	public String getType()
	{
		return _C__89_RequestPetition;
	}
}
