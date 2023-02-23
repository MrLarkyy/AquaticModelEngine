package xyz.larkyy.aquaticmodelengine.generator.java;

import org.bukkit.Bukkit;
import org.bukkit.util.EulerAngle;

public class JavaRotation {

    private final float angle;
    private final String axis;
    private final double[] origin;

    public JavaRotation(EulerAngle angle, double[] origin) {
        var x = Math.abs(angle.getX());
        var y = Math.abs(angle.getY());
        var z = Math.abs(angle.getZ());

        if (x > y) {
            if (x > z) {
                this.axis = "x";
                this.angle = getAngle(angle.getX());
            } else {
                this.axis = "z";
                this.angle = getAngle(angle.getZ());
            }
        } else if (z > y) {
            this.axis = "z";
            this.angle = getAngle(angle.getZ());
        } else {
            this.axis = "y";
            this.angle = getAngle(angle.getY());
        }
        this.origin = new double[3];
        this.origin[0] = origin[0]/2.5+8;
        this.origin[1] = origin[1]/2.5;
        this.origin[2] = origin[2]/2.5+8;
    }

    private float getAngle(double rads) {
        return Math.round((float)Math.toDegrees(rads)*10f)/10f;
    }

    public float getAngle() {
        return angle;
    }
}
