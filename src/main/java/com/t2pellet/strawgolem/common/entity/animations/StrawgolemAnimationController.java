package com.t2pellet.strawgolem.common.entity.animations;

import com.t2pellet.strawgolem.common.entity.StrawGolem;
import com.t2pellet.strawgolem.common.util.VersionSafeGeckolib;
import org.jetbrains.annotations.NotNull;
//? if < 1.19.3 {
/*import software.bernie.geckolib3.core.builder.RawAnimation;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
*///?} else {
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
//?}

public class StrawgolemAnimationController extends AnimationController<StrawGolem> {

    public StrawgolemAnimationController(
            StrawGolem animatable,
            String name,
            //? if >= 1.19.3 {
            AnimationStateHandler<StrawGolem> animationPredicate
            //?} else
            /*IAnimationPredicate<StrawGolem> animationPredicate*/
    ) {
        super(animatable, name, 4, animationPredicate);
    }

    protected void setAnimation(@NotNull String animation) {
        //? if > 1.19.3 {
        var loopTypeLoop = Animation.LoopType.LOOP;
        //?} else {
        /*var loopTypeLoop = ILoopType.EDefaultLoopTypes.LOOP;
        *///?}
        setAnimation(animation, loopTypeLoop);
    }

    protected void setAnimation(
            @NotNull String animation,
            //? if < 1.19.3 {
            /*ILoopType loopType
            *///?} else
            Animation.LoopType loopType
    ) {
        var current = getCurrentAnimation();
        if (current == null) return;
        //? if < 1.19.3 {
        /*String animationName = current.animationName;
        *///?} else
        String animationName = current.animation().name();
        if (!animationName.equals(animation)) {
            RawAnimation rawAnimation = VersionSafeGeckolib.createAnimation(animation, loopType);
            setAnimation(rawAnimation);
        }
    }
}
