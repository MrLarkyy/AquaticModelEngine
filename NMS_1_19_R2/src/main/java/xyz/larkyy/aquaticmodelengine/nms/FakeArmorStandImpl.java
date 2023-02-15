package xyz.larkyy.aquaticmodelengine.nms;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Rotations;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.protocol.game.ClientboundRemoveEntitiesPacket;
import net.minecraft.network.protocol.game.ClientboundSetEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundSetEquipmentPacket;
import net.minecraft.server.level.ServerLevel;
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
import org.bukkit.util.EulerAngle;
import xyz.larkyy.aquaticmodelengine.api.FakeArmorStand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FakeArmorStandImpl implements FakeArmorStand {

    private final ArmorStand armorStand;

    public FakeArmorStandImpl(Location location) {
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
    }

    public void teleport(Location location) {

    }

    public void setHeadPose(EulerAngle eulerAngle) {
        armorStand.setHeadPose(new Rotations((float) eulerAngle.getX(), (float) eulerAngle.getY(), (float) eulerAngle.getZ()));
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

        var bukkitEntity = armorStand.getBukkitEntity();
        var location = bukkitEntity.getLocation();
        var packet = new ClientboundAddEntityPacket(
                armorStand.getId(),
                armorStand.getUUID(),
                armorStand.getX(),
                armorStand.getY(),
                armorStand.getZ(),
                armorStand.getBukkitYaw(),
                0,
                EntityType.ARMOR_STAND,
                0,
                new Vec3(0, 0, 0),
                0
        );
        packets.add(packet);

        packets.addAll(updateMetaPackets());
        packets.addAll(updateEquipmentPackets());

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
        var packet = new ClientboundSetEquipmentPacket(armorStand.getId(), Arrays.asList(
                new Pair<>(EquipmentSlot.HEAD, armorStand.getItemBySlot(EquipmentSlot.HEAD))
        ));
        packets.add(packet);
        return packets;
    }
}
