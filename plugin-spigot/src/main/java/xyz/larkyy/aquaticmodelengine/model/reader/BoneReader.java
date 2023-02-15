package xyz.larkyy.aquaticmodelengine.model.reader;

import xyz.larkyy.aquaticmodelengine.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.model.template.TemplateBone;

import java.util.Map;

public class BoneReader {

    public ModelBone loadBone(TemplateBone templateBone, Map<String, ModelBone> loadedBones, ModelBone parentBone, SpawnedModel spawnedModel) {
        ModelBone modelBone = new ModelBone(templateBone,spawnedModel);
        modelBone.setParent(parentBone);
        for (var tB : templateBone.getChildren()) {
            modelBone.getChildren().add(loadBone(tB,loadedBones,modelBone,spawnedModel));
        }
        loadedBones.put(templateBone.getName(), modelBone);
        return modelBone;
    }

}
