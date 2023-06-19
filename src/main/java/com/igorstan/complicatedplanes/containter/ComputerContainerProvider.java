package com.igorstan.complicatedplanes.containter;

import com.igorstan.complicatedplanes.registry.Containers;
import com.igorstan.complicatedplanes.upgrades.ComputerUpgrade;
import dan200.computercraft.core.computer.Computer;
import dan200.computercraft.shared.Registry;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.inventory.ComputerMenuWithoutInventory;
import dan200.computercraft.shared.pocket.items.ItemPocketComputer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ComputerContainerProvider implements INamedContainerProvider {
    private final ServerComputer computer;
    private final ITextComponent name;
    private final PlaneEntity planeEntity;
    private final ComputerUpgrade computerUpgrade;
    private final boolean isTypingOnly;

    public ComputerContainerProvider(ServerComputer computer, ComputerUpgrade computerUpgrade, PlaneEntity planeEntity, boolean isTypingOnly) {
        this.computer = computer;
        this.name = planeEntity.getDisplayName();
        this.computerUpgrade = computerUpgrade;
        this.isTypingOnly = isTypingOnly;
        this.planeEntity = planeEntity;
    }

    @Nonnull
    public ITextComponent getDisplayName() {
        return this.name;
    }

    @Nullable
    public Container createMenu(int id, @Nonnull PlayerInventory inventory, @Nonnull PlayerEntity entity) {
        return new ComputerMenuWithoutInventory(Registry.ModContainers.COMPUTER.get(), id, inventory, (p) -> {
            return ComputerUpgrade.getServerComputer(entity.level.getServer(), planeEntity) == this.computer;
        }, this.computer, this.computerUpgrade.getFamily());
    }
}