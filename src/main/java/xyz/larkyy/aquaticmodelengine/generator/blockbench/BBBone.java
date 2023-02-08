package xyz.larkyy.aquaticmodelengine.generator.blockbench;

import java.util.ArrayList;
import java.util.List;

public class BBBone {

    private final String uuid;
    private final String name;
    private final double[] origin;
    private final double[] rotation;

    private final transient List<BBBone> subbones;
    private final transient List<BBElement> elementList;

    public BBBone(String uuid, String name, double[] origin, double[] rotation) {
        this.uuid = uuid;
        this.name = name;
        this.origin = origin;
        this.rotation = rotation;
        subbones = new ArrayList<>();
        elementList = new ArrayList<>();
    }

    private BBBone() {
        this.uuid = "";
        this.name = "";
        this.origin = new double[]{};
        this.rotation = new double[]{};
        subbones = new ArrayList<>();
        elementList = new ArrayList<>();
    }

    public double[] getRotation() {
        return rotation;
    }

    public double[] getOrigin() {
        return origin;
    }

    public String getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public List<BBElement> getElements() {
        return elementList;
    }

    public List<BBBone> getChildren() {
        return subbones;
    }
}
