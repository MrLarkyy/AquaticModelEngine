package xyz.larkyy.aquaticmodelengine.model;

import org.bukkit.scheduler.BukkitRunnable;

public class ModelTicker extends BukkitRunnable {

    private final ModelHandler modelHandler;

    public ModelTicker(ModelHandler modelHandler) {
        this.modelHandler = modelHandler;
    }

    @Override
    public void run() {
        modelHandler.tickModels();
    }
}
