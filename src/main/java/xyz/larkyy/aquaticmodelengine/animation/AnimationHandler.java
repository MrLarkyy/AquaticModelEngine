package xyz.larkyy.aquaticmodelengine.animation;

import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.model.spawned.SpawnedModel;

import java.util.HashMap;
import java.util.Map;

public class AnimationHandler {

    private final Map<String,RunningAnimation> runningAnimations = new HashMap<>();
    private final SpawnedModel spawnedModel;

    public AnimationHandler(SpawnedModel spawnedModel) {
        this.spawnedModel = spawnedModel;
    }

    public void update() {
        for (var entry : runningAnimations.entrySet()) {
            var animation = entry.getValue();
            var key = entry.getKey();

            if (!animation.update()) {
                runningAnimations.remove(key);
            }
        }
    }

    public Vector getPosition(ModelBone modelBone) {
        Vector finalVector = new Vector(0d,0d,0d);
        for (var animation : runningAnimations.values()) {
            var vector = animation.getPosition(modelBone.getTemplateBone().getName());
            if (vector == null) {
                continue;
            }
            finalVector.add(vector);
        }
        return finalVector;
    }

    public EulerAngle getRotation(ModelBone modelBone) {
        EulerAngle finalAngle = EulerAngle.ZERO;
        for (var animation : runningAnimations.values()) {
            var angle = animation.getRotation(modelBone.getTemplateBone().getName());
            if (angle == null) {
                continue;
            }
            finalAngle = finalAngle.add(angle.getX(),angle.getY(),angle.getZ());
        }
        return finalAngle;
    }

    public void playAnimation(String name, double speed) {
        TemplateAnimation templateAnimation = spawnedModel.getModelTemplate().getAnimation(name);
        if (templateAnimation == null) {
            return;
        }
        var runningAnimation = new RunningAnimation(templateAnimation,speed);
        runningAnimations.put(name,runningAnimation);
    }

    public boolean isPlaying(String name) {
        var running = runningAnimations.get(name);
        return running != null;
    }

    public void stopAnimation(String name) {
        runningAnimations.remove(name);
    }

    public void stopAnimations() {
        runningAnimations.clear();
    }
}
