package xyz.larkyy.aquaticmodelengine.api.model.template;

import xyz.larkyy.aquaticmodelengine.api.model.animation.TemplateAnimation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class ModelTemplate {

    private final String name;
    private final List<TemplateBone> bones = new ArrayList<>();
    private final Map<String, TemplateAnimation> animations = new HashMap<>();

    public ModelTemplate(String name) {
        this.name = name;
    }


    public List<TemplateBone> getBones() {
        return bones;
    }

    public Map<String, TemplateAnimation> getAnimations() {
        return animations;
    }

    public String getName() {
        return name;
    }

    public abstract TemplateAnimation getAnimation(String string);
}
