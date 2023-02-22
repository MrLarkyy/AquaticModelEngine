package xyz.larkyy.aquaticmodelengine.api.model.holder.impl;

import org.bukkit.Location;
import xyz.larkyy.aquaticmodelengine.api.model.holder.ModelHolder;

import java.util.UUID;

public class DummyModelHolder extends ModelHolder {

    private Location location;

    public DummyModelHolder(Location location) {
        this.location = location;
    }

    @Override
    public boolean checkNull() {
        return location != null;
    }

    @Override
    public void teleport(Location location) {
        if (location == null) {
            return;
        }
        this.location = location;
    }

    @Override
    public Location getLocation() {
        return location;
    }

    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public void remove() {

    }
}
