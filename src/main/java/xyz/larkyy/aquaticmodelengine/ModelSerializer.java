package xyz.larkyy.aquaticmodelengine;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModelSerializer {

    private final JavaPlugin plugin;

    public ModelSerializer(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void loadModels() {
        var files = new File(plugin.getDataFolder() + "/models").listFiles();
        for (var file : files) {
            if (!file.getName().endsWith(".bbmodel")) {
                continue;
            }
            loadModel(file);
        }
    }

    public void loadModel(File file) {
        JsonParser parser = new JsonParser();
        try {
            JsonObject object = parser.parse(new FileReader(file)).getAsJsonObject();
            var elements = object.get("elements").getAsJsonArray();
            var cubes = loadCubes(elements);
            var bones = loadBones(object.getAsJsonArray("outliner"),cubes);

            var model = new Model(bones);
            Bukkit.broadcastMessage("Loaded model!");


        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String,Cube> loadCubes(JsonArray array) {
        Map<String,Cube> allCubes = new HashMap<>();
        for (var item : array) {
            var element = item.getAsJsonObject();
            var type = element.get("type").getAsString();

            if (type.equalsIgnoreCase("cube")) {
                var cube = loadCube(element);
                allCubes.put(cube.getUuid(),cube);
            }
        }
        return allCubes;
    }

    public Cube loadCube(JsonObject element) {
        var origin = element.get("origin").getAsJsonArray();
        var pivot = readPoint(origin);

        var fromPoint = readPoint(element.getAsJsonArray("from"));
        var toPoint = readPoint(element.getAsJsonArray("to"));

        var size = new Size(
                toPoint.getX() - fromPoint.getX(),
                toPoint.getY() - fromPoint.getY(),
                toPoint.getZ() - fromPoint.getZ()
        );

        var rotation = element.has("rotation") ? readPoint(element.getAsJsonArray("rotation")) : new Point();
        String uuid = element.get("uuid").getAsString();

        return new Cube(uuid,size,rotation,pivot);
    }

    private List<Bone> loadBones(JsonArray array, Map<String,Cube> cubes) {
        List<Bone> bones = new ArrayList<>();
        for (var item : array) {
            var object = item.getAsJsonObject();
            if (object.has("children")) {
                var bone = loadBone(object,cubes);
                bones.add(bone);
            }
        }
        return bones;
    }

    private Bone loadBone(JsonObject object, Map<String,Cube> cubes) {
        var pivot = readPoint(object.getAsJsonArray("origin"));
        var parts = loadChildren(object.getAsJsonArray("children"),cubes);

        return new Bone(pivot,parts);
    }

    private List<Part> loadChildren(JsonArray array, Map<String,Cube> cubes) {
        List<Part> parts = new ArrayList<>();
        for (var item : array) {
            if (item.isJsonObject()) {
                var bone = loadBone(item.getAsJsonObject(),cubes);
                parts.add(bone);
            } else {
                var cube = cubes.get(item.getAsString());
                if (cube != null) {
                    parts.add(cube);
                }
            }
        }
        return parts;
    }

    private Point readPoint(JsonArray array) {
        final List<Double> list = new ArrayList<>();
        for (var item : array) {
            var i = item.getAsDouble();
            list.add(i);
        }
        if (list.size() < 3) {
            return new Point();
        }
        return new Point(list.get(0), list.get(1), list.get(2));
    }

}
