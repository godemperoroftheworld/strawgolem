package com.t2pellet.strawgolem.client.registry;

import com.t2pellet.haybale.client.registry.api.EntityRendererEntryType;
import com.t2pellet.haybale.common.registry.api.RegistryClass;
import com.t2pellet.strawgolem.client.renderer.StrawGolemRenderer;
import com.t2pellet.strawgolem.common.entity.StrawGolem;
import com.t2pellet.strawgolem.common.registry.StrawgolemEntities;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@RegistryClass.IRegistryClass(EntityRendererProvider.class)
public class StrawgolemEntityRenderers implements RegistryClass {

    @IRegistryEntry
    public static final EntityRendererEntryType<StrawGolem> STRAW_GOLEM_RENDERER = new EntityRendererEntryType<>(StrawgolemEntities.STRAW_GOLEM::get, StrawGolemRenderer::new);

}