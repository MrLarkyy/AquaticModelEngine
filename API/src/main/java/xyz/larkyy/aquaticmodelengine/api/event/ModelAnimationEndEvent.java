package xyz.larkyy.aquaticmodelengine.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import xyz.larkyy.aquaticmodelengine.api.model.animation.RunningAnimation;

public class ModelAnimationEndEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();

    private final RunningAnimation runningAnimation;

    public ModelAnimationEndEvent(RunningAnimation runningAnimation) {
        this.runningAnimation = runningAnimation;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public RunningAnimation getRunningAnimation() {
        return runningAnimation;
    }
}
