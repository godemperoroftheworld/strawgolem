package com.t2pellet.strawgolem.common.entity.animations;

import com.t2pellet.strawgolem.StrawgolemConfig;
import com.t2pellet.strawgolem.common.entity.StrawGolem;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;

public class StrawgolemHarvestController extends StrawgolemAnimationController {

    public static final AnimationBuilder HARVEST_BLOCK_ANIM = new AnimationBuilder().addAnimation("harvest_block");
    public static final AnimationBuilder HARVEST_ITEM_ANIM = new AnimationBuilder().addAnimation("harvest_item");

    private static PlayState predicate(AnimationEvent<StrawGolem> event) {
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
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    public StrawgolemHarvestController(StrawGolem animatable) {
        super(animatable, "harvest", StrawgolemHarvestController::predicate);
        registerCustomInstructionListener(event -> {
            if (event.instructions.equals("completeHarvest")) {
                animatable.setPickingUpBlock(false);
                animatable.setPickingUpItem(false);
            }
        });
    }
}
