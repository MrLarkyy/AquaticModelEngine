package xyz.larkyy.aquaticmodelengine.model.spawned;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.AquaticModelEngine;
import xyz.larkyy.aquaticmodelengine.api.FakeArmorStand;
import xyz.larkyy.aquaticmodelengine.model.template.TemplateBone;
import xyz.larkyy.aquaticmodelengine.util.math.Quaternion;

import java.util.ArrayList;
import java.util.List;

public class ModelBone {
    private final TemplateBone templateBone;
    private FakeArmorStand boneEntity = null;
    private final List<ModelBone> children = new ArrayList<>();
    private ModelBone parent = null;
    private final SpawnedModel spawnedModel;
    public ModelBone(TemplateBone templateBone, SpawnedModel spawnedModel) {
        this.templateBone = templateBone;
        this.spawnedModel = spawnedModel;
    }

    public List<ModelBone> getChildren() {
        return children;
    }

    public ModelBone getParent() {
        return parent;
    }

    public void setParent(ModelBone parent) {
        this.parent = parent;
    }

    /*
    RECODED VERSION
    */
    public void tick2(Vector parentPivot, EulerAngle parentAngle) {
        if (boneEntity == null) {
            return;
        }
        if (spawnedModel.getBoundEntity() == null) {
            return;
        }
        var loc = spawnedModel.getBoundEntity().getLocation().clone().add(1.5,0,0);

        var finalPivot = getFinalPivot2(parentPivot,parentAngle).clone();
        var finalRotation = getFinalRotation2(parentAngle);

        for (var bone : children) {
            bone.tick2(finalPivot.clone(),finalRotation);
        }

        finalPivot.rotateAroundY(-Math.toRadians(loc.getYaw()));
        finalPivot.multiply(0.0625d);
        var finalLocation = loc.clone().add(finalPivot);

        boneEntity.setHeadPose(finalRotation);
        boneEntity.teleport(
                finalLocation
        );
        //boneEntity.setRotation(loc.getYaw(),0);
    }

    public TemplateBone getTemplateBone() {
        return templateBone;
    }

    public void spawnModel2(Vector parentPivot, EulerAngle parentAngle) {
        if (spawnedModel.getBoundEntity() == null) {
            return;
        }
        var loc = spawnedModel.getBoundEntity().getLocation().clone();

        var yaw = loc.getYaw();

        var finalPivot = getFinalPivot2(parentPivot,parentAngle).clone();
        var finalRotation = getFinalRotation2(parentAngle);

        for (var bone : children) {
            bone.spawnModel2(finalPivot.clone(),finalRotation);
        }

        if (boneEntity != null) {
            removeModel();
        }

        finalPivot.rotateAroundY(-Math.toRadians(loc.getYaw()));
        finalPivot.multiply(0.0625d);

        loc.add(finalPivot);

        boneEntity = AquaticModelEngine.getInstance().getEntityHandler().spawn(loc, armorStand -> {
            armorStand.setGravity(false);
            armorStand.setMarker(true);
            armorStand.setPersistent(false);
            armorStand.setInvisible(true);
            armorStand.setRotation(yaw,0);
            var is = new ItemStack(Material.LEATHER_HORSE_ARMOR);
            var im = is.getItemMeta();
            LeatherArmorMeta lam = (LeatherArmorMeta) im;
            lam.setColor(Color.fromRGB(255,255,255));
            lam.setCustomModelData(templateBone.getModelId());
            is.setItemMeta(lam);
            armorStand.getEquipment().setHelmet(is);
            armorStand.setHeadPose(finalRotation);
        });

        Bukkit.getOnlinePlayers().forEach(p -> {
            boneEntity.show(p);
        });

        /*
        boneEntity = loc.getWorld().spawn(loc, ArmorStand.class, entity -> {
            entity.setGravity(false);
            entity.setMarker(true);
            entity.setPersistent(false);
            entity.setInvisible(true);
            entity.setRotation(yaw,0);
            var is = new ItemStack(Material.LEATHER_HORSE_ARMOR);
            var im = is.getItemMeta();
            LeatherArmorMeta lam = (LeatherArmorMeta) im;
            lam.setColor(Color.fromRGB(255,255,255));
            lam.setCustomModelData(templateBone.getModelId());
            is.setItemMeta(lam);
            entity.getEquipment().setHelmet(is);
            entity.setHeadPose(finalRotation);
        });
         */
    }

    public void removeModel() {
        Bukkit.getOnlinePlayers().forEach(p -> boneEntity.hide(p));
        boneEntity.remove();
    }

    public void teleport(Location location) {
        if (boneEntity != null) {
            boneEntity.teleport(location);
        }
    }

    private EulerAngle getFinalRotation() {
        EulerAngle rotation = templateBone.getRotation();

        var animationRotation = spawnedModel.getAnimationHandler().getRotation(this);
        /*
        if (getParent() == null) {
            animationRotation = animationRotation.setX(animationRotation.getX()+Math.toRadians(180));
        }

        Quaternion quat = new Quaternion(rotation);
        Quaternion animationQuat = new Quaternion(animationRotation);

        Quaternion result = animationQuat.mul(quat);
        rotation = result.getEulerAnglesXYZ();

         */
        rotation = rotation.add(-animationRotation.getX(),animationRotation.getY(),animationRotation.getZ());

        if (getParent() != null) {
            var parentRotation = getParent().getFinalRotation();
            parentRotation = new EulerAngle(
                    -parentRotation.getX(),
                    parentRotation.getY(),
                    parentRotation.getZ()
            );

            Quaternion startQuat = new Quaternion(parentRotation);
            Quaternion rotationQuat = new Quaternion(rotation);

            Quaternion resultQuat = rotationQuat.mul(startQuat);
            var resultEuler = resultQuat.getEulerAnglesXYZ();
            resultEuler = resultEuler.setX(-resultEuler.getX());
            rotation = resultEuler;
        }

        return rotation;
    }

    /*
        RECODED VERSION
     */

    private EulerAngle getFinalRotation2(EulerAngle parentAngle) {
        EulerAngle rotation = templateBone.getRotation();

        var animationRotation = spawnedModel.getAnimationHandler().getRotation(this);
        /*
        if (getParent() == null) {
            animationRotation = animationRotation.setX(animationRotation.getX()+Math.toRadians(180));
        }

        Quaternion quat = new Quaternion(rotation);
        Quaternion animationQuat = new Quaternion(animationRotation);

        Quaternion result = animationQuat.mul(quat);
        rotation = result.getEulerAnglesXYZ();

         */
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

    private Vector getFinalPivot() {
        Vector pivot = templateBone.getOrigin().clone();
        Vector animationPivot = spawnedModel.getAnimationHandler().getPosition(this);

        pivot.add(animationPivot);

        if (getParent() != null) {
            pivot = getParent().getTemplateBone().getOrigin().clone().subtract(pivot);
            var rotation = getParent().getFinalRotation();
            pivot.rotateAroundX(rotation.getX());
            pivot.rotateAroundY(-rotation.getY());
            pivot.rotateAroundZ(-rotation.getZ());

            pivot = parent.getFinalPivot().subtract(pivot);
        }
        return pivot;
    }

    /*
        RECODED VERSION
     */
    private Vector getFinalPivot2(Vector parentPivot, EulerAngle parentRotation) {
        Vector pivot = templateBone.getOrigin().clone();
        Vector animationPivot = spawnedModel.getAnimationHandler().getPosition(this);

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

    public FakeArmorStand getBoneEntity() {
        return boneEntity;
    }
}
