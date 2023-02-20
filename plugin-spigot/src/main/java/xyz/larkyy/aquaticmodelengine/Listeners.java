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
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            var holder = AquaticModelEngine.getInstance().getModelHandler().getModelHolder(e.getPlayer());
                            var spawned = AquaticModelEngine.getInstance().getModelHandler().spawnEmote(holder,"http://textures.minecraft.net/texture/dbf21e3f9164bfd9e745f84ebce9d346c0c946f0bb5c24509af3fd29743506a3",false,"steve2");
                            spawned.playAnimation("wave",1);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    e.getPlayer().setInvisible(false);
                                }
                            }.runTaskLater(AquaticModelEngine.getInstance(),60);
                        }
                    }.runTaskLater(AquaticModelEngine.getInstance(),1);

                }
            }.runTask(AquaticModelEngine.getInstance());
        }
    }
}
