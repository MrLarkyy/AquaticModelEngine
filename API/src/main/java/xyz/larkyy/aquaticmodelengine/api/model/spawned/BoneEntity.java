package xyz.larkyy.aquaticmodelengine.api.model.spawned;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import xyz.larkyy.aquaticmodelengine.api.FakeArmorStand;

public abstract class BoneEntity {

    private final FakeArmorStand fakeArmorStand;
    private final ModelBone modelBone;

    private EulerAngle prevHeadPose = EulerAngle.ZERO;
    private Location prevLocation = null;

    public BoneEntity(ModelBone modelBone, FakeArmorStand fakeArmorStand) {
        this.modelBone = modelBone;
        this.fakeArmorStand = fakeArmorStand;
    }

    public ModelBone getModelBone() {
        return modelBone;
    }

    public EulerAngle getPrevHeadPose() {
        return prevHeadPose;
    }

    public FakeArmorStand getFakeArmorStand() {
        return fakeArmorStand;
    }

    public Location getPrevLocation() {
        return prevLocation;
    }

    public void setPrevHeadPose(EulerAngle prevHeadPose) {
        this.prevHeadPose = prevHeadPose;
    }

    public void setPrevLocation(Location prevLocation) {
        this.prevLocation = prevLocation;
    }

    public abstract void setHeadPose(EulerAngle angle);

    public abstract void show(Player player);

    public abstract void hide(Player player);

    public abstract void remove();

    public abstract void teleport(Location location);
}
