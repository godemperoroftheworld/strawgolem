//? if forge {
package com.t2pellet.strawgolem.forge;

import com.t2pellet.haybale.HaybaleMod;
import com.t2pellet.haybale.client.HaybaleModClient;
import com.t2pellet.haybale.forge.HaybaleForgeMod;
import com.t2pellet.strawgolem.Constants;
import com.t2pellet.strawgolem.StrawgolemCommon;
import com.t2pellet.strawgolem.client.StrawgolemClient;
import com.t2pellet.strawgolem.common.events.ContainerClickHandler;
import com.t2pellet.strawgolem.common.events.CropGrowthHandler;
import com.t2pellet.strawgolem.common.util.container.ContainerUtil;
import com.t2pellet.strawgolem.fabric.events.CropGrowthEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;


@Mod(Constants.MOD_ID)
@HaybaleMod.IMod(Constants.MOD_ID)
public class StrawgolemForge extends HaybaleForgeMod {

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
        MinecraftForge.EVENT_BUS.addListener((Consumer<CropGrowthEvent>) event -> {
            if (event.getLevel() instanceof ServerLevel serverLevel) {
                CropGrowthHandler.onCropGrowth(serverLevel, event.getPos());
            }
        });
        MinecraftForge.EVENT_BUS.addListener((Consumer<PlayerInteractEvent.RightClickBlock>) event -> {
            if (ContainerUtil.isContainer(event.getLevel(), event.getPos())) {
                ContainerClickHandler.onContainerClicked(event.getEntity(), event.getPos());
            }
        });
    }
}
//?}
