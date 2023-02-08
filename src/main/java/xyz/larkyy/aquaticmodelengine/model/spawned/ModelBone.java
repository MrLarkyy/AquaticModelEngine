package xyz.larkyy.aquaticmodelengine.model.spawned;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.EulerAngle;
import xyz.larkyy.aquaticmodelengine.model.template.TemplateBone;

import java.util.ArrayList;
import java.util.List;

public class ModelBone {
    private final TemplateBone templateBone;
    private Entity boneEntity = null;
    private final List<ModelBone> children = new ArrayList<>();
    private ModelBone parent = null;
    public ModelBone(TemplateBone templateBone) {
        this.templateBone = templateBone;
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

    }

    public TemplateBone getTemplateBone() {
        return templateBone;
    }

    public void spawnModel(Location location) {
        if (boneEntity != null) {
            removeModel();
        }
        var loc = location.clone();
        var vector = templateBone.getOrigin().clone().multiply(0.0625);
        Bukkit.broadcastMessage("Vector: "+vector.getX()+" "+vector.getY()+" "+vector.getZ());
        loc.add(vector);
        var as = loc.getWorld().spawn(loc, ArmorStand.class, entity -> {
            entity.setGravity(false);
            entity.setMarker(true);
            entity.setPersistent(false);
            entity.setInvisible(false);
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
            rotation = rotation.add(
                    parent.getFinalRotation().getX(),
                    parent.getFinalRotation().getY(),
                    parent.getFinalRotation().getZ()
            );
        }
        return rotation;
    }

    public Entity getBoneEntity() {
        return boneEntity;
    }
}
