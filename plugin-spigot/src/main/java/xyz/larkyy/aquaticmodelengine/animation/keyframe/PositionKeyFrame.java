package xyz.larkyy.aquaticmodelengine.animation.keyframe;

import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.animation.InterpolationType;
import xyz.larkyy.aquaticmodelengine.animation.KeyFrame;

public class PositionKeyFrame extends KeyFrame {

    private final Vector vector;

    public PositionKeyFrame(InterpolationType interpolationType, Vector vector) {
        super(interpolationType);
        this.vector = vector;
    }

    public Vector getVector() {
        return vector;
    }
}
