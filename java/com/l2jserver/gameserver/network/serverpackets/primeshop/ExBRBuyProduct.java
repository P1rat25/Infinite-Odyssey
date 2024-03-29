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
package com.l2jserver.gameserver.network.serverpackets.primeshop;

import com.l2jserver.gameserver.model.interfaces.IIdentifiable;
import com.l2jserver.gameserver.network.serverpackets.L2GameServerPacket;

/**
 * @author Gnacik, UnAfraid
 */
public class ExBRBuyProduct extends L2GameServerPacket
{
	public enum ExBrProductReplyType implements IIdentifiable
	{
		SUCCESS(1),
		LACK_OF_POINT(-1),
		INVALID_PRODUCT(-2),
		USER_CANCEL(-3),
		INVENTROY_OVERFLOW(-4),
		CLOSED_PRODUCT(-5),
		SERVER_ERROR(-6),
		BEFORE_SALE_DATE(-7),
		AFTER_SALE_DATE(-8),
		INVALID_USER(-9),
		INVALID_ITEM(-10),
		INVALID_USER_STATE(-11),
		NOT_DAY_OF_WEEK(-12),
		NOT_TIME_OF_DAY(-13),
		SOLD_OUT(-14);
		private final int _id;
		
		private ExBrProductReplyType(int id)
		{
			_id = id;
		}
		
		@Override
		public int getId()
		{
			return _id;
		}
	}
	
	private final int _reply;
	
	public ExBRBuyProduct(final ExBrProductReplyType type)
	{
		_reply = type.getId();
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xFE);
		writeH(0xD9);
		writeD(_reply);
	}
}
