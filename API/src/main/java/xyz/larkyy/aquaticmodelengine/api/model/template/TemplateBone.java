package xyz.larkyy.aquaticmodelengine.api.model.template;

import org.bukkit.Material;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

public abstract class TemplateBone {

    private int modelId;
    private Material material = Material.LEATHER_HORSE_ARMOR;
    private final String name;
    private final List<TemplateBone> children = new ArrayList<>();
    private TemplateBone parent = null;
    private final Vector origin;
    private final EulerAngle rotation;

    public TemplateBone(String name, Vector origin, EulerAngle rotation) {
        this.origin = origin;
        this.rotation = rotation;
        this.name = name;
    }

    public TemplateBone getParent() {
        return parent;
    }

    public void setParent(TemplateBone parent) {
        this.parent = parent;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public List<TemplateBone> getChildren() {
        return children;
    }

    public int getModelId() {
        return modelId;
    }

    public Vector getOrigin() {
        return origin;
    }

    public EulerAngle getRotation() {
        return rotation;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }
}
