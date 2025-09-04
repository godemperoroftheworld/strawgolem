package com.t2pellet.strawgolem.common.entity.animations;

import com.t2pellet.strawgolem.common.entity.StrawGolem;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;

public class StrawgolemArmsController extends StrawgolemAnimationController {

    public static final AnimationBuilder SCARED_ANIM = new AnimationBuilder().addAnimation("arms_scared");
    public static final AnimationBuilder HOLDING_BLOCK_ANIM = new AnimationBuilder().addAnimation("arms_hold_block");
    public static final AnimationBuilder HOLDING_ITEM_ANIM = new AnimationBuilder().addAnimation("arms_hold_item");
    public static final AnimationBuilder RUN_ARMS_ANIM = new AnimationBuilder().addAnimation("arms_run");
    public static final AnimationBuilder WALK_ARMS_ANIM = new AnimationBuilder().addAnimation("arms_walk");
    public static final AnimationBuilder IDLE_ANIM = new AnimationBuilder().addAnimation("arms_idle");

    private static final IAnimationPredicate<StrawGolem> PREDICATE = event -> {
        StrawGolem golem = event.getAnimatable();
        if (golem.isPickingUpItem() || golem.isPickingUpBlock()) {
            return PlayState.STOP;
        }

        AnimationController<StrawGolem> controller = event.getController();
        if (golem.isScared()) controller.setAnimation(SCARED_ANIM);
        else if (golem.shouldHoldAboveHead()) controller.setAnimation(HOLDING_BLOCK_ANIM);
        else if (golem.getHeldItem().has()) controller.setAnimation(HOLDING_ITEM_ANIM);
        else if (golem.isRunning()) controller.setAnimation(RUN_ARMS_ANIM);
        else if (golem.isMoving()) controller.setAnimation(WALK_ARMS_ANIM);
        else controller.setAnimation(IDLE_ANIM);

        return PlayState.CONTINUE;
    };

    public StrawgolemArmsController(StrawGolem animatable) {
        super(animatable, "arms_controller", PREDICATE);
    }
}
