package com.t2pellet.strawgolem.common.entity.goals.golem;

import com.t2pellet.haybale.common.utils.VersionHelper;
import com.t2pellet.strawgolem.common.entity.StrawGolem;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class GolemBeShyGoal extends GolemFleeEntityGoal<Player> {

    public GolemBeShyGoal(StrawGolem golem) {
        super(golem, Player.class, 1.0F, false);
    }

    @Override
    public boolean canUse() {
        this.toAvoid = VersionHelper.getLevel(this.mob).getNearestPlayer(this.mob, this.maxDist);
        if (this.toAvoid == null) {
            return false;
        } else if (isTempting()) {
            return false;
        } else {
            Vec3 vec3 = DefaultRandomPos.getPosAway(this.mob, 16, 7, this.toAvoid.position());
            if (vec3 == null) {
                return false;
            } else if (this.toAvoid.distanceToSqr(vec3.x, vec3.y, vec3.z) < this.toAvoid.distanceToSqr(this.mob)) {
                return false;
            } else {
                this.path = this.pathNav.createPath(vec3.x, vec3.y, vec3.z, 0);
                return this.path != null;
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && !isTempting();
    }

    private boolean isTempting() {
        return toAvoid != null && toAvoid.isHolding(StrawGolem.REPAIR_ITEM);
    }
}
