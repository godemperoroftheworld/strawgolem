package com.t2pellet.strawgolem.common.entity.capabilities.harvester;

import com.t2pellet.tlib.entity.capability.api.Capability;
import com.t2pellet.tlib.entity.capability.api.ICapabilityHaver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;

import java.util.Optional;

public interface Harvester extends Capability {

    static <E extends Entity & ICapabilityHaver> Harvester getInstance(E entity) {
        return new HarvesterImpl<>(entity);
    }

    void queueHarvest(BlockPos pos);
    Optional<BlockPos> startHarvest();
    void completeHarvest();
    void clearQueue();
    void clearHarvest();
    boolean isHarvesting();
    boolean isHarvestingBlock();
    Optional<BlockPos> getHarvesting();
    void findHarvestables();

}
