package xyz.larkyy.aquaticmodelengine;

import org.bukkit.Bukkit;
import org.bukkit.entity.Pig;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.aquaticmodelengine.model.spawned.SpawnedModelImpl;

public class Listeners implements Listener {

    private SpawnedModelImpl spawnedModel = null;

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (e.getMessage().toLowerCase().contains("apply model")) {
                    Pig pig = e.getPlayer().getLocation().getWorld().spawn(e.getPlayer().getLocation(), Pig.class);
                    var holder = AquaticModelEngine.getInstance().getModelHandler().getModelHolder(pig);
                    if (e.getMessage().toLowerCase().contains("robot")) {
                        spawnedModel = AquaticModelEngine.getInstance().getModelHandler().spawnModel(holder,"tutorialbot");
                        spawnedModel.applyModel();
                        Bukkit.broadcastMessage("Applying model");
                        spawnedModel.playAnimation("idle",1);
                    } else if (e.getMessage().toLowerCase().contains("otter")) {
                        spawnedModel = AquaticModelEngine.getInstance().getModelHandler().spawnModel(holder,"big_otter");
                        spawnedModel.applyModel();
                        Bukkit.broadcastMessage("Applying model");
                        spawnedModel.playAnimation("death",1);
                    }
                    else {
                        spawnedModel = AquaticModelEngine.getInstance().getModelHandler().spawnModel(holder,"test2");
                        spawnedModel.applyModel();
                        Bukkit.broadcastMessage("Applying model");
                        spawnedModel.playAnimation("testanimation",1);
                    }

                } else if (e.getMessage().toLowerCase().contains("remove models")) {
                    if (spawnedModel != null) {
                        AquaticModelEngine.getInstance().getModelHandler().getModelHolders().values().forEach(holder ->
                                AquaticModelEngine.getInstance().getModelHandler().deleteHolder(holder));
                        Bukkit.broadcastMessage("Removing models");
                    }
                }
            }
        }.runTask(AquaticModelEngine.getInstance());

    }

}
