package com.t2pellet.strawgolem.common.entity.capabilities.held_item;

import com.t2pellet.haybale.common.capability.api.AbstractCapability;
import com.t2pellet.haybale.common.capability.api.ICapabilityHaver;
import com.t2pellet.haybale.common.utils.VersionHelper;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

class HeldItemImpl<E extends Entity & ICapabilityHaver> extends AbstractCapability<E> implements HeldItem {

    private final SimpleContainer container = new SimpleContainer(1);

    protected HeldItemImpl(E e) {
        super(e);
    }

    @Override
    public void set(ItemStack stack) {
        container.setItem(0, stack);
        synchronize();
    }

    @Override
    public boolean has() {
        ItemStack result = get();
        return result != null && !result.isEmpty();
    }

    @Override
    public ItemStack get() {
        return container.getItem(0);
    }

    @Override
    public void drop() {
        ItemStack heldItem = get();
        set(ItemStack.EMPTY);
        ItemEntity itemEntity = new ItemEntity(getLevel(), entity.getX(), entity.getY() + 1, entity.getZ(), heldItem);
        getLevel().addFreshEntity(itemEntity);
    }

    @Override
    public Tag writeTag() {
        return container.createTag();
    }

    @Override
    public void readTag(Tag tag) {
        container.fromTag((ListTag) tag);
    }

    private Level getLevel() {
        return VersionHelper.getLevel(entity);
    }
}
