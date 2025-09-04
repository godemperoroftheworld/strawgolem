package com.t2pellet.strawgolem.common.entity.goals.golem;

import com.t2pellet.strawgolem.StrawgolemConfig;
import com.t2pellet.strawgolem.common.entity.StrawGolem;
import com.t2pellet.strawgolem.common.entity.capabilities.harvester.Harvester;
import com.t2pellet.strawgolem.common.registry.StrawgolemSounds;
import com.t2pellet.strawgolem.common.util.crop.CropUtil;
import com.t2pellet.tlib.Services;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MoveToBlockGoal;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;


public class HarvestCropGoal extends MoveToBlockGoal {

    private final StrawGolem golem;

    public HarvestCropGoal(StrawGolem golem) {
        super(golem, StrawgolemConfig.Behaviour.golemWalkSpeed.get(), StrawgolemConfig.Harvesting.harvestRange.get());
        this.golem = golem;
    }

    @Override
    protected boolean isValidTarget(LevelReader levelReader, BlockPos blockPos) {
        Optional<BlockPos> harvestPos = golem.getHarvester().getHarvesting();
        return harvestPos.isPresent() && harvestPos.get().equals(blockPos) && CropUtil.isGrownCrop((LevelAccessor) levelReader, blockPos);
    }

    @Override
    public boolean canUse() {
        if (golem.getHeldItem().has()) return false;
        Optional<BlockPos> harvestPos = golem.getHarvester().startHarvest();
        if (harvestPos.isPresent()) {
            blockPos = harvestPos.get();
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return !golem.getHeldItem().has() && this.isValidTarget(this.mob.level, this.blockPos);
    }

    @Override
    protected int nextStartTick(PathfinderMob $$0) {
        return reducedTickDelay(100 + $$0.getRandom().nextInt(100));
    }

    @Override
    public void tick() {
        BlockPos targetPos = this.getMoveToTarget();
        if (blockPos.closerToCenterThan(this.mob.position(), this.acceptedDistance())) {
            Harvester harvester = golem.getHarvester();
            golem.getNavigation().stop();
            if (harvester.isHarvestingBlock()) {
                golem.setPickingUpBlock(true);
            } else {
                golem.setPickingUpItem(true);
            }
            harvester.completeHarvest();
            Services.SIDE.scheduleServer(40, () -> {
                harvester.findHarvestables();
                golem.setPickingUpBlock(false);
                golem.setPickingUpItem(false);
            });
        } else {
            if (this.shouldRecalculatePath()) {
                this.mob.getNavigation().moveTo((double)((float)targetPos.getX()) + 0.5D, (double)targetPos.getY() + 0.5D, (double)((float)targetPos.getZ()) + 0.5D, this.speedModifier);
            }
            if (!golem.getLookControl().isLookingAtTarget()) {
                golem.getLookControl().setLookAt(Vec3.atCenterOf(blockPos));
            }
        }
    }

    @Override
    public void start() {
        super.start();
        golem.playSound(StrawgolemSounds.GOLEM_INTERESTED.get());
        // Update the tether to the crop we're harvesting
        golem.getTether().update(blockPos);
    }

    @Override
    public double acceptedDistance() {
        return 1.2D;
    }
}
