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
package handlers.effecthandlers;

import com.l2jserver.gameserver.datatables.SkillData;
import com.l2jserver.gameserver.model.PcCondOverride;
import com.l2jserver.gameserver.model.StatsSet;
import com.l2jserver.gameserver.model.actor.instance.L2PcInstance;
import com.l2jserver.gameserver.model.conditions.Condition;
import com.l2jserver.gameserver.model.effects.AbstractEffect;
import com.l2jserver.gameserver.model.skills.BuffInfo;
import com.l2jserver.gameserver.model.skills.Skill;

/**
 * @author NviX
 */
public final class Duel extends AbstractEffect
{
	
	public Duel(Condition attachCond, Condition applyCond, StatsSet set, StatsSet params)
	{
		super(attachCond, applyCond, set, params);
	}
	
	@Override
	public void onStart(BuffInfo info)
	{
		L2PcInstance effector = info.getEffector().getActingPlayer();
		L2PcInstance effected = info.getEffected().getActingPlayer();
		
		if (effected.isDead() || (effector == null) || (effected.getPvpFlag() == 0))
		{
			return;
		}
		
		Skill skill = SkillData.getInstance().getSkill(10319, 1);
		skill.applyEffects(effector, effector);
		
		effector.setIsInvul(true);
		effected.addOverrideCond(PcCondOverride.VULNERABLE_ALL_PLAYERS);
		effected.setTarget(effector);
		effected.setLockedTarget(effector);
	}
	
	@Override
	public void onExit(BuffInfo info)
	{
		info.getEffected().getActingPlayer().setLockedTarget(null);
		info.getEffected().removeOverridedCond(PcCondOverride.VULNERABLE_ALL_PLAYERS);
		info.getEffector().setIsInvul(false);
		info.getEffector().getEffectList().remove(true, info.getEffector().getEffectList().getBuffInfoBySkillId(10319));
	}
}
