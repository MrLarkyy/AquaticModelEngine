package xyz.larkyy.aquaticmodelengine.generator.blockbench;

import com.google.gson.*;
import org.bukkit.Material;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import xyz.larkyy.aquaticmodelengine.AquaticModelEngine;
import xyz.larkyy.aquaticmodelengine.api.model.animation.InterpolationType;
import xyz.larkyy.aquaticmodelengine.api.model.animation.LoopMode;
import xyz.larkyy.aquaticmodelengine.api.model.animation.TemplateAnimation;
import xyz.larkyy.aquaticmodelengine.api.model.animation.Timeline;
import xyz.larkyy.aquaticmodelengine.api.model.template.ModelTemplateImpl;
import xyz.larkyy.aquaticmodelengine.api.model.template.TemplateBone;
import xyz.larkyy.aquaticmodelengine.api.model.template.TemplateBoneImpl;
import xyz.larkyy.aquaticmodelengine.api.model.template.player.LimbType;
import xyz.larkyy.aquaticmodelengine.api.model.template.player.PlayerTemplateBoneImpl;
import xyz.larkyy.aquaticmodelengine.generator.java.*;

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
    private ModelTemplateImpl modelTemplate = null;

    public ModelTemplateImpl generateEmote(File file, JavaBaseItem baseItem) {
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
        ModelTemplateImpl modelTemplate = new ModelTemplateImpl(name);
        this.modelTemplate = modelTemplate;

        var resolution = loadResolution(object.getAsJsonObject("resolution"));

        loadElements(object.getAsJsonArray("elements"));
        loadTextures(object.getAsJsonArray("textures"));
        loadBones(object.getAsJsonArray("outliner"),modelTemplate,resolution,true);
        loadAnimations(object.getAsJsonArray("animations"));

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
            var limbType = LimbType.get(entry.getKey());
            if (limbType != null) {
                continue;
            }
            File itemFile = new File(mainFolder+"/assets/aquaticengine/models/"+name+"/"+entry.getKey()+".json");
            try(FileWriter writer = new FileWriter(itemFile)) {
                writer.write(gson.toJson(entry.getValue()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return modelTemplate;
    }

    public ModelTemplateImpl generate(File file, JavaBaseItem baseItem) {
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
        ModelTemplateImpl modelTemplate = new ModelTemplateImpl(name);
        this.modelTemplate = modelTemplate;

        var resolution = loadResolution(object.getAsJsonObject("resolution"));

        loadElements(object.getAsJsonArray("elements"));
        loadTextures(object.getAsJsonArray("textures"));
        loadBones(object.getAsJsonArray("outliner"),modelTemplate,resolution,false);
        loadAnimations(object.getAsJsonArray("animations"));

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

    private BBResolution loadResolution(JsonObject object) {
        Gson gson = new Gson();
        var resolution = gson.fromJson(object, BBResolution.class);
        return resolution;
    }

    private List<BBElement> loadElements(JsonArray array) {
        List<BBElement> list = new ArrayList<>();
        for (var item : array) {
            var element = loadElement(item.getAsJsonObject());
            if (element != null) {
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

    private List<BBBone> loadBones(JsonArray array, ModelTemplateImpl template, BBResolution resolution, boolean emote) {
        List<BBBone> bones = new ArrayList<>();
        for (var item : array) {
            bones.add(loadBone(item.getAsJsonObject(),template.getBones(),null,resolution, emote));
        }
        return bones;
    }

    private BBBone loadBone(JsonObject object, List<TemplateBone> bones, TemplateBone parentBone, BBResolution resolution, boolean emote) {
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

        TemplateBone templateBone;
        if (emote) {
            LimbType limbType = LimbType.get(bone.getName());
            if (limbType == null) {
                templateBone = new TemplateBoneImpl(
                        bone.getName(),
                        new Vector(-bone.getOrigin()[0],bone.getOrigin()[1],-bone.getOrigin()[2]),
                        rotation
                );
            } else {
                templateBone = new PlayerTemplateBoneImpl(
                        bone.getName(),
                        new Vector(-bone.getOrigin()[0],bone.getOrigin()[1],-bone.getOrigin()[2]),
                        rotation,
                        limbType
                );
                templateBone.setMaterial(Material.PLAYER_HEAD);
            }
        } else {
            templateBone = new TemplateBoneImpl(
                    bone.getName(),
                    new Vector(-bone.getOrigin()[0],bone.getOrigin()[1],-bone.getOrigin()[2]),
                    rotation
            );
        }

        templateBone.setParent(parentBone);
        bones.add(templateBone);
        if (object.has("children")) {
            for (var item : object.getAsJsonArray("children")) {
                if (item.isJsonObject()) {
                    var subBone = loadBone(item.getAsJsonObject(),templateBone.getChildren(), templateBone,resolution,emote);
                    bone.getChildren().add(subBone);
                } else {
                    if (templateBone instanceof PlayerTemplateBoneImpl) {
                        continue;
                    }
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
                    javaFaces.put("up",new JavaFace(element.getFaces().getUp(),resolution));
                    javaFaces.put("down",new JavaFace(element.getFaces().getDown(),resolution));
                    javaFaces.put("south",new JavaFace(element.getFaces().getSouth(),resolution));
                    javaFaces.put("north",new JavaFace(element.getFaces().getNorth(),resolution));
                    javaFaces.put("east",new JavaFace(element.getFaces().getEast(),resolution));
                    javaFaces.put("west",new JavaFace(element.getFaces().getWest(),resolution));

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
        return bone;
    }

    private void loadTextures(JsonArray array) {
        if (array == null) return;
        Gson gson = new Gson();
        for (var item : array) {
            var texture = gson.fromJson(item,BBTexture.class);
            if (texture != null) {
                textures.put(textures.size(), texture);
            }
        }
    }

    private void loadAnimations(JsonArray array) {
        if (array == null) return;
        for (var item : array) {
            var animation = loadAnimation(item.getAsJsonObject());
            modelTemplate.addAnimation(animation);
        }
    }

    private TemplateAnimation loadAnimation(JsonObject object) {
        var templateAnimation = new TemplateAnimation(
                object.get("name").getAsString(),
                object.get("length").getAsDouble(),
                LoopMode.valueOf(object.get("loop").getAsString().toUpperCase())
        );
        if (object.has("animators")) {
            for (var entry : object.get("animators").getAsJsonObject().entrySet()) {
                var obj = entry.getValue().getAsJsonObject();
                setupTimeline(templateAnimation,obj);
            }
        }
        return templateAnimation;
    }

    private void setupTimeline(TemplateAnimation templateAnimation, JsonObject object) {
        var name = object.get("name").getAsString();
        var timeline = templateAnimation.getTimeline(name);
        if (timeline == null) {
            timeline = new Timeline();
            templateAnimation.addTimeline(name,timeline);
        }
        for (var item : object.get("keyframes").getAsJsonArray()) {
            var obj = item.getAsJsonObject();
            var type = obj.get("channel").getAsString();
            var time = obj.get("time").getAsDouble();
            var interpolation = obj.get("interpolation").getAsString();

            InterpolationType interpolationType;
            switch (interpolation.toUpperCase()) {
                case "LINEAR": {
                    interpolationType = InterpolationType.LINEAR;
                    break;
                }
                case "CATMULLROM": {
                    interpolationType = InterpolationType.SMOOTH;
                    break;
                }
                case "STEP": {
                    interpolationType = InterpolationType.STEP;
                    break;
                }
                default:
                    interpolationType = InterpolationType.LINEAR;
                    break;
            }

            var datapoints = obj.get("data_points").getAsJsonArray().get(0).getAsJsonObject();

            switch (type.toLowerCase()) {
                case "position": {
                    Vector vector = new Vector(
                            datapoints.get("x").getAsDouble(),
                            datapoints.get("y").getAsDouble(),
                            -datapoints.get("z").getAsDouble()
                    );
                    timeline.addPositionFrame(time,vector,interpolationType);
                    break;
                }
                case "rotation": {
                    EulerAngle eulerAngle = new EulerAngle(
                            Math.toRadians(datapoints.get("x").getAsDouble()),
                            Math.toRadians(datapoints.get("y").getAsDouble()),
                            Math.toRadians(datapoints.get("z").getAsDouble())
                    );
                    timeline.addRotationFrame(time,eulerAngle,interpolationType);
                }
            }
        }
    }

}
