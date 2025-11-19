package com.t2pellet.strawgolem.common.registry;

import com.t2pellet.haybale.common.registry.api.ParticleEntryType;
import com.t2pellet.haybale.common.registry.api.RegistryClass;
import net.minecraft.core.particles.SimpleParticleType;

@RegistryClass.IRegistryClass(SimpleParticleType.class)
public class StrawgolemParticles implements RegistryClass {

    @IRegistryEntry
    public static final ParticleEntryType FLY_PARTICLE = new ParticleEntryType("fly");


}
