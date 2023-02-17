package xyz.larkyy.aquaticmodelengine.api.model.holder.impl;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import xyz.larkyy.aquaticmodelengine.api.model.holder.ModelHolder;

import java.util.UUID;

public class EntityModelHolder extends ModelHolder {

    private final Entity boundEntity;

    public EntityModelHolder(Entity entity) {
        this.boundEntity = entity;
    }

    @Override
    public void teleport(Location location) {
        boundEntity.teleport(location);
    }

    @Override
    public Location getLocation() {
        return boundEntity.getLocation();
    }

    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public void remove() {
        boundEntity.remove();
    }
}
