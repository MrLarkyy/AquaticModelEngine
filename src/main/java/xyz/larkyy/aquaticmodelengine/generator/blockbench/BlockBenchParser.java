package xyz.larkyy.aquaticmodelengine.generator.blockbench;

import com.google.gson.*;
import org.bukkit.Bukkit;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.AquaticModelEngine;
import xyz.larkyy.aquaticmodelengine.generator.java.*;
import xyz.larkyy.aquaticmodelengine.model.template.ModelTemplate;
import xyz.larkyy.aquaticmodelengine.model.template.TemplateBone;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;

public class BlockBenchParser {

    private int modelsMade = 0;
    private final int modelDataOffset = 0;
    private final Map<String,BBElement> cachedElements = new HashMap<>();
    private final Map<String, JavaItem> cachedJavaItems = new HashMap<>();
    private final Map<Integer,BBTexture> textures = new HashMap<>();
    private JavaBaseItem baseItem = null;
    private ModelTemplate modelTemplate = null;

    public ModelTemplate generate(File file, JavaBaseItem baseItem) {
        cachedElements.clear();
        cachedJavaItems.clear();
        textures.clear();

        var name = file.getName();
        if (!name.endsWith(".bbmodel")) {
            return null;
        }

        name = name.substring(0,name.length()-8).toLowerCase();

        this.baseItem = baseItem;

        JsonParser parser = new JsonParser();
        JsonObject object;
        try {
            object = parser.parse(new FileReader(file)).getAsJsonObject();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        ModelTemplate modelTemplate = new ModelTemplate(name);
        this.modelTemplate = modelTemplate;

        loadElements(object.getAsJsonArray("elements"));
        loadTextures(object.getAsJsonArray("textures"));
        loadBones(object.getAsJsonArray("outliner"),modelTemplate);

        Bukkit.broadcastMessage("Loaded items: "+cachedJavaItems.size());

        File mainFolder = AquaticModelEngine.getPlugin(AquaticModelEngine.class).getDataFolder();
        mainFolder.mkdirs();
        var modelFolder = new File(mainFolder+"/assets/aquaticengine/models/"+name);
        modelFolder.mkdirs();
        new File(mainFolder+"/assets/aquaticengine/textures/"+name).mkdirs();
        new File(mainFolder+"/assets/minecraft").mkdirs();

        for (var texture : textures.values()) {
            File textureFile = new File(mainFolder+"/assets/aquaticengine/textures/"+name,texture.getName().toLowerCase()+".png");
            byte[] bytes = Base64.getDecoder().decode(texture.getSource().split(",")[1]);
            try {
                BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(bytes));
                ImageIO.write(bufferedImage,"png",textureFile);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        Gson gson = new Gson();
        for (var entry : cachedJavaItems.entrySet()) {
            File itemFile = new File(mainFolder+"/assets/aquaticengine/models/"+name+"/"+entry.getKey()+".json");
            try(FileWriter writer = new FileWriter(itemFile)) {
                writer.write(gson.toJson(entry.getValue()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return modelTemplate;
    }

    private List<BBElement> loadElements(JsonArray array) {
        List<BBElement> list = new ArrayList<>();
        for (var item : array) {
            var element = loadElement(item.getAsJsonObject());
            if (element == null) {
                Bukkit.broadcastMessage("Element is null!");
            } else {
                Bukkit.broadcastMessage("Loaded an element!");
                list.add(element);
            }
        }
        return list;
    }

    private BBElement loadElement(JsonObject object) {
        Gson gson = new Gson();
        var element = gson.fromJson(object, BBElement.class);
        cachedElements.put(element.getUuid(),element);
        return element;
    }

    private List<BBBone> loadBones(JsonArray array, ModelTemplate template) {
        List<BBBone> bones = new ArrayList<>();
        for (var item : array) {
            bones.add(loadBone(item.getAsJsonObject(),template.getBones(),null));
        }
        return bones;
    }

    private BBBone loadBone(JsonObject object, List<TemplateBone> bones, TemplateBone parentBone) {
        Gson gson = new Gson();
        var bone = gson.fromJson(object, BBBone.class);
        EulerAngle rotation;
        if (bone.getRotation() == null || bone.getRotation().length < 3) {
            rotation = new EulerAngle(0,0,0);
        } else {
            rotation = new EulerAngle(
                -Math.toRadians(bone.getRotation()[0]),
                -Math.toRadians(bone.getRotation()[1]),
                Math.toRadians(bone.getRotation()[2])
            );
        }

        var templateBone = new TemplateBone(
                bone.getName(),
                new Vector(-bone.getOrigin()[0],bone.getOrigin()[1],-bone.getOrigin()[2]),
                rotation
        );
        templateBone.setParent(parentBone);
        bones.add(templateBone);
        if (object.has("children")) {
            for (var item : object.getAsJsonArray("children")) {
                if (item.isJsonObject()) {
                    var subBone = loadBone(item.getAsJsonObject(),templateBone.getChildren(), templateBone);
                    bone.getChildren().add(subBone);
                } else {
                    var uuid = item.getAsString();
                    var element = cachedElements.get(uuid);
                    if (element == null) {
                       continue;
                    }
                    bone.getElements().add(element);
                    var javaItem = cachedJavaItems.get(bone.getName());
                    if (javaItem == null) {
                        var modelData = modelDataOffset+modelsMade+1;
                        javaItem = new JavaItem(modelData);
                        templateBone.setModelId(modelData);
                        baseItem.getOverrides().add(new JavaOverride(modelTemplate.getName(),bone.getName(),modelData));
                        modelsMade++;
                        var textures = javaItem.getTextures();
                        for (var entry : this.textures.entrySet()) {
                            textures.put(entry.getKey(),"aquaticengine:"+modelTemplate.getName()+"/"+entry.getValue().getName().toLowerCase());
                        }
                        cachedJavaItems.put(bone.getName(),javaItem);
                    }
                    Map<String, JavaFace> javaFaces = new HashMap<>();
                    javaFaces.put("up",new JavaFace(element.getFaces().getUp()));
                    javaFaces.put("down",new JavaFace(element.getFaces().getDown()));
                    javaFaces.put("south",new JavaFace(element.getFaces().getSouth()));
                    javaFaces.put("north",new JavaFace(element.getFaces().getNorth()));
                    javaFaces.put("east",new JavaFace(element.getFaces().getEast()));
                    javaFaces.put("west",new JavaFace(element.getFaces().getWest()));

                    double[] newFrom = new double[]{
                            element.getFrom()[0]-bone.getOrigin()[0],
                            element.getFrom()[1]-bone.getOrigin()[1],
                            element.getFrom()[2]-bone.getOrigin()[2]
                    };
                    double[] newTo = new double[]{
                            element.getTo()[0]-bone.getOrigin()[0],
                            element.getTo()[1]-bone.getOrigin()[1],
                            element.getTo()[2]-bone.getOrigin()[2]
                    };
                    double[] newOrigin = new double[] {
                            element.getOrigin()[0]-bone.getOrigin()[0],
                            element.getOrigin()[1]-bone.getOrigin()[1],
                            element.getOrigin()[2]-bone.getOrigin()[2]
                    };
                    javaItem.getElements().add(new JavaCube(
                            newFrom,
                            newTo,
                            new JavaRotation(element.getEulerAngle(),newOrigin),
                            javaFaces
                    ));
                }
            }
        }
        Bukkit.broadcastMessage("Loaded bone");
        return bone;
    }

    private void loadTextures(JsonArray array) {
        Gson gson = new Gson();
        for (var item : array) {
            var texture = gson.fromJson(item,BBTexture.class);
            if (texture != null) {
                Bukkit.broadcastMessage("Loaded texture");
                textures.put(texture.getId(),texture);
            }
        }
    }

}
