package com.t2pellet.strawgolem.common.registry;

import com.t2pellet.haybale.common.registry.api.ItemEntryType;
import com.t2pellet.haybale.common.registry.api.RegistryClass;
import net.minecraft.world.item.Item;

@RegistryClass.IRegistryClass(Item.class)
public class StrawgolemItems implements RegistryClass {

    private static final Item.Properties strawHatProperties = new Item.Properties()
            .stacksTo(1);

    @IRegistryEntry
    public static final ItemEntryType strawHat = new ItemEntryType("straw_hat", strawHatProperties);
}
