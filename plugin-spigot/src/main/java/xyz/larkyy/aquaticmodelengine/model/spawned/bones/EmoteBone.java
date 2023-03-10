package xyz.larkyy.aquaticmodelengine.model.spawned.bones;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.AquaticModelEngine;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.player.PlayerModel;
import xyz.larkyy.aquaticmodelengine.api.model.template.TemplateBone;
import xyz.larkyy.aquaticmodelengine.api.model.template.player.LimbType;
import xyz.larkyy.aquaticmodelengine.api.model.template.player.PlayerTemplateBone;
import xyz.larkyy.aquaticmodelengine.model.spawned.BoneEntityImpl;
import xyz.larkyy.aquaticmodelengine.api.math.Quaternion;

public class EmoteBone extends ModelBone {

    public EmoteBone(TemplateBone templateBone, PlayerModel playerModel) {
        super(templateBone,playerModel);
    }

    @Override
    public void tick(Vector parentPivot, EulerAngle parentAngle) {
        if (getBoneEntity() == null) {
            return;
        }
        if (getSpawnedModel().getModelHolder() == null) {
            return;
        }
        float offsetYaw = 0;

        var loc = getSpawnedModel().getModelHolder().getLocation().clone().add(0,-1.4375,0);
        if (getPlayerModel().rotateHead() && ((PlayerTemplateBone)getTemplateBone()).getLimbType() == LimbType.HEAD) {
            var rot1 = getSpawnedModel().getModelHolder().getBodyRotation();
            var rot2 = getSpawnedModel().getModelHolder().getHeadRotation();

            offsetYaw = rot2-rot1;
            loc.setYaw(getSpawnedModel().getModelHolder().getHeadRotation());
        } else {
            loc.setYaw(getSpawnedModel().getModelHolder().getBodyRotation());
        }


        var finalPivot = getFinalPivot(parentPivot,parentAngle).clone();
        var finalRotation = getFinalRotation(parentAngle);


        for (var bone : getChildren()) {
            bone.tick(finalPivot.clone(),finalRotation.add(0,Math.toRadians(offsetYaw),0));
        }

        finalPivot.rotateAroundY(-Math.toRadians(loc.getYaw()));
        finalPivot.multiply(0.0625d);
        var finalLocation = loc.clone().add(finalPivot);
        getAttachmentModelHolder().tick(finalLocation.clone(),finalRotation);

        Vector v = new Vector(0.3125,0.09,0);
        v.rotateAroundY(-Math.toRadians(loc.getYaw()));
        finalLocation.add(v);


        getBoneEntity().setHeadPose(finalRotation);
        getBoneEntity().teleport(
                finalLocation
        );
    }

    @Override
    public void spawnModel(Vector parentPivot, EulerAngle parentAngle) {
        if (getSpawnedModel().getModelHolder() == null) {
            return;
        }
        var loc = getSpawnedModel().getModelHolder().getLocation().clone();

        var yaw = loc.getYaw();

        var finalPivot = getFinalPivot(parentPivot,parentAngle).clone();
        var finalRotation = getFinalRotation(parentAngle);

        for (var bone : getChildren()) {
            bone.spawnModel(finalPivot.clone(),finalRotation);
        }

        if (getBoneEntity() != null) {
            removeModel();
        }

        finalPivot.rotateAroundY(-Math.toRadians(loc.getYaw()));
        finalPivot.multiply(0.0625d);

        loc.add(finalPivot);

        Vector v = new Vector(0.3125,0.09,0);
        v.rotateAroundY(-Math.toRadians(loc.getYaw()));
        loc.add(v);

        getAttachmentModelHolder().tick(loc.clone(),finalRotation);
        setBoneEntity(new BoneEntityImpl(this, AquaticModelEngine.getInstance().getEntityHandler().spawn(loc, armorStand -> {
            armorStand.setGravity(false);
            armorStand.setMarker(true);
            armorStand.setPersistent(false);
            armorStand.setInvisible(true);
            armorStand.setRotation(yaw,0);
            armorStand.setRightArmPose(EulerAngle.ZERO);
            var is = AquaticModelEngine.getInstance().getNmsHandler().setSkullTexture(null,getPlayerModel().getTexture());
            var im = is.getItemMeta();
            im.setCustomModelData(((PlayerTemplateBone)getTemplateBone()).getModelId(getPlayerModel().getTexture().isSlim()));
            is.setItemMeta(im);
            armorStand.getEquipment().setItem(EquipmentSlot.HAND,is);
        })));
    }

    @Override
    public void removeModel() {
        Bukkit.getOnlinePlayers().forEach(p -> getBoneEntity().hide(p));
        getAttachmentModelHolder().getSpawnedModels().values().forEach(SpawnedModel::removeModel);
        getBoneEntity().remove();
    }

    @Override
    public void teleport(Location location) {
        if (getBoneEntity() != null) {
            getBoneEntity().teleport(location);
        }
    }

    @Override
    public EulerAngle getFinalRotation(EulerAngle parentAngle) {
        EulerAngle rotation = getTemplateBone().getRotation();

        var animationRotation = getSpawnedModel().getAnimationHandler().getRotation(this);
        rotation = rotation.add(animationRotation.getX(),animationRotation.getY(),animationRotation.getZ());

        if (getParent() != null) {
            var parentRotation = parentAngle;
            parentRotation = new EulerAngle(
                    parentRotation.getX(),
                    parentRotation.getY(),
                    parentRotation.getZ()
            );

            Quaternion startQuat = new Quaternion(parentRotation);
            Quaternion rotationQuat = new Quaternion(rotation);

            Quaternion resultQuat = rotationQuat.mul(startQuat);
            var resultEuler = resultQuat.getEulerAnglesXYZ();
            resultEuler = resultEuler.setX(resultEuler.getX());
            rotation = resultEuler;
        }

        return rotation;
    }

    /*
        RECODED VERSION
     */
    @Override
    public Vector getFinalPivot(Vector parentPivot, EulerAngle parentRotation) {
        Vector pivot = getTemplateBone().getOrigin().clone();
        Vector animationPivot = getSpawnedModel().getAnimationHandler().getPosition(this);

        pivot.add(animationPivot);

        if (getParent() != null) {
            pivot = getParent().getTemplateBone().getOrigin().clone().subtract(pivot);
            pivot.rotateAroundX(parentRotation.getX());
            pivot.rotateAroundY(-parentRotation.getY());
            pivot.rotateAroundZ(-parentRotation.getZ());

            pivot = parentPivot.clone().subtract(pivot);
        }
        return pivot;
    }

    @Override
    public void show(Player player) {
        getBoneEntity().show(player);
    }

    @Override
    public void hide(Player player) {
        getBoneEntity().hide(player);
    }

    public PlayerModel getPlayerModel() {
        return (PlayerModel)getSpawnedModel();
    }
}
