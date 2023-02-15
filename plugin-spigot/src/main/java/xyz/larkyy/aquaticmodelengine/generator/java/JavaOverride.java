package xyz.larkyy.aquaticmodelengine.generator.java;

import java.util.HashMap;
import java.util.Map;

public class JavaOverride {

    private final Map<String,Integer> predicate;
    private final String model;

    public JavaOverride(String modelName, String boneName,int modelData) {
        this.model = "aquaticengine:"+modelName+"/"+boneName;
        predicate  = new HashMap<>();
        predicate.put("custom_model_data",modelData);
    }
}
