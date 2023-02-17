package xyz.larkyy.aquaticmodelengine.api.model.animation.keyframe;

import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.api.model.animation.InterpolationType;
import xyz.larkyy.aquaticmodelengine.api.model.animation.KeyFrame;

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
