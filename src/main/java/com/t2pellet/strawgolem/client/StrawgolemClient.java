package com.t2pellet.strawgolem.client;

import com.t2pellet.haybale.client.HaybaleModClient;
import com.t2pellet.haybale.common.registry.api.RegistryClass;
import com.t2pellet.strawgolem.client.registry.StrawgolemEntityRenderers;
import com.t2pellet.strawgolem.client.registry.StrawgolemParticleFactories;

public class StrawgolemClient extends HaybaleModClient {
    public static final StrawgolemClient INSTANCE = new StrawgolemClient();

    @Override
    public Class<? extends RegistryClass> entityRenderers() {
        return StrawgolemEntityRenderers.class;
    }

    @Override
    public Class<? extends RegistryClass> particleFactories() {
        return StrawgolemParticleFactories.class;
    }
}
