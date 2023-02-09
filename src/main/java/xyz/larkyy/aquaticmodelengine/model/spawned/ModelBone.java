package xyz.larkyy.aquaticmodelengine.model.spawned;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.model.template.TemplateBone;
import xyz.larkyy.aquaticmodelengine.util.math.Quaternion;

import java.util.ArrayList;
import java.util.List;

public class ModelBone {
    private final TemplateBone templateBone;
    private Entity boneEntity = null;
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

    public void tick() {
        if (boneEntity == null) {
            return;
        }
        if (spawnedModel.getBoundEntity() == null) {
            return;
        }
        var loc = spawnedModel.getBoundEntity().getLocation().clone();

        var finalPivot = getFinalPivot().clone().multiply(0.0625);
        finalPivot.rotateAroundY(-Math.toRadians(loc.getYaw()));

        var finalLocation = loc.clone().add(finalPivot);

        boneEntity.teleport(
                finalLocation
        );
        boneEntity.setRotation(loc.getYaw(),0);
    }

    public TemplateBone getTemplateBone() {
        return templateBone;
    }

    public void spawnModel(Location location) {
        var yaw = location.getYaw();

        if (boneEntity != null) {
            removeModel();
        }

        var finalPivot = getFinalPivot().clone().multiply(0.0625);
        finalPivot.rotateAroundY(-Math.toRadians(yaw));

        var loc = location.clone();
        //Bukkit.broadcastMessage("§e"+templateBone.getName() +"'s vector: "+vector.getX()+" "+vector.getY()+" "+vector.getZ());
        loc.add(finalPivot);
        var as = loc.getWorld().spawn(loc, ArmorStand.class, entity -> {
            entity.setGravity(false);
            entity.setMarker(true);
            entity.setPersistent(false);
            entity.setInvisible(true);
            entity.setRotation(yaw,0);
            var is = new ItemStack(Material.LEATHER_HORSE_ARMOR);
            var im = is.getItemMeta();
            im.setCustomModelData(templateBone.getModelId());
            is.setItemMeta(im);
            entity.getEquipment().setHelmet(is);
            entity.setHeadPose(getFinalRotation());
        });
        boneEntity = as;
    }

    public void removeModel() {
        boneEntity.remove();
    }

    public void teleport(Location location) {
        if (boneEntity != null) {
            boneEntity.teleport(location);
        }
    }

    private EulerAngle getFinalRotation() {
        EulerAngle rotation = templateBone.getRotation();
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
            //Bukkit.broadcastMessage("Previous: "+Math.toDegrees(resultEuler.getX())+" "+Math.toDegrees(resultEuler.getY())+" "+Math.toDegrees(resultEuler.getZ()));
            resultEuler = resultEuler.setX(-resultEuler.getX());
            //Bukkit.broadcastMessage("New: "+Math.toDegrees(resultEuler.getX())+" "+Math.toDegrees(resultEuler.getY())+" "+Math.toDegrees(resultEuler.getZ()));
            //Bukkit.broadcastMessage("§dRotation: "+Math.toDegrees(resultEuler.getX())+" "+Math.toDegrees(resultEuler.getY())+" "+Math.toDegrees(resultEuler.getZ()));
            rotation = resultEuler;
        }

        return rotation;
    }

    private Vector getFinalPivot() {
        Vector pivot = templateBone.getOrigin().clone();

        if (templateBone.getName().equalsIgnoreCase("bone4")) {
            //Bukkit.broadcastMessage("Original Vector: " + pivot.getX() + " " + pivot.getY() + " " + pivot.getZ());
            if (getParent() != null) {
                //var parentPivot = getParent().getTemplateBone().getOrigin();
                //Bukkit.broadcastMessage("Parent Vector: " + parentPivot.getX() + " " + parentPivot.getY() + " " + parentPivot.getZ());
                pivot = getParent().getTemplateBone().getOrigin().clone().subtract(pivot);
                //Bukkit.broadcastMessage("Subtracted Vector: " + pivot.getX() + " " + pivot.getY() + " " + pivot.getZ());
                var rotation = getParent().getTemplateBone().getRotation();
                //Bukkit.broadcastMessage("Rotating around: "+Math.toDegrees(rotation.getX())+" "+Math.toDegrees(rotation.getY())+" "+Math.toDegrees(rotation.getZ()));
                pivot.rotateAroundX(rotation.getX());
                pivot.rotateAroundY(-rotation.getY());
                pivot.rotateAroundZ(-rotation.getZ());
                //Bukkit.broadcastMessage("Rotated Vector: " + pivot.getX() + " " + pivot.getY() + " " + pivot.getZ());

                pivot = parent.getFinalPivot().subtract(pivot);
                //Bukkit.broadcastMessage("Final Vector: " + pivot.getX() + " " + pivot.getY() + " " + pivot.getZ());
            }
        }
        return pivot;
    }

    public Entity getBoneEntity() {
        return boneEntity;
    }
}
