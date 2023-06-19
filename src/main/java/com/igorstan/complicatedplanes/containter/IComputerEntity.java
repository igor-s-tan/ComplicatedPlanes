package com.igorstan.complicatedplanes.containter;

import dan200.computercraft.shared.computer.core.ComputerFamily;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;

import javax.annotation.Nonnull;

public interface IComputerEntity {
    String NBT_ID = "ComputerId";

    default int getComputerID(@Nonnull PlaneEntity planeEntity) {
        CompoundNBT nbt = planeEntity.getPersistentData();
        return nbt != null && nbt.contains("ComputerId") ? nbt.getInt("ComputerId") : -1;
    }

    default String getLabel(@Nonnull PlaneEntity planeEntity) {
        return planeEntity.hasCustomName() ? planeEntity.getDisplayName().getString() : null;
    }

    ComputerFamily getFamily();

    ItemStack withFamily(@Nonnull PlaneEntity var1, @Nonnull ComputerFamily var2);
}

