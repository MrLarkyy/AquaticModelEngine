package xyz.larkyy.aquaticmodelengine.api.model.template.player;

import org.bukkit.Material;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.api.model.template.TemplateBone;

public abstract class PlayerTemplateBone extends TemplateBone {

    private final LimbType limbType;

    public PlayerTemplateBone(String name, Vector origin, EulerAngle rotation, LimbType limbType) {
        super(name, origin, rotation);
        this.limbType = limbType;

        this.setMaterial(Material.PLAYER_HEAD);
    }

    public int getModelId(boolean slim) {
        if (slim) {
            return limbType.getSlimId();
        }
        return limbType.getModelId();
    }
}
