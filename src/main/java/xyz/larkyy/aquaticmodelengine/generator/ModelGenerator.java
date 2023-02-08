package xyz.larkyy.aquaticmodelengine.generator;

import com.google.gson.Gson;
import xyz.larkyy.aquaticmodelengine.AquaticModelEngine;
import xyz.larkyy.aquaticmodelengine.TemplateRegistry;
import xyz.larkyy.aquaticmodelengine.generator.java.JavaBaseItem;
import xyz.larkyy.aquaticmodelengine.generator.blockbench.BlockBenchParser;
import xyz.larkyy.aquaticmodelengine.model.reader.ModelReader;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class ModelGenerator {

    private final BlockBenchParser parser = new BlockBenchParser();
    private final JavaBaseItem baseItem = new JavaBaseItem();

    private final ModelReader modelReader = new ModelReader();
    private final TemplateRegistry registry = new TemplateRegistry();

    public void generateModels() {
        var mainFolder = AquaticModelEngine.getPlugin(AquaticModelEngine.class).getDataFolder();
        mainFolder.mkdirs();

        var modelsFolder = new File(mainFolder,"models");
        modelsFolder.mkdirs();

        for (var file : modelsFolder.listFiles()) {
            var modelTemplate = parser.generate(file,baseItem);
            registry.addTemplate(modelTemplate);
        }

        var baseItemFolder = new File(mainFolder,"assets/minecraft/models/item");
        baseItemFolder.mkdirs();
        var baseItemFile = new File(baseItemFolder,"leather_horse_armor.json");

        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(baseItemFile)) {
            writer.write(gson.toJson(baseItem));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ModelReader getModelReader() {
        return modelReader;
    }

    public TemplateRegistry getRegistry() {
        return registry;
    }
}
