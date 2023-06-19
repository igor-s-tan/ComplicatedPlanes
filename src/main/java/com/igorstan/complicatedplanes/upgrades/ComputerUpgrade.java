package com.igorstan.complicatedplanes.upgrades;


import com.google.common.base.Objects;
import com.igorstan.complicatedplanes.api.PlaneAPI;
import com.igorstan.complicatedplanes.containter.ComputerContainerProvider;
import com.igorstan.complicatedplanes.containter.IComputerEntity;
import com.igorstan.complicatedplanes.registry.Containers;
import com.igorstan.complicatedplanes.registry.Upgrades;
import com.mojang.blaze3d.matrix.MatrixStack;

import dan200.computercraft.ComputerCraft;
import dan200.computercraft.api.ComputerCraftAPI;
import dan200.computercraft.api.pocket.IPocketUpgrade;
import dan200.computercraft.core.computer.Computer;
import dan200.computercraft.core.computer.ComputerSide;
import dan200.computercraft.shared.Registry;

import dan200.computercraft.shared.computer.apis.CommandAPI;
import dan200.computercraft.shared.computer.blocks.IComputerTile;
import dan200.computercraft.shared.computer.core.ComputerFamily;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.computer.core.ServerComputerRegistry;
import dan200.computercraft.shared.computer.core.ServerContext;
import dan200.computercraft.shared.computer.inventory.ComputerMenuWithoutInventory;

import dan200.computercraft.shared.computer.items.ComputerItemFactory;
import dan200.computercraft.shared.computer.items.IComputerItem;
import dan200.computercraft.shared.computer.menu.ComputerMenu;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import dan200.computercraft.shared.pocket.apis.PocketAPI;
import dan200.computercraft.shared.pocket.core.PocketServerComputer;
import dan200.computercraft.shared.pocket.inventory.PocketComputerMenuProvider;
import dan200.computercraft.shared.pocket.items.PocketComputerItemFactory;
import dan200.computercraft.shared.turtle.apis.TurtleAPI;
import net.minecraft.block.BlockState;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.INamedContainerProvider;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;

import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;

import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.client.model.data.EmptyModelData;


import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import net.minecraftforge.items.ItemStackHandler;

import xyz.przemyk.simpleplanes.entities.PlaneEntity;
import xyz.przemyk.simpleplanes.setup.SimplePlanesEntities;
import xyz.przemyk.simpleplanes.upgrades.LargeUpgrade;
import xyz.przemyk.simpleplanes.upgrades.Upgrade;
import xyz.przemyk.simpleplanes.upgrades.UpgradeType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Map;

public class ComputerUpgrade extends Upgrade implements IComputerEntity {
    public final ItemStackHandler itemStackHandler = new ItemStackHandler(27);
    private final ComputerFamily family;
    public float xxaa;

    public ComputerUpgrade(PlaneEntity planeEntity, ComputerFamily family) {
        super((UpgradeType) Upgrades.COMPUTER.get(), planeEntity);
        this.family = family;
    }


    public void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        ItemStack stack = event.getItemStack();
        if (!this.planeEntity.level.isClientSide) {
            ServerComputer computer = this.createServerComputer((ServerWorld)this.planeEntity.level, this.planeEntity.getPlayer(), this.planeEntity.getPlayer().inventory, planeEntity);
            computer.turnOn();
            boolean stop = false;
            if (!stop) {
                (new ComputerContainerData(computer, stack)).open(this.planeEntity.getPlayer(), new ComputerContainerProvider(computer, this, this.getPlaneEntity(), false));
            }
        }
    }


    public void writePacket(PacketBuffer buffer) {

    }

    public void readPacket(PacketBuffer buffer) {

    }
    @Override
    public void tick() {
        if(!this.planeEntity.level.isClientSide && this.planeEntity.getPlayer() != null) {
            ServerComputer computer = this.createServerComputer((ServerWorld)this.planeEntity.level, this.planeEntity.getPlayer(), this.planeEntity.getPlayer().inventory, this.planeEntity);
            computer.keepAlive();
        }
    }

    @Override
    public void dropItems() {
        this.planeEntity.spawnAtLocation(Registry.ModItems.COMPUTER_NORMAL.get().asItem());
    }

    @Nullable
    public static ServerComputer getServerComputer(MinecraftServer server, PlaneEntity planeEntity) {
        return (ServerComputer)ServerContext.get(server).registry().get(getSessionID(planeEntity), getInstanceID(planeEntity));
    }

    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, float partialTicks) {
        matrixStack.pushPose();
        EntityType<?> entityType = this.planeEntity.getType();
        if (entityType == SimplePlanesEntities.HELICOPTER.get()) {
            matrixStack.translate(0.0, 0.0, -0.3);
        } else if (entityType == SimplePlanesEntities.LARGE_PLANE.get()) {
            matrixStack.translate(0.0, 0.0, 0.1);
        }

        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F));
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        matrixStack.translate(-0.4, -1.0, -1.3);
        matrixStack.scale(0.82F, 0.82F, 0.82F);
        BlockState state = Registry.ModBlocks.COMPUTER_NORMAL.get().defaultBlockState();
        Minecraft.getInstance().getBlockRenderer().renderBlock(state, matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
        matrixStack.popPose();
    }

    @Nonnull
    public ServerComputer createServerComputer(ServerWorld world, Entity entity, @Nullable IInventory inventory, @Nonnull PlaneEntity planeEntity) {
        if (world.isClientSide) {
            throw new IllegalStateException("Cannot call createServerComputer on the client");
        } else {
            int sessionID = getSessionID(planeEntity);
            ServerComputerRegistry registry = ServerContext.get(world.getServer()).registry();
            ServerComputer computer = (ServerComputer)registry.get(sessionID, getInstanceID(planeEntity));
            if (computer == null) {
                int computerID = this.getComputerID(planeEntity);
                if (computerID < 0) {
                    computerID = ComputerCraftAPI.createUniqueNumberedSaveDir(world, "computer");
                    setComputerID(planeEntity, computerID);
                }

                computer = new ServerComputer(world, this.getComputerID(planeEntity), this.getLabel(planeEntity), this.getFamily(), ComputerCraft.computerTermWidth, ComputerCraft.computerTermHeight);
                setInstanceID(planeEntity, computer.register());
                setSessionID(planeEntity, registry.getSessionID());

                computer.addAPI(new PlaneAPI(SimplePlanesEntities.PLANE.get(), computer, this.planeEntity));
                if (isMarkedOn(planeEntity) && entity instanceof PlayerEntity) {
                    computer.turnOn();
                }

                if (inventory != null) {
                    inventory.setChanged();
                }
            }

            computer.setWorld(world);
            return computer;
        }
    }

    public static int getInstanceID(@Nonnull PlaneEntity planeEntity) {
        CompoundNBT nbt = planeEntity.getPersistentData();
        return nbt != null && nbt.contains("Instanceid") ? nbt.getInt("Instanceid") : -1;
    }

    private static void setInstanceID(@Nonnull PlaneEntity planeEntity, int instanceID) {
        planeEntity.getPersistentData().putInt("Instanceid", instanceID);
    }

    private static int getSessionID(@Nonnull PlaneEntity planeEntity) {
        CompoundNBT nbt = planeEntity.getPersistentData();
        return nbt != null && nbt.contains("SessionId") ? nbt.getInt("SessionId") : -1;
    }

    private static void setSessionID(@Nonnull PlaneEntity planeEntity, int sessionID) {
        planeEntity.getPersistentData().putInt("SessionId", sessionID);
    }

    private static boolean isMarkedOn(@Nonnull PlaneEntity planeEntity) {
        CompoundNBT nbt = planeEntity.getPersistentData();
        return nbt != null && nbt.getBoolean("On");
    }
    private static void setComputerID(@Nonnull PlaneEntity planeEntity, int computerID) {
        planeEntity.getPersistentData().putInt("ComputerId", computerID);
    }

    @Override
    public int getComputerID(@Nonnull PlaneEntity planeEntity) {
        return IComputerEntity.super.getComputerID(planeEntity);
    }

    @Override
    public String getLabel(@Nonnull PlaneEntity planeEntity) {
        return IComputerEntity.super.getLabel(planeEntity);
    }

    @Override
    public ComputerFamily getFamily() {
        return this.family;
    }

    @Override
    public ItemStack withFamily(@Nonnull PlaneEntity var1, @Nonnull ComputerFamily var2) {
        return ComputerItemFactory.create(this.getComputerID(planeEntity), this.getLabel(planeEntity), family);
    }
}