package com.igorstan.complicatedplanes.containter;

import com.igorstan.complicatedplanes.registry.Containers;
import com.igorstan.complicatedplanes.registry.Upgrades;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IntArray;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import xyz.przemyk.simpleplanes.container.FuelSlot;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;



public class LavaEngineContainer extends Container {
    public final IIntArray engineData;

    public LavaEngineContainer(int id, PlayerInventory playerInventory) {
        this(id, playerInventory, new ItemStackHandler(), new IntArray(2));
    }

    public LavaEngineContainer(int id, PlayerInventory playerInventory, IItemHandler itemHandler, IIntArray engineData) {
        super((ContainerType) Containers.LAVA_ENGINE.get(), id);
        this.engineData = engineData;
        //here
        this.addSlot(new FuelSlot(itemHandler, 0, 80, 62));

        int k;
        for(k = 0; k < 3; ++k) {
            for(int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + k * 9 + 9, 8 + j * 18, 84 + k * 18));
            }
        }

        for(k = 0; k < 9; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        this.addDataSlots(engineData);
    }

    public boolean stillValid(PlayerEntity playerIn) {
        Entity entity = playerIn.getVehicle();
        return entity instanceof PlaneEntity && entity.isAlive() ? ((PlaneEntity)entity).upgrades.containsKey(Upgrades.LAVA_ENGINE.getId()) : false;
    }

    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index != 0) {
                if (ForgeHooks.getBurnTime(itemstack1) > 0) {
                    if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 1 && index < 28) {
                    if (!this.moveItemStackTo(itemstack1, 28, 37, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 28 && index < 37 && !this.moveItemStackTo(itemstack1, 3, 30, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 1, 37, false)) {
                return ItemStack.EMPTY;
            }
        }

        return itemstack;
    }
}

