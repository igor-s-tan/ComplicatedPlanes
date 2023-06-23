package com.igorstan.complicatedplanes.event;

import com.igorstan.complicatedplanes.ComputerAccessItem;
import com.igorstan.complicatedplanes.containter.ComputerContainerProvider;
import com.igorstan.complicatedplanes.network.MovementPacket;
import com.igorstan.complicatedplanes.network.Networking;
import com.igorstan.complicatedplanes.registry.Upgrades;
import com.igorstan.complicatedplanes.upgrades.BarrelUpgrade;
import com.igorstan.complicatedplanes.upgrades.ComputerUpgrade;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;
import xyz.przemyk.simpleplanes.setup.SimplePlanesItems;
import xyz.przemyk.simpleplanes.upgrades.Upgrade;
import xyz.przemyk.simpleplanes.upgrades.UpgradeType;
import xyz.przemyk.simpleplanes.upgrades.storage.ChestUpgrade;

import java.util.Optional;

import static xyz.przemyk.simpleplanes.setup.SimplePlanesUpgrades.getLargeUpgradeFromItem;
import static xyz.przemyk.simpleplanes.setup.SimplePlanesUpgrades.getUpgradeFromItem;


@Mod.EventBusSubscriber({Dist.DEDICATED_SERVER})
public class CommonEventHandler {
    @SubscribeEvent(
            priority = EventPriority.LOWEST
    )
    public static void Interaction(PlayerInteractEvent.EntityInteract event) {

        if(!event.getWorld().isClientSide) {
            if (event.getTarget() instanceof PlaneEntity) {
                PlaneEntity planeEntity = (PlaneEntity) event.getTarget();
                Optional<UpgradeType> upgradeType1 = getUpgradeFromItem(event.getPlayer().getMainHandItem().getItem());
                Optional<UpgradeType> upgradeType2 = getLargeUpgradeFromItem(event.getPlayer().getMainHandItem().getItem());
                boolean flag = false;
                if(upgradeType1.isPresent()) {
                    flag = planeEntity.canAddUpgrade(upgradeType1.get());
                }
                if(upgradeType2.isPresent()) {
                    flag = planeEntity.canAddUpgrade(upgradeType2.get());
                }

                if (planeEntity.upgrades.containsKey(new ResourceLocation("complicatedplanes:computer"))) {
                    if (event.getPlayer().getMainHandItem().getItem() instanceof ComputerAccessItem && planeEntity.getPlayer() != null) {
                        ComputerUpgrade upgrade = (ComputerUpgrade) planeEntity.upgrades.get(new ResourceLocation("complicatedplanes:computer"));
                        ServerComputer computer = upgrade.createServerComputer((ServerWorld) event.getWorld(), event.getPlayer(), event.getPlayer().inventory, planeEntity);
                        computer.keepAlive();
                        computer.turnOn();
                        (new ComputerContainerData(computer, event.getItemStack())).open(event.getPlayer(), new ComputerContainerProvider(computer, upgrade, planeEntity, false));
                        event.setCanceled(true);
                    }
                    else if (event.getPlayer().getMainHandItem().getItem() != SimplePlanesItems.WRENCH.get() && event.getTarget() instanceof PlaneEntity && !flag) {
                        if (planeEntity.upgrades.containsKey(new ResourceLocation("complicatedplanes:fake_player"))) {
                            event.setCanceled(true);
                        }
                    }

                }
                else if (event.getPlayer().getMainHandItem().getItem() != SimplePlanesItems.WRENCH.get() && event.getTarget() instanceof PlaneEntity && !flag) {
                    if (planeEntity.upgrades.containsKey(new ResourceLocation("complicatedplanes:fake_player"))) {
                        event.setCanceled(true);
                    }
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
            priority = EventPriority.LOWEST
    )
    public static void MovePlane(TickEvent.PlayerTickEvent event) {
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
    @SubscribeEvent(
            priority = EventPriority.LOWEST
    )
    public static void MoveFakePlayer(EntityMountEvent event) {
        if(!event.getEntityMounting().level.isClientSide && event.getEntityMounting() instanceof FakePlayer && event.getEntityMounting().getVehicle() instanceof PlaneEntity && event.isMounting()) {
            FakePlayer fakePlayer = (FakePlayer) event.getEntityMounting();
            PlaneEntity planeEntity = (PlaneEntity) fakePlayer.getVehicle();
            ResourceLocation name = new ResourceLocation("complicatedplanes:computer");
            if(planeEntity.upgrades.containsKey(name)) {
                ComputerUpgrade upgrade = (ComputerUpgrade) planeEntity.upgrades.get(name);
                fakePlayer.xxa = upgrade.xxaa;
                fakePlayer.zza = upgrade.zzaa;
            }
        }
    }
}
