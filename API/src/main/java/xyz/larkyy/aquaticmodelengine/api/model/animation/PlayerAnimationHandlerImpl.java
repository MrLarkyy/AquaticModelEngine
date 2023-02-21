package xyz.larkyy.aquaticmodelengine.api.model.animation;

import org.bukkit.Bukkit;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.api.event.EmoteAnimationStateChangeEvent;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.player.PlayerModel;

import java.util.HashMap;
import java.util.Map;

public class PlayerAnimationHandlerImpl extends AnimationHandler{


    private final RunningAnimation preAnimation;
    private final RunningAnimation animation;
    private final RunningAnimation postAnimation;
    private State animationState = State.PRE;

    public PlayerAnimationHandlerImpl(PlayerModel spawnedModel, TemplateAnimation preAnimation, TemplateAnimation animation, TemplateAnimation postAnimation) {
        super(spawnedModel);
        this.postAnimation = postAnimation == null ? null : new RunningAnimation(this,postAnimation,1);
        this.preAnimation = preAnimation == null ? null : new RunningAnimation(this,preAnimation,1);
        this.animation = animation == null ? null : new RunningAnimation(this,animation,1);
    }

    @Override
    public void update() {
        switch (animationState) {
            case PRE -> {
                if (preAnimation == null || !preAnimation.update()) {
                    animationState = State.PLAYING;
                    var event = new EmoteAnimationStateChangeEvent((PlayerModel) getSpawnedModel());
                    Bukkit.getPluginManager().callEvent(event);
                }
            }
            case PLAYING -> {
                if (animation == null || !animation.update()) {
                    animationState = State.POST;
                    var event = new EmoteAnimationStateChangeEvent((PlayerModel) getSpawnedModel());
                    Bukkit.getPluginManager().callEvent(event);
                }
            }
            case POST -> {
                if (postAnimation == null || !postAnimation.update()) {
                    return;
                }
            }
        }
    }

    @Override
    public Vector getPosition(ModelBone modelBone) {
        Vector finalVector = new Vector(0d,0d,0d);

        RunningAnimation animation;
        switch (animationState) {
            case PRE -> {
                animation = this.preAnimation;
            }
            case PLAYING -> {
                animation = this.animation;
            }
            default -> {
                animation = this.postAnimation;
            }
        }
        if (animation == null) {
            return finalVector;
        }
        var vector = animation.getPosition(modelBone.getTemplateBone().getName());
        if (vector == null) {
            return finalVector;
        }
        finalVector.add(vector);
        return finalVector;
    }

    @Override
    public EulerAngle getRotation(ModelBone modelBone) {
        EulerAngle finalAngle = EulerAngle.ZERO;

        RunningAnimation animation;
        switch (animationState) {
            case PRE -> {
                animation = this.preAnimation;
            }
            case PLAYING -> {
                animation = this.animation;
            }
            default -> {
                animation = this.postAnimation;
            }
        }
        if (animation == null) {
            return finalAngle;
        }
        var angle = animation.getRotation(modelBone.getTemplateBone().getName());
        if (angle == null) {
            return finalAngle;
        }
        finalAngle = finalAngle.add(angle.getX(),angle.getY(),angle.getZ());
        return finalAngle;
    }

    @Override
    public void playAnimation(String name, double speed) {
    }

    @Override
    public boolean isPlaying(String name) {
        var animation = getRunningAnimations().get(name);
        if (animation == null) {
            return false;
        }
        return animation.getPhase() != AnimationPhase.END;
    }

    public boolean isPlaying() {
        return getRunningAnimations().values().stream().anyMatch(v -> v.getPhase()!=AnimationPhase.END);
    }

    @Override
    public Map<String,RunningAnimation> getRunningAnimations() {
        Map<String,RunningAnimation> map = new HashMap<>();
        if (preAnimation != null) map.put(preAnimation.getName(),preAnimation);
        if (animation != null) map.put(animation.getName(),animation);
        if (postAnimation != null) map.put(postAnimation.getName(),postAnimation);
        return map;
    }

    @Override
    public void stopAnimation(String name) {
        var animation = getRunningAnimations().get(name);
        if (animation != null) {
            animation.stop();
        }
    }

    @Override
    public void stopAnimations() {
        getRunningAnimations().values().forEach(RunningAnimation::stop);
    }

    private enum State {
        PRE,
        PLAYING,
        POST
    }
}
