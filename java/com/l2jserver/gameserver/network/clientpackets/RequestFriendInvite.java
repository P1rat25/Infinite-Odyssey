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
import com.l2jserver.gameserver.model.BlockList;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.serverpackets.FriendAddRequest;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;

public final class RequestFriendInvite extends L2GameClientPacket
{
	private static final String _C__77_REQUESTFRIENDINVITE = "[C] 77 RequestFriendInvite";
	
	private String _name;
	
	@Override
	protected void readImpl()
	{
		_name = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance activeChar = getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		final L2PcInstance friend = L2World.getInstance().getPlayer(_name);
		
		// Target is not found in the game.
		if ((friend == null) || !friend.isOnline() || friend.isInvisible())
		{
			activeChar.sendPacket(SystemMessageId.THE_USER_WHO_REQUESTED_TO_BECOME_FRIENDS_IS_NOT_FOUND_IN_THE_GAME);
			return;
		}
		// You cannot add yourself to your own friend list.
		if (friend == activeChar)
		{
			activeChar.sendPacket(SystemMessageId.YOU_CANNOT_ADD_YOURSELF_TO_YOUR_OWN_FRIEND_LIST);
			return;
		}
		// Target is in olympiad.
		if (activeChar.isInOlympiadMode() || friend.isInOlympiadMode())
		{
			activeChar.sendPacket(SystemMessageId.A_USER_CURRENTLY_PARTICIPATING_IN_THE_OLYMPIAD_CANNOT_SEND_PARTY_AND_FRIEND_INVITATIONS);
			return;
		}
		// Target blocked active player.
		if (BlockList.isBlocked(friend, activeChar))
		{
			activeChar.sendMessage("You are in target's block list.");
			return;
		}
		SystemMessage sm;
		// Target is blocked.
		if (BlockList.isBlocked(activeChar, friend))
		{
			sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_BLOCKED_C1);
			sm.addCharName(friend);
			activeChar.sendPacket(sm);
			return;
		}
		// Target already in friend list.
		if (activeChar.getFriendList().containsKey(friend.getObjectId()))
		{
			sm = SystemMessage.getSystemMessage(SystemMessageId.THIS_PLAYER_IS_ALREADY_REGISTERED_ON_YOUR_FRIENDS_LIST);
			sm.addString(_name);
			activeChar.sendPacket(sm);
			return;
		}
		// Target is busy.
		if (friend.isProcessingRequest())
		{
			sm = SystemMessage.getSystemMessage(SystemMessageId.C1_IS_ON_ANOTHER_TASK_PLEASE_TRY_AGAIN_LATER);
			sm.addString(_name);
			activeChar.sendPacket(sm);
			return;
		}
		if (Config.FACTION_SYSTEM_ENABLED && ((friend.isEvil() && activeChar.isGood()) || (friend.isGood() && activeChar.isEvil())))
		{
			activeChar.sendMessage("You cannot have a friend of the opposing faction.");
			return;
		}
		// Friend request sent.
		activeChar.onTransactionRequest(friend);
		friend.sendPacket(new FriendAddRequest(activeChar.getName()));
		sm = SystemMessage.getSystemMessage(SystemMessageId.YOU_VE_REQUESTED_C1_TO_BE_ON_YOUR_FRIENDS_LIST);
		sm.addString(_name);
		activeChar.sendPacket(sm);
	}
	
	@Override
	public String getType()
	{
		return _C__77_REQUESTFRIENDINVITE;
	}
}
