package xyz.larkyy.aquaticmodelengine.api.event;


import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ModelLoadEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();
    private final State state;

    public ModelLoadEvent(State state) {
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public enum State {
        FINISHED, LOADING
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
}
