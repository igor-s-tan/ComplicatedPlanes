package com.igorstan.complicatedplanes.registry;

import com.igorstan.complicatedplanes.containter.BarrelStorageContainer;
import com.igorstan.complicatedplanes.containter.LavaEngineContainer;
import dan200.computercraft.shared.computer.inventory.ComputerMenuWithoutInventory;
import dan200.computercraft.shared.computer.inventory.ContainerComputerBase;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import dan200.computercraft.shared.network.container.ContainerData;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


public class Containers {
    public static final DeferredRegister<ContainerType<?>> CONTAINERS;
    public static final RegistryObject<ContainerType<BarrelStorageContainer>> BARREL_STORAGE;
    public static final RegistryObject<ContainerType<BarrelStorageContainer>> LAVA_ENGINE;
//    public static final RegistryObject<ContainerType<ContainerComputerBase>> COMPUTER;
//    public static final RegistryObject<ContainerType<ContainerComputerBase>> COMPUTER_NO_TERM;



    public static void init() {
        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    static {
        CONTAINERS = DeferredRegister.create(ForgeRegistries.CONTAINERS, "complicatedplanes");

        BARREL_STORAGE = CONTAINERS.register("barrel_storage", () -> {
            return IForgeContainerType.create(BarrelStorageContainer::new);
        });
        LAVA_ENGINE = CONTAINERS.register("lava_engine", () -> {
            return new ContainerType(LavaEngineContainer::new);
        });

//        COMPUTER = CONTAINERS.register("computer", () -> {
//            return ContainerData.toType(ComputerContainerData::new, ComputerMenuWithoutInventory::new);
//        });
//        COMPUTER_NO_TERM = CONTAINERS.register("computer_no_term", () -> {
//            return ContainerData.toType(ComputerContainerData::new, ComputerMenuWithoutInventory::new);
//        });

    }
}
