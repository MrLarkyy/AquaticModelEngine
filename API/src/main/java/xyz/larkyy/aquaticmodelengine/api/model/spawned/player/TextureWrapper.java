package xyz.larkyy.aquaticmodelengine.api.model.spawned.player;

import com.google.gson.Gson;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class TextureWrapper {
    private static final Gson gson = new Gson();

    private static final String jsonTemplate = "{\"textures\":{\"SKIN\":{\"url\":\"\",\"metadata\":{}}}}";

    private final SimpleProfileTexture payload;

    private String url;

    private boolean isSlim;

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public boolean isSlim() {
        return this.isSlim;
    }

    public void setSlim(boolean isSlim) {
        this.isSlim = isSlim;
    }

    public static TextureWrapper fromBase64(String base64) {
        String decoded = new String(Base64.getDecoder().decode(base64));
        SimpleProfileTexture temp = gson.fromJson(decoded, SimpleProfileTexture.class);
        String url = (temp.getTextures().get(MinecraftProfileTexture.Type.SKIN)).getUrl();
        boolean isSlim = "slim".equals((temp.getTextures().get(MinecraftProfileTexture.Type.SKIN)).getMetadata("model"));
        return new TextureWrapper(url, isSlim);
    }

    public TextureWrapper(String url, boolean isSlim) {
        this.payload = gson.fromJson("{\"textures\":{\"SKIN\":{\"url\":\"\",\"metadata\":{}}}}", SimpleProfileTexture.class);
        setUrl(url);
        setSlim(isSlim);
    }

    public String toBase64() {
        updatePayload();
        String json = gson.toJson(this.payload);
        return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    }

    private void updatePayload() {
        Map<String, String> meta = new HashMap<>();
        if (this.isSlim)
            meta.put("model", "slim");
        this.payload.getTextures().put(MinecraftProfileTexture.Type.SKIN, new MinecraftProfileTexture(this.url, meta));
    }

    private static class SimpleProfileTexture {
        private Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> textures;

        public Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> getTextures() {
            return this.textures;
        }
    }
}
