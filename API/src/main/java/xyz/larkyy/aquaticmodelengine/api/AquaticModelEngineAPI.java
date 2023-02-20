package xyz.larkyy.aquaticmodelengine.api;


import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.larkyy.aquaticmodelengine.api.model.IModelHandler;
import xyz.larkyy.aquaticmodelengine.api.model.holder.ModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.api.model.template.ModelTemplateImpl;

import java.util.UUID;

public class AquaticModelEngineAPI {

    public static AquaticModelEngineAPI api;
    public static JavaPlugin pluginInstance;

    public IModelHandler modelHandler;

    public AquaticModelEngineAPI(IModelHandler entityHandler) {
        this.modelHandler = entityHandler;
    }

    public static IModelHandler getModelHandler() {
        return api.modelHandler;
    }

    public static SpawnedModel spawnModel(ModelHolder modelHolder, ModelTemplateImpl template) {
        return api.modelHandler.spawnModel(modelHolder,template);
    }

    public SpawnedModel spawnModel(ModelHolder modelHolder, String model) {
        return api.modelHandler.spawnModel(modelHolder,model);
    }

    public void despawnModel(SpawnedModel spawnedModel) {
        api.modelHandler.despawnModel(spawnedModel);
    }

    // Removes all holder models and removes the holder entity
    public void deleteHolder(ModelHolder modelHolder) {
        api.modelHandler.deleteHolder(modelHolder);
    }

    // Removes all holder models and keeps the holder entity alive
    public void removeHolder(ModelHolder modelHolder) {
        api.modelHandler.removeHolder(modelHolder);
    }

    public ModelHolder getModelHolder(Entity entity) {
        return api.modelHandler.getModelHolder(entity);
    }

    public ModelHolder getModelHolder(UUID uuid) {
        return api.modelHandler.getModelHolder(uuid);
    }

    public ModelHolder createDummyModelHolder(Location location) {
        return api.modelHandler.createDummyModelHolder(location);
    }
}
