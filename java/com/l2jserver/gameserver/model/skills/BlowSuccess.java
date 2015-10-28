package com.l2jserver.gameserver.model.skills;

import java.util.HashMap;
import java.util.Map;

import com.l2jserver.gameserver.model.actor.L2Character;

public class BlowSuccess
{
	private static Map<String, Boolean> _success = new HashMap<>();
	
	public static BlowSuccess getInstance()
	{
		return SingletonHolder._instance;
	}
	
	private static class SingletonHolder
	{
		protected static final BlowSuccess _instance = new BlowSuccess();
	}
	
	public void remove(L2Character l2Character, Skill skill)
	{
		_success.remove(makeKey(l2Character, skill));
	}
	
	public boolean get(L2Character l2Character, Skill skill)
	{
		return _success.get(makeKey(l2Character, skill));
	}
	
	public void set(L2Character l2Character, Skill skill, boolean success)
	{
		_success.put(makeKey(l2Character, skill), success);
	}
	
	private String makeKey(L2Character l2Character, Skill skill)
	{
		return "" + l2Character.getObjectId() + ":" + skill.getId();
	}
}