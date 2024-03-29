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
package com.l2jserver.gameserver.model.items.enchant;

import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.items.type.EtcItemType;

/**
 * @author UnAfraid
 */
public final class EnchantSupportItem extends AbstractEnchantItem
{
	private final boolean _isWeapon;
	
	public EnchantSupportItem(StatsSet set)
	{
		super(set);
		if ((getItem().getItemType() == EtcItemType.SCRL_INC_ENCHANT_PROP_WP) || (getItem().getItemType() == EtcItemType.BLESS_SCRL_INC_ENCHANT_PROP_WP) || (getItem().getItemType() == EtcItemType.GIANT_SCRL_INC_ENCHANT_PROP_WP) || (getItem().getItemType() == EtcItemType.GIANT_SCRL_BLESS_INC_ENCHANT_PROP_WP) || (getItem().getItemType() == EtcItemType.SCRL_BLESS_INC_ENCHANT_PROP_WP) || (getItem().getItemType() == EtcItemType.BLESS_DROP_SCRL_INC_ENCHANT_PROP_WP) || (getItem().getItemType() == EtcItemType.GIANT2_SCRL_BLESS_INC_ENCHANT_PROP_WP))
		{
			_isWeapon = true;
		}
		else
		{
			_isWeapon = false;
		}
	}
	
	@Override
	public boolean isWeapon()
	{
		return _isWeapon;
	}
}
