package xyz.larkyy.aquaticmodelengine.model.spawned;

import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.AquaticModelEngine;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.player.PlayerModel;
import xyz.larkyy.aquaticmodelengine.api.model.template.TemplateBone;
import xyz.larkyy.aquaticmodelengine.api.model.template.player.PlayerTemplateBone;
import xyz.larkyy.aquaticmodelengine.util.math.Quaternion;

public class ModelBoneImpl extends ModelBone {
    public ModelBoneImpl(TemplateBone templateBone, SpawnedModel spawnedModel) {
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

        var finalPivot = getFinalPivot(parentPivot,parentAngle).clone();
        var finalRotation = getFinalRotation(parentAngle);

        for (var bone : getChildren()) {
            bone.tick(finalPivot.clone(),finalRotation);
        }

        finalPivot.rotateAroundY(-Math.toRadians(loc.getYaw()));
        finalPivot.multiply(0.0625d);
        var finalLocation = loc.clone().add(finalPivot);

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

        setBoneEntity(new BoneEntityImpl(this,AquaticModelEngine.getInstance().getEntityHandler().spawn(loc, armorStand -> {
            armorStand.setGravity(false);
            armorStand.setMarker(true);
            armorStand.setPersistent(false);
            armorStand.setInvisible(true);
            armorStand.setRotation(yaw,0);

            ItemStack is;
            if (getSpawnedModel() instanceof PlayerModel playerModel
                    && getTemplateBone() instanceof PlayerTemplateBone playerTemplateBone) {
                is = new ItemStack(Material.PLAYER_HEAD);
                is = AquaticModelEngine.getInstance().getNmsHandler().setSkullTexture(is,playerModel.getTexture());
                var im = is.getItemMeta();
                im.setCustomModelData(playerTemplateBone.getModelId(playerModel.getTexture().isSlim()));
                is.setItemMeta(im);

            } else {

                is = new ItemStack(getTemplateBone().getMaterial());
                var im = is.getItemMeta();
                LeatherArmorMeta lam = (LeatherArmorMeta) im;
                lam.setColor(Color.fromRGB(255,255,255));
                lam.setCustomModelData(getTemplateBone().getModelId());
                is.setItemMeta(lam);
            }
            armorStand.getEquipment().setHelmet(is);
            armorStand.setHeadPose(finalRotation);
        }))
        );
    }

    @Override
    public void removeModel() {
        Bukkit.getOnlinePlayers().forEach(p -> getBoneEntity().hide(p));
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
}
