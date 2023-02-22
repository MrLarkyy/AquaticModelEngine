package xyz.larkyy.aquaticmodelengine.api.model.holder.impl;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import xyz.larkyy.aquaticmodelengine.api.model.holder.ModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.holder.rotation.PlayerRotation;

import java.util.UUID;

public class PlayerModelHolder extends ModelHolder {
    private final Player player;
    private PlayerRotation bodyRotation = null;

    public PlayerModelHolder(Player player) {
        this.player = player;
    }

    @Override
    public void teleport(Location location) {
        player.teleport(location);
    }

    @Override
    public Location getLocation() {
        return player.getLocation();
    }

    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public void remove() {
    }

    public Player getPlayer() {
        return player;
    }

    @Override
    public boolean tick() {
        if (bodyRotation != null) {
            bodyRotation.tickMovement(getLocation());
        }
        return super.tick();
    }

    @Override
    public boolean checkNull() {
        return player != null && player.isOnline();
    }

    @Override
    public float getBodyRotation() {
        if (bodyRotation == null) {
            bodyRotation = new PlayerRotation(player);
            return getLocation().getYaw();
        }
        return bodyRotation.getBodyYaw();
    }

    @Override
    public float getHeadRotation() {
        return getLocation().getYaw();
    }
}
