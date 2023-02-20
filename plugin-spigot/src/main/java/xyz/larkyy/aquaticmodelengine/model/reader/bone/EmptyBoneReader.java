package xyz.larkyy.aquaticmodelengine.model.reader.bone;

import org.bukkit.Bukkit;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.api.model.template.TemplateBone;
import xyz.larkyy.aquaticmodelengine.model.spawned.bones.EmptyBone;

import java.util.Map;

public class EmptyBoneReader extends AbstractBoneReader {

    public ModelBone loadBone(TemplateBone templateBone, Map<String, ModelBone> loadedBones, ModelBone parentBone, SpawnedModel spawnedModel) {
        EmptyBone modelBone = new EmptyBone(templateBone,spawnedModel);
        modelBone.setParent(parentBone);
        for (var tB : templateBone.getChildren()) {
            modelBone.getChildren().add(getBoneReader().loadBone(tB,loadedBones,modelBone,spawnedModel));
        }
        loadedBones.put(templateBone.getName(), modelBone);
        Bukkit.broadcastMessage("Loaded empty bone");
        return modelBone;
    }

}
