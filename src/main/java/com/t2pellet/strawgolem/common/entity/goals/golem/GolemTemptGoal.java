package com.t2pellet.strawgolem.common.entity.goals.golem;

import com.t2pellet.strawgolem.StrawgolemConfig;
import com.t2pellet.strawgolem.common.entity.StrawGolem;
import net.minecraft.world.entity.ai.goal.TemptGoal;
import net.minecraft.world.item.crafting.Ingredient;

public class GolemTemptGoal extends TemptGoal {

    private final StrawGolem golem;

    public GolemTemptGoal(StrawGolem golem) {
        super(golem, StrawgolemConfig.Behaviour.golemWalkSpeed.get(), Ingredient.of(StrawGolem.REPAIR_ITEM), false);
        this.golem = golem;
    }

    @Override
    public boolean canUse() {
        return !golem.getHeldItem().has() && super.canUse();
    }
}
