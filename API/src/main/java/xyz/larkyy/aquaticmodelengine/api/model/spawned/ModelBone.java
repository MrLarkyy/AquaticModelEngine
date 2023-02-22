package xyz.larkyy.aquaticmodelengine.api.model.spawned;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.api.math.Quaternion;
import xyz.larkyy.aquaticmodelengine.api.model.holder.impl.AttachmentModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.template.TemplateBone;

import java.util.ArrayList;
import java.util.List;

public abstract class ModelBone {

    private ModelBone parent = null;
    private final TemplateBone templateBone;
    private final SpawnedModel spawnedModel;
    private final AttachmentModelHolder attachmentModelHolder;
    private BoneEntity boneEntity = null;
    private final List<ModelBone> children = new ArrayList<>();

    public ModelBone(TemplateBone templateBone, SpawnedModel spawnedModel) {
        this.templateBone = templateBone;
        this.spawnedModel = spawnedModel;

        attachmentModelHolder = new AttachmentModelHolder(this);
    }

    public SpawnedModel getSpawnedModel() {
        return spawnedModel;
    }

    public AttachmentModelHolder getAttachmentModelHolder() {
        return attachmentModelHolder;
    }

    public ModelBone getParent() {
        return parent;
    }

    public TemplateBone getTemplateBone() {
        return templateBone;
    }

    public List<ModelBone> getChildren() {
        return children;
    }

    public void setParent(ModelBone parent) {
        this.parent = parent;
    }

    public BoneEntity getBoneEntity() {
        return boneEntity;
    }

    public void setBoneEntity(BoneEntity boneEntity) {
        this.boneEntity = boneEntity;
    }

    public abstract void tick(Vector parentPivot, EulerAngle parentAngle);

    public abstract void spawnModel(Vector parentPivot, EulerAngle parentAngle);

    public abstract void removeModel();

    public abstract void teleport(Location location);

    public abstract EulerAngle getFinalRotation(EulerAngle parentAngle);

    public abstract Vector getFinalPivot(Vector parentPivot, EulerAngle parentRotation);

    public abstract void show(Player player);

    public abstract void hide(Player player);

}
