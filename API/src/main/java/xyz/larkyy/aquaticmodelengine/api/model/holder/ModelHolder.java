package xyz.larkyy.aquaticmodelengine.api.model.holder;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class ModelHolder {

    private final Map<String, SpawnedModel> spawnedModels;

    public ModelHolder() {
        this.spawnedModels = new HashMap<>();
    }

    public Map<String, SpawnedModel> getSpawnedModels() {
        return spawnedModels;
    }

    public void tick() {
        spawnedModels.values().forEach(SpawnedModel::tick);
    }

    public void addModel(SpawnedModel spawnedModel) {
        if (spawnedModels.containsKey(spawnedModel.getModelTemplate().getName())) {
            return;
        }
        spawnedModels.put(spawnedModel.getModelTemplate().getName(),spawnedModel);
    }

    public abstract void teleport(Location location);
    public abstract Location getLocation();

    public abstract UUID getUniqueId();

    public abstract void remove();

}
