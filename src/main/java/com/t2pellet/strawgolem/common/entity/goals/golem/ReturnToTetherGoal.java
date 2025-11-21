package com.t2pellet.strawgolem.common.entity.goals.golem;

import com.t2pellet.haybale.common.utils.VersionHelper;
import com.t2pellet.strawgolem.StrawgolemConfig;
import com.t2pellet.strawgolem.common.entity.StrawGolem;
import com.t2pellet.strawgolem.common.registry.StrawgolemSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;


public class ReturnToTetherGoal extends MoveToBlockGoal {

    private final StrawGolem golem;
    private final ServerLevel level;

    public ReturnToTetherGoal(StrawGolem golem) {
        super(golem, StrawgolemConfig.Behaviour.golemWalkSpeed.get(), 24);
        this.golem = golem;
        this.level = (ServerLevel) VersionHelper.getLevel(golem);
    }

    @Override
    protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
        return golem.getTether().get().equals(blockPos);
    }

    @Override
    public boolean canUse() {
        return !golem.getHeldItem().has() && findNearestBlock();
    }

    @Override
    public boolean canContinueToUse() {
        return !golem.getHeldItem().has() && isValidTarget(level, blockPos) && !isReachedTarget();
    }

    @Override
    protected boolean findNearestBlock() {
        blockPos = golem.getTether().get();
        return golem.getTether().isTooFar();
    }

    @Override
    public void tick() {
        super.tick();
        if (!level.isClientSide) {
            if (!golem.getLookControl().isLookingAtTarget()) {
                golem.getLookControl().setLookAt(Vec3.atCenterOf(blockPos));
            }
        }
    }

    @Override
    public void start() {
        super.start();
        golem.playSound(StrawgolemSounds.GOLEM_INTERESTED.get());
    }

    @Override
    public double acceptedDistance() {
        return golem.getRandom().nextInt(8);
    }
}
