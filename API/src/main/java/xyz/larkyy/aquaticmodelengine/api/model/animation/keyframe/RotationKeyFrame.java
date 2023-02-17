package xyz.larkyy.aquaticmodelengine.api.model.animation.keyframe;

import org.bukkit.util.EulerAngle;
import xyz.larkyy.aquaticmodelengine.api.model.animation.InterpolationType;
import xyz.larkyy.aquaticmodelengine.api.model.animation.KeyFrame;

public class RotationKeyFrame extends KeyFrame {

    private final EulerAngle eulerAngle;

    public RotationKeyFrame(InterpolationType interpolationType, EulerAngle eulerAngle) {
        super(interpolationType);
        this.eulerAngle = eulerAngle;
    }

    public EulerAngle getEulerAngle() {
        return eulerAngle;
    }
}
