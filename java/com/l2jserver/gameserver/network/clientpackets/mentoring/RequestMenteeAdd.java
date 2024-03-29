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
package com.l2jserver.gameserver.network.clientpackets.mentoring;

import com.l2jserver.Config;
import com.l2jserver.gameserver.model.L2World;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.network.SystemMessageId;
import com.l2jserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.l2jserver.gameserver.network.serverpackets.SystemMessage;
import com.l2jserver.gameserver.network.serverpackets.mentoring.ExMentorAdd;

/**
 * @author Gnacik, UnAfraid
 */
public class RequestMenteeAdd extends L2GameClientPacket
{
	private String _target;
	
	@Override
	protected void readImpl()
	{
		_target = readS();
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance mentor = getClient().getActiveChar();
		if (mentor == null)
		{
			return;
		}
		
		final L2PcInstance mentee = L2World.getInstance().getPlayer(_target);
		if (mentee == null)
		{
			return;
		}
		
		if (Config.FACTION_SYSTEM_ENABLED && ((mentor.isEvil() && mentee.isGood()) || (mentor.isGood() && mentee.isEvil())))
		{
			mentor.sendMessage("You cannot mentor a member of the opposing faction.");
			return;
		}
		
		if (ConfirmMenteeAdd.validate(mentor, mentee))
		{
			mentor.sendPacket(SystemMessage.getSystemMessage(SystemMessageId.YOU_HAVE_OFFERED_TO_BECOME_S1_S_MENTOR).addCharName(mentee));
			mentee.sendPacket(new ExMentorAdd(mentor));
		}
	}
	
	@Override
	public String getType()
	{
		return getClass().getSimpleName();
	}
}