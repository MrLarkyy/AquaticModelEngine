package xyz.larkyy.aquaticmodelengine.api.model.animation;

import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;

import java.util.HashMap;
import java.util.Map;

public abstract class AnimationHandler {

    private final SpawnedModel spawnedModel;
    private final Map<String,RunningAnimation> runningAnimations = new HashMap<>();

    public AnimationHandler(SpawnedModel spawnedModel) {
        this.spawnedModel = spawnedModel;
    }

    public SpawnedModel getSpawnedModel() {
        return spawnedModel;
    }

    public Map<String, RunningAnimation> getRunningAnimations() {
        return runningAnimations;
    }

    public abstract void update();

    public abstract Vector getPosition(ModelBone modelBone);

    public abstract EulerAngle getRotation(ModelBone modelBone);

    public abstract void playAnimation(String name, double speed);

    public abstract boolean isPlaying(String name);

    public abstract void stopAnimation(String name);

    public abstract void stopAnimations();
}
