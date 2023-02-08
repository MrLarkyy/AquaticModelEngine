package xyz.larkyy.aquaticmodelengine.model.template;

import java.util.ArrayList;
import java.util.List;

public class ModelTemplate {

    private final String name;
    private final List<TemplateBone> bones = new ArrayList<>();

    public ModelTemplate(String name) {
        this.name = name;
    }

    public List<TemplateBone> getBones() {
        return bones;
    }

    public String getName() {
        return name;
    }
}
