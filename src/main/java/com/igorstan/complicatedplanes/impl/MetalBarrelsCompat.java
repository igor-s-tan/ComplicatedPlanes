//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.igorstan.complicatedplanes.impl;

import java.util.function.Consumer;

import com.igorstan.complicatedplanes.registry.Upgrades;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.ObjectHolder;
import xyz.przemyk.simpleplanes.setup.SimplePlanesUpgrades;
import xyz.przemyk.simpleplanes.upgrades.UpgradeType;

public class MetalBarrelsCompat {
    public static final String MODID = "metalbarrels";
    public static final String IRON_BARREL_NAME = "metalbarrels:iron_barrel";
    public static final String GOLD_BARREL_NAME = "metalbarrels:gold_barrel";
    public static final String DIAMOND_BARREL_NAME = "metalbarrels:diamond_barrel";
    public static final String COPPER_BARREL_NAME = "metalbarrels:copper_barrel";
    public static final String SILVER_BARREL_NAME = "metalbarrels:silver_barrel";
    public static final String CRYSTAL_BARREL_NAME = "metalbarrels:crystal_barrel";
    public static final String OBSIDIAN_BARREL_NAME = "metalbarrels:obsidian_barrel";
    @ObjectHolder("metalbarrels:iron_barrel")
    public static final Item IRON_BARREL = null;
    @ObjectHolder("metalbarrels:gold_barrel")
    public static final Item GOLD_BARREL = null;
    @ObjectHolder("metalbarrels:diamond_barrel")
    public static final Item DIAMOND_BARREL = null;
    @ObjectHolder("metalbarrels:copper_barrel")
    public static final Item COPPER_BARREL = null;
    @ObjectHolder("metalbarrels:silver_barrel")
    public static final Item SILVER_BARREL = null;
    @ObjectHolder("metalbarrels:crystal_barrel")
    public static final Item CRYSTAL_BARREL = null;
    @ObjectHolder("metalbarrels:obsidian_barrel")
    public static final Item OBSIDIAN_BARREL = null;
    @ObjectHolder("metalbarrels:netherite_barrel")
    public static final Item NETHERITE_BARREL = null;
    public static final ResourceLocation IRON_BARREL_GUI = new ResourceLocation("metalbarrels", "textures/gui/container/iron.png");
    public static final ResourceLocation GOLD_BARREL_GUI = new ResourceLocation("metalbarrels", "textures/gui/container/gold.png");
    public static final ResourceLocation DIAMOND_BARREL_GUI = new ResourceLocation("metalbarrels", "textures/gui/container/diamond.png");
    public static final ResourceLocation COPPER_BARREL_GUI = new ResourceLocation("metalbarrels", "textures/gui/container/copper.png");
    public static final ResourceLocation SILVER_BARREL_GUI = new ResourceLocation("metalbarrels", "textures/gui/container/silver.png");
    public static final ResourceLocation VANILLA_BARREL_GUI = new ResourceLocation("complicatedplanes", "textures/gui/vanilla_barrel.png");
    public static final ResourceLocation NETHERITE_BARREL_GUI = new ResourceLocation("metalbarrels", "textures/gui/container/netherite.png");

    public MetalBarrelsCompat() {
    }

    private static void registerBarrel(Item barrelItem) {
        if (barrelItem != null) {
            SimplePlanesUpgrades.registerLargeUpgradeItem(barrelItem, (UpgradeType) Upgrades.BARREL.get());
        }

    }

    public static void registerUpgradeItems() {
        registerBarrel(IRON_BARREL);
        registerBarrel(GOLD_BARREL);
        registerBarrel(DIAMOND_BARREL);
        registerBarrel(COPPER_BARREL);
        registerBarrel(SILVER_BARREL);
        registerBarrel(CRYSTAL_BARREL);
        registerBarrel(OBSIDIAN_BARREL);
        registerBarrel(NETHERITE_BARREL);
    }

    public static int getSize(String barrelType) {
        switch (barrelType) {
            case "metalbarrels:iron_barrel":
                return 54;
            case "metalbarrels:gold_barrel":
                return 81;
            case "metalbarrels:diamond_barrel":
            case "metalbarrels:obsidian_barrel":
            case "metalbarrels:crystal_barrel":
                return 108;
            case "metalbarrels:copper_barrel":
                return 45;
            case "metalbarrels:silver_barrel":
                return 72;
            case "metalbarrels:netherite_barrel":
                return 135;
            default:
                return 27;
        }
    }

    public static int getRowCount(String barrelType) {
        return getSize(barrelType) / getRowLength(barrelType);
    }

    public static int getRowLength(String barrelType) {
        switch (barrelType) {
            case "metalbarrels:diamond_barrel":
            case "metalbarrels:crystal_barrel":
            case "metalbarrels:obsidian_barrel":
                return 12;
            case "metalbarrels:netherite_barrel":
                return 15;
            default:
                return 9;
        }
    }

    public static void addSlots(String barrelType, IItemHandler itemHandler, int rowCount, PlayerInventory playerInventory, Consumer<Slot> addSlotFunction) {
        int rowLength;
        int ySize;
        int leftCol;

        rowLength = getRowLength(barrelType);
//here
        for(ySize = 0; ySize < rowCount; ++ySize) {
            for(leftCol = 0; leftCol < rowLength; ++leftCol) {
                addSlotFunction.accept(new SlotItemHandler(itemHandler, leftCol + ySize * rowLength, 8 + leftCol * 18, 18 + ySize * 18));
            }
        }


        rowLength = getXSize(barrelType);
        ySize = getYSize(barrelType);
        leftCol = (rowLength - 162) / 2 + 1;


        for (int row = 0; row < 3; ++row) {
            for(int column = 0; column < 9; ++column) {
                addSlotFunction.accept(new Slot(playerInventory, column + row * 9 + 9, leftCol + column * 18, ySize - (4 - row) * 18 - 10));
            }
        }

        for(int column = 0; column < 9; ++column) {
            addSlotFunction.accept(new Slot(playerInventory, column, leftCol + column * 18, ySize - 24));
        }

    }

    public static int getXSize(String barrelType) {
        switch (barrelType) {
            case "metalbarrels:netherite_barrel":
                return 284;
            case "metalbarrels:diamond_barrel":
            case "metalbarrels:crystal_barrel":
            case "metalbarrels:obsidian_barrel":
                return 230;
            default:
                return 176;
        }
    }

    public static int getYSize(String barrelType) {
        switch (barrelType) {
            case "metalbarrels:iron_barrel":
                return 222;
            case "metalbarrels:gold_barrel":
            case "metalbarrels:diamond_barrel":
            case "metalbarrels:crystal_barrel":
            case "metalbarrels:obsidian_barrel":
            case "metalbarrels:netherite_barrel":
                return 276;
            case "metalbarrels:silver_barrel":
                return 258;
            case "metalbarrels:copper_barrel":
                return 204;
            default:
                return 168;
        }
    }

    public static ResourceLocation getGuiTexture(String barrelType) {
        switch (barrelType) {
            case "metalbarrels:iron_barrel":
                return IRON_BARREL_GUI;
            case "metalbarrels:gold_barrel":
                return GOLD_BARREL_GUI;
            case "metalbarrels:diamond_barrel":
            case "metalbarrels:crystal_barrel":
            case "metalbarrels:obsidian_barrel":
                return DIAMOND_BARREL_GUI;
            case "metalbarrels:copper_barrel":
                return COPPER_BARREL_GUI;
            case "metalbarrels:silver_barrel":
                return SILVER_BARREL_GUI;
            case "metalbarrels:netherite_barrel":
                return NETHERITE_BARREL_GUI;
            default:
                return VANILLA_BARREL_GUI;
        }
    }

    public static int getTextureYSize(String barrelType) {
        switch (barrelType) {
            case "metalbarrels:gold_barrel":
            case "metalbarrels:diamond_barrel":
            case "metalbarrels:crystal_barrel":
            case "metalbarrels:obsidian_barrel":
            case "metalbarrels:silver_barrel":
            case "metalbarrels:netherite_barrel":
                return 512;
            default:
                return 256;
        }
    }public static int getTextureXSize(String barrelType) {
        switch (barrelType) {
            case "metalbarrels:netherite_barrel":
                return 512;
            default:
                return 256;
        }
    }
}
