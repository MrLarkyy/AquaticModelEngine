package xyz.larkyy.aquaticmodelengine.nms.nms1_19_2;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.inventory.CraftItemStack;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import xyz.larkyy.aquaticmodelengine.api.INMSHandler;
import xyz.larkyy.aquaticmodelengine.api.model.spawned.player.TextureWrapper;

import java.util.UUID;

public class NMSHandler implements INMSHandler {
    @Override
    public String getTexture(Player player) {
        return (((CraftPlayer)player).getHandle().getGameProfile().getProperties().get("textures").iterator().next()).getValue();
    }

    @Override
    public ItemStack setSkullTexture(ItemStack skull, TextureWrapper texture) {
        if (skull == null)
            skull = new ItemStack(Material.PLAYER_HEAD);
        net.minecraft.world.item.ItemStack nmsSkull = CraftItemStack.asNMSCopy(skull);
        var tag = nmsSkull.getOrCreateTag();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        profile.getProperties().put("textures", new Property("textures", texture.toBase64()));

        tag.put("SkullOwner",NbtUtils.writeGameProfile(new CompoundTag(),profile));
        nmsSkull.setTag(tag);
        return CraftItemStack.asBukkitCopy(nmsSkull);
    }
}
