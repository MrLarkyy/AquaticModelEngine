package xyz.larkyy.aquaticmodelengine.api.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.player.PlayerModel;

public class EmoteEndEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private final PlayerModel playerModel;

    public EmoteEndEvent(PlayerModel playerModel) {
        this.playerModel = playerModel;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLERS;
    }

    public PlayerModel getPlayerModel() {
        return playerModel;
    }
}
