package xyz.larkyy.aquaticmodelengine.model.spawned;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import xyz.larkyy.aquaticmodelengine.api.FakeArmorStand;
import xyz.larkyy.aquaticmodelengine.model.RenderHandler;

public class BoneEntity {

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

    public void setHeadPose(EulerAngle angle) {
        if (angle.equals(prevHeadPose)) {
            return;
        }
        prevHeadPose = angle;
        fakeArmorStand.setHeadPose(angle);
        getRenderHandler().getSeenBy().forEach(uuid -> {
            fakeArmorStand.updateHeadRotation(Bukkit.getPlayer(uuid));
        });
    }

    public void teleport(Location location) {
        if (location.equals(prevLocation)) {
            return;
        }
        prevLocation = location;
        fakeArmorStand.teleport(location);
        getRenderHandler().getSeenBy().forEach(uuid -> {
            fakeArmorStand.updatePosition(Bukkit.getPlayer(uuid));
        });

    }

    public void show(Player player) {
        fakeArmorStand.show(player);
    }

    public void hide(Player player) {
        fakeArmorStand.hide(player);
    }

    public void remove() {
        fakeArmorStand.remove();
    }

    private RenderHandler getRenderHandler() {
        return modelBone.getSpawnedModel().getRenderHandler();
    }
}
