package xyz.larkyy.aquaticmodelengine.nms.nms1_19_2;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Rotations;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.Vec3;

import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_19_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_19_R1.util.CraftVector;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import xyz.larkyy.aquaticmodelengine.api.AquaticModelEngineAPI;
import xyz.larkyy.aquaticmodelengine.api.FakeArmorStand;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class FakeArmorStandImpl implements FakeArmorStand {

    private final ArmorStand armorStand;

    public FakeArmorStandImpl(Location location, Consumer<org.bukkit.entity.ArmorStand> factory) {
        CraftWorld cw = (CraftWorld) location.getWorld();
        ServerLevel worldServer = cw.getHandle();
        this.armorStand = EntityType.ARMOR_STAND.create(
                worldServer,
                null,
                null,
                null,
                new BlockPos(CraftVector.toNMS(location.toVector())),
                MobSpawnType.COMMAND,
                true,
                false
        );
        factory.accept((org.bukkit.entity.ArmorStand) armorStand.getBukkitEntity());
    }

    public void teleport(Location location) {
        armorStand.getBukkitEntity().teleport(location);
    }

    public void setHeadPose(EulerAngle eulerAngle) {
        armorStand.setHeadPose(new Rotations((float) Math.toDegrees(eulerAngle.getX()),
                (float) Math.toDegrees(eulerAngle.getY()),
                (float) Math.toDegrees(eulerAngle.getZ())));
        armorStand.setRightArmPose(new Rotations((float) Math.toDegrees(eulerAngle.getX()),
                (float) Math.toDegrees(eulerAngle.getY()),
                (float) Math.toDegrees(eulerAngle.getZ())));
    }

    public void setHeadItem(ItemStack itemStack) {

    }

    public void hide(Player player) {
        ((CraftPlayer) player).getHandle().connection.connection.send(hidePacket());
    }

    public void show(Player player) {
        var packets = showPackets();

        for (var packet : packets) {
            ((CraftPlayer) player).getHandle().connection.connection.send(packet);
        }

        new BukkitRunnable() {
            @Override
            public void run() {
                for (var packet : updateEquipmentPackets()) {
                    ((CraftPlayer) player).getHandle().connection.connection.send(packet);
                }
            }
        }.runTaskLater(AquaticModelEngineAPI.pluginInstance,1);

    }

    @Override
    public void remove() {
        armorStand.remove(Entity.RemovalReason.DISCARDED);
    }

    @Override
    public void updateHeadRotation(Player player) {
        var packets = updateMetaPackets();

        for (var packet : packets) {
            ((CraftPlayer) player).getHandle().connection.connection.send(packet);
        }
    }

    @Override
    public void updatePosition(Player player) {
        final var packet = new ClientboundTeleportEntityPacket(armorStand);
        ((CraftPlayer)player).getHandle().connection.connection.send(packet);
    }

    @Override
    public int getId() {
        return armorStand.getId();
    }

    @Override
    public org.bukkit.entity.ArmorStand getArmorstand() {
        return (org.bukkit.entity.ArmorStand) armorStand.getBukkitEntity();
    }

    private List<Packet<?>> showPackets() {
        List<Packet<?>> packets = new ArrayList<>();
        packets.add(new ClientboundTeleportEntityPacket(armorStand));
        var packet = new ClientboundAddEntityPacket(
                armorStand.getId(),
                armorStand.getUUID(),
                armorStand.getX(),
                armorStand.getY(),
                armorStand.getZ(),
                armorStand.getBukkitYaw(),
                armorStand.getBukkitYaw(),
                EntityType.ARMOR_STAND,
                0,
                new Vec3(0, 0, 0),
                0
        );
        packets.add(packet);
        packets.addAll(updateMetaPackets());

        return packets;
    }

    private Packet<?> hidePacket() {
        return new ClientboundRemoveEntitiesPacket(armorStand.getId());
    }

    private List<Packet<?>> updateMetaPackets() {
        List<Packet<?>> packets = new ArrayList<>();
        var packet = new ClientboundSetEntityDataPacket(armorStand.getId(), armorStand.getEntityData(), true);
        packets.add(packet);
        return packets;
    }

    private List<Packet<?>> updateEquipmentPackets() {
        List<Packet<?>> packets = new ArrayList<>();
        var packet = new ClientboundSetEquipmentPacket(armorStand.getId(), List.of(
                new Pair<>(EquipmentSlot.HEAD, armorStand.getItemBySlot(EquipmentSlot.HEAD)),
                new Pair<>(EquipmentSlot.MAINHAND, armorStand.getItemBySlot(EquipmentSlot.MAINHAND))
        ));
        packets.add(packet);
        return packets;
    }
}
