package xyz.larkyy.aquaticmodelengine.api.model.template;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;

public class TemplateRegistry {

    private final Map<String, ModelTemplateImpl> templates = new HashMap<>();
    private final Map<String, ModelTemplateImpl> emotes = new HashMap<>();

    public void addTemplate(ModelTemplateImpl template) {
        Bukkit.getConsoleSender().sendMessage("§6[AquaticModelEngine]§f Added template: "+template.getName());
        templates.put(template.getName(),template);
    }

    public void addEmote(ModelTemplateImpl emote) {
        Bukkit.getConsoleSender().sendMessage("§6[AquaticModelEngine]§f Added emote: "+emote.getName());
        emotes.put(emote.getName(),emote);
    }

    public void clear() {
        templates.clear();
        emotes.clear();
    }

    public ModelTemplateImpl getTemplate(String name) {
        return templates.get(name);
    }

    public ModelTemplateImpl getEmote(String name) {
        return emotes.get(name);
    }
}
