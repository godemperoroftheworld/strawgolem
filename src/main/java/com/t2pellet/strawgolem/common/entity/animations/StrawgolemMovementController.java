package com.t2pellet.strawgolem.common.entity.animations;

import com.t2pellet.strawgolem.common.entity.StrawGolem;
import com.t2pellet.strawgolem.common.util.VersionSafeGeckolib;
//? if < 1.19.3 {
/*import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.RawAnimation;
import software.bernie.geckolib3.core.controller.AnimationController;
*///?} else {
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
//?}

public class StrawgolemMovementController extends StrawgolemAnimationController {

    public static final RawAnimation LEGS_RUN_ANIM = VersionSafeGeckolib.createLoop("legs_run");
    public static final RawAnimation LEGS_WALK_ANIM = VersionSafeGeckolib.createLoop("legs_walk");
    public static final RawAnimation LEGS_IDLE_ANIM = VersionSafeGeckolib.createLoop("legs_idle");

    //? if < 1.19.3 {
    /*private static final AnimationController.IAnimationPredicate<StrawGolem> PREDICATE
    *///?} else
    private static final AnimationStateHandler<StrawGolem> PREDICATE
            = event -> {
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