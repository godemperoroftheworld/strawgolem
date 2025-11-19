//? if fabric {
/*package com.t2pellet.strawgolem.fabric;

import com.t2pellet.haybale.HaybaleMod;
import com.t2pellet.haybale.client.HaybaleModClient;
import com.t2pellet.haybale.fabric.HaybaleFabricMod;
import com.t2pellet.strawgolem.Constants;
import com.t2pellet.strawgolem.StrawgolemCommon;
import com.t2pellet.strawgolem.client.StrawgolemClient;
import com.t2pellet.strawgolem.common.events.ContainerClickHandler;
import com.t2pellet.strawgolem.common.events.CropGrowthHandler;
import com.t2pellet.strawgolem.common.util.container.ContainerUtil;
import com.t2pellet.strawgolem.fabric.events.CropGrowthCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.world.InteractionResult;

@HaybaleMod.IMod(Constants.MOD_ID)
public class StrawgolemFabric extends HaybaleFabricMod {

    @Override
    protected HaybaleMod getCommonMod() {
        return StrawgolemCommon.INSTANCE;
    }

    @Override
    protected HaybaleModClient getClientMod() {
        return StrawgolemClient.INSTANCE;
    }

    @Override
    protected void registerEvents() {
        CropGrowthCallback.EVENT.register(CropGrowthHandler::onCropGrowth);
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (ContainerUtil.isContainer(world, hitResult.getBlockPos())) {
                return ContainerClickHandler.onContainerClicked(player, hitResult.getBlockPos());
            }
            return InteractionResult.PASS;
        });
    }
}
*///?}