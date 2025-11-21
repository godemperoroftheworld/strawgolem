package com.t2pellet.strawgolem.common.block;

import net.minecraft.world.level.block.AttachedStemBlock;
import net.minecraft.world.level.block.StemBlock;

public interface StemGrownBlock {

    public abstract StemBlock getStem();

    public abstract AttachedStemBlock getAttachedStem();
}
