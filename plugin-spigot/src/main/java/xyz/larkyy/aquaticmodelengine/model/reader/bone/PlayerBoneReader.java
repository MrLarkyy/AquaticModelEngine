package xyz.larkyy.aquaticmodelengine.model.reader.bone;

import org.bukkit.Bukkit;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.player.PlayerModel;
import xyz.larkyy.aquaticmodelengine.api.model.template.TemplateBone;
import xyz.larkyy.aquaticmodelengine.model.spawned.bones.EmoteBone;
import xyz.larkyy.aquaticmodelengine.model.spawned.bones.EmptyBone;

import java.util.Map;

public class PlayerBoneReader extends AbstractBoneReader{

    public ModelBone loadBone(TemplateBone templateBone, Map<String, ModelBone> loadedBones, ModelBone parentBone, SpawnedModel spawnedModel) {
        ModelBone modelBone;
        if (spawnedModel instanceof PlayerModel playerModel) {
            modelBone = new EmoteBone(templateBone,playerModel);
            Bukkit.broadcastMessage("Loaded emote bone");
        } else {
            modelBone = new EmptyBone(templateBone,spawnedModel);
            Bukkit.broadcastMessage("Loaded empty bone");
        }

        modelBone.setParent(parentBone);
        for (var tB : templateBone.getChildren()) {
            modelBone.getChildren().add(getBoneReader().loadBone(tB,loadedBones,modelBone,spawnedModel));
        }
        loadedBones.put(templateBone.getName(), modelBone);
        return modelBone;
    }
}
