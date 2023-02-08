package xyz.larkyy.aquaticmodelengine;

import java.util.List;

public class Bone implements Part {

    private final List<Part> parts;
    private final Point pivot;

    public Bone(Point pivot, List<Part> parts) {
        this.parts = parts;
        this.pivot = pivot;
    }

    public List<Part> getParts() {
        return parts;
    }

    public Point getPivot() {
        return pivot;
    }
}
