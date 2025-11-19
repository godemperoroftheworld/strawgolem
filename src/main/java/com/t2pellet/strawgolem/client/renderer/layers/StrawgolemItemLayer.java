package com.t2pellet.strawgolem.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.t2pellet.strawgolem.client.model.StrawgolemGeoModel;
import com.t2pellet.strawgolem.common.entity.StrawGolem;
import com.t2pellet.strawgolem.common.entity.capabilities.held_item.HeldItem;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
//? if < 1.19.3 {
/*import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
*///?} else {
import net.minecraft.world.item.ItemDisplayContext;
import org.joml.Quaternionf;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;
import software.bernie.geckolib.renderer.GeoRenderer;
import com.mojang.math.Axis;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.RenderType;
//?}

//? if < 1.19.3 {
/*public class StrawgolemItemLayer extends GeoLayerRenderer<StrawGolem> {
*///?} else
public class StrawgolemItemLayer extends GeoRenderLayer<StrawGolem> {

    private final ItemInHandRenderer itemInHandRenderer;
    private final StrawgolemGeoModel model;

    public StrawgolemItemLayer(
            //? if < 1.19.3 {
            /*IGeoRenderer<StrawGolem> entityRendererIn,
            *///?} else
            GeoRenderer<StrawGolem> entityRendererIn,
            StrawgolemGeoModel model,
            ItemInHandRenderer itemInHandRenderer
    ) {
        super(entityRendererIn);
        this.model = model;
        this.itemInHandRenderer = itemInHandRenderer;
    }

    @Override
    //? if < 1.19.3 {
    /*public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, StrawGolem golem, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
    *///?} else
    public void render(PoseStack matrixStackIn, StrawGolem golem, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferIn, VertexConsumer buffer, float partialTick, int packedLightIn, int packedOverlay) {
        HeldItem heldItem = golem.getHeldItem();
        if (heldItem.has()) {
            matrixStackIn.pushPose();
            this.model.translateToHand(matrixStackIn);
            this.renderItem(matrixStackIn, bufferIn, packedLightIn, golem);
            matrixStackIn.popPose();
        }
    }

    private void renderItem(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, StrawGolem golem) {
        HeldItem heldItem = golem.getHeldItem();
        boolean holdAboveHead = golem.shouldHoldAboveHead();
        boolean isBlock = golem.isHoldingBlock();
        matrixStackIn.pushPose();
        float rotationAmount = isBlock ? -180.0F : -90.0F;
        //? if < 1.19.3 {
        /*Quaternion quaternion = Vector3f.XP.rotationDegrees(rotationAmount);
        *///?} else
        Quaternionf quaternion = Axis.XP.rotationDegrees(rotationAmount);
        matrixStackIn.mulPose(quaternion);
        matrixStackIn.translate(0, holdAboveHead ? isBlock ? -0.3F : 0.0F : -0.45F, holdAboveHead ? isBlock ? 0.0F : 0.1F : -0.15F);
        matrixStackIn.scale(0.5F, 0.5F, 0.5F);
        this.itemInHandRenderer.renderItem(
                golem,
                heldItem.get(),
                //? if < 1.19.3 {
                /*ItemTransforms.TransformType.NONE,
                *///?} else
                ItemDisplayContext.NONE,
                false,
                matrixStackIn,
                bufferIn,
                packedLightIn
        );
        matrixStackIn.popPose();
    }
}
