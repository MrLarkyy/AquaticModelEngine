package xyz.larkyy.aquaticmodelengine;

import java.util.List;

public class Model {

    private final List<Bone> bones;

    public Model(List<Bone> bones) {
        this.bones = bones;
    }

    public List<Bone> getBones() {
        return bones;
    }
}
