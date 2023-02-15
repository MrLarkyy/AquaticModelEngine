package xyz.larkyy.aquaticmodelengine.model.template;

import xyz.larkyy.aquaticmodelengine.animation.TemplateAnimation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelTemplate {

    private final String name;
    private final List<TemplateBone> bones = new ArrayList<>();
    private final Map<String, TemplateAnimation> animations = new HashMap<>();

    public Map<String, TemplateAnimation> getAnimations() {
        return animations;
    }

    public TemplateAnimation getAnimation(String string) {
        return animations.get(string);
    }

    public void addAnimation(TemplateAnimation templateAnimation) {
        animations.put(templateAnimation.getName(),templateAnimation);
    }

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
