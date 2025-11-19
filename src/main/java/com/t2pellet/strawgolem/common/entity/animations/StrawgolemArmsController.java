package com.t2pellet.strawgolem.common.entity.animations;


import com.t2pellet.strawgolem.common.entity.StrawGolem;
import com.t2pellet.strawgolem.common.util.VersionSafeGeckolib;
//? if >= 1.19.4 {
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
//?} else {
/*import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.RawAnimation;
import software.bernie.geckolib3.core.controller.AnimationController;
*///?}

public class StrawgolemArmsController extends StrawgolemAnimationController {

    public static final RawAnimation SCARED_ANIM = VersionSafeGeckolib.createLoop("arms_scared");
    public static final RawAnimation HOLDING_BLOCK_ANIM = VersionSafeGeckolib.createLoop("arms_hold_block");
    public static final RawAnimation HOLDING_ITEM_ANIM = VersionSafeGeckolib.createLoop("arms_hold_item");
    public static final RawAnimation RUN_ARMS_ANIM = VersionSafeGeckolib.createLoop("arms_run");
    public static final RawAnimation WALK_ARMS_ANIM = VersionSafeGeckolib.createLoop("arms_walk");
    public static final RawAnimation IDLE_ANIM = VersionSafeGeckolib.createLoop("arms_idle");

    //? if >= 1.19.3 {
    private static final AnimationStateHandler<StrawGolem> PREDICATE
    //?} else
    /*private static final AnimationController.IAnimationPredicate<StrawGolem> PREDICATE*/
            = event -> {
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