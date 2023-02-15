package xyz.larkyy.aquaticmodelengine.util.math;

import org.bukkit.util.Vector;

public class TimelineUtil {

    /**
     *
     * @param lower - lower vector - lower point of curve
     * @param higher - higher vector - higher point of curve
     * @param d - percentage / progress
     */
    public static Vector lerp(Vector lower, Vector higher, double d) {
        if (lower.equals(higher)) {
            return lower;
        }
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

    // Vectors - points of our curve
    public static Vector lerp(Vector v1, Vector v2, double d1, double d2) {
        return new Vector(
                lerp(v1.getX(), v2.getX(), d1, d2),
                lerp(v1.getY(), v2.getY(), d1, d2),
                lerp(v1.getZ(), v2.getZ(), d1, d2));
    }

    // Vectors - points of our curve
    public static Vector smoothLerp(Vector v1, Vector v2, Vector v3, Vector v4, double d) {
        double d1 = 0.0D, d2 = 1.0D, d3 = 2.0D, d4 = 3.0D;
        d = (d3 - d2) * d + d2;
        Vector vector1 = lerp(v1, v2, (d2 - d) / (d2 - d1), (d - d1) / (d2 - d1));
        Vector vector2 = lerp(v2, v3, (d3 - d) / (d3 - d2), (d - d2) / (d3 - d2));
        Vector vector3 = lerp(v3, v4, (d4 - d) / (d4 - d3), (d - d3) / (d4 - d3));
        Vector vector4 = lerp(vector1, vector2, (d3 - d) / (d3 - d1), (d - d1) / (d3 - d1));
        Vector vector5 = lerp(vector2, vector3, (d4 - d) / (d4 - d2), (d - d2) / (d4 - d2));
        return lerp(vector4, vector5, (d3 - d) / (d3 - d2), (d - d2) / (d3 - d2));
    }
}
