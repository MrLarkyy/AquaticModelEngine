package xyz.larkyy.aquaticmodelengine.model.spawned;

import org.bukkit.entity.Entity;
import xyz.larkyy.aquaticmodelengine.AquaticModelEngine;
import xyz.larkyy.aquaticmodelengine.model.template.ModelTemplate;
import xyz.larkyy.aquaticmodelengine.model.template.TemplateBone;

import java.util.Map;

public class SpawnedModel {

    private final Entity boundEntity;
    private final ModelTemplate modelTemplate;

    private final Map<String,ModelBone> bones;

    public SpawnedModel(Entity boundEntity, ModelTemplate template) {
        this.boundEntity = boundEntity;
        this.modelTemplate = template;

        bones = AquaticModelEngine.getInstance().getModelGenerator().getModelReader().loadModelBones(this);
    }

    public ModelTemplate getModelTemplate() {
        return modelTemplate;
    }

    public ModelBone addBone(TemplateBone templateBone) {
        var bone = new ModelBone(templateBone,this);
        bones.put(templateBone.getName(),bone);
        return bone;
    }

    public void removeBone(String boneName) {
        var bone = bones.remove(boneName);
        if (bone == null) {
            return;
        }
        bone.removeModel();
    }

    public void tick() {
        for (var bone : bones.values()) {
            bone.tick();
        }
    }
    public ModelBone getBone(String name) {
        return this.bones.get(name);
    }

    public void applyModel() {
        for (var model : bones.values()) {
            model.spawnModel(boundEntity.getLocation().clone());
        }
    }


    public void removeModel() {
        for (var model : bones.values()) {
            model.removeModel();
        }
    }

    public Map<String, ModelBone> getBones() {
        return bones;
    }

    public Entity getBoundEntity() {
        return boundEntity;
    }
}
