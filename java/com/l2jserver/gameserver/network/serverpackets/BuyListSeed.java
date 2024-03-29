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
package com.l2jserver.gameserver.network.serverpackets;

import java.util.ArrayList;
import java.util.List;

import com.l2jserver.gameserver.instancemanager.CastleManorManager;
import com.l2jserver.gameserver.model.SeedProduction;

/**
 * @author l3x
 */
public final class BuyListSeed extends L2GameServerPacket
{
	private final int _manorId;
	private final long _money;
	private final List<SeedProduction> _list = new ArrayList<>();
	
	public BuyListSeed(long currentMoney, int castleId)
	{
		_money = currentMoney;
		_manorId = castleId;
		
		for (SeedProduction s : CastleManorManager.getInstance().getSeedProduction(castleId, false))
		{
			if ((s.getAmount() > 0) && (s.getPrice() > 0))
			{
				_list.add(s);
			}
		}
	}
	
	@Override
	protected final void writeImpl()
	{
		writeC(0xe9);
		
		writeQ(_money); // current money
		writeD(0x00); // TODO: Find me!
		writeD(_manorId); // manor id
		
		if (!_list.isEmpty())
		{
			writeH(_list.size()); // list length
			for (SeedProduction s : _list)
			{
				writeC(0x00); // mask item 0 to print minimal item information
				writeD(s.getId()); // ObjectId
				writeD(s.getId()); // ItemId
				writeC(0xFF); // T1
				writeQ(s.getAmount()); // Quantity
				writeC(0x05); // Item Type 2 : 00-weapon, 01-shield/armor, 02-ring/earring/necklace, 03-questitem, 04-adena, 05-item
				writeC(0x00); // Filler (always 0)
				writeH(0x00); // Equipped : 00-No, 01-yes
				writeQ(0x00); // Slot : 0006-lr.ear, 0008-neck, 0030-lr.finger, 0040-head, 0100-l.hand, 0200-gloves, 0400-chest, 0800-pants, 1000-feet, 4000-r.hand, 8000-r.hand
				writeC(0x00); // Enchant level (pet level shown in control item)
				writeC(0x00);
				writeD(-1);
				writeD(-9999);
				writeC(0x01); // GOD Item enabled = 1 disabled (red) = 0
				writeQ(s.getPrice()); // price
			}
			_list.clear();
		}
		else
		{
			writeH(0x00);
		}
	}
}