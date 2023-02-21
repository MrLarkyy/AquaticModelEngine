package xyz.larkyy.aquaticmodelengine.api.model.animation;

import org.bukkit.Bukkit;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.api.event.ModelAnimationEndEvent;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;

public class RunningAnimation {

    private double speed;

    private double time = 0d;
    private AnimationPhase phase = AnimationPhase.PLAYING;
    private final TemplateAnimation templateAnimation;
    private final AnimationHandler animationHandler;

    public RunningAnimation(AnimationHandler animationHandler, TemplateAnimation templateAnimation, double speed) {
        this.animationHandler = animationHandler;
        this.templateAnimation = templateAnimation;
        this.speed = speed;
    }
    public RunningAnimation(AnimationHandler animationHandler, TemplateAnimation templateAnimation) {
        this(animationHandler, templateAnimation,1d);
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getSpeed() {
        return speed;
    }

    public boolean update() {
        if (phase == AnimationPhase.END) {
            return false;
        }
        switch (templateAnimation.getLoopMode()) {
            case ONCE:
                if (time < templateAnimation.getLength()) {
                    var newTime = time+speed/20d;
                    if (newTime > templateAnimation.getLength()) {
                        newTime = templateAnimation.getLength();
                    }
                    time = newTime;
                    return true;
                }
                break;
            case LOOP:
                // +0.05d to also be able to get the last frame of the animation (+ 1tick)
                time = (time + speed / 20) % (templateAnimation.getLength()+0.05d);
                return true;
            case HOLD: {
                var newTime = time+speed/20d;
                if (newTime > templateAnimation.getLength()) {
                    newTime = templateAnimation.getLength();
                }
                time = newTime;
                return true;
            }
        }
        this.phase = AnimationPhase.END;
        var event = new ModelAnimationEndEvent(this);
        Bukkit.getPluginManager().callEvent(event);
        return false;
    }

    public Vector getPosition(String bone) {
        return templateAnimation.getPosition(bone,time);
    }

    public EulerAngle getRotation(String bone) {
        return templateAnimation.getRotation(bone,time);
    }

    public void stop() {
        this.phase = AnimationPhase.END;
    }

    public AnimationPhase getPhase() {
        return phase;
    }
    public String getName() {
        return templateAnimation.getName();
    }

    public AnimationHandler getAnimationHandler() {
        return animationHandler;
    }
}
