package com.t2pellet.strawgolem.common.util;

//? if > 1.19.3 {
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.RawAnimation;
//?} else {
/*import software.bernie.geckolib3.core.builder.RawAnimation;
import software.bernie.geckolib3.core.builder.ILoopType;
*///?}

public class VersionSafeGeckolib {
    private VersionSafeGeckolib() {
    }

    //? if < 1.19.3 {
    /*public static RawAnimation createLoop(String name) {
        return new RawAnimation().loop(name);
    }
    *///?} else {
    public static RawAnimation createLoop(String name) {
        return RawAnimation.begin().thenLoop(name);
    }
    //?}

    //? if < 1.19.3 {
    /*public static RawAnimation createAnimation(String name, ILoopType loopType) {
        return new RawAnimation().addAnimation(name, loopType);
    }
    *///?} else {
    public static RawAnimation createAnimation(String name, Animation.LoopType loopType) {
        return RawAnimation.begin().then(name, loopType);
    }
    //?}
}
