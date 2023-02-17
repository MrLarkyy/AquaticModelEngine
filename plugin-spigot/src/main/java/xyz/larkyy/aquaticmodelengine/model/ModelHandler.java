package xyz.larkyy.aquaticmodelengine.model;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import xyz.larkyy.aquaticmodelengine.AquaticModelEngine;
import xyz.larkyy.aquaticmodelengine.api.model.holder.ModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.holder.impl.DummyModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.holder.impl.EntityModelHolder;
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
        modelHolder.remove();
    }

    // Removes all holder models and keeps the holder entity alive
    public void removeHolder(ModelHolder modelHolder) {
        modelHolder.getSpawnedModels().values().forEach(SpawnedModel::removeModel);
        modelHolders.remove(modelHolder.getUniqueId());
    }

    public ModelHolder getModelHolder(Entity entity) {
        if (entity == null) {
            return null;
        }
        var holder = modelHolders.get(entity.getUniqueId());
        if (holder == null) {
            holder = new EntityModelHolder(entity);
            modelHolders.put(entity.getUniqueId(),holder);
        }
        return holder;
    }

    public ModelHolder getModelHolder(UUID uuid) {
        if (uuid == null) {
            return null;
        }
        return modelHolders.get(uuid);
    }

    public ModelHolder createDummyModelHolder(Location location) {
        var holder = new DummyModelHolder(location);
        modelHolders.put(holder.getUniqueId(),holder);
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
