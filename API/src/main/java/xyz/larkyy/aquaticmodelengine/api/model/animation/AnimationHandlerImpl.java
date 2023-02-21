package xyz.larkyy.aquaticmodelengine.api.model.animation;

import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;

public class AnimationHandlerImpl extends AnimationHandler {

    public AnimationHandlerImpl(SpawnedModel spawnedModel) {
        super(spawnedModel);
    }

    @Override
    public void update() {
        this.getRunningAnimations().entrySet().removeIf(entry -> !entry.getValue().update());
    }

    @Override
    public Vector getPosition(ModelBone modelBone) {
        Vector finalVector = new Vector(0d,0d,0d);
        for (var animation : getRunningAnimations().values()) {
            var vector = animation.getPosition(modelBone.getTemplateBone().getName());
            if (vector == null) {
                continue;
            }
            finalVector.add(vector);
        }
        return finalVector;
    }

    @Override
    public EulerAngle getRotation(ModelBone modelBone) {
        EulerAngle finalAngle = EulerAngle.ZERO;
        for (var animation : getRunningAnimations().values()) {
            var angle = animation.getRotation(modelBone.getTemplateBone().getName());
            if (angle == null) {
                continue;
            }
            finalAngle = finalAngle.add(angle.getX(),angle.getY(),angle.getZ());
        }
        return finalAngle;
    }

    @Override
    public void playAnimation(String name, double speed) {
        TemplateAnimation templateAnimation = getSpawnedModel().getModelTemplate().getAnimation(name);
        if (templateAnimation == null) {
            return;
        }
        var runningAnimation = new RunningAnimation(this,templateAnimation,speed);
        getRunningAnimations().put(name,runningAnimation);
    }

    @Override
    public boolean isPlaying(String name) {
        var running = getRunningAnimations().get(name);
        return running != null;
    }

    @Override
    public void stopAnimation(String name) {
        getRunningAnimations().remove(name);
    }

    @Override
    public void stopAnimations() {
        getRunningAnimations().clear();
    }
}
