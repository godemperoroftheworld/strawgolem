package com.t2pellet.strawgolem.neoforge;

import com.t2pellet.haybale.Haybale;
import com.t2pellet.haybale.HaybaleMod;
import com.t2pellet.haybale.client.HaybaleModClient;
import com.t2pellet.haybale.neoforge.HaybaleNeoforgeMod;
import com.t2pellet.strawgolem.StrawgolemCommon;
import com.t2pellet.strawgolem.client.StrawgolemClient;
import com.t2pellet.strawgolem.common.events.ContainerClickHandler;
import com.t2pellet.strawgolem.common.events.CropGrowthHandler;
import com.t2pellet.strawgolem.common.util.container.ContainerUtil;
import net.minecraft.server.level.ServerLevel;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

import java.util.function.Consumer;

@Mod(Haybale.MODID)
@HaybaleMod.IMod(Haybale.MODID)
public class StrawgolemNeoforge extends HaybaleNeoforgeMod {

    private static StrawgolemNeoforge instance;

    public static StrawgolemNeoforge getInstance() {
        return instance;
    }

    public StrawgolemNeoforge(IEventBus modBus) {
        super(modBus);
    }

    @Override
    protected HaybaleMod getCommonMod() {
        return StrawgolemCommon.INSTANCE;
    }

    @Override
    protected HaybaleModClient getClientMod() {
        return StrawgolemClient.INSTANCE;
    }

    @Override
    protected void registerEvents(IEventBus modBus) {
        NeoForge.EVENT_BUS.addListener((Consumer<BlockEvent.CropGrowEvent>) event -> {
            if (event.getLevel() instanceof ServerLevel serverLevel) {
                CropGrowthHandler.onCropGrowth(serverLevel, event.getPos());
            }
        });
        NeoForge.EVENT_BUS.addListener((Consumer<PlayerInteractEvent.RightClickBlock>) event -> {
            if (ContainerUtil.isContainer(event.getLevel(), event.getPos())) {
                ContainerClickHandler.onContainerClicked(event.getEntity(), event.getPos());
            }
        });
        super.registerEvents(modBus);
    }
}
