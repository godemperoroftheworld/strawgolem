package com.t2pellet.strawgolem.common.entity.capabilities.deliverer;

import com.t2pellet.haybale.common.capability.api.Capability;
import com.t2pellet.haybale.common.capability.api.ICapabilityHaver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public interface Deliverer extends Capability  {

    static <E extends Entity & ICapabilityHaver> Deliverer getInstance(E entity) {
        return new DelivererImpl<>((LivingEntity & ICapabilityHaver) entity);
    }

    BlockPos getDeliverPos();
    void setPriorityPos(BlockPos pos);
    void deliver(BlockPos pos);

}
