package com.t2pellet.strawgolem.common.entity.capabilities.deliverer;

import com.t2pellet.haybale.common.capability.api.AbstractCapability;
import com.t2pellet.haybale.common.capability.api.ICapabilityHaver;
import com.t2pellet.haybale.common.utils.VersionHelper;
import com.t2pellet.strawgolem.common.util.VisibilityUtil;
import com.t2pellet.strawgolem.common.util.container.ContainerUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.*;

public class DelivererImpl<E extends LivingEntity & ICapabilityHaver> extends AbstractCapability<E> implements Deliverer {

    private final Set<BlockPos> containerSet = new HashSet<>();
    private BlockPos priorityContainer;
    private ResourceLocation level;

    protected DelivererImpl(E e) {
        super(e);
        level = getLevel().dimension().location();
    }

    @Override
    public BlockPos getDeliverPos() {
        // Clear memory if we change dimensions
        if (!getLevel().dimension().location().equals(level)) {
            clearData();
        }
        Optional<BlockPos> cachedPos = closestRememberedValidDeliverable();
        return cachedPos.orElseGet(() -> scanForDeliverable(entity.blockPosition()));
    }

    @Override
    public void setPriorityPos(BlockPos pos) {
        if (!getLevel().dimension().location().equals(level)) {
            clearData();
        }
        if (ContainerUtil.isContainer(getLevel(), pos)) {
            if (!containerSet.contains(pos)) {
                containerSet.add(pos);
            }
            priorityContainer = pos;
        }
    }

    private void clearData() {
        containerSet.clear();
        priorityContainer = null;
        level = getLevel().dimension().location();
    }

    private Optional<BlockPos> closestRememberedValidDeliverable() {
        if (priorityContainer != null && canDeliverToPos(getLevel(), priorityContainer)) {
            return Optional.of(priorityContainer);
        }
        return containerSet.stream()
                .filter(p -> canDeliverToPos(getLevel(), p))
                .min(Comparator.comparingDouble(p -> p.distManhattan(entity.blockPosition())));
    }

    private boolean canDeliverToPos(LevelAccessor level, BlockPos pos) {
        ItemStack deliveringStack = entity.getMainHandItem();
        return VisibilityUtil.canSee(entity, pos) && ContainerUtil.isContainer(level, pos) && !ContainerUtil.findSlotsInContainer(level, pos, deliveringStack).isEmpty();
    }

    private BlockPos scanForDeliverable(BlockPos query) {
        for (int x = -24; x <= 24; ++x) {
            for (int y = -12; y <= 12; ++y) {
                for (int z = -24; z <= 24; ++z) {
                    BlockPos pos = query.offset(x, y, z);
                    if (ContainerUtil.isContainer(getLevel(), pos) && VisibilityUtil.canSee(entity, pos)) {
                        containerSet.add(pos);
                        return pos;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public void deliver(BlockPos pos) {
        ItemStack stack = entity.getMainHandItem().copy();
        // Need an item to deliver
        if (!stack.isEmpty()) {
            // Deliver what we can to the container if it exists
            if (ContainerUtil.isContainer(getLevel(), pos)) {
                List<Integer> slots = ContainerUtil.findSlotsInContainer(getLevel(), pos, stack);
                if (!slots.isEmpty()) {
                    ContainerUtil.addToContainer(getLevel(), pos, stack, slots);
                }
                // Drop remaining items
                getLevel().addFreshEntity(new ItemEntity(getLevel(), pos.getX(), pos.getY() + 1, pos.getZ(), stack));
                entity.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                // Interactions
                getLevel().gameEvent(entity, GameEvent.CONTAINER_OPEN, pos);
                getLevel().playSound(null, pos, SoundEvents.CHEST_CLOSE, SoundSource.BLOCKS, 1.0F, 1.0F);
                getLevel().gameEvent(entity, GameEvent.CONTAINER_CLOSE, pos);
            }
        }
    }

    @Override
    public Tag writeTag() {
        CompoundTag deliverTag = new CompoundTag();
        if (priorityContainer != null) {
            deliverTag.put("priority", NbtUtils.writeBlockPos(priorityContainer));
        }
        ListTag positionsTag = new ListTag();
        for (BlockPos pos : containerSet) {
            if (ContainerUtil.isContainer(getLevel(), pos)) {
                positionsTag.add(NbtUtils.writeBlockPos(pos));
            }
        }
        deliverTag.put("positions", positionsTag);
        return deliverTag;
    }

    @Override
    public void readTag(Tag tag) {
        if (tag instanceof CompoundTag deliverTag) {
            CompoundTag priority = deliverTag.getCompound("priority");
            if (!priority.isEmpty()) {
                priorityContainer = NbtUtils.readBlockPos(priority);
            }
            ListTag positions = deliverTag.getList("positions", Tag.TAG_COMPOUND);
            readPositions(positions);
        } else {
            readPositions(tag);
        }
    }

    private void readPositions(Tag tag) {
        ListTag positions = (ListTag) tag;
        containerSet.clear();
        for (Tag position : positions) {
            BlockPos pos = NbtUtils.readBlockPos((CompoundTag) position);
            if (ContainerUtil.isContainer(getLevel(), pos)) {
                containerSet.add(pos);
            }
        }
    }

    private Level getLevel() {
        return VersionHelper.getLevel(entity);
    }
}
