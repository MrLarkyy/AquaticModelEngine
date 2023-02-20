package xyz.larkyy.aquaticmodelengine.model.spawned;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import xyz.larkyy.aquaticmodelengine.api.FakeArmorStand;
import xyz.larkyy.aquaticmodelengine.api.model.RenderHandler;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.BoneEntity;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;

public class BoneEntityImpl extends BoneEntity {

    public BoneEntityImpl(ModelBone modelBone, FakeArmorStand fakeArmorStand) {
        super(modelBone,fakeArmorStand);
    }


    @Override
    public void setHeadPose(EulerAngle angle) {
        if (angle.equals(getPrevHeadPose())) {
            return;
        }
        setPrevHeadPose(angle);
        getFakeArmorStand().setHeadPose(angle);
        getRenderHandler().getSeenBy().forEach(uuid -> {
            var player = Bukkit.getPlayer(uuid);
            if (player != null) {
                getFakeArmorStand().updateHeadRotation(player);
            }
        });
    }

    @Override
    public void teleport(Location location) {
        /*
        if (location.equals(getPrevLocation())) {
            return;
        }

         */
        setPrevLocation(location);
        getFakeArmorStand().teleport(location);
        getRenderHandler().getSeenBy().forEach(uuid -> {
            var player = Bukkit.getPlayer(uuid);
            if (player != null) {
                getFakeArmorStand().updatePosition(player);
            }
        });

    }

    @Override
    public void show(Player player) {
        getFakeArmorStand().show(player);
    }

    @Override
    public void hide(Player player) {
        getFakeArmorStand().hide(player);
    }

    @Override
    public void remove() {
        getFakeArmorStand().remove();
    }

    private RenderHandler getRenderHandler() {
        return getModelBone().getSpawnedModel().getRenderHandler();
    }
}
