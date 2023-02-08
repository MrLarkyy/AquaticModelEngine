package xyz.larkyy.aquaticmodelengine;

public class Cube implements Part {

    private final Size size;
    private final Point pivot;
    private final Point rotation;
    private final String uuid;

    public Cube(String uuid, Size size, Point rotation, Point pivot) {
        this.pivot = pivot;
        this.size = size;
        this.rotation = rotation;
        this.uuid = uuid;
    }

    public Point getPivot() {
        return pivot;
    }

    public Size getSize() {
        return size;
    }

    public Point getRotation() {
        return rotation;
    }

    public String getUuid() {
        return uuid;
    }
}
