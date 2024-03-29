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

import com.l2jserver.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author UnAfraid
 */
public class ExPutShapeShiftingExtractionItemResult extends L2GameServerPacket
{
	public static ExPutShapeShiftingExtractionItemResult FAILED = new ExPutShapeShiftingExtractionItemResult(0x00);
	public static ExPutShapeShiftingExtractionItemResult SUCCESS = new ExPutShapeShiftingExtractionItemResult(0x01);
	
	private final int _result;
	
	public ExPutShapeShiftingExtractionItemResult(int result)
	{
		_result = result;
	}
	
	@Override
	protected void writeImpl()
	{
		writeC(0xFE);
		writeH(0x12B);
		writeD(_result);
	}
}