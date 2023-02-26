package xyz.larkyy.aquaticmodelengine.api.model.holder.impl;

import org.bukkit.Location;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.api.model.holder.ModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.api.model.template.player.PlayerTemplateBone;

import java.util.UUID;

public class AttachmentModelHolder extends ModelHolder {

    private final ModelBone modelBone;
    private EulerAngle rotation = EulerAngle.ZERO;
    private Location location;

    public AttachmentModelHolder(ModelBone modelBone) {
        this.modelBone = modelBone;
        location = modelBone.getSpawnedModel().getModelHolder().getLocation();
    }

    @Override
    public boolean checkNull() {
        return modelBone != null;
    }

    @Override
    public void teleport(Location location) {
    }

    @Override
    public Location getLocation() {
        var loc = location.clone().add(0,1.4375,0);
        if (modelBone.getTemplateBone() instanceof PlayerTemplateBone) {
            loc.add(0,2*-0.09,0);
        }
        return loc;
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


    public void tick(Location location, EulerAngle rotation) {
        this.location = location;
        this.rotation = rotation;
        super.tick();
    }

    public Vector getParentOrigin() {
        return modelBone.getTemplateBone().getOrigin();
    }
}
