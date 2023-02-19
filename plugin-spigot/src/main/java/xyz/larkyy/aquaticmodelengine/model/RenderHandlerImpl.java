package xyz.larkyy.aquaticmodelengine.model;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.api.model.RenderHandler;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;

import java.util.*;

public class RenderHandlerImpl extends RenderHandler {

    public RenderHandlerImpl(SpawnedModel spawnedModel, int viewDistance) {
        super(spawnedModel,viewDistance);
        setFilter((p) -> true);
    }

    @Override
    public boolean checkCanBeSeen(Player player) {
        Location loc = getSpawnedModel().getModelHolder().getLocation();

        if (!player.getWorld().equals(loc.getWorld())) {
            return false;
        }
        var eVec = loc.toVector();
        var pVec = player.getLocation().toVector();

        var vec = eVec.subtract(pVec);

        var minVec = new Vector(-getRenderDistance(),-getRenderDistance(),-getRenderDistance());
        var maxVec = new Vector(getRenderDistance(),getRenderDistance(),getRenderDistance());

        return (vec.isInAABB(minVec,maxVec));
    }

    @Override
    public void checkViewers() {
        if (isHidden()) {
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

    @Override
    public void show() {
        setHidden(false);
        checkViewers();
    }

    @Override
    public void hide() {
        for (UUID uuid : new ArrayList<>(getSeenBy())) {
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
        setHidden(true);
    }

    @Override
    public void show(Player player) {
        if (getSeenBy().contains(player.getUniqueId())) {
            return;
        }
        if (isHidden()) {
            return;
        }

        if (!getFilter().apply(player)) {
            return;
        }

        getSeenBy().add(player.getUniqueId());

        getSpawnedModel().getBones().values().forEach(bone -> {
            bone.show(player);
        });
    }

    @Override
    public void hide(Player player) {
        if (!getSeenBy().contains(player.getUniqueId())) {
            return;
        }
        getSeenBy().remove(player.getUniqueId());

        getSpawnedModel().getBones().values().forEach(bone -> {
            bone.hide(player);
        });
    }

    @Override
    public void removeViewerFromCache(OfflinePlayer player) {
        getSeenBy().remove(player.getUniqueId());
    }

    @Override
    public void blacklistPlayer(Player player) {
        getBlacklist().add(player.getUniqueId());
        hide(player);
    }

    @Override
    public void unblacklistPlayer(Player player) {
        getBlacklist().remove(player.getUniqueId());
        show(player);
    }
}
