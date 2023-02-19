package xyz.larkyy.aquaticmodelengine.api;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.player.TextureWrapper;

public interface INMSHandler {

    String getTexture(Player player);

    ItemStack setSkullTexture(ItemStack skull, TextureWrapper texture);
}
