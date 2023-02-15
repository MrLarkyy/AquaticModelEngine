package xyz.larkyy.aquaticmodelengine.generator.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaItem {
    private final transient int modelData;
    private final List<JavaCube> elements = new ArrayList<>();
    private final Map<Integer,String> textures = new HashMap<>();
    private final Map<String,JavaDisplay> display = new HashMap<>();

    public JavaItem(int modelData) {
        display.put("head",new JavaDisplay());
        this.modelData = modelData;
    }

    public int getModelData() {
        return modelData;
    }

    public List<JavaCube> getElements() {
        return elements;
    }

    public Map<Integer, String> getTextures() {
        return textures;
    }

    public Map<String, JavaDisplay> getDisplay() {
        return display;
    }
}
