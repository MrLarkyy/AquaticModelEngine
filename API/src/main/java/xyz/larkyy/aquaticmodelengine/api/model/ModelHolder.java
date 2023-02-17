package xyz.larkyy.aquaticmodelengine.api.model;

import org.bukkit.entity.Entity;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.api.model.template.ModelTemplateImpl;

import java.util.HashMap;
import java.util.Map;

public class ModelHolder {

    private final Map<String, SpawnedModel> spawnedModels;
    private final Entity boundEntity;

    public ModelHolder(Entity entity) {
        this.spawnedModels = new HashMap<>();
        this.boundEntity = entity;
    }

    public Entity getBoundEntity() {
        return boundEntity;
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


}
