package com.t2pellet.strawgolem.common.entity.goals.golem;

import com.t2pellet.strawgolem.StrawgolemConfig;
import com.t2pellet.strawgolem.common.entity.StrawGolem;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;

public class GolemWanderGoal extends WaterAvoidingRandomStrollGoal {

    private final StrawGolem golem;

    public GolemWanderGoal(StrawGolem golem) {
        super(golem, StrawgolemConfig.Behaviour.golemWalkSpeed.get());
        this.golem = golem;
    }

    @Override
    public boolean canUse() {
        return !golem.getHeldItem().has() && !golem.getTether().isTooFar() && super.canUse();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && !golem.getTether().isTooFar();
    }
}
