package xyz.larkyy.aquaticmodelengine.api.model;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.larkyy.aquaticmodelengine.api.model.holder.ModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;
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
    ModelHolder getModelHolder(Player player);
    ModelHolder getModelHolder(UUID uuid);
    ModelHolder createDummyModelHolder(Location location);
    void tickModels();

    SpawnedModel spawnEmote(ModelHolder holder, Player player, String emote, String preAnimation, String animation, String postAnimation, boolean rotateHead);
    SpawnedModel spawnEmote(ModelHolder holder, Player player, ModelTemplateImpl template, String preAnimation, String animation, String postAnimation, boolean rotateHead);
    SpawnedModel spawnEmote(ModelHolder holder, String url, boolean slim, String emote, String preAnimation, String animation, String postAnimation, boolean rotateHead);
    SpawnedModel spawnEmote(ModelHolder holder, String url, boolean slim, ModelTemplateImpl template, String preAnimation, String animation, String postAnimation, boolean rotateHead);

    SpawnedModel attachModel(ModelBone modelBone, String model);
    SpawnedModel attachModel(ModelBone modelBone, ModelTemplateImpl template);
}
