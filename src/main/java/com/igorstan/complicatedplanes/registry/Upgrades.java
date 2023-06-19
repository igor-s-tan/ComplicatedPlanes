//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.igorstan.complicatedplanes.registry;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;


import com.igorstan.complicatedplanes.upgrades.*;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import xyz.przemyk.simpleplanes.upgrades.UpgradeType;



public class Upgrades {
    private static final DeferredRegister<UpgradeType> UPGRADE_TYPES = DeferredRegister.create(UpgradeType.class, "complicatedplanes");

    public static final RegistryObject<UpgradeType> LAVA_FLOATY_BEDDING;
    public static final RegistryObject<UpgradeType> BARREL;
    public static final RegistryObject<UpgradeType> LAVA_ENGINE;
    public static final RegistryObject<UpgradeType> COMPUTER;
    public static final RegistryObject<UpgradeType> FAKE_PLAYER;
    private static final Map<Item, UpgradeType> ITEM_UPGRADE_MAP = new HashMap();
    private static final Map<Item, UpgradeType> LARGE_ITEM_UPGRADE_MAP = new HashMap();

    public Upgrades() {
    }

    public static void init() {
        UPGRADE_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

    public static void registerUpgradeItem(Item item, UpgradeType upgradeType) {
        ITEM_UPGRADE_MAP.put(item, upgradeType);
    }

    public static void registerLargeUpgradeItem(Item item, UpgradeType upgradeType) {
        LARGE_ITEM_UPGRADE_MAP.put(item, upgradeType);
    }

    public static Optional<UpgradeType> getUpgradeFromItem(Item item) {
        return ITEM_UPGRADE_MAP.containsKey(item) ? Optional.of(ITEM_UPGRADE_MAP.get(item)) : Optional.empty();
    }

    public static Optional<UpgradeType> getLargeUpgradeFromItem(Item item) {
        return LARGE_ITEM_UPGRADE_MAP.containsKey(item) ? Optional.of(LARGE_ITEM_UPGRADE_MAP.get(item)) : Optional.empty();
    }

    static {
        LAVA_FLOATY_BEDDING = UPGRADE_TYPES.register("lava_floaty_bedding", () -> {
            return new UpgradeType(LavaFloatingUpgrade::new);
        });
        BARREL = UPGRADE_TYPES.register("barrel", () -> {
            return new UpgradeType(BarrelUpgrade::new);
        });
        LAVA_ENGINE = UPGRADE_TYPES.register("lava_engine", () -> {
            return new UpgradeType(LavaEngineUpgrade::new);
        });
        COMPUTER = UPGRADE_TYPES.register("computer", () -> {
            return new UpgradeType(planeEntity -> new ComputerUpgrade(planeEntity, ComputerFamily.NORMAL));
        });
        FAKE_PLAYER = UPGRADE_TYPES.register("fake_player", () -> {
            return new UpgradeType(FakePlayerUpgrade::new);
        });
    }

}





