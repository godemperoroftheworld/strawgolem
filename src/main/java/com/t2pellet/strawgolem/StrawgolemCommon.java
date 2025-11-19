package com.t2pellet.strawgolem;

import com.t2pellet.haybale.HaybaleMod;
import com.t2pellet.haybale.common.capability.api.registry.IModCapabilities;
import com.t2pellet.haybale.common.config.api.Config;
import com.t2pellet.haybale.common.registry.api.RegistryClass;
import com.t2pellet.strawgolem.common.entity.capabilities.StrawgolemCapabilities;
import com.t2pellet.strawgolem.common.registry.StrawgolemEntities;
import com.t2pellet.strawgolem.common.registry.StrawgolemItems;
import com.t2pellet.strawgolem.common.registry.StrawgolemParticles;
import com.t2pellet.strawgolem.common.registry.StrawgolemSounds;

import java.io.IOException;


public class StrawgolemCommon extends HaybaleMod {
    public static final StrawgolemCommon INSTANCE = new StrawgolemCommon();

    @Override
    public Class<? extends RegistryClass> particles() {
        return StrawgolemParticles.class;
    }

    @Override
    public Class<? extends RegistryClass> entities() {
        return StrawgolemEntities.class;
    }

    @Override
    public Class<? extends RegistryClass> items() {
        return StrawgolemItems.class;
    }

    @Override
    public Class<? extends RegistryClass> sounds() {
        return StrawgolemSounds.class;
    }

    @Override
    public IModCapabilities capabilities() {
        return new StrawgolemCapabilities();
    }

    @Override
    public Config config() throws IOException, IllegalAccessException {
        return new StrawgolemConfig();
    }
}