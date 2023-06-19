package com.igorstan.complicatedplanes;

import com.igorstan.complicatedplanes.event.ClientEventHandler;
import com.igorstan.complicatedplanes.event.CommonEventHandler;
import com.igorstan.complicatedplanes.impl.MetalBarrelsCompat;
import com.igorstan.complicatedplanes.network.Networking;
import com.igorstan.complicatedplanes.registry.Containers;
import com.igorstan.complicatedplanes.registry.Items;
import com.igorstan.complicatedplanes.registry.Upgrades;
import com.igorstan.complicatedplanes.screen.BarrelStorageScreen;
import dan200.computercraft.shared.Registry;
import net.minecraft.block.Block;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.InterModComms;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModEnqueueEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.przemyk.simpleplanes.setup.SimplePlanesUpgrades;
import xyz.przemyk.simpleplanes.upgrades.UpgradeType;

import java.util.stream.Collectors;


@Mod("complicatedplanes")
public class ComplicatedPlanesMod
{
    private static final Logger LOGGER = LogManager.getLogger();

    public ComplicatedPlanesMod() {
        Items.init();
        Upgrades.init();
        Containers.init();
        Networking.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::enqueueIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::processIMC);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::doClientStuff);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event)
    {
        MinecraftForge.EVENT_BUS.register(CommonEventHandler.class);
        event.enqueueWork(() -> {
            SimplePlanesUpgrades.registerUpgradeItem(Items.LAVA_FLOATY_BEDDING.get(), (UpgradeType) Upgrades.LAVA_FLOATY_BEDDING.get());
            SimplePlanesUpgrades.registerLargeUpgradeItem(net.minecraft.item.Items.BARREL, (UpgradeType)Upgrades.BARREL.get());
            SimplePlanesUpgrades.registerUpgradeItem((Item) Items.LAVA_ENGINE.get(), (UpgradeType)Upgrades.LAVA_ENGINE.get());
            SimplePlanesUpgrades.registerUpgradeItem(Registry.ModItems.COMPUTER_NORMAL.get(), (UpgradeType) Upgrades.COMPUTER.get());
            SimplePlanesUpgrades.registerUpgradeItem(net.minecraft.item.Items.ACACIA_LOG, Upgrades.FAKE_PLAYER.get());
            MetalBarrelsCompat.registerUpgradeItems();
        });
    }

    private void doClientStuff(final FMLClientSetupEvent event) {
        MinecraftForge.EVENT_BUS.register(ClientEventHandler.class);
        ScreenManager.register((ContainerType)Containers.BARREL_STORAGE.get(), BarrelStorageScreen::new);
    }

    private void enqueueIMC(final InterModEnqueueEvent event)
    {
        InterModComms.sendTo("complicatedplanes", "helloworld", () -> { LOGGER.info("Hello world from the MDK"); return "Hello world";});
    }

    private void processIMC(final InterModProcessEvent event)
    {
        LOGGER.info("Got IMC {}", event.getIMCStream().
                map(m->m.getMessageSupplier().get()).
                collect(Collectors.toList()));
    }

    @SubscribeEvent
    public void onServerStarting(FMLServerStartingEvent event) {

    }


    @Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistryEvents {
        @SubscribeEvent
        public static void onBlocksRegistry(final RegistryEvent.Register<Block> blockRegistryEvent) {
        }
    }
}
