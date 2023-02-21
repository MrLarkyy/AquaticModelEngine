package xyz.larkyy.aquaticmodelengine.generator.java;

import xyz.larkyy.aquaticmodelengine.generator.blockbench.BBFaces;
import xyz.larkyy.aquaticmodelengine.generator.blockbench.BBResolution;

public class JavaFace {

    private final double[] uv;
    private final int rotation;
    private final String texture;
    private final int tintindex = 0;

    public JavaFace(BBFaces.BBFace face, BBResolution resolution) {
        this.uv = new double[4];
        var ratio = ((float)resolution.getWidth()/16);
        uv[0] = face.getUv()[0]/ratio;
        uv[1] = face.getUv()[1]/ratio;
        uv[2] = face.getUv()[2]/ratio;
        uv[3] = face.getUv()[3]/ratio;
        this.rotation = face.getRotation();
        var texture = face.getTexture();
        if (texture == null) {
            this.texture = "#missing";
        } else {
            this.texture = "#"+texture;
        }
    }

}
