package xyz.larkyy.aquaticmodelengine.model.template;

import org.bukkit.Bukkit;
import xyz.larkyy.aquaticmodelengine.model.template.ModelTemplate;

import java.util.HashMap;
import java.util.Map;

public class TemplateRegistry {

    private final Map<String, ModelTemplate> templates = new HashMap<>();

    public void addTemplate(ModelTemplate template) {
        Bukkit.broadcastMessage("Added template: "+template.getName());
        templates.put(template.getName(),template);
    }

    public ModelTemplate getTemplate(String name) {
        return templates.get(name);
    }
}
