package xyz.larkyy.aquaticmodelengine.api.model.spawned.player;

import org.bukkit.entity.Player;
import xyz.larkyy.aquaticmodelengine.api.AquaticModelEngineAPI;
import xyz.larkyy.aquaticmodelengine.api.model.holder.ModelHolder;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.SpawnedModel;
import xyz.larkyy.aquaticmodelengine.api.model.template.ModelTemplate;

public abstract class PlayerModel extends SpawnedModel {
    private final TextureWrapper texture;

    public PlayerModel(ModelTemplate modelTemplate, Player player, TextureWrapper texture) {
        super(AquaticModelEngineAPI.getModelHandler().getModelHolder(player.getPlayer()),modelTemplate);
        this.texture = texture;
        //this.texture = TextureWrapper.fromBase64(AquaticModelEngine.getInstance().getNmsHandler().getTexture(player));
    }

    public PlayerModel(ModelTemplate modelTemplate, ModelHolder modelHolder, String url, boolean slim) {
        super(modelHolder,modelTemplate);
        this.texture = new TextureWrapper(url,slim);
    }

    public PlayerModel(ModelTemplate modelTemplate, ModelHolder modelHolder, TextureWrapper texture) {
        super(modelHolder,modelTemplate);
        this.texture = texture;
        //this.texture = TextureWrapper.fromBase64(AquaticModelEngine.getInstance().getNmsHandler().getTexture(player));
    }

    public TextureWrapper getTexture() {
        return texture;
    }
}
