package xyz.larkyy.aquaticmodelengine.model.reader;

import xyz.larkyy.aquaticmodelengine.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.model.template.TemplateBone;

import java.util.Map;

public class BoneReader {

    public ModelBone loadBone(TemplateBone templateBone, Map<String, ModelBone> loadedBones,ModelBone parentBone) {
        ModelBone modelBone = new ModelBone(templateBone);
        modelBone.setParent(parentBone);
        for (var tB : templateBone.getChildren()) {
            modelBone.getChildren().add(loadBone(tB,loadedBones,modelBone));
        }
        loadedBones.put(templateBone.getName(), modelBone);
        return modelBone;
    }

}
