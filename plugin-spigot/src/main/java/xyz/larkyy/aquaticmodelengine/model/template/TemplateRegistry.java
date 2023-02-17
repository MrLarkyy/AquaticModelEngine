package xyz.larkyy.aquaticmodelengine.model.template;

import org.bukkit.Bukkit;
import xyz.larkyy.aquaticmodelengine.api.model.template.ModelTemplateImpl;

import java.util.HashMap;
import java.util.Map;

public class TemplateRegistry {

    private final Map<String, ModelTemplateImpl> templates = new HashMap<>();

    public void addTemplate(ModelTemplateImpl template) {
        Bukkit.broadcastMessage("Added template: "+template.getName());
        templates.put(template.getName(),template);
    }

    public ModelTemplateImpl getTemplate(String name) {
        return templates.get(name);
    }
}
