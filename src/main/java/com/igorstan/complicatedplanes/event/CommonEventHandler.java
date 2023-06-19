package com.igorstan.complicatedplanes.event;

import com.igorstan.complicatedplanes.AccessItem;
import com.igorstan.complicatedplanes.api.PlaneAPI;
import com.igorstan.complicatedplanes.containter.ComputerContainerProvider;
import com.igorstan.complicatedplanes.network.MovementPacket;
import com.igorstan.complicatedplanes.network.Networking;
import com.igorstan.complicatedplanes.upgrades.ComputerUpgrade;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;

@Mod.EventBusSubscriber({Dist.DEDICATED_SERVER})
public class CommonEventHandler {
    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public static void Interaction(PlayerInteractEvent.EntityInteract event) {
        if(!event.getWorld().isClientSide) {
            if (event.getPlayer().getMainHandItem().getItem() instanceof AccessItem) {
                if (event.getTarget() instanceof PlaneEntity) {
                    PlaneEntity planeEntity = (PlaneEntity) event.getTarget();
                    if (planeEntity.upgrades.containsKey(new ResourceLocation("complicatedplanes:computer"))) {

                        ComputerUpgrade upgrade = (ComputerUpgrade) planeEntity.upgrades.get(new ResourceLocation("complicatedplanes:computer"));
                        ServerComputer computer = upgrade.createServerComputer((ServerWorld)event.getWorld(), event.getPlayer(), event.getPlayer().inventory, planeEntity);
                        computer.keepAlive();
                        computer.turnOn();
                        (new ComputerContainerData(computer, event.getItemStack())).open(event.getPlayer(), new ComputerContainerProvider(computer, upgrade, planeEntity, false));
                    }
                }
            }

            if (event.getPlayer().isShiftKeyDown() && event.getTarget() instanceof PlaneEntity) {
                PlaneEntity planeEntity = (PlaneEntity) event.getTarget();
                if (planeEntity.upgrades.containsKey(new ResourceLocation("complicatedplanes:fake_player"))) {
                    event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public static void ShiftCancellation(EntityMountEvent event) {
        if(!event.getEntityBeingMounted().level.isClientSide &&
                event.isDismounting() &&
                event.getEntityMounting().getVehicle() instanceof PlaneEntity) {
            PlaneEntity planeEntity = (PlaneEntity) event.getEntityMounting().getVehicle();
            if(!(planeEntity.isOnGround() || planeEntity.isOnWater() || planeEntity.isOnFire())) {
                event.setCanceled(true);
            }
        }
    }

    @SubscribeEvent(
            priority = EventPriority.HIGHEST
    )
    public static void UpdateCancellation(TickEvent.PlayerTickEvent event) {
        if(event.player.getVehicle() instanceof PlaneEntity && !event.player.level.isClientSide) {
            PlaneEntity planeEntity = (PlaneEntity) event.player.getVehicle();
            ResourceLocation name = new ResourceLocation("complicatedplanes:computer");
            if(planeEntity.upgrades.containsKey(name)) {
                ComputerUpgrade upgrade = (ComputerUpgrade) planeEntity.upgrades.get(name);
                ServerPlayerEntity player = (ServerPlayerEntity) event.player;
                Networking.INSTANCE.sendTo(new MovementPacket(upgrade.xxaa, upgrade.zzaa), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            }
        }
    }

}
