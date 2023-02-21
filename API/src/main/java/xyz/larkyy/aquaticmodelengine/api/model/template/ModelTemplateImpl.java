package xyz.larkyy.aquaticmodelengine.api.model.template;

import xyz.larkyy.aquaticmodelengine.api.model.animation.TemplateAnimation;

public class ModelTemplateImpl extends ModelTemplate {

    public void addAnimation(TemplateAnimation templateAnimation) {
        getAnimations().put(templateAnimation.getName(),templateAnimation);
    }

    public ModelTemplateImpl(String name) {
        super(name);
    }

    @Override
    public TemplateAnimation getAnimation(String string) {
        return getAnimations().get(string);
    }
}
