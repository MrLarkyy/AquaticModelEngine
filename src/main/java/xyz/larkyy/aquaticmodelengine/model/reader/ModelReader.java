package xyz.larkyy.aquaticmodelengine.model.reader;

import xyz.larkyy.aquaticmodelengine.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.model.template.ModelTemplate;

import java.util.HashMap;
import java.util.Map;

public class ModelReader {

    private final BoneReader boneReader = new BoneReader();

    public Map<String, ModelBone> loadModelBones(ModelTemplate modelTemplate) {
        Map<String,ModelBone> loadedBones = new HashMap<>();
        for (var bone : modelTemplate.getBones()) {
            boneReader.loadBone(bone,loadedBones,null);
        }
        return loadedBones;
    }



}
