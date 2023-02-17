package xyz.larkyy.aquaticmodelengine.model.reader;

import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.model.spawned.ModelBoneImpl;
import xyz.larkyy.aquaticmodelengine.model.spawned.SpawnedModelImpl;

import java.util.HashMap;
import java.util.Map;

public class ModelReader {

    private final BoneReader boneReader = new BoneReader();

    public Map<String, ModelBone> loadModelBones(SpawnedModel spawnedModel) {
        Map<String, ModelBone> loadedBones = new HashMap<>();
        for (var bone : spawnedModel.getModelTemplate().getBones()) {
            boneReader.loadBone(bone,loadedBones,null,spawnedModel);
        }
        return loadedBones;
    }



}
