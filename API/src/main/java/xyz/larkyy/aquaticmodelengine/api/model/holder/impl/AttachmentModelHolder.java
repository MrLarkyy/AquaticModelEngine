package xyz.larkyy.aquaticmodelengine.api.model.holder.impl;

import org.bukkit.Location;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.api.model.holder.ModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;

import java.util.UUID;

public class AttachmentModelHolder extends ModelHolder {

    private final ModelBone modelBone;
    private EulerAngle rotation = EulerAngle.ZERO;
    private Vector pivot = new Vector();

    public AttachmentModelHolder(ModelBone modelBone) {
        this.modelBone = modelBone;
    }

    @Override
    public void teleport(Location location) {
    }

    @Override
    public Location getLocation() {
        return modelBone.getSpawnedModel().getModelHolder().getLocation();
    }

    @Override
    public UUID getUniqueId() {
        return null;
    }

    @Override
    public void remove() {

    }

    @Override
    public EulerAngle getRotation() {
        return rotation;
    }

    @Override
    public Vector getPivot() {
        return pivot;
    }

    public void tick(Vector pivot, EulerAngle rotation) {
        this.pivot = pivot;
        this.rotation = rotation;
        super.tick();
    }

    public Vector getParentOrigin() {
        return modelBone.getTemplateBone().getOrigin();
    }
}
