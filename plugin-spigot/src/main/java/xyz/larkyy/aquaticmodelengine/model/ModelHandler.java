package xyz.larkyy.aquaticmodelengine.model;

import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import xyz.larkyy.aquaticmodelengine.AquaticModelEngine;
import xyz.larkyy.aquaticmodelengine.api.model.IModelHandler;
import xyz.larkyy.aquaticmodelengine.api.model.holder.ModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.holder.impl.DummyModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.holder.impl.EntityModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.holder.impl.PlayerModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.api.model.template.ModelTemplateImpl;
import xyz.larkyy.aquaticmodelengine.model.player.PlayerModelImpl;
import xyz.larkyy.aquaticmodelengine.model.spawned.SpawnedModelImpl;

import java.util.*;

public class ModelHandler implements IModelHandler {

    private final Map<UUID,ModelHolder> modelHolders = new HashMap<>();
    private final ModelTicker modelTicker = new ModelTicker(this);

    public Map<UUID,ModelHolder> getModelHolders() {
        return modelHolders;
    }

    public SpawnedModelImpl spawnModel(ModelHolder modelHolder, ModelTemplateImpl template) {
        var spawned = new SpawnedModelImpl(modelHolder,template);
        modelHolder.addModel(spawned);
        spawned.applyModel();
        return spawned;
    }

    public SpawnedModelImpl spawnModel(ModelHolder modelHolder, String model) {
        var m = AquaticModelEngine.getInstance().getModelGenerator().getRegistry().getTemplate(model);
        return spawnModel(modelHolder,m);
    }

    public SpawnedModel attachModel(ModelBone modelBone, String model) {
        var m = AquaticModelEngine.getInstance().getModelGenerator().getRegistry().getTemplate(model);
        return attachModel(modelBone,m);
    }
    public SpawnedModel attachModel(ModelBone modelBone, ModelTemplateImpl template) {
        var holder = modelBone.getAttachmentModelHolder();
        var spawned = new SpawnedModelImpl(holder,template);
        holder.addModel(spawned);
        spawned.applyModel();
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
    public ModelHolder getModelHolder(Player player) {
        if (player == null) {
            return null;
        }
        var holder = modelHolders.get(player.getUniqueId());
        if (holder == null) {
            holder = new PlayerModelHolder(player);
            modelHolders.put(player.getUniqueId(),holder);
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
            if (!holder.tick()) {
                removeHolder(holder);
            }
        }
    }

    @Override
    public SpawnedModel spawnEmote(ModelHolder holder, Player player, String emote, String preAnimation, String animation, String postAnimation, boolean rotateHead) {
        var m = AquaticModelEngine.getInstance().getModelGenerator().getRegistry().getEmote(emote);
        return spawnEmote(holder,player,m,preAnimation,animation,postAnimation,rotateHead);
    }

    @Override
    public SpawnedModel spawnEmote(ModelHolder holder, Player player, ModelTemplateImpl template,
                                   String preAnimation, String animation, String postAnimation,
                                   boolean rotateHead) {
        if (template == null) {
            return null;
        }

        var preAnimationTemplate = template.getAnimation(preAnimation);
        var animationTemplate = template.getAnimation(animation);
        var postAnimationTemplate = template.getAnimation(postAnimation);

        var spawned = new PlayerModelImpl(template,holder,player,preAnimationTemplate,animationTemplate,postAnimationTemplate,rotateHead);
        holder.setEmote(spawned);
        spawned.applyModel();
        return spawned;
    }

    @Override
    public SpawnedModel spawnEmote(ModelHolder holder, String url, boolean slim, String emote, String preAnimation, String animation, String postAnimation, boolean rotateHead) {
        var m = AquaticModelEngine.getInstance().getModelGenerator().getRegistry().getEmote(emote);
        return spawnEmote(holder,url,slim,m,preAnimation,animation,postAnimation,rotateHead);
    }

    @Override
    public SpawnedModel spawnEmote(ModelHolder holder, String url, boolean slim, ModelTemplateImpl template, String preAnimation, String animation, String postAnimation, boolean rotateHead) {
        if (template == null) {
            return null;
        }

        var preAnimationTemplate = template.getAnimation(preAnimation);
        var animationTemplate = template.getAnimation(animation);
        var postAnimationTemplate = template.getAnimation(postAnimation);

        var spawned = new PlayerModelImpl(template,holder,url,slim, preAnimationTemplate, animationTemplate, postAnimationTemplate,rotateHead);
        holder.setEmote(spawned);
        spawned.applyModel();
        return spawned;
    }

    public void startTicking() {
        modelTicker.runTaskTimer(AquaticModelEngine.getInstance(),1,1);
    }
}
