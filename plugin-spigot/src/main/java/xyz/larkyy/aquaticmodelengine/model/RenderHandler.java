package xyz.larkyy.aquaticmodelengine.model;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.model.spawned.SpawnedModel;

import java.util.*;
import java.util.function.Function;

public class RenderHandler {

    private final List<UUID> seenBy = new ArrayList<>();
    private final List<UUID> blacklist = new ArrayList<>();
    private Function<Player,Boolean> filter;

    private boolean hidden = false;

    private final double viewDistance;
    private final SpawnedModel spawnedModel;

    public RenderHandler(SpawnedModel spawnedModel, int viewDistance) {
        this.viewDistance = viewDistance;
        this.spawnedModel = spawnedModel;
        this.filter = (p) -> true;
    }

    public boolean checkCanBeSeen(Player player) {
        Entity e = spawnedModel.getBoundEntity();

        if (!player.getWorld().equals(e.getWorld())) {
            return false;
        }
        var eVec = e.getLocation().toVector();
        var pVec = player.getLocation().toVector();

        var vec = eVec.subtract(pVec);

        var minVec = new Vector(-viewDistance,-viewDistance,-viewDistance);
        var maxVec = new Vector(viewDistance,viewDistance,viewDistance);

        return (vec.isInAABB(minVec,maxVec));
    }

    public void checkViewers() {
        if (hidden) {
            return;
        }
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!checkCanBeSeen(player)) {
                hide(player);
                continue;
            }
            show(player);
        }
    }

    public void show() {
        hidden = false;
        checkViewers();
    }

    public void hide() {
        for (UUID uuid : new ArrayList<>(seenBy)) {
            var offlinePlayer = Bukkit.getOfflinePlayer(uuid);
            if (!offlinePlayer.isOnline()) {
                removeViewerFromCache(offlinePlayer);
                continue;
            }
            var player = offlinePlayer.getPlayer();
            if (player == null) {
                removeViewerFromCache(offlinePlayer);
                continue;
            }
            hide(player);
        }
        hidden = true;
    }

    public void show(Player player) {
        if (seenBy.contains(player.getUniqueId())) {
            return;
        }
        if (hidden) {
            return;
        }

        if (!filter.apply(player)) {
            return;
        }

        seenBy.add(player.getUniqueId());

        spawnedModel.getBones().values().forEach(bone -> {
            bone.show(player);
        });
    }

    public void hide(Player player) {
        if (!seenBy.contains(player.getUniqueId())) {
            return;
        }
        seenBy.remove(player.getUniqueId());

        spawnedModel.getBones().values().forEach(bone -> {
            bone.hide(player);
        });
    }

    public void removeViewerFromCache(OfflinePlayer player) {
        seenBy.remove(player.getUniqueId());
    }

    public void blacklistPlayer(Player player) {
        blacklist.add(player.getUniqueId());
        hide(player);
    }
    public void unblacklistPlayer(Player player) {
        blacklist.remove(player.getUniqueId());
        show(player);
    }

    public void setFilter(Function<Player, Boolean> filter) {
        this.filter = filter;
    }

    public List<UUID> getSeenBy() {
        return Collections.unmodifiableList(seenBy);
    }
}
