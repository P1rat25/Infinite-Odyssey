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
package com.l2jserver.gameserver.network.clientpackets.shuttle;

import com.l2jserver.gameserver.model.Location;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.items.type.WeaponType;
import com.l2jserver.gameserver.network.clientpackets.L2GameClientPacket;
import com.l2jserver.gameserver.network.serverpackets.ActionFailed;
import com.l2jserver.gameserver.network.serverpackets.shuttle.ExMoveToLocationInShuttle;
import com.l2jserver.gameserver.network.serverpackets.shuttle.ExStopMoveInShuttle;

/**
 * @author UnAfraid
 */
public final class MoveToLocationInShuttle extends L2GameClientPacket
{
	private static final String _C__75_MOVETOLOCATIONINVEHICLE = "[C] 81 MoveToLocationInVehicle";
	
	private int _boatId;
	private int _targetX;
	private int _targetY;
	private int _targetZ;
	private int _originX;
	private int _originY;
	private int _originZ;
	
	@Override
	protected void readImpl()
	{
		_boatId = readD(); // objectId of boat
		_targetX = readD();
		_targetY = readD();
		_targetZ = readD();
		_originX = readD();
		_originY = readD();
		_originZ = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final L2PcInstance activeChar = getClient().getActiveChar();
		if (activeChar == null)
		{
			return;
		}
		
		if ((_targetX == _originX) && (_targetY == _originY) && (_targetZ == _originZ))
		{
			activeChar.sendPacket(new ExStopMoveInShuttle(activeChar, _boatId));
			return;
		}
		
		if (activeChar.isAttackingNow() && (activeChar.getActiveWeaponItem() != null) && (activeChar.getActiveWeaponItem().getItemType() == WeaponType.BOW))
		{
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		if (activeChar.isSitting() || activeChar.isMovementDisabled())
		{
			activeChar.sendPacket(ActionFailed.STATIC_PACKET);
			return;
		}
		
		activeChar.setInVehiclePosition(new Location(_targetX, _targetY, _targetZ));
		activeChar.broadcastPacket(new ExMoveToLocationInShuttle(activeChar, _originX, _originY, _originZ));
	}
	
	@Override
	public String getType()
	{
		return _C__75_MOVETOLOCATIONINVEHICLE;
	}
}
