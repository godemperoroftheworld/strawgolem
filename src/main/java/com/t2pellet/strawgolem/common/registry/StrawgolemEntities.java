package com.t2pellet.strawgolem.common.registry;

import com.t2pellet.haybale.common.registry.api.EntityEntryType;
import com.t2pellet.haybale.common.registry.api.RegistryClass;
import com.t2pellet.strawgolem.common.entity.StrawGolem;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

@RegistryClass.IRegistryClass(EntityType.class)
public class StrawgolemEntities implements RegistryClass {

    @IRegistryEntry
    public static final EntityEntryType<StrawGolem> STRAW_GOLEM = new EntityEntryType<>("strawgolem", StrawGolem::new, StrawGolem::createAttributes, MobCategory.CREATURE, 0.6F, 0.9F);

}
