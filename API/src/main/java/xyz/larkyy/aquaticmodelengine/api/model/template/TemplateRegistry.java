package xyz.larkyy.aquaticmodelengine.api.model.template;

import org.bukkit.Bukkit;
import xyz.larkyy.aquaticmodelengine.api.model.template.ModelTemplateImpl;
import xyz.larkyy.aquaticmodelengine.api.model.template.player.PlayerModelTemplate;

import java.util.HashMap;
import java.util.Map;

public class TemplateRegistry {

    private final Map<String, ModelTemplateImpl> templates = new HashMap<>();
    private final Map<String, ModelTemplateImpl> emotes = new HashMap<>();

    public void addTemplate(ModelTemplateImpl template) {
        Bukkit.broadcastMessage("Added template: "+template.getName());
        templates.put(template.getName(),template);
    }

    public void addEmote(ModelTemplateImpl emote) {
        Bukkit.broadcastMessage("Added emote: "+emote.getName());
        emotes.put(emote.getName(),emote);
    }

    public ModelTemplateImpl getTemplate(String name) {
        return templates.get(name);
    }

    public ModelTemplateImpl getEmote(String name) {
        return emotes.get(name);
    }
}
