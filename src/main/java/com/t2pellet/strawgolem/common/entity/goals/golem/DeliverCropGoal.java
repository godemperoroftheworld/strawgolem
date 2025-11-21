package com.t2pellet.strawgolem.common.entity.goals.golem;

import com.t2pellet.haybale.common.utils.VersionHelper;
import com.t2pellet.strawgolem.StrawgolemConfig;
import com.t2pellet.strawgolem.common.entity.StrawGolem;
import com.t2pellet.strawgolem.common.registry.StrawgolemSounds;
import com.t2pellet.strawgolem.common.util.container.ContainerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;


public class DeliverCropGoal extends MoveToBlockGoal {

    private final StrawGolem golem;
    private final ServerLevel level;

    public DeliverCropGoal(StrawGolem golem) {
        super(golem, StrawgolemConfig.Behaviour.golemWalkSpeed.get(), StrawgolemConfig.Harvesting.harvestRange.get());
        this.golem = golem;
        this.level = (ServerLevel) VersionHelper.getLevel(golem);
    }

    @Override
    protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
        return ContainerUtil.isContainer((LevelAccessor) levelReader, blockPos);
    }

    @Override
    public boolean canUse() {
        return golem.getHeldItem().has() && !golem.isPickingUpItem() && !golem.isPickingUpBlock() && findNearestBlock();
    }

    @Override
    public boolean canContinueToUse() {
        return golem.getHeldItem().has() && isValidTarget(level, blockPos);
    }

    @Override
    public void tick() {
        BlockPos blockpos = this.getMoveToTarget();
        if (blockPos.closerToCenterThan(this.mob.position(), this.acceptedDistance())) {
            golem.getNavigation().stop();
            golem.getDeliverer().deliver(blockPos);
        } else {
            if (this.shouldRecalculatePath()) {
                this.mob.getNavigation().moveTo((double)((float)blockpos.getX()) + 0.5D, (double)blockpos.getY() + 0.5D, (double)((float)blockpos.getZ()) + 0.5D, this.speedModifier);
            }
            if (!golem.getLookControl().isLookingAtTarget()) {
                golem.getLookControl().setLookAt(Vec3.atCenterOf(blockPos));
            }
        }
    }

    @Override
    public void start() {
        super.start();
        if (golem.isHoldingBlock()) golem.playSound(StrawgolemSounds.GOLEM_STRAINED.get());
        else golem.playSound(StrawgolemSounds.GOLEM_INTERESTED.get());
    }

    @Override
    public double acceptedDistance() {
        return 1.6D;
    }

    @Override
    protected boolean findNearestBlock() {
        BlockPos blockPos = golem.getDeliverer().getDeliverPos();
        if (isValidTarget(VersionHelper.getLevel(mob), blockPos)) {
            this.blockPos = blockPos;
            return true;
        }
        return false;
    }
}
