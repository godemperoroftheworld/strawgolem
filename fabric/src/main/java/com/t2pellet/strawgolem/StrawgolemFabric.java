package com.t2pellet.strawgolem;

import com.t2pellet.strawgolem.client.StrawgolemClient;
import com.t2pellet.strawgolem.events.ContainerClickHandler;
import com.t2pellet.strawgolem.events.CropGrowthCallback;
import com.t2pellet.strawgolem.events.CropGrowthHandler;
import com.t2pellet.strawgolem.registry.StrawgolemItems;
import com.t2pellet.strawgolem.util.container.ContainerUtil;
import com.t2pellet.tlib.TLibFabricMod;
import com.t2pellet.tlib.TLibMod;
import com.t2pellet.tlib.client.TLibModClient;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;


@TLibMod.IMod(Constants.MOD_ID)
public class StrawgolemFabric extends TLibFabricMod {

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
        CropGrowthCallback.EVENT.register(CropGrowthHandler::onCropGrowth);
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (ContainerUtil.isContainer(world, hitResult.getBlockPos())) {
                return ContainerClickHandler.onContainerClicked(player, hitResult.getBlockPos());
            }
            return InteractionResult.PASS;
        });
        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.TOOLS_AND_UTILITIES).register(entries -> {
            Collection<ItemStack> items = CreativeModeTabs.TOOLS_AND_UTILITIES.getDisplayItems();
            ItemStack item = (ItemStack) items.toArray()[items.size() - 1];
            entries.addAfter(item.getItem(), StrawgolemItems.strawHat.get());
        });
    }
}
