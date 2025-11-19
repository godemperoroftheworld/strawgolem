//? if forge {
/*package com.t2pellet.strawgolem.forge;

import com.t2pellet.haybale.HaybaleMod;
import com.t2pellet.haybale.forge.HaybaleForgeMod;
import com.t2pellet.strawgolem.Constants;
import com.t2pellet.strawgolem.StrawgolemCommon;
import com.t2pellet.strawgolem.client.StrawgolemClient;
import com.t2pellet.strawgolem.common.events.ContainerClickHandler;
import com.t2pellet.strawgolem.common.events.CropGrowthHandler;
import com.t2pellet.strawgolem.common.registry.StrawgolemItems;
import com.t2pellet.strawgolem.common.util.container.ContainerUtil;
import com.t2pellet.strawgolem.fabric.events.CropGrowthEvent;
import net.minecraft.server.level.ServerLevel;

import java.util.function.Consumer;


@Mod(Constants.MOD_ID)
@HaybaleMod.IMod(Constants.MOD_ID)
public class StrawgolemForge extends HaybaleForgeMod {

    @Override
    protected TLibMod getCommonMod() {
        return StrawgolemCommon.INSTANCE;
    }

    @Override
    protected TLibModClient getClientMod() {
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
        MinecraftForge.EVENT_BUS.addListener((Consumer<CreativeModeTabEvent.BuildContents>) event -> {
            if (event.getTab() == CreativeModeTabs.TOOLS_AND_UTILITIES) {
                event.accept(StrawgolemItems.strawHat.get());
            }
        });
    }
}
*///?}
