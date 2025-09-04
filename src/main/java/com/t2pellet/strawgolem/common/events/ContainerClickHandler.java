package com.t2pellet.strawgolem.common.events;

import com.t2pellet.strawgolem.common.entity.StrawGolem;
import com.t2pellet.strawgolem.common.entity.StrawGolemOrderer;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class ContainerClickHandler {

    private static final Component ORDERED_MESSAGE = Component.translatable("strawgolem.ordering.complete");

    private ContainerClickHandler() {}

    public static InteractionResult onContainerClicked(Player player, BlockPos pos) {
        if (!player.isCrouching() || !player.getMainHandItem().isEmpty()) return InteractionResult.PASS;
        StrawGolemOrderer orderer = (StrawGolemOrderer) (Object) player;
        Optional<StrawGolem> golemOptional = orderer.getOrderedGolem();
        if (golemOptional.isPresent()) {
            StrawGolem strawGolem = golemOptional.get();
            strawGolem.getDeliverer().setPriorityPos(pos);
            player.displayClientMessage(ORDERED_MESSAGE, true);
            orderer.setOrderedGolem(null);
            return InteractionResult.SUCCESS;
        }
        orderer.setOrderedGolem(null);
        return InteractionResult.FAIL;
    }
}
