package xyz.larkyy.aquaticmodelengine.api;

import org.bukkit.Location;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.ArmorStand;

import java.util.function.Consumer;

public interface IEntityHandler {

    FakeArmorStand spawn(Location location, Consumer<ArmorStand> factory);

}
