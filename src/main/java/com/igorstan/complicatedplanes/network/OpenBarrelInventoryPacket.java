package com.igorstan.complicatedplanes.network;

import com.igorstan.complicatedplanes.registry.Upgrades;
import com.igorstan.complicatedplanes.upgrades.BarrelUpgrade;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;
import xyz.przemyk.simpleplanes.upgrades.Upgrade;


import java.util.function.Supplier;

public class OpenBarrelInventoryPacket {
    public OpenBarrelInventoryPacket() {
    }

    public OpenBarrelInventoryPacket(PacketBuffer buffer) {
    }

    public void toBytes(PacketBuffer buffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = (NetworkEvent.Context)ctxSup.get();
        ctx.enqueueWork(() -> {
            ServerPlayerEntity player = ctx.getSender();
            if (player != null && player.getVehicle() instanceof PlaneEntity) {
                PlaneEntity plane = (PlaneEntity)player.getVehicle();
                Upgrade barrel = (Upgrade)plane.upgrades.get(Upgrades.BARREL.getId());
                if (barrel instanceof BarrelUpgrade) {
                    NetworkHooks.openGui(player, (INamedContainerProvider)barrel, (buffer) -> {
                        buffer.writeUtf(((BarrelUpgrade)barrel).barrelType.getRegistryName().toString());
                    });
                }
            }

        });
        ctx.setPacketHandled(true);
    }
}

