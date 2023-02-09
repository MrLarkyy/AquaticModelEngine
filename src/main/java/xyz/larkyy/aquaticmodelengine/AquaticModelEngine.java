package xyz.larkyy.aquaticmodelengine;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.EulerAngle;
import org.joml.Matrix3f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import xyz.larkyy.aquaticmodelengine.generator.ModelGenerator;
import xyz.larkyy.aquaticmodelengine.model.ModelHandler;
import xyz.larkyy.aquaticmodelengine.util.math.Quaternion;

import java.io.File;

public final class AquaticModelEngine extends JavaPlugin {

    private static AquaticModelEngine instance;
    private ModelGenerator modelGenerator;
    private ModelHandler modelHandler;
    @Override
    public void onEnable() {
        instance = this;
        getDataFolder().mkdirs();
        File file = new File(getDataFolder()+"/models");
        file.mkdirs();

        //ModelSerializer serializer = new ModelSerializer(this);
        //serializer.loadModels();
        modelGenerator = new ModelGenerator();
        modelGenerator.generateModels();

        modelHandler = new ModelHandler();

        getServer().getPluginManager().registerEvents(new Listeners(),this);

        EulerAngle startAngle = new EulerAngle(Math.toRadians(45),Math.toRadians(0),Math.toRadians(0));
        EulerAngle rotationAngle = new EulerAngle(Math.toRadians(0),Math.toRadians(0),Math.toRadians(-90));

        Quaternion startQuat = new Quaternion(startAngle);
        Quaternion rotationQuat = new Quaternion(rotationAngle);

        Quaternion resultQuat = rotationQuat.mul(startQuat);
        var resultEuler = resultQuat.getEulerAnglesXYZ();

        Bukkit.broadcastMessage("Rotated: "+Math.toDegrees(resultEuler.getX())+" "+Math.toDegrees(resultEuler.getY())+" "+Math.toDegrees(resultEuler.getZ()));
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
}
