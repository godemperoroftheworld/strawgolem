package com.t2pellet.strawgolem.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.t2pellet.strawgolem.StrawgolemConfig;
import com.t2pellet.strawgolem.client.model.StrawgolemGeoModel;
import com.t2pellet.strawgolem.client.renderer.layers.StrawgolemItemLayer;
import com.t2pellet.strawgolem.common.entity.StrawGolem;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
//? if < 1.19.3 {
/*import org.jetbrains.annotations.Nullable;
import net.minecraft.client.renderer.RenderType;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.geo.render.built.GeoModel;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;
import com.mojang.blaze3d.vertex.VertexConsumer;
*///?} else {
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
//?}

public class StrawGolemRenderer extends GeoEntityRenderer<StrawGolem> {

    public StrawGolemRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new StrawgolemGeoModel());
        //? if < 1.19.3 {
        /*this.addLayer(
        *///?} else
        this.addRenderLayer(
                new StrawgolemItemLayer(
                        this,
                        getModelProvider(),
                        renderManager.getItemInHandRenderer()
                )
        );
    }

    private StrawgolemGeoModel getModelProvider() {
        //? if < 1.19.3 {
        /*return (StrawgolemGeoModel) modelProvider;
        *///?} else
        return (StrawgolemGeoModel) getGeoModel();
    }

    //? if < 1.19.3 {
    /*private void hideBone(String name, boolean hidden) {
        GeoBone bone = (GeoBone) getModelProvider().getBone(name);
        bone.setHidden(hidden);
    }
    *///?} else {
    private void hideBone(String name, boolean hidden) {
        getModelProvider().getBone(name).ifPresent(geoBone -> {
            geoBone.setHidden(hidden);
        });
    }
    //?}

    @Override
    //? if >= 1.19.3 {
    public void render(StrawGolem animatable, float entityYaw, float partialTick, @NotNull PoseStack poseStack, @NotNull MultiBufferSource bufferSource, int packedLight) {
    //?} else
    /*public void render(GeoModel model, StrawGolem animatable, float partialTick, RenderType type, PoseStack poseStack, @Nullable MultiBufferSource bufferSource, @Nullable VertexConsumer buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {*/
        // Set whether to render hat/barrel
        hideBone("hat", !animatable.hasHat());
        hideBone("barrel", !animatable.hasBarrel());
        // Shivering animation
        if (StrawgolemConfig.Visual.golemShiversWhenDecayingFast.get() && animatable.isInWaterOrRain()) {
            if (animatable.isInWater() || !animatable.hasHat()) {
                shiver(animatable, poseStack);
            }
        } else if (StrawgolemConfig.Visual.golemShiversWhenCold.get() && animatable.isInCold()) {
            shiver(animatable, poseStack);
        }
        //? if < 1.19.3 {
        /*super.render(model, animatable, partialTick, type, poseStack, bufferSource, buffer, packedLight, packedOverlay, red, green, blue, alpha);
        *///?} else
        super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private void shiver(StrawGolem animatable, PoseStack poseStack) {
        double offX = animatable.getRandom().nextDouble() / 32 - 1 / 64F;
        double offZ = animatable.getRandom().nextDouble() / 32 - 1 / 64F;
        poseStack.translate(offX, 0, offZ);
    }

}
