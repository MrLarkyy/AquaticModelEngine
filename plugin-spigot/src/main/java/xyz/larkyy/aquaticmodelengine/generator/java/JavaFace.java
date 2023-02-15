package xyz.larkyy.aquaticmodelengine.generator.java;

import xyz.larkyy.aquaticmodelengine.generator.blockbench.BBFaces;

public class JavaFace {

    private final double[] uv;
    private final int rotation;
    private final String texture;
    private final int tintindex = 0;

    public JavaFace(BBFaces.BBFace face) {
        this.uv = new double[4];
        uv[0] = face.getUv()[0]/4;
        uv[1] = face.getUv()[1]/4;
        uv[2] = face.getUv()[2]/4;
        uv[3] = face.getUv()[3]/4;
        this.rotation = face.getRotation();
        var texture = face.getTexture();
        if (texture == null) {
            this.texture = "#missing";
        } else {
            this.texture = "#"+texture;
        }
    }

}
