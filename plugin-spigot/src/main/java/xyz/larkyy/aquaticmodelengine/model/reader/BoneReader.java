package xyz.larkyy.aquaticmodelengine.model.reader;

import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.api.model.template.TemplateBone;
import xyz.larkyy.aquaticmodelengine.model.spawned.ModelBoneImpl;

import java.util.Map;

public class BoneReader {

    public ModelBoneImpl loadBone(TemplateBone templateBone, Map<String, ModelBone> loadedBones, ModelBone parentBone, SpawnedModel spawnedModel) {
        ModelBoneImpl modelBone = new ModelBoneImpl(templateBone,spawnedModel);
        modelBone.setParent(parentBone);
        for (var tB : templateBone.getChildren()) {
            modelBone.getChildren().add(loadBone(tB,loadedBones,modelBone,spawnedModel));
        }
        loadedBones.put(templateBone.getName(), modelBone);
        return modelBone;
    }

}
