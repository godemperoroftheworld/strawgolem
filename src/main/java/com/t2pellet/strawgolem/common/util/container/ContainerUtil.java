package com.t2pellet.strawgolem.common.util.container;

import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ContainerUtil {

    private ContainerUtil() {}

    public static boolean isContainer(@NotNull LevelAccessor level, BlockPos pos) {
        return pos != null && level.getBlockEntity(pos) instanceof Container;
    }

    public static List<Integer> findSlotsInContainer(@NotNull LevelAccessor level, @NotNull BlockPos pos, @Nullable ItemStack stack) {
        Container container = (Container) level.getBlockEntity(pos);
        Item itemToPut = stack != null ? stack.getItem() : null;
        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < container.getContainerSize(); ++i) {
            ItemStack item = container.getItem(i);
            if (itemToPut != null && item.is(itemToPut) && item.getMaxStackSize() > item.getCount()) {
                list.add(i);
            } else if (item.isEmpty()) {
                list.add(i);
            }
        }
        return list;
    }

    public static int addToContainer(@NotNull LevelAccessor level, @NotNull BlockPos pos, @NotNull ItemStack stack, List<Integer> slots) {
        Container container = (Container) level.getBlockEntity(pos);
        int oldStackSize = stack.getCount();
        for (int slot : slots) {
            if (stack.isEmpty()) break;
            ItemStack containerStack = container.getItem(slot);
            if (containerStack.isEmpty()) {
                container.setItem(slot, stack.copy());
                stack.setCount(0);
                return oldStackSize;
            } else {
                int canGrow = containerStack.getMaxStackSize() - containerStack.getCount();
                int toGrow = Math.min(stack.getCount(), canGrow);
                containerStack.grow(toGrow);
                stack.shrink(toGrow);
            }
        }
        return stack.getCount() - oldStackSize;
    }

}
