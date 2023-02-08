package xyz.larkyy.aquaticmodelengine.model;

import org.bukkit.entity.Entity;
import xyz.larkyy.aquaticmodelengine.AquaticModelEngine;
import xyz.larkyy.aquaticmodelengine.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.model.template.ModelTemplate;

import java.util.ArrayList;
import java.util.List;

public class ModelHandler {

    private final List<SpawnedModel> spawnedModels = new ArrayList<>();

    public List<SpawnedModel> getSpawnedModels() {
        return spawnedModels;
    }

    public SpawnedModel spawnModel(Entity boundEntity, ModelTemplate template) {
        return new SpawnedModel(boundEntity,template);
    }

    public SpawnedModel spawnModel(Entity boundEntity, String model) {
        var m = AquaticModelEngine.getInstance().getModelGenerator().getRegistry().getTemplate(model);
        return spawnModel(boundEntity,m);
    }

    public void despawnModel(SpawnedModel spawnedModel) {
        spawnedModels.remove(spawnedModel);
        spawnedModel.removeModel();
    }
}
