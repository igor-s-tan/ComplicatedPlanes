package com.igorstan.complicatedplanes.network;

import com.igorstan.complicatedplanes.containter.ComputerContainerProvider;
import com.igorstan.complicatedplanes.upgrades.ComputerUpgrade;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkEvent;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;

import java.util.function.Supplier;

public class OpenComputerPacket {
    public OpenComputerPacket() {
    }

    public OpenComputerPacket(PacketBuffer buffer) {
    }

    public void toBytes(PacketBuffer buffer) {
    }

    public void handle(Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = (NetworkEvent.Context)ctxSup.get();
        ctx.enqueueWork(() -> {
            ServerPlayerEntity sender = ctx.getSender();
            if (sender != null) {
                Entity entity = sender.getVehicle();
                if (entity instanceof PlaneEntity) {
                    PlaneEntity planeEntity = (PlaneEntity)entity;
                    if (planeEntity.upgrades.containsKey(new ResourceLocation("complicatedplanes:computer"))) {
                        ItemStack stack = Items.ACACIA_BOAT.getDefaultInstance();
                        ComputerUpgrade upgrade = (ComputerUpgrade) planeEntity.upgrades.get(new ResourceLocation("complicatedplanes:computer"));
                        ServerComputer computer = upgrade.createServerComputer((ServerWorld)planeEntity.level, sender, sender.inventory, planeEntity);
                        computer.turnOn();
                        (new ComputerContainerData(computer, stack)).open(sender, new ComputerContainerProvider(computer, upgrade, planeEntity, false));
                    }
                }
            }

        });
        ctx.setPacketHandled(true);
    }
}
