package com.t2pellet.strawgolem.common.util;

import com.t2pellet.haybale.common.utils.VersionHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

public class VisibilityUtil {

    private VisibilityUtil() {}

    public static boolean canSee(LivingEntity e, BlockPos query) {
        Level level = VersionHelper.getLevel(e);
        Vec3 entityPos = e.getEyePosition();
        Vec3 queryPos = Vec3.atCenterOf(query);
        ClipContext ctx = new ClipContext(entityPos, queryPos, ClipContext.Block.OUTLINE, ClipContext.Fluid.NONE, e);
        BlockHitResult result = level.clip(ctx);
        return result.getBlockPos().equals(query);
    }

}
