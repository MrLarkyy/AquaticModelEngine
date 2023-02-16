package xyz.larkyy.aquaticmodelengine.model.spawned;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.AquaticModelEngine;
import xyz.larkyy.aquaticmodelengine.animation.AnimationHandler;
import xyz.larkyy.aquaticmodelengine.model.RenderHandler;
import xyz.larkyy.aquaticmodelengine.model.template.ModelTemplate;
import xyz.larkyy.aquaticmodelengine.model.template.TemplateBone;

import java.util.HashMap;
import java.util.Map;

public class SpawnedModel {

    private final Entity boundEntity;
    private final ModelTemplate modelTemplate;
    private final AnimationHandler animationHandler;

    private RenderHandler renderHandler;

    private final Map<String,ModelBone> bones;
    private final Map<String,ModelBone> parentBones = new HashMap<>();

    public SpawnedModel(Entity boundEntity, ModelTemplate template) {
        this.boundEntity = boundEntity;
        this.modelTemplate = template;

        bones = AquaticModelEngine.getInstance().getModelGenerator().getModelReader().loadModelBones(this);
        for (var bone : bones.values()) {
            addParentBone(bone);
        }
        animationHandler = new AnimationHandler(this);
        renderHandler = new RenderHandler(this,15);
    }

    public ModelTemplate getModelTemplate() {
        return modelTemplate;
    }

    public void addParentBone(ModelBone modelBone) {
        if (modelBone.getParent() == null) {
            parentBones.put(modelBone.getTemplateBone().getName(),modelBone);
            Bukkit.broadcastMessage("Adding parent");
        }
    }

    public ModelBone addBone(TemplateBone templateBone) {
        var bone = new ModelBone(templateBone,this);
        bones.put(templateBone.getName(),bone);
        return bone;
    }

    public void removeBone(String boneName) {
        var bone = bones.remove(boneName);
        if (bone == null) {
            return;
        }
        bone.removeModel();
    }

    public void tick() {
        animationHandler.update();
        /*
        for (var bone : bones.values()) {
            bone.tick();
        }
         */
        for (var bone : parentBones.values()) {
            bone.tick2(new Vector(), EulerAngle.ZERO);
        }
        renderHandler.checkViewers();
    }
    public ModelBone getBone(String name) {
        return this.bones.get(name);
    }

    public void applyModel() {
        /*
        for (var model : bones.values()) {
            model.spawnModel(boundEntity.getLocation().clone());
        }
         */
        for (var model : parentBones.values()) {
            model.spawnModel2(new Vector(), EulerAngle.ZERO);
        }
    }

    public void removeModel() {
        for (var model : bones.values()) {
            model.removeModel();
        }
    }

    public Map<String, ModelBone> getBones() {
        return bones;
    }

    public Entity getBoundEntity() {
        return boundEntity;
    }

    public AnimationHandler getAnimationHandler() {
        return animationHandler;
    }

    public RenderHandler getRenderHandler() {
        return renderHandler;
    }

    public void setRenderHandler(RenderHandler renderHandler) {
        this.renderHandler = renderHandler;
    }

    public void show() {
        renderHandler.show();
    }

    public void hide() {
        renderHandler.hide();
    }

    public void show(Player player) {
        renderHandler.show(player);
    }

    public void hide(Player player) {
        renderHandler.hide(player);
    }

    public void playAnimation(String name, double speed) {
        getAnimationHandler().playAnimation(name,speed);
    }

}
