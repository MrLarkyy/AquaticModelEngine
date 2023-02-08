package xyz.larkyy.aquaticmodelengine;

public class Size {

    private final double sizeX;
    private final double sizeY;
    private final double sizeZ;

    public Size(double sizeX, double sizeY, double sizeZ) {
        this.sizeX = sizeX;
        this.sizeY = sizeY;
        this.sizeZ = sizeZ;
    }
    public Size(Point point) {
        this.sizeX = point.getX();
        this.sizeY = point.getY();
        this.sizeZ = point.getZ();
    }

    public double getSizeX() {
        return sizeX;
    }

    public double getSizeY() {
        return sizeY;
    }

    public double getSizeZ() {
        return sizeZ;
    }
}
