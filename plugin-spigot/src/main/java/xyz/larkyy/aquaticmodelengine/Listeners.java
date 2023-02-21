package xyz.larkyy.aquaticmodelengine;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.aquaticmodelengine.api.event.EmoteEndEvent;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;

public class Listeners implements Listener {

    private SpawnedModel spawnedModel;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (e.getMessage().toLowerCase().contains("apply model open")) {
            new BukkitRunnable() {
                @Override
                public void run() {

                    var holder = AquaticModelEngine.getInstance().getModelHandler().getModelHolder(e.getPlayer());
                    var spawned = AquaticModelEngine.getInstance().getModelHandler().spawnModel(holder,"openemote");
                    spawned.playAnimation("open",1);
                }
            }.runTask(AquaticModelEngine.getInstance());
        }

        if (e.getMessage().toLowerCase().contains("play emote open")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getPlayer().setInvisible(true);
                    var holder = AquaticModelEngine.getInstance().getModelHandler().getModelHolder(e.getPlayer());
                    AquaticModelEngine.getInstance().getModelHandler().spawnEmote(
                            holder,
                            e.getPlayer(),
                            "openemote",
                            null,
                            "open",
                            null);
                }
            }.runTask(AquaticModelEngine.getInstance());
        }
        if (e.getMessage().toLowerCase().contains("play emote wave")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getPlayer().setInvisible(true);
                    var holder = AquaticModelEngine.getInstance().getModelHandler().getModelHolder(e.getPlayer());
                    var spawned = AquaticModelEngine.getInstance().getModelHandler().spawnEmote(
                            holder,
                            e.getPlayer(),
                            "steve2",
                            null,
                            "wave",
                            null);
                    var bone = spawned.getBone("head");
                    AquaticModelEngine.getInstance().getModelHandler().attachModel(bone,"big_otter");

                }
            }.runTask(AquaticModelEngine.getInstance());
        }
        if (e.getMessage().toLowerCase().contains("play emote glider")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getPlayer().setInvisible(true);
                    var holder = AquaticModelEngine.getInstance().getModelHandler().getModelHolder(e.getPlayer());
                    spawnedModel = AquaticModelEngine.getInstance().getModelHandler().spawnEmote(
                            holder,
                            e.getPlayer(),
                            "glideremote",
                            "pre",
                            "animation",
                            null);

                }
            }.runTask(AquaticModelEngine.getInstance());
        }

        if (e.getMessage().toLowerCase().contains("stop emote")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    if (spawnedModel == null) {
                        return;
                    }
                    spawnedModel.getAnimationHandler().stopAnimation("animation");

                }
            }.runTask(AquaticModelEngine.getInstance());
        }
    }

    @EventHandler
    public void onEmoteEnd(EmoteEndEvent e) {
        Bukkit.getPlayer("MrLarkyy_").setInvisible(false);
    }
}
