package xyz.larkyy.aquaticmodelengine;

import org.bukkit.plugin.java.JavaPlugin;
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

        //ModelSerializer serializer = new ModelSerializer(this);
        //serializer.loadModels();
        modelGenerator = new ModelGenerator();
        modelGenerator.generateModels();

        modelHandler = new ModelHandler();

        getServer().getPluginManager().registerEvents(new Listeners(),this);

        modelHandler.startTicking();

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
