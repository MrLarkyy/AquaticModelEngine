package xyz.larkyy.aquaticmodelengine.model;

import org.bukkit.entity.Entity;
import xyz.larkyy.aquaticmodelengine.AquaticModelEngine;
import xyz.larkyy.aquaticmodelengine.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.model.template.ModelTemplate;

import java.util.ArrayList;
import java.util.List;

public class ModelHandler {

    private final List<SpawnedModel> spawnedModels = new ArrayList<>();
    private final ModelTicker modelTicker = new ModelTicker(this);

    public List<SpawnedModel> getSpawnedModels() {
        return spawnedModels;
    }

    public SpawnedModel spawnModel(Entity boundEntity, ModelTemplate template) {
        return new SpawnedModel(boundEntity,template);
    }

    public SpawnedModel spawnModel(Entity boundEntity, String model) {
        var m = AquaticModelEngine.getInstance().getModelGenerator().getRegistry().getTemplate(model);
        var spawnedModel = spawnModel(boundEntity,m);
        spawnedModels.add(spawnedModel);
        return spawnedModel;
    }

    public void despawnModel(SpawnedModel spawnedModel) {
        spawnedModels.remove(spawnedModel);
        spawnedModel.removeModel();
    }

    public void tickModels() {
        for (var model : spawnedModels) {
            model.tick();
        }
    }

    public void startTicking() {
        modelTicker.runTaskTimer(AquaticModelEngine.getInstance(),1,1);
    }
}
