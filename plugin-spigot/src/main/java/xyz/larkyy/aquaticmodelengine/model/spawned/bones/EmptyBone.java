package xyz.larkyy.aquaticmodelengine.model.spawned.bones;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.api.model.template.TemplateBone;
import xyz.larkyy.aquaticmodelengine.api.math.Quaternion;

public class EmptyBone extends ModelBone {

    public EmptyBone(TemplateBone templateBone, SpawnedModel playerModel) {
        super(templateBone,playerModel);
    }

    @Override
    public void tick(Vector parentPivot, EulerAngle parentAngle) {
        if (getSpawnedModel().getModelHolder() == null) {
            return;
        }

        var finalPivot = getFinalPivot(parentPivot,parentAngle).clone();
        var finalRotation = getFinalRotation(parentAngle);

        var loc = getSpawnedModel().getModelHolder().getLocation().clone().add(0,-1.4375,0);
        loc.setYaw(getSpawnedModel().getModelHolder().getBodyRotation());
        for (var bone : getChildren()) {
            bone.tick(finalPivot.clone(),finalRotation);
        }

        finalPivot.rotateAroundY(-Math.toRadians(loc.getYaw()));
        finalPivot.multiply(0.0625d);
        var finalLocation = loc.clone().add(finalPivot);

        Vector v = new Vector(0.3125,0.09,0);
        v.rotateAroundY(-Math.toRadians(loc.getYaw()));
        finalLocation.add(v);

        getAttachmentModelHolder().tick(finalLocation.clone(),finalRotation);

        /*
        getBoneEntity().setHeadPose(finalRotation);
        getBoneEntity().teleport(
                finalLocation
        );
         */
    }

    @Override
    public void spawnModel(Vector parentPivot, EulerAngle parentAngle) {
        if (getSpawnedModel().getModelHolder() == null) {
            return;
        }
        var finalPivot = getFinalPivot(parentPivot,parentAngle).clone();
        var finalRotation = getFinalRotation(parentAngle);

        var loc = getSpawnedModel().getModelHolder().getLocation().clone().add(0,-1.4375,0);

        for (var bone : getChildren()) {
            bone.spawnModel(finalPivot.clone(),finalRotation);
        }

        finalPivot.rotateAroundY(-Math.toRadians(loc.getYaw()));
        finalPivot.multiply(0.0625d);
        var finalLocation = loc.clone().add(finalPivot);

        Vector v = new Vector(0.3125,0.09,0);
        v.rotateAroundY(-Math.toRadians(loc.getYaw()));
        finalLocation.add(v);

        getAttachmentModelHolder().tick(finalLocation.clone(),finalRotation);
    }

    @Override
    public void removeModel() {
        getAttachmentModelHolder().getSpawnedModels().values().forEach(SpawnedModel::removeModel);
    }

    @Override
    public void teleport(Location location) {
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
        //getBoneEntity().show(player);
    }

    @Override
    public void hide(Player player) {
        //getBoneEntity().hide(player);
    }
}
