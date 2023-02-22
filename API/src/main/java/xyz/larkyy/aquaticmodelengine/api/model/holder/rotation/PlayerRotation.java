package xyz.larkyy.aquaticmodelengine.api.model.holder.rotation;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.larkyy.aquaticmodelengine.api.utils.RotationMathUtil;

public class PlayerRotation {

    private float bodyYaw = 0;
    private Location from;

    public PlayerRotation(Player player) {
        this.from = player.getLocation();
    }

    public void tickMovement(Location to) {
        float yaw = to.getYaw();
        double i = to.getX() - from.getX();
        double d = to.getZ() - from.getZ();
        float f = (float)(i * i + d * d);
        float g = this.bodyYaw;
        if (f > 0.0025000002F) {
            float l = (float) RotationMathUtil.atan2(d, i) * 57.295776F - 90.0F;
            float m = Math.abs(RotationMathUtil.wrapDegrees(yaw) - l);
            if (95.0F < m && m < 265.0F) {
                g = l - 180.0F;
            } else {
                g = l;
            }
        }

        this.turnBody(g, yaw);
        from = to;
    }

    public void turnBody(float bodyRotation, float yaw) {
        float f = RotationMathUtil.wrapDegrees(bodyRotation - bodyYaw);
        bodyYaw += f * 0.3F;
        float g = RotationMathUtil.wrapDegrees(yaw - bodyYaw);
        if (g < -75.0F) {
            g = -75.0F;
        }

        if (g >= 75.0F) {
            g = 75.0F;
        }

        this.bodyYaw = yaw - g;
        if (g * g > 2500.0F) {
            this.bodyYaw += g * 0.2F;
        }
    }


    public float getBodyYaw() {
        return bodyYaw;
    }



}
