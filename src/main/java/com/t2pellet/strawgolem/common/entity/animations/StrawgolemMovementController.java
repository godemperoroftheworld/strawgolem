package com.t2pellet.strawgolem.common.entity.animations;

import com.t2pellet.strawgolem.common.entity.StrawGolem;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;

public class StrawgolemMovementController extends StrawgolemAnimationController {

    public static final AnimationBuilder LEGS_RUN_ANIM = new AnimationBuilder().addAnimation("legs_run");
    public static final AnimationBuilder LEGS_WALK_ANIM = new AnimationBuilder().addAnimation("legs_walk");
    public static final AnimationBuilder LEGS_IDLE_ANIM = new AnimationBuilder().addAnimation("legs_idle");

    private static final IAnimationPredicate<StrawGolem> PREDICATE = event -> {
        StrawGolem golem = event.getAnimatable();
        if (golem.isPickingUpBlock() || golem.isPickingUpItem()) return PlayState.STOP;

        AnimationController<StrawGolem> controller = event.getController();
        if (golem.isRunning()) controller.setAnimation(LEGS_RUN_ANIM);
        else if (golem.isMoving()) controller.setAnimation(LEGS_WALK_ANIM);
        else controller.setAnimation(LEGS_IDLE_ANIM);

        return PlayState.CONTINUE;
    };

    public StrawgolemMovementController(StrawGolem animatable) {
        super(animatable, "move_controller", PREDICATE);
    }

}
