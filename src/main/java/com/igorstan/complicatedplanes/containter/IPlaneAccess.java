package com.igorstan.complicatedplanes.containter;

import com.mojang.authlib.GameProfile;
import dan200.computercraft.api.lua.MethodResult;
import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.ITurtleUpgrade;
import dan200.computercraft.api.turtle.TurtleAnimation;
import dan200.computercraft.api.turtle.TurtleSide;
import net.minecraft.inventory.IInventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface IPlaneAccess {
    @Nonnull
    World getWorld();

    @Nonnull
    BlockPos getPosition();

    boolean teleportTo(@Nonnull World var1, @Nonnull BlockPos var2);

    @Nullable
    GameProfile getOwningPlayer();

    @Nonnull
    MethodResult executeCommand(@Nonnull IPlaneCommand var1);
}
