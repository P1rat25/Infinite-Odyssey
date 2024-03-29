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
package com.l2jserver.gameserver.model.stats;

import com.l2jserver.gameserver.model.actor.L2Character;

/**
 * @author Sdw
 */
public enum BaseStats
{
	STR // #TODO Check if correct
	{
		@Override
		public double calcBonus(L2Character actor)
		{
			return Math.pow(1.009, actor.getSTR() - 49);
		}
	},
	INT // @TODO Update
	{
		@Override
		public double calcBonus(L2Character actor)
		{
			return Math.pow(1.01, actor.getINT() - 49.4);
		}
	},
	DEX // Updated and better Formula to match Ertheia
	{
		@Override
		public double calcBonus(L2Character actor)
		{
			return Math.pow(1.004558949443461, actor.getDEX() - 19.27356040917275);
		}
	},
	WIT // Updated and better Formula to match Ertheia
	{
		@Override
		public double calcBonus(L2Character actor)
		{
			return Math.pow(1.013832042738272, actor.getWIT() - 64.57078483041223);
		}
	},
	CON // Updated and better Formula to match Ertheia
	{
		@Override
		public double calcBonus(L2Character actor)
		{
			return Math.pow(1.011685289099497, actor.getCON() - 34.80273839854561);
		}
	},
	MEN // Updated and better Formula to match Ertheia
	{
		@Override
		public double calcBonus(L2Character actor)
		{
			return Math.pow(1.003687502032154, actor.getMEN() + 30.4505503162);
		}
	},
	CHA // Addition for Ertheia
	{
		@Override
		public double calcBonus(L2Character actor)
		{
			return Math.pow(1.001, actor.getCHA() - 43);
		}
	},
	LUC // @TODO: Implement
	{
		@Override
		public double calcBonus(L2Character actor)
		{
			return 1;
		}
	},
	NONE
	{
		@Override
		public double calcBonus(L2Character actor)
		{
			return 1;
		}
	};
	
	public abstract double calcBonus(L2Character actor);
}
