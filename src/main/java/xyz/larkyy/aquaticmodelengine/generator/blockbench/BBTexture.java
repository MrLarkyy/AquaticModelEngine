package xyz.larkyy.aquaticmodelengine.generator.blockbench;

public class BBTexture {

    private final String name;
    private final String uuid;
    private final String source;
    private final String id;

    public BBTexture(String name, String uuid, String source, String id) {
        this.name = name;
        this.uuid = uuid;
        this.source = source;
        this.id = id;
    }

    public String getName() {
        return name.toLowerCase();
    }

    public String getUuid() {
        return uuid;
    }

    public String getSource() {
        return source;
    }

    public String getId() {
        return id;
    }
}
