package xyz.larkyy.aquaticmodelengine.model.reader.bone;

import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.api.model.template.TemplateBone;
import xyz.larkyy.aquaticmodelengine.model.spawned.bones.BasicBone;

import java.util.Map;

public class BasicBoneReader extends AbstractBoneReader {

    public ModelBone loadBone(TemplateBone templateBone, Map<String, ModelBone> loadedBones, ModelBone parentBone, SpawnedModel spawnedModel) {
        BasicBone modelBone = new BasicBone(templateBone,spawnedModel);
        modelBone.setParent(parentBone);
        for (var tB : templateBone.getChildren()) {
            modelBone.getChildren().add(getBoneReader().loadBone(tB,loadedBones,modelBone,spawnedModel));
        }
        loadedBones.put(templateBone.getName(), modelBone);
        return modelBone;
    }

}
