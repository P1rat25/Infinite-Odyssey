/*
 * Copyright (C) 2004-2015 L2J Server
 * 
 * This file is part of L2J Server.
 * 
 * L2J Server is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation(Inventory.PAPERDOLL_UNDER), either version 3 of the License(Inventory.PAPERDOLL_UNDER), or
 * (at your option) any later version.
 * 
 * L2J Server is distributed in the hope that it will be useful(Inventory.PAPERDOLL_UNDER),
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not(Inventory.PAPERDOLL_UNDER), see <http://www.gnu.org/licenses/>.
 */
package com.l2jserver.gameserver.enums;

import com.l2jserver.gameserver.model.interfaces.IUpdateTypeComponent;
import com.l2jserver.gameserver.model.itemcontainer.Inventory;

/**
 * @author UnAfraid
 */
public enum InventorySlot implements IUpdateTypeComponent
{
	UNDER(Inventory.PAPERDOLL_UNDER),
	REAR(Inventory.PAPERDOLL_REAR),
	LEAR(Inventory.PAPERDOLL_LEAR),
	NECK(Inventory.PAPERDOLL_NECK),
	RFINGER(Inventory.PAPERDOLL_RFINGER),
	LFINGER(Inventory.PAPERDOLL_LFINGER),
	HEAD(Inventory.PAPERDOLL_HEAD),
	RHAND(Inventory.PAPERDOLL_RHAND),
	LHAND(Inventory.PAPERDOLL_LHAND),
	GLOVES(Inventory.PAPERDOLL_GLOVES),
	CHEST(Inventory.PAPERDOLL_CHEST),
	LEGS(Inventory.PAPERDOLL_LEGS),
	FEET(Inventory.PAPERDOLL_FEET),
	CLOAK(Inventory.PAPERDOLL_CLOAK),
	LRHAND(Inventory.PAPERDOLL_LRHAND),
	HAIR(Inventory.PAPERDOLL_HAIR),
	HAIR2(Inventory.PAPERDOLL_DHAIR),
	RBRACELET(Inventory.PAPERDOLL_RBRACELET),
	LBRACELET(Inventory.PAPERDOLL_LBRACELET),
	DECO1(Inventory.PAPERDOLL_TALISMAN1),
	DECO2(Inventory.PAPERDOLL_TALISMAN2),
	DECO3(Inventory.PAPERDOLL_TALISMAN3),
	DECO4(Inventory.PAPERDOLL_TALISMAN4),
	DECO5(Inventory.PAPERDOLL_TALISMAN5),
	DECO6(Inventory.PAPERDOLL_TALISMAN6),
	BELT(Inventory.PAPERDOLL_BELT),
	BROOCH(Inventory.PAPERDOLL_BROOCH),
	BROOCH_JEWEL(Inventory.PAPERDOLL_BROOCH_STONE1),
	BROOCH_JEWEL2(Inventory.PAPERDOLL_BROOCH_STONE2),
	BROOCH_JEWEL3(Inventory.PAPERDOLL_BROOCH_STONE3),
	BROOCH_JEWEL4(Inventory.PAPERDOLL_BROOCH_STONE4),
	BROOCH_JEWEL5(Inventory.PAPERDOLL_BROOCH_STONE5),
	BROOCH_JEWEL6(Inventory.PAPERDOLL_BROOCH_STONE6);
	
	private final int _paperdollSlot;
	
	private InventorySlot(int paperdollSlot)
	{
		_paperdollSlot = paperdollSlot;
	}
	
	public int getSlot()
	{
		return _paperdollSlot;
	}
	
	@Override
	public int getMask()
	{
		return ordinal();
	}
}
