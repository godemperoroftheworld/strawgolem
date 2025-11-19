package com.t2pellet.strawgolem.common.entity.animations;

import com.t2pellet.strawgolem.StrawgolemConfig;
import com.t2pellet.strawgolem.common.entity.StrawGolem;
import com.t2pellet.strawgolem.common.util.VersionSafeGeckolib;
//? if < 1.19.3 {
/*import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.RawAnimation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
*///?} else {
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
//?}

public class StrawgolemHarvestController extends StrawgolemAnimationController {

    public static final RawAnimation HARVEST_BLOCK_ANIM = VersionSafeGeckolib.createLoop("harvest_block");
    public static final RawAnimation HARVEST_ITEM_ANIM = VersionSafeGeckolib.createLoop("harvest_item");

    //? if < 1.19.3 {
    /*private static PlayState predicate(AnimationEvent<StrawGolem> event) {
    *///?} else
    private static PlayState predicate(AnimationState<StrawGolem> event) {
        // Appropriate animation for regular crop or gourd crop
        if (event.getAnimatable().isPickingUpBlock()) {
            if (StrawgolemConfig.Visual.showHarvestBlockAnimation.get()) {
                event.getController().setAnimation(HARVEST_BLOCK_ANIM);
                return PlayState.CONTINUE;
            }
        } else if (event.getAnimatable().isPickingUpItem()) {
            if (StrawgolemConfig.Visual.showHarvestItemAnimation.get()) {
                event.getController().setAnimation(event.getAnimatable().hasBarrel() ? HARVEST_BLOCK_ANIM : HARVEST_ITEM_ANIM);
                return PlayState.CONTINUE;
            }
        }
        //? if < 1.19.3
        /*event.getController().markNeedsReload();*/
        return PlayState.STOP;
    }

    public StrawgolemHarvestController(StrawGolem animatable) {
        super(animatable, "harvest", StrawgolemHarvestController::predicate);
        //? if < 1.19.3 {
        /*registerCustomInstructionListener(event -> {
            if (event.instructions.equals("completeHarvest")) {
        *///?} else {
        setCustomInstructionKeyframeHandler(event -> {
            if (event.getKeyframeData().getInstructions().equals("completeHarvest")) {
        //?}
                animatable.setPickingUpBlock(false);
                animatable.setPickingUpItem(false);
            }
        });
    }
}
