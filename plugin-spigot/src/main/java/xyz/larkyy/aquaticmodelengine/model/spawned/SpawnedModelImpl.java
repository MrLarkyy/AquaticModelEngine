package xyz.larkyy.aquaticmodelengine.model.spawned;

import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.AquaticModelEngine;
import xyz.larkyy.aquaticmodelengine.api.model.animation.AnimationHandlerImpl;
import xyz.larkyy.aquaticmodelengine.api.model.ModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.api.model.template.ModelTemplateImpl;
import xyz.larkyy.aquaticmodelengine.api.model.template.TemplateBone;
import xyz.larkyy.aquaticmodelengine.model.RenderHandlerImpl;


public class SpawnedModelImpl extends SpawnedModel {


    public SpawnedModelImpl(ModelHolder modelHolder, ModelTemplateImpl template) {
        super(modelHolder,template);

        setRenderHandler(new RenderHandlerImpl(this,15));
        setAnimationHandler(new AnimationHandlerImpl(this));

        setBones(AquaticModelEngine.getInstance().getModelGenerator().getModelReader().loadModelBones(this));
        for (var bone : getBones().values()) {
            addParentBone(bone);
        }
    }


    @Override
    public void addParentBone(ModelBone modelBone) {
        if (modelBone.getParent() == null) {
            getParentBones().put(modelBone.getTemplateBone().getName(),modelBone);
        }
    }

    @Override
    public ModelBoneImpl addBone(TemplateBone templateBone) {
        var bone = new ModelBoneImpl(templateBone,this);
        getBones().put(templateBone.getName(),bone);
        return bone;
    }

    @Override
    public void removeBone(String boneName) {
        var bone = getBones().remove(boneName);
        if (bone == null) {
            return;
        }
        bone.removeModel();
    }

    @Override
    public void tick() {
        getAnimationHandler().update();
        /*
        for (var bone : bones.values()) {
            bone.tick();
        }
         */
        for (var bone : getParentBones().values()) {
            bone.tick(new Vector(), EulerAngle.ZERO);
        }
        getRenderHandler().checkViewers();
    }

    @Override
    public ModelBone getBone(String name) {
        return this.getBones().get(name);
    }

    @Override
    public void applyModel() {
        for (var model : getParentBones().values()) {
            model.spawnModel(new Vector(), EulerAngle.ZERO);
        }
    }

    @Override
    public void removeModel() {
        for (var model : getBones().values()) {
            model.removeModel();
        }
    }

    @Override
    public void show() {
        getRenderHandler().show();
    }

    @Override
    public void hide() {
        getRenderHandler().hide();
    }

    @Override
    public void show(Player player) {
        getRenderHandler().show(player);
    }

    @Override
    public void hide(Player player) {
        getRenderHandler().hide(player);
    }

    @Override
    public void playAnimation(String name, double speed) {
        getAnimationHandler().playAnimation(name,speed);
    }

}
