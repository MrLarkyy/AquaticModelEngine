package xyz.larkyy.aquaticmodelengine.model;

import org.bukkit.entity.Entity;
import xyz.larkyy.aquaticmodelengine.AquaticModelEngine;
import xyz.larkyy.aquaticmodelengine.api.model.ModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.api.model.template.ModelTemplateImpl;
import xyz.larkyy.aquaticmodelengine.model.spawned.SpawnedModelImpl;

import java.util.*;

public class ModelHandler {

    private final Map<UUID,ModelHolder> modelHolders = new HashMap<>();
    private final ModelTicker modelTicker = new ModelTicker(this);

    public Map<UUID,ModelHolder> getModelHolders() {
        return modelHolders;
    }

    public SpawnedModelImpl spawnModel(ModelHolder modelHolder, ModelTemplateImpl template) {
        var spawned = new SpawnedModelImpl(modelHolder,template);
        modelHolder.addModel(spawned);
        return spawned;
    }

    public SpawnedModelImpl spawnModel(ModelHolder modelHolder, String model) {
        var m = AquaticModelEngine.getInstance().getModelGenerator().getRegistry().getTemplate(model);
        var spawned = spawnModel(modelHolder,m);
        modelHolder.addModel(spawned);
        return spawned;
    }

    public void despawnModel(SpawnedModel spawnedModel) {
        spawnedModel.getModelHolder().getSpawnedModels().remove(spawnedModel.getModelTemplate().getName());
        spawnedModel.removeModel();
    }

    // Removes all holder models and removes the holder entity
    public void deleteHolder(ModelHolder modelHolder) {
        removeHolder(modelHolder);
        modelHolder.getBoundEntity().remove();
    }

    // Removes all holder models and keeps the holder entity alive
    public void removeHolder(ModelHolder modelHolder) {
        modelHolder.getSpawnedModels().values().forEach(SpawnedModel::removeModel);
        modelHolders.remove(modelHolder.getBoundEntity().getUniqueId());
    }

    public ModelHolder getModelHolder(Entity entity) {
        if (entity == null) {
            return null;
        }
        var holder = modelHolders.get(entity.getUniqueId());
        if (holder == null) {
            holder = new ModelHolder(entity);
            modelHolders.put(entity.getUniqueId(),holder);
        }
        return holder;
    }

    public void tickModels() {
        for (var holder : modelHolders.values()) {
            holder.tick();
        }
    }

    public void startTicking() {
        modelTicker.runTaskTimer(AquaticModelEngine.getInstance(),1,1);
    }
}
