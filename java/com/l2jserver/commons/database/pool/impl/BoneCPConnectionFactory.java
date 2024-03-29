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
package com.l2jserver.commons.database.pool.impl;

import java.util.logging.Logger;

import javax.sql.DataSource;

import com.l2jserver.commons.database.pool.AbstractConnectionFactory;
import com.l2jserver.commons.database.pool.IConnectionFactory;

/**
 * BoneCP Connection Factory implementation.<br>
 * <b>Note that this class is not public to prevent external initialization.</b><br>
 * <b>Access it through {@link ConnectionFactory} and proper configuration.</b>
 * @author Zoey76
 */
final class BoneCPConnectionFactory extends AbstractConnectionFactory
{
	private static final Logger LOG = Logger.getLogger(BoneCPConnectionFactory.class.getName());
	
	private final DataSource _dataSource = null;
	
	public BoneCPConnectionFactory()
	{
		LOG.severe("BoneCP is not supported yet, nothing is going to work!");
	}
	
	@Override
	public void close()
	{
		throw new UnsupportedOperationException("BoneCP is not supported yet!");
	}
	
	@Override
	public DataSource getDataSource()
	{
		return _dataSource;
	}
	
	public static IConnectionFactory getInstance()
	{
		return SingletonHolder.INSTANCE;
	}
	
	private static class SingletonHolder
	{
		protected static final IConnectionFactory INSTANCE = new BoneCPConnectionFactory();
	}
}
