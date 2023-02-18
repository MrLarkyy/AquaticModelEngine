package xyz.larkyy.aquaticmodelengine;

import org.bukkit.Bukkit;
import org.bukkit.entity.Pig;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.aquaticmodelengine.api.IEntityHandler;
import xyz.larkyy.aquaticmodelengine.generator.ModelGenerator;
import xyz.larkyy.aquaticmodelengine.model.ModelHandler;
import xyz.larkyy.aquaticmodelengine.nms.EntityHandlerImpl;

import java.io.File;

public final class AquaticModelEngine extends JavaPlugin {

    private static AquaticModelEngine instance;
    private ModelGenerator modelGenerator;
    private ModelHandler modelHandler;

    private IEntityHandler entityHandler;

    @Override
    public void onEnable() {
        instance = this;
        getDataFolder().mkdirs();
        File file = new File(getDataFolder()+"/models");
        file.mkdirs();
        entityHandler = new EntityHandlerImpl();
        modelGenerator = new ModelGenerator();

        new BukkitRunnable() {
            @Override
            public void run() {
                modelGenerator.generateModels();

                modelHandler = new ModelHandler();

                modelHandler.startTicking();
            }
        }.runTaskLater(this,1);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static AquaticModelEngine getInstance() {
        return instance;
    }

    public ModelGenerator getModelGenerator() {
        return modelGenerator;
    }

    public ModelHandler getModelHandler() {
        return modelHandler;
    }

    public IEntityHandler getEntityHandler() {
        return entityHandler;
    }
}
