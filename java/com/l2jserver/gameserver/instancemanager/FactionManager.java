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
package com.l2jserver.gameserver.instancemanager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.l2jserver.commons.database.pool.impl.ConnectionFactory;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;

/**
 * Contains objectId and factionId for all players.
 * @author Mobius
 */
public class FactionManager
{
	private static Logger _log = Logger.getLogger(FactionManager.class.getName());
	private final Map<Integer, Integer> _playerFactions = new ConcurrentHashMap<>();
	
	protected FactionManager()
	{
		loadAll();
	}
	
	private void loadAll()
	{
		_playerFactions.clear();
		try (Connection con = ConnectionFactory.getInstance().getConnection();
			Statement s = con.createStatement();
			ResultSet rs = s.executeQuery("SELECT charId, faction FROM characters"))
		{
			while (rs.next())
			{
				final int id = rs.getInt(1);
				_playerFactions.put(id, rs.getInt(4));
			}
		}
		catch (SQLException e)
		{
			_log.log(Level.WARNING, getClass().getSimpleName() + ": Could not load character faction information: " + e.getMessage(), e);
		}
		_log.info(getClass().getSimpleName() + ": Loaded " + _playerFactions.size() + " character faction values.");
	}
	
	public final int getFactionByCharId(int id)
	{
		if (id <= 0)
		{
			return 0;
		}
		
		Integer factionId = _playerFactions.get(id);
		if (factionId != null)
		{
			return factionId;
		}
		
		try (Connection con = ConnectionFactory.getInstance().getConnection();
			PreparedStatement ps = con.prepareStatement("SELECT faction FROM characters WHERE charId=?"))
		{
			ps.setInt(1, id);
			try (ResultSet rset = ps.executeQuery())
			{
				if (rset.next())
				{
					factionId = rset.getInt(1);
					_playerFactions.put(id, factionId);
					return factionId;
				}
			}
		}
		catch (SQLException e)
		{
			_log.log(Level.WARNING, getClass().getSimpleName() + ": Could not check existing char id: " + e.getMessage(), e);
		}
		
		return 0; // not found
	}
	
	public final boolean isSameFaction(L2PcInstance player1, L2PcInstance player2)
	{
		// TODO: Maybe add support for multiple factions?
		// if (getFactionByCharId(player1.getId()) == getFactionByCharId(player2.getId()))
		// {
		// return true;
		// }
		if ((player1.isGood() && player2.isGood()) || (player1.isEvil() && player2.isEvil()))
		{
			return true;
		}
		return false;
	}
	
	public static FactionManager getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final FactionManager _instance = new FactionManager();
	}
}
