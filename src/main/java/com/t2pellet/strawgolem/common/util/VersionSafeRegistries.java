package com.t2pellet.strawgolem.common.util;

import net.minecraft.core.DefaultedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
//? if >= 1.19.3 {
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
//?}

public class VersionSafeRegistries {
    private VersionSafeRegistries() {}

    public static DefaultedRegistry<Item> item() {
        //? if >= 1.19.3 {
        return BuiltInRegistries.ITEM;
        //?} else
        /*return Registry.ITEM;*/
    }

    public static ResourceKey<Registry<Item>> itemKey() {
        //? if >= 1.19.3 {
        return Registries.ITEM;
        //?} else
        /*return Registry.ITEM_REGISTRY;*/
    }

    public static DefaultedRegistry<Block> block() {
        //? if >= 1.19.3 {
        return BuiltInRegistries.BLOCK;
        //?} else
        /*return Registry.BLOCK;*/
    }

    public static ResourceKey<Registry<Block>> blockKey() {
        //? if >= 1.19.3 {
        return Registries.BLOCK;
        //?} else
        /*return Registry.BLOCK_REGISTRY;*/
    }
}
