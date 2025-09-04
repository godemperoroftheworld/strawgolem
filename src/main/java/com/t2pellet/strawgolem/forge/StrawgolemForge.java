package com.t2pellet.strawgolem.forge;

import com.t2pellet.strawgolem.Constants;
import com.t2pellet.strawgolem.StrawgolemCommon;
import com.t2pellet.strawgolem.client.StrawgolemClient;
import com.t2pellet.strawgolem.common.events.ContainerClickHandler;
import com.t2pellet.strawgolem.common.events.CropGrowthHandler;
import com.t2pellet.strawgolem.common.registry.StrawgolemItems;
import com.t2pellet.strawgolem.common.util.container.ContainerUtil;
import com.t2pellet.strawgolem.fabric.events.CropGrowthEvent;
import com.t2pellet.tlib.TLibForgeMod;
import com.t2pellet.tlib.TLibMod;
import com.t2pellet.tlib.client.TLibModClient;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.CreativeModeTabEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Consumer;


@Mod(Constants.MOD_ID)
@TLibMod.IMod(Constants.MOD_ID)
public class StrawgolemForge extends TLibForgeMod {

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