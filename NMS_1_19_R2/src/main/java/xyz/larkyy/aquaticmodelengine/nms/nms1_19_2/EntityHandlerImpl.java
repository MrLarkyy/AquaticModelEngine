package xyz.larkyy.aquaticmodelengine.nms.nms1_19_2;

import com.mojang.datafixers.util.Pair;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.ItemStack;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import xyz.larkyy.aquaticmodelengine.api.FakeArmorStand;
import xyz.larkyy.aquaticmodelengine.api.IEntityHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class EntityHandlerImpl implements IEntityHandler {


    private final Map<Integer, Entity> entities = new HashMap<>();

    @Override
    public FakeArmorStand spawn(Location location, Consumer<ArmorStand> factory) {
        return new FakeArmorStandImpl(location, factory);
    }

    public void updateEntity(int id, Consumer<org.bukkit.entity.Entity> factory) {
        net.minecraft.world.entity.Entity entity = entities.get(id);

        if (factory != null) {
            factory.accept(entity.getBukkitEntity());
        }

        final var packetMetadata = new ClientboundSetEntityDataPacket(entity.getId(), entity.getEntityData(), true);
        sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),packetMetadata);

        if (entity instanceof LivingEntity livingEntity) {
            final List<Pair<EquipmentSlot, ItemStack>> equipmentMap = new ArrayList<>();
            for (EquipmentSlot value : EquipmentSlot.values()) {
                equipmentMap.add(Pair.of(value,livingEntity.getItemBySlot(value)));
            }
            final var packet = new ClientboundSetEquipmentPacket(entity.getId(),equipmentMap);
            sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),packet);
        }
    }

    public void teleportEntity(int id, Location location) {
        if (!entities.containsKey(id)) {
            return;
        }
        net.minecraft.world.entity.Entity entity = entities.get(id);

        entity.getBukkitEntity().teleport(location);
        final var packet = new ClientboundTeleportEntityPacket(entity);

        sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),packet);
    }

    public void moveEntity(int id, Location location) {
        if (!entities.containsKey(id)) {
            return;
        }
        net.minecraft.world.entity.Entity entity = entities.get(id);
        Location prevLoc = entity.getBukkitEntity().getLocation();

        entity.getBukkitEntity().teleport(location);
        final var packet = new ClientboundMoveEntityPacket.PosRot(
                id,
                (short)((location.getX() * 32 - prevLoc.getX() * 32) * 128),
                (short)((location.getY() * 32 - prevLoc.getY() * 32) * 128),
                (short)((location.getZ() * 32 - prevLoc.getZ() * 32) * 128),
                (byte) ((int) (location.getYaw() * 256.0F / 360.0F)),
                (byte) ((int) (location.getPitch() * 256.0F / 360.0F)),
                true
        );

        sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),packet);
        sendPacket(new ArrayList<>(Bukkit.getOnlinePlayers()),
                new ClientboundRotateHeadPacket(entities.get(id),(byte) ((int) (location.getYaw() * 256.0F / 360.0F)))
        );
    }

    public org.bukkit.entity.Entity getEntity(int id) {
        return entities.get(id).getBukkitEntity();
    }

    public void showEntity(int id) {

    }

    public void hideEntity(int id) {

    }

    private void sendPacket(List<Player> players, Packet packet) {
        players.forEach(player -> {
            ((CraftPlayer)player).getHandle().connection.send(packet);
        });
    }
}
