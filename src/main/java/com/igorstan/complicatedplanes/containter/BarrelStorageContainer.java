//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.igorstan.complicatedplanes.containter;

import com.igorstan.complicatedplanes.impl.MetalBarrelsCompat;
import com.igorstan.complicatedplanes.registry.Containers;
import com.igorstan.complicatedplanes.registry.Upgrades;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;

public class BarrelStorageContainer extends Container {
    public final int rowCount;
    public final int size;
    public final String barrelType;

    public BarrelStorageContainer(int id, PlayerInventory playerInventory, PacketBuffer buffer) {
        this(id, playerInventory, buffer.readUtf(32767));
    }

    private BarrelStorageContainer(int id, PlayerInventory playerInventory, String barrelType) {
        this(id, playerInventory, new ItemStackHandler(MetalBarrelsCompat.getSize(barrelType)), barrelType);
    }

    public BarrelStorageContainer(int id, PlayerInventory playerInventory, IItemHandler itemHandler, String barrelType) {
        super(Containers.BARREL_STORAGE.get(), id);
        this.rowCount = MetalBarrelsCompat.getRowCount(barrelType);
        MetalBarrelsCompat.addSlots(barrelType, itemHandler, this.rowCount, playerInventory, this::addSlot);
        this.size = itemHandler.getSlots();
        this.barrelType = barrelType;
    }

    public boolean stillValid(PlayerEntity playerIn) {
        Entity entity = playerIn.getVehicle();
        return entity instanceof PlaneEntity && entity.isAlive() ? ((PlaneEntity)entity).upgrades.containsKey(Upgrades.BARREL.getId()) : false;
    }

    public ItemStack quickMoveStack(PlayerEntity playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.size) {
                if (!this.moveItemStackTo(itemstack1, this.size, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(itemstack1, 0, this.size, false)) {
                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }
}
