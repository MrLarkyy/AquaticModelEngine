package xyz.larkyy.aquaticmodelengine;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class Listeners implements Listener {


    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        if (e.getMessage().toLowerCase().contains("play emote open")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getPlayer().setInvisible(true);
                    var holder = AquaticModelEngine.getInstance().getModelHandler().getModelHolder(e.getPlayer());
                    var spawned = AquaticModelEngine.getInstance().getModelHandler().spawnEmote(holder,e.getPlayer(),"openemote");
                    spawned.playAnimation("open",1);
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            e.getPlayer().setInvisible(false);
                        }
                    }.runTaskLater(AquaticModelEngine.getInstance(),60);
                }
            }.runTask(AquaticModelEngine.getInstance());
        }
        if (e.getMessage().toLowerCase().contains("play emote wave")) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    e.getPlayer().setInvisible(true);
                    var holder = AquaticModelEngine.getInstance().getModelHandler().getModelHolder(e.getPlayer());
                    var spawned = AquaticModelEngine.getInstance().getModelHandler().spawnEmote(holder,e.getPlayer(),"steve2");
                    spawned.playAnimation("wave",1);
                    var bone = spawned.getBone("head");
                    AquaticModelEngine.getInstance().getModelHandler().attachModel(bone,"big_otter");
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            e.getPlayer().setInvisible(false);
                        }
                    }.runTaskLater(AquaticModelEngine.getInstance(),60);

                }
            }.runTask(AquaticModelEngine.getInstance());
        }
    }
}
