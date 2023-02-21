package xyz.larkyy.aquaticmodelengine.model.spawned.bones;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.AquaticModelEngine;
import xyz.larkyy.aquaticmodelengine.api.model.holder.impl.AttachmentModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.api.model.template.TemplateBone;
import xyz.larkyy.aquaticmodelengine.model.spawned.BoneEntityImpl;
import xyz.larkyy.aquaticmodelengine.api.math.Quaternion;

public class BasicBone extends ModelBone {
    public BasicBone(TemplateBone templateBone, SpawnedModel spawnedModel) {
        super(templateBone,spawnedModel);
    }

    @Override
    public void tick(Vector parentPivot, EulerAngle parentAngle) {
        if (getBoneEntity() == null) {
            return;
        }
        if (getSpawnedModel().getModelHolder() == null) {
            return;
        }
        var loc = getSpawnedModel().getModelHolder().getLocation().clone().add(0,-1.4375,0);
        loc.setYaw(getSpawnedModel().getModelHolder().getBodyRotation());
        var finalPivot = getFinalPivot(parentPivot,parentAngle).clone();
        var finalRotation = getFinalRotation(parentAngle);

        for (var bone : getChildren()) {
            bone.tick(finalPivot.clone(),finalRotation);
        }


        finalPivot.rotateAroundY(-Math.toRadians(loc.getYaw()));
        finalPivot.multiply(0.0625d);
        var finalLocation = loc.clone().add(finalPivot);

        getAttachmentModelHolder().tick(finalLocation.clone(),finalRotation);

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
        getAttachmentModelHolder().tick(loc.clone(),finalRotation);

        setBoneEntity(new BoneEntityImpl(this,AquaticModelEngine.getInstance().getEntityHandler().spawn(loc, armorStand -> {
            armorStand.setGravity(false);
            armorStand.setMarker(true);
            armorStand.setPersistent(false);
            armorStand.setInvisible(true);
            armorStand.setRotation(yaw,0);

            ItemStack is = new ItemStack(getTemplateBone().getMaterial());
            var im = is.getItemMeta();
            LeatherArmorMeta lam = (LeatherArmorMeta) im;
            lam.setColor(Color.fromRGB(255,255,255));
            lam.setCustomModelData(getTemplateBone().getModelId());
            is.setItemMeta(lam);
            armorStand.getEquipment().setHelmet(is);
            armorStand.setHeadPose(finalRotation);
        })));
    }

    @Override
    public void removeModel() {
        Bukkit.getOnlinePlayers().forEach(p -> getBoneEntity().hide(p));
        getBoneEntity().remove();
        getAttachmentModelHolder().getSpawnedModels().values().forEach(SpawnedModel::removeModel);
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

        if (getParent() != null || getSpawnedModel().getModelHolder() instanceof AttachmentModelHolder) {
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
}
