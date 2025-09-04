package com.t2pellet.strawgolem.common.entity.goals.golem;

import com.t2pellet.strawgolem.StrawgolemConfig;
import com.t2pellet.strawgolem.common.entity.StrawGolem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;

public class GolemFleeEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {

    private final StrawGolem golem;
    private final boolean panic;

    public GolemFleeEntityGoal(StrawGolem mob, Class<T> clazz, float distance, boolean panic) {
        super(mob, clazz, distance, StrawgolemConfig.Behaviour.golemWalkSpeed.get(), StrawgolemConfig.Behaviour.golemRunSpeed.get());
        this.golem = mob;
        this.panic = panic;
    }

    @Override
    public boolean canUse() {
        if (!super.canUse() || toAvoid == null) return false;

        Class<?> targetClass = toAvoid.getClass();
        if (Monster.class.isAssignableFrom(targetClass)) {
            return StrawgolemConfig.Behaviour.golemsRunFromMonsters.get();
        } else if (Raider.class.isAssignableFrom(targetClass)) {
            return StrawgolemConfig.Behaviour.golemsRunFromRaiders.get();
        } else if (Animal.class.isAssignableFrom(targetClass)) {
            return StrawgolemConfig.Behaviour.golemsRunFromLivestock.get();
        } else if (Player.class.isAssignableFrom(targetClass)) {
            return StrawgolemConfig.Behaviour.golemsRunFromPlayers.get() && !toAvoid.isHolding(StrawGolem.REPAIR_ITEM);
        }
        return true;
    }

    @Override
    public void start() {
        super.start();
        if (panic) {
            golem.getHeldItem().drop();
            golem.setScared(true);
        }
    }

    @Override
    public void stop() {
        super.stop();
        golem.setScared(false);
    }
}
