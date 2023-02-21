package xyz.larkyy.aquaticmodelengine.model.player;

import org.bukkit.entity.Player;
import xyz.larkyy.aquaticmodelengine.AquaticModelEngine;
import xyz.larkyy.aquaticmodelengine.api.model.animation.AnimationHandlerImpl;
import xyz.larkyy.aquaticmodelengine.api.model.animation.PlayerAnimationHandlerImpl;
import xyz.larkyy.aquaticmodelengine.api.model.animation.RunningAnimation;
import xyz.larkyy.aquaticmodelengine.api.model.animation.TemplateAnimation;
import xyz.larkyy.aquaticmodelengine.api.model.holder.ModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.ModelBone;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.player.PlayerModel;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.player.TextureWrapper;
import xyz.larkyy.aquaticmodelengine.api.model.template.ModelTemplate;
import xyz.larkyy.aquaticmodelengine.api.model.template.TemplateBone;
import xyz.larkyy.aquaticmodelengine.api.model.template.player.LimbType;
import xyz.larkyy.aquaticmodelengine.model.RenderHandlerImpl;
import xyz.larkyy.aquaticmodelengine.model.spawned.bones.BasicBone;

import java.util.ArrayList;
import java.util.List;

public class PlayerModelImpl extends PlayerModel {

    public PlayerModelImpl(ModelTemplate modelTemplate, ModelHolder modelHolder, String url, boolean slim, TemplateAnimation preAnimation, TemplateAnimation animation, TemplateAnimation postAnimation) {
        super(modelTemplate, modelHolder, url, slim);

        setRenderHandler(new RenderHandlerImpl(this,15));
        setAnimationHandler(new PlayerAnimationHandlerImpl(this,preAnimation,animation,postAnimation));

        setBones(AquaticModelEngine.getInstance().getModelGenerator().getModelReader().loadModelBones(this));
        for (var bone : getBones().values()) {
            addParentBone(bone);
        }
    }

    public PlayerModelImpl(ModelTemplate modelTemplate, ModelHolder modelHolder, Player player, TemplateAnimation preAnimation, TemplateAnimation animation, TemplateAnimation postAnimation) {
        super(modelTemplate, modelHolder, TextureWrapper.fromBase64(AquaticModelEngine.getInstance().getNmsHandler().getTexture(player)));

        setRenderHandler(new RenderHandlerImpl(this,15));
        setAnimationHandler(new PlayerAnimationHandlerImpl(this,preAnimation,animation,postAnimation));

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
    public BasicBone addBone(TemplateBone templateBone) {
        var bone = new BasicBone(templateBone,this);
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
        if (!((PlayerAnimationHandlerImpl)getAnimationHandler()).isPlaying()) {
            return;
        }
        for (var bone : getParentBones().values()) {
            bone.tick(getModelHolder().getPivot(), getModelHolder().getRotation());
        }
        getRenderHandler().checkViewers();
    }

    @Override
    public ModelBone getBone(String name) {
        return this.getBones().get(name);
    }

    @Override
    public void applyModel() {
        if (!((PlayerAnimationHandlerImpl)getAnimationHandler()).isPlaying()) {
            return;
        }
        for (var model : getParentBones().values()) {
            model.spawnModel(getModelHolder().getPivot(), getModelHolder().getRotation());
        }
        tick();
    }

    @Override
    public void removeModel() {
        for (var model : getBones().values()) {
            model.removeModel();
        }
    }

    @Override
    public void spawn(Player player) {
        List<ModelBone> spawnedBones = new ArrayList<>();
        for (LimbType type : LimbType.values()) {
            if (getBones().containsKey(type.toString().toLowerCase())) {
                var bone = getBone(type.toString().toLowerCase());
                bone.show(player);
                spawnedBones.add(bone);
            }
        }
        for (ModelBone bone : getBones().values()) {
            if (spawnedBones.contains(bone)) {
                continue;
            }
            bone.show(player);
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
        //getAnimationHandler().playAnimation(name,speed);
    }

}
