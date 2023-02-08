package xyz.larkyy.aquaticmodelengine.generator.blockbench;

import org.bukkit.util.EulerAngle;

public class BBElement {

    private final double[] from;
    private final double[] to;
    private final double[] rotation;
    private final double[] origin;
    private final String type;
    private final String uuid;
    private final String name;
    private final BBFaces faces;

    public BBElement(String uuid, String type, String name, double[] from, double[] to,
                     double[] rotation, double[] origin, BBFaces faces) {
        this.uuid = uuid;
        this.type = type;
        this.name = name;
        this.from = from;
        this.to = to;
        this.rotation = rotation;
        this.origin = origin;
        this.faces = faces;
    }

    public String getName() {
        return name;
    }

    public String getUuid() {
        return uuid;
    }

    public String getType() {
        return type;
    }

    public double[] getFrom() {
        return from;
    }

    public double[] getOrigin() {
        return origin;
    }

    public double[] getRotation() {
        return rotation;
    }

    public double[] getTo() {
        return to;
    }

    public BBFaces getFaces() {
        return faces;
    }

    public EulerAngle getEulerAngle() {
        if (rotation == null || rotation.length < 3) {
            return new EulerAngle(0,0,0);
        }
        return new EulerAngle(Math.toRadians(rotation[0]),Math.toRadians(rotation[1]),Math.toRadians(rotation[2]));
    }
}
