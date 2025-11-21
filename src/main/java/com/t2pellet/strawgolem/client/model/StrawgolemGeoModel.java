package com.t2pellet.strawgolem.client.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.t2pellet.strawgolem.Constants;
import com.t2pellet.strawgolem.StrawgolemConfig;
import com.t2pellet.strawgolem.common.entity.StrawGolem;
import com.t2pellet.strawgolem.common.entity.capabilities.decay.DecayState;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import net.minecraft.world.entity.HumanoidArm;
//? if < 1.19.3 {
/*import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;
import software.bernie.geckolib3.util.RenderUtils;
*///?} else {
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;
import software.bernie.geckolib.util.RenderUtils;
//?}

//? if < 1.19.3 {
/*public class StrawgolemGeoModel extends AnimatedGeoModel<StrawGolem> implements ArmedModel {
*///?} else
public class StrawgolemGeoModel extends GeoModel<StrawGolem> implements ArmedModel {

    private static final ResourceLocation modelResource = new ResourceLocation(Constants.MOD_ID, "geo/strawgolem.geo.json");
    private static final ResourceLocation animationResource = new ResourceLocation(Constants.MOD_ID, "animations/strawgolem.animation.json");

    // Textures
    private static final ResourceLocation newTextureResource = new ResourceLocation(Constants.MOD_ID, "textures/straw_golem.png");
    private static final ResourceLocation oldTextureResource = new ResourceLocation(Constants.MOD_ID, "textures/straw_golem_old.png");
    private static final ResourceLocation dyingTextureResource = new ResourceLocation(Constants.MOD_ID, "textures/straw_golem_dying.png");

    @Override
    public ResourceLocation getModelResource(StrawGolem golem) {
        return modelResource;
    }

    @Override
    public ResourceLocation getTextureResource(StrawGolem golem) {
        if (!StrawgolemConfig.Visual.golemDecayingTexture.get()) return newTextureResource;

        DecayState state = golem.getDecay().getState();
        switch (state) {
            case NEW -> {
                return newTextureResource;
            }
            case OLD -> {
                return oldTextureResource;
            }
            default -> {
                return dyingTextureResource;
            }
        }
    }



    @Override
    public ResourceLocation getAnimationResource(StrawGolem golem) {
        return animationResource;
    }

    @Override
    //? if >= 1.19.3 {
    public void setCustomAnimations(StrawGolem animatable, long instanceId, AnimationState<StrawGolem> animationEvent) {
    //?} else
    /*public void setCustomAnimations(StrawGolem animatable, int instanceId, AnimationEvent animationEvent) {*/
        super.setCustomAnimations(animatable, instanceId, animationEvent);
        var head = this.getAnimationProcessor().getBone("head");

        //? if < 1.19.3 {
        /*EntityModelData extraData = (EntityModelData) animationEvent.getExtraDataOfType(EntityModelData.class).get(0);
        if (head != null) {
            head.setRotationX(extraData.headPitch * (float) Math.PI / 180);
            head.setRotationY(extraData.netHeadYaw * (float) Math.PI / 180);
        }
        *///?} else {
        EntityModelData extraData = animationEvent.getData(DataTickets.ENTITY_MODEL_DATA);
        if (head != null) {
            head.setRotX(extraData.headPitch() * (float) Math.PI / 180);
            head.setRotY(extraData.netHeadYaw() * (float) Math.PI / 180);
        }
        //?}
    }

    @Override
    public void translateToHand(@NotNull HumanoidArm _ignore, @NotNull PoseStack poseStack) {
        //? if < 1.19.3 {
        /*var arms = (GeoBone) getBone("arms");
        var upper = (GeoBone) getBone("upper");
        *///?} else {
        var arms = getBone("arms").orElseThrow();
        var upper = getBone("upper").orElseThrow();
        //?}
        RenderUtils.prepMatrixForBone(poseStack, upper);
        RenderUtils.translateAndRotateMatrixForBone(poseStack, upper);
        RenderUtils.prepMatrixForBone(poseStack, arms);
        RenderUtils.translateAndRotateMatrixForBone(poseStack, arms);
    }
}
