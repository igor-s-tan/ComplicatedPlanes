package com.igorstan.complicatedplanes.registry;

import com.igorstan.complicatedplanes.AccessItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.przemyk.simpleplanes.setup.SimplePlanesItems;

@Mod.EventBusSubscriber(
        modid = "complicatedplanes",
        bus = Mod.EventBusSubscriber.Bus.MOD
)
public class Items {
    public static final RegistryObject<Item> LAVA_FLOATY_BEDDING;
    public static final RegistryObject<Item> LAVA_ENGINE;
    public static final RegistryObject<Item> ACCESS_ITEM;
    public static final DeferredRegister<Item> ITEMS;
    public static void init() {
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }
    static {
        ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "complicatedplanes");
        LAVA_FLOATY_BEDDING = ITEMS.register("lava_floaty_bedding", () -> {
            return new Item((new Item.Properties()).tab(SimplePlanesItems.ITEM_GROUP));
        });
        LAVA_ENGINE = ITEMS.register("lava_engine", () -> {
            return new Item((new Item.Properties()).tab(SimplePlanesItems.ITEM_GROUP));
        });
        ACCESS_ITEM = ITEMS.register("access_item", () -> {
            return new AccessItem((new Item.Properties()).tab(SimplePlanesItems.ITEM_GROUP));
        });
    }
}
