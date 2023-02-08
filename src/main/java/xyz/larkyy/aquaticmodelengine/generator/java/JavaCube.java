package xyz.larkyy.aquaticmodelengine.generator.java;

import java.util.Map;

public class JavaCube {

    private final double[] from;
    private final double[] to;
    private final JavaRotation rotation;
    private final Map<String,JavaFace> faces;

    public JavaCube(double[] from, double[] to, JavaRotation rotation,Map<String,JavaFace> faces) {
        this.from = new double[3];
        this.from[0] = from[0]/2.5+8;
        this.from[1] = from[1]/2.5;
        this.from[2] = from[2]/2.5+8;
        this.to = new double[3];
        this.to[0] = to[0]/2.5+8;
        this.to[1] = to[1]/2.5;
        this.to[2] = to[2]/2.5+8;
        if (rotation.getAngle() == 0) {
            this.rotation = null;
        } else {
            this.rotation = rotation;
        }
        this.faces = faces;
    }

}
