/*
 * Copyright (C) 2004-2015 L2J DataPack
 * 
 * This file is part of L2J DataPack.
 * 
 * L2J DataPack is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * L2J DataPack is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package handlers.effecthandlers;

import java.util.StringTokenizer;

import com.l2jserver.gameserver.enums.ShotType;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.L2Character;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.conditions.Condition;
import com.l2jserver.gameserver.model.effects.AbstractEffect;
import com.l2jserver.gameserver.model.effects.L2EffectType;
import com.l2jserver.gameserver.model.skills.AbnormalType;
import com.l2jserver.gameserver.model.skills.BuffInfo;
import com.l2jserver.gameserver.model.skills.Skill;
import com.l2jserver.gameserver.model.stats.Formulas;
import com.l2jserver.gameserver.model.stats.Stats;

/**
 * Fatal Blow effect implementation.
 * @author Adry_85
 */
public final class FatalBlow extends AbstractEffect
{
	private final String _targetAbnormalType;
	private final double _skillAddPower;
	
	public FatalBlow(Condition attachCond, Condition applyCond, StatsSet set, StatsSet params)
	{
		super(attachCond, applyCond, set, params);
		
		_targetAbnormalType = params.getString("targetAbnormalType", "NULL");
		_skillAddPower = params.getDouble("skillAddPower", 1);
	}
	
	@Override
	public boolean calcSuccess(BuffInfo info)
	{
		return !Formulas.calcPhysicalSkillEvasion(info.getEffector(), info.getEffected(), info.getSkill()) && Formulas.calcBlowSuccess(info.getEffector(), info.getEffected(), info.getSkill());
	}
	
	@Override
	public L2EffectType getEffectType()
	{
		return L2EffectType.PHYSICAL_ATTACK;
	}
	
	@Override
	public boolean isInstant()
	{
		return true;
	}
	
	@Override
	public void onStart(BuffInfo info)
	{
		L2Character target = info.getEffected();
		L2Character activeChar = info.getEffector();
		Skill skill = info.getSkill();
		
		if (activeChar.isAlikeDead())
		{
			return;
		}
		
		boolean ss = skill.useSoulShot() && activeChar.isChargedShot(ShotType.SOULSHOTS);
		byte shld = Formulas.calcShldUse(activeChar, target, skill);
		double damage = Formulas.calcBlowDamage(activeChar, target, skill, shld, ss);
		
		if (_targetAbnormalType != "NULL")
		{
			StringTokenizer st = new StringTokenizer(_targetAbnormalType, ",");
			while (st.hasMoreTokens())
			{
				String abnormal = st.nextToken().trim();
				if (target.getEffectList().getBuffInfoByAbnormalType(AbnormalType.valueOf(abnormal)) != null)
				{
					damage *= _skillAddPower;
					break;
				}
			}
		}
		
		boolean crit = Formulas.calcCrit(activeChar, target, skill);
		if (crit)
		{
			damage *= 2;
		}
		
		// reduce damage if target has maxdamage buff
		double maxDamage = (target.getStat().calcStat(Stats.MAX_SKILL_DAMAGE, 0, null, null));
		if (maxDamage > 0)
		{
			damage = (int) maxDamage;
		}
		
		target.reduceCurrentHp(damage, activeChar, skill);
		target.notifyDamageReceived(damage, activeChar, skill, crit, false);
		
		// Manage attack or cast break of the target (calculating rate, sending message...)
		if (!target.isRaid() && Formulas.calcAtkBreak(target, damage))
		{
			target.breakAttack();
			target.breakCast();
		}
		
		if (activeChar.isPlayer())
		{
			L2PcInstance activePlayer = activeChar.getActingPlayer();
			activePlayer.sendDamageMessage(target, (int) damage, false, true, false);
		}
		
		// Check if damage should be reflected
		Formulas.calcDamageReflected(activeChar, target, skill, true);
	}
}