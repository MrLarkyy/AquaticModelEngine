package xyz.larkyy.aquaticmodelengine;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.aquaticmodelengine.model.spawned.SpawnedModel;

public class Listeners implements Listener {

    private SpawnedModel spawnedModel = null;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (e.getMessage().toLowerCase().contains("apply model")) {
                    if (e.getMessage().toLowerCase().contains("robot")) {
                        spawnedModel = AquaticModelEngine.getInstance().getModelHandler().spawnModel(e.getPlayer(),"tutorialbot");
                        spawnedModel.applyModel();
                        Bukkit.broadcastMessage("Applying model");
                        spawnedModel.playAnimation("idle",1);
                    } else if (e.getMessage().toLowerCase().contains("otter")) {
                        spawnedModel = AquaticModelEngine.getInstance().getModelHandler().spawnModel(e.getPlayer(),"big_otter");
                        spawnedModel.applyModel();
                        Bukkit.broadcastMessage("Applying model");
                        spawnedModel.playAnimation("death",1);
                    }
                    else {
                        spawnedModel = AquaticModelEngine.getInstance().getModelHandler().spawnModel(e.getPlayer(),"test2");
                        spawnedModel.applyModel();
                        Bukkit.broadcastMessage("Applying model");
                        spawnedModel.playAnimation("testanimation",1);
                    }

                } else if (e.getMessage().toLowerCase().contains("remove model")) {
                    if (spawnedModel != null) {
                        AquaticModelEngine.getInstance().getModelHandler().despawnModel(spawnedModel);
                        Bukkit.broadcastMessage("Removing model");
                    }
                }
            }
        }.runTask(AquaticModelEngine.getInstance());

    }

}
