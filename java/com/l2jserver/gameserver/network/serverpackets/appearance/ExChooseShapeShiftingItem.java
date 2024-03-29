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
package com.l2jserver.gameserver.network.serverpackets.appearance;

import com.l2jserver.gameserver.model.items.appearance.AppearanceStone;
import com.l2jserver.gameserver.model.items.appearance.AppearanceTargetType;
import com.l2jserver.gameserver.model.items.appearance.AppearanceType;
import com.l2jserver.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author UnAfraid
 */
public class ExChooseShapeShiftingItem extends L2GameServerPacket
{
	
	private final AppearanceType _type;
	private final AppearanceTargetType _targetType;
	private final int _itemId;
	
	public ExChooseShapeShiftingItem(AppearanceStone stone)
	{
		_type = stone.getType();
		_targetType = stone.getTargetTypes().size() > 1 ? AppearanceTargetType.ALL : stone.getTargetTypes().get(0);
		_itemId = stone.getId();
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x129);
		writeD(_targetType != null ? _targetType.ordinal() : 0);
		writeD(_type != null ? _type.ordinal() : 0);
		writeD(_itemId);
	}
}
