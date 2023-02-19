package xyz.larkyy.aquaticmodelengine;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.larkyy.aquaticmodelengine.api.AquaticModelEngineAPI;
import xyz.larkyy.aquaticmodelengine.api.IEntityHandler;
import xyz.larkyy.aquaticmodelengine.api.INMSHandler;
import xyz.larkyy.aquaticmodelengine.generator.ModelGenerator;
import xyz.larkyy.aquaticmodelengine.model.ModelHandler;
import xyz.larkyy.aquaticmodelengine.nms.nms1_19_2.NMSHandler;
import xyz.larkyy.aquaticmodelengine.nms.nms1_19_3.EntityHandlerImpl;

import java.io.File;

public final class AquaticModelEngine extends JavaPlugin {

    private static AquaticModelEngine instance;
    private ModelGenerator modelGenerator;
    private ModelHandler modelHandler;
    private INMSHandler nmsHandler;

    private IEntityHandler entityHandler;

    @Override
    public void onLoad() {
        modelHandler = new ModelHandler();
        AquaticModelEngineAPI.api = new AquaticModelEngineAPI(modelHandler);
    }
    @Override
    public void onEnable() {
        instance = this;
        getDataFolder().mkdirs();
        File file = new File(getDataFolder()+"/models");
        file.mkdirs();
        switch (getServer().getBukkitVersion()) {
            case "1.19.2-R0.1-SNAPSHOT" -> {
                entityHandler = new xyz.larkyy.aquaticmodelengine.nms.nms1_19_2.EntityHandlerImpl();
                nmsHandler = new NMSHandler();
            }
            case "1.19.3-R0.1-SNAPSHOT" -> {
                entityHandler = new EntityHandlerImpl();
            }
        }
        modelGenerator = new ModelGenerator();

        new BukkitRunnable() {
            @Override
            public void run() {
                modelGenerator.generateModels();


                modelHandler.startTicking();
                getServer().getPluginManager().registerEvents(new Listeners(),AquaticModelEngine.this);
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

    public INMSHandler getNmsHandler() {
        return nmsHandler;
    }
}
