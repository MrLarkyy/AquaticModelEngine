package xyz.larkyy.aquaticmodelengine.model.reader.bone;

import xyz.larkyy.aquaticmodelengine.AquaticModelEngine;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.api.model.template.TemplateBone;
import xyz.larkyy.aquaticmodelengine.model.reader.BoneReader;

import java.util.Map;

public abstract class AbstractBoneReader {

    public BoneReader getBoneReader() {
        return AquaticModelEngine.getInstance().getModelGenerator().getModelReader().getBoneReader();
    }

    public abstract ModelBone loadBone(TemplateBone templateBone, Map<String, ModelBone> loadedBones, ModelBone parentBone, SpawnedModel spawnedModel);
}
