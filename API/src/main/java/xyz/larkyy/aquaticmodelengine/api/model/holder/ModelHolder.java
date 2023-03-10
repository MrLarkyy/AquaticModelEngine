package xyz.larkyy.aquaticmodelengine.api.model.holder;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.api.event.EmoteEndEvent;
import xyz.larkyy.aquaticmodelengine.api.model.animation.AnimationPhase;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.player.PlayerModel;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class ModelHolder {

    private final Map<String, SpawnedModel> spawnedModels;
    private PlayerModel emote = null;

    public ModelHolder() {
        this.spawnedModels = new HashMap<>();
    }

    public Map<String, SpawnedModel> getSpawnedModels() {
        return spawnedModels;
    }

    public PlayerModel getEmote() {
        return emote;
    }

    public void stopEmote() {
        if (emote != null) {
            emote.removeModel();
            emote = null;
        }
    }

    public void setEmote(PlayerModel emote) {
        if (this.emote != null) {
            emote.removeModel();
        }
        this.emote = emote;
    }

    public boolean tick() {
        if (!checkNull()) {
            return false;
        }
        spawnedModels.values().forEach(SpawnedModel::tick);
        if (emote != null) {
            emote.tick();
            if (!(emote.getAnimationHandler().getRunningAnimations().values().stream().anyMatch(v -> v.getPhase()!= AnimationPhase.END))) {
                emote.removeModel();
                var event = new EmoteEndEvent(emote);
                Bukkit.getPluginManager().callEvent(event);
                emote = null;
            }
        }
        return true;
    }

    public abstract boolean checkNull();

    public void addModel(SpawnedModel spawnedModel) {
        if (spawnedModels.containsKey(spawnedModel.getModelTemplate().getName())) {
            return;
        }
        spawnedModels.put(spawnedModel.getModelTemplate().getName(),spawnedModel);
    }

    public abstract void teleport(Location location);
    public abstract Location getLocation();

    public abstract UUID getUniqueId();

    public abstract void remove();

    public EulerAngle getRotation() {
        return EulerAngle.ZERO;
    }

    public Vector getPivot() {
        return new Vector();
    }

    public float getBodyRotation() {
        return getLocation().getYaw();
    }

    public float getHeadRotation() {
        return getLocation().getYaw();
    }

}
