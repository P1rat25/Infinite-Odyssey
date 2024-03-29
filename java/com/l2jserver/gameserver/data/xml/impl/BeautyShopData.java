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
package com.l2jserver.gameserver.data.xml.impl;

import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import com.l2jserver.gameserver.enums.Race;
import com.l2jserver.gameserver.enums.Sex;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.beautyshop.BeautyData;
import com.l2jserver.gameserver.model.beautyshop.BeautyItem;
import com.l2jserver.util.data.xml.IXmlReader;

/**
 * @author Sdw
 */
public final class BeautyShopData implements IXmlReader
{
	private final Map<Race, Map<Sex, BeautyData>> _beautyList = new HashMap<>();
	private final Map<Sex, BeautyData> _beautyData = new HashMap<>();
	
	protected BeautyShopData()
	{
		load();
	}
	
	@Override
	public synchronized void load()
	{
		_beautyList.clear();
		_beautyData.clear();
		parseDatapackFile("BeautyShop.xml");
	}
	
	@Override
	public void parseDocument(Document doc)
	{
		NamedNodeMap attrs;
		StatsSet set;
		Node att;
		Race race = null;
		Sex sex = null;
		
		for (Node n = doc.getFirstChild(); n != null; n = n.getNextSibling())
		{
			if ("list".equalsIgnoreCase(n.getNodeName()))
			{
				for (Node d = n.getFirstChild(); d != null; d = d.getNextSibling())
				{
					if ("race".equalsIgnoreCase(d.getNodeName()))
					{
						att = d.getAttributes().getNamedItem("type");
						if (att != null)
						{
							race = parseEnum(att, Race.class);
						}
						
						for (Node b = d.getFirstChild(); b != null; b = b.getNextSibling())
						{
							if ("sex".equalsIgnoreCase(b.getNodeName()))
							{
								att = b.getAttributes().getNamedItem("type");
								if (att != null)
								{
									sex = parseEnum(att, Sex.class);
								}
								
								BeautyData beautyData = new BeautyData();
								
								for (Node a = b.getFirstChild(); a != null; a = a.getNextSibling())
								{
									if ("hair".equalsIgnoreCase(a.getNodeName()))
									{
										attrs = a.getAttributes();
										set = new StatsSet();
										for (int i = 0; i < attrs.getLength(); i++)
										{
											att = attrs.item(i);
											set.set(att.getNodeName(), att.getNodeValue());
										}
										BeautyItem hair = new BeautyItem(set);
										
										for (Node g = a.getFirstChild(); g != null; g = g.getNextSibling())
										{
											if ("color".equalsIgnoreCase(g.getNodeName()))
											{
												attrs = g.getAttributes();
												set = new StatsSet();
												for (int i = 0; i < attrs.getLength(); i++)
												{
													att = attrs.item(i);
													set.set(att.getNodeName(), att.getNodeValue());
												}
												hair.addColor(set);
											}
										}
										beautyData.addHair(hair);
									}
									else if ("face".equalsIgnoreCase(a.getNodeName()))
									{
										attrs = a.getAttributes();
										set = new StatsSet();
										for (int i = 0; i < attrs.getLength(); i++)
										{
											att = attrs.item(i);
											set.set(att.getNodeName(), att.getNodeValue());
										}
										BeautyItem face = new BeautyItem(set);
										beautyData.addFace(face);
									}
								}
								
								_beautyData.put(sex, beautyData);
							}
						}
						_beautyList.put(race, _beautyData);
					}
				}
			}
		}
	}
	
	public boolean hasBeautyData(Race race, Sex sex)
	{
		return _beautyList.containsKey(race) && _beautyList.get(race).containsKey(sex);
	}
	
	public BeautyData getBeautyData(Race race, Sex sex)
	{
		if (_beautyList.containsKey(race))
		{
			return _beautyList.get(race).get(sex);
		}
		return null;
	}
	
	public static BeautyShopData getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final BeautyShopData _instance = new BeautyShopData();
	}
}
