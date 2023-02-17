package xyz.larkyy.aquaticmodelengine.api.model;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Function;

public abstract class RenderHandler {

    private final SpawnedModel spawnedModel;
    private double renderDistance;

    private final List<UUID> seenBy = new ArrayList<>();
    private final List<UUID> blacklist = new ArrayList<>();
    private Function<Player,Boolean> filter;

    private boolean hidden = false;
    public RenderHandler(SpawnedModel spawnedModel, double distance) {
        this.spawnedModel = spawnedModel;
        this.renderDistance = distance;
    }

    public void setRenderDistance(double renderDistance) {
        this.renderDistance = renderDistance;
    }

    public SpawnedModel getSpawnedModel() {
        return spawnedModel;
    }

    public double getRenderDistance() {
        return renderDistance;
    }

    public abstract boolean checkCanBeSeen(Player player);

    public abstract void checkViewers();

    public abstract void show();

    public abstract void hide();

    public abstract void show(Player player);

    public abstract void hide(Player player);

    public abstract void removeViewerFromCache(OfflinePlayer player);

    public abstract void blacklistPlayer(Player player);

    public abstract void unblacklistPlayer(Player player);

    public void setFilter(Function<Player, Boolean> filter) {
        this.filter = filter;
    }

    public boolean isHidden() {
        return hidden;
    }

    public List<UUID> getBlacklist() {
        return blacklist;
    }

    public Function<Player, Boolean> getFilter() {
        return filter;
    }

    public List<UUID> getSeenBy() {
        return seenBy;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }
}
