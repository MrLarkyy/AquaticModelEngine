package xyz.larkyy.aquaticmodelengine.model.reader;

import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.player.PlayerModel;
import xyz.larkyy.aquaticmodelengine.api.model.template.TemplateBone;
import xyz.larkyy.aquaticmodelengine.api.model.template.player.LimbType;
import xyz.larkyy.aquaticmodelengine.model.reader.bone.AbstractBoneReader;
import xyz.larkyy.aquaticmodelengine.model.reader.bone.BasicBoneReader;
import xyz.larkyy.aquaticmodelengine.model.reader.bone.EmptyBoneReader;
import xyz.larkyy.aquaticmodelengine.model.reader.bone.PlayerBoneReader;
import xyz.larkyy.aquaticmodelengine.model.spawned.bones.BasicBone;

import java.util.HashMap;
import java.util.Map;

public class BoneReader {

    private final BasicBoneReader basicBoneReader = new BasicBoneReader();
    private final EmptyBoneReader emptyBoneReader = new EmptyBoneReader();
    private final PlayerBoneReader playerBoneReader = new PlayerBoneReader();

    public ModelBone loadBone(TemplateBone templateBone, Map<String, ModelBone> loadedBones, ModelBone parentBone, SpawnedModel spawnedModel) {

        if (templateBone.getModelId() == 0) {
            return emptyBoneReader.loadBone(templateBone,loadedBones,parentBone,spawnedModel);
        }
        if (spawnedModel instanceof PlayerModel playerModel) {
            var limbType = LimbType.get(templateBone.getName());
            if (limbType != null) {
                return playerBoneReader.loadBone(templateBone,loadedBones,parentBone,playerModel);
            }
            else if (templateBone.getName().equalsIgnoreCase("player_root")) {
                return emptyBoneReader.loadBone(templateBone,loadedBones,parentBone,playerModel);
            }
        }
        if (templateBone.getName().equalsIgnoreCase("hitbox")) {
            return emptyBoneReader.loadBone(templateBone,loadedBones,parentBone,spawnedModel);
        }
        return basicBoneReader.loadBone(templateBone,loadedBones,parentBone,spawnedModel);
    }

}
