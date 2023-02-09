package xyz.larkyy.aquaticmodelengine.util.math;

import org.bukkit.util.Vector;

public class TimelineUtil {

    public static Vector lerp(Vector lower, Vector higher, double d) {
        return new Vector(
                lerp(lower.getX(), higher.getX(), d),
                lerp(lower.getY(), higher.getY(), d),
                lerp(lower.getZ(), higher.getZ(), d)
        );
    }

    public static double lerp(double d1, double d2, double d3) {
        return (1.0D - d3) * d1 + d3 * d2;
    }

    public static double lerp(double d1, double d2, double d3, double d4) {
        return d3 * d1 + d4 * d2;
    }
}
