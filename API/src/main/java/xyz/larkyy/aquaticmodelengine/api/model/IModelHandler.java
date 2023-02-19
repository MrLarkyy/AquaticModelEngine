package xyz.larkyy.aquaticmodelengine.api.model;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import xyz.larkyy.aquaticmodelengine.api.model.holder.ModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.api.model.template.ModelTemplateImpl;

import java.util.UUID;

public interface IModelHandler {

    SpawnedModel spawnModel(ModelHolder modelHolder, ModelTemplateImpl template);
    SpawnedModel spawnModel(ModelHolder modelHolder, String model);
    void despawnModel(SpawnedModel spawnedModel);
    void deleteHolder(ModelHolder modelHolder);
    void removeHolder(ModelHolder modelHolder);
    ModelHolder getModelHolder(Entity entity);
    ModelHolder getModelHolder(UUID uuid);
    ModelHolder createDummyModelHolder(Location location);
    void tickModels();
}
