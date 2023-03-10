package xyz.larkyy.aquaticmodelengine.api.model.spawned;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.api.model.holder.ModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.RenderHandler;
import xyz.larkyy.aquaticmodelengine.api.model.animation.AnimationHandler;
import xyz.larkyy.aquaticmodelengine.api.model.template.ModelTemplate;
import xyz.larkyy.aquaticmodelengine.api.model.template.TemplateBone;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class SpawnedModel {

    private final ModelHolder modelHolder;
    private final ModelTemplate modelTemplate;
    private RenderHandler renderHandler = null;
    private Map<String,ModelBone> bones = new HashMap<>();
    private final Map<String,ModelBone> parentBones = new HashMap<>();
    private AnimationHandler animationHandler = null;

    private final Map<UUID,Vector> playerOffsets = new HashMap<>();
    public Map<UUID,Vector> getPlayerOffsets() {
        return playerOffsets;
    }

    public Vector getPlayerOffset(Player player) {
        var offset = playerOffsets.get(player.getUniqueId());
        if (offset == null) {
            offset = new Vector();
        }
        return offset.clone();
    }
    public void setPlayerOffset(Player player, Vector holderOffset) {
        playerOffsets.put(player.getUniqueId(),holderOffset);
    }

    public SpawnedModel(ModelHolder modelHolder, ModelTemplate modelTemplate) {
        this.modelHolder = modelHolder;
        this.modelTemplate = modelTemplate;
    }

    public void setBones(Map<String, ModelBone> bones) {
        this.bones = bones;
    }

    public void setAnimationHandler(AnimationHandler animationHandler) {
        this.animationHandler = animationHandler;
    }

    public ModelHolder getModelHolder() {
        return modelHolder;
    }

    public ModelTemplate getModelTemplate() {
        return modelTemplate;
    }

    public abstract void addParentBone(ModelBone modelBone);

    public abstract ModelBone addBone(TemplateBone templateBone);

    public abstract void removeBone(String boneName);

    public abstract void tick();

    public abstract ModelBone getBone(String name);

    public abstract void applyModel();

    public abstract void removeModel();

    public Map<String, ModelBone> getBones() {
        return bones;
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

    public abstract void spawn(Player player);

    public abstract void show();

    public abstract void hide();

    public abstract void show(Player player);

    public abstract void hide(Player player);

    public abstract void playAnimation(String name, double speed);

    public Map<String, ModelBone> getParentBones() {
        return parentBones;
    }
}
