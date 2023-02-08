package xyz.larkyy.aquaticmodelengine.generator.java;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaBaseItem {

    private final List<JavaOverride> overrides = new ArrayList<>();
    private final String parent;
    private final Map<String,String> textures;

    public JavaBaseItem() {
        parent = "minecraft:item/generated";
        textures = new HashMap<>();
        textures.put("layer0","minecraft:item/leather_horse_armor");
    }

    public List<JavaOverride> getOverrides() {
        return overrides;
    }
}
