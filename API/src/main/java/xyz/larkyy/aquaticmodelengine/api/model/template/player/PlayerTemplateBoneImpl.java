package xyz.larkyy.aquaticmodelengine.api.model.template.player;

import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

public class PlayerTemplateBoneImpl extends PlayerTemplateBone{
    public PlayerTemplateBoneImpl(String name, Vector origin, EulerAngle rotation, LimbType limbType) {
        super(name, origin, rotation, limbType);
    }
}
