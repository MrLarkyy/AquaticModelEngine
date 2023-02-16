package xyz.larkyy.aquaticmodelengine.api;

import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;

public interface FakeArmorStand {

    void teleport(Location location);

    void setHeadPose(EulerAngle eulerAngle);

    void hide(Player player);

    void show(Player player);

    void remove();

    void updateHeadRotation(Player player);

    void updatePosition(Player player);

    int getId();

    ArmorStand getArmorstand();
}
