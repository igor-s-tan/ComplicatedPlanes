package com.igorstan.complicatedplanes.upgrades;


import com.igorstan.complicatedplanes.WirelessModuleEntity;
import com.igorstan.complicatedplanes.registry.Items;
import com.igorstan.complicatedplanes.registry.Upgrades;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.matrix.MatrixStack;
import dan200.computercraft.shared.Registry;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.GameType;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.items.ItemStackHandler;
import xyz.przemyk.simpleplanes.entities.LargePlaneEntity;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;
import xyz.przemyk.simpleplanes.setup.SimplePlanesEntities;
import xyz.przemyk.simpleplanes.upgrades.Upgrade;
import xyz.przemyk.simpleplanes.upgrades.floating.FloatingModel;
import xyz.przemyk.simpleplanes.upgrades.floating.HelicopterFloatingModel;
import xyz.przemyk.simpleplanes.upgrades.floating.LargeFloatingModel;

import java.util.EventListener;
import java.util.UUID;

public class FakePlayerUpgrade extends Upgrade {
    public static final ResourceLocation TEXTURE = new ResourceLocation("complicatedplanes", "textures/plane_upgrades/plane_lid.png");
    public final ItemStackHandler itemStackHandler = new ItemStackHandler(27);

    public WirelessModuleEntity fakePlayer;

    public FakePlayerUpgrade(PlaneEntity planeEntity) {
        super(Upgrades.FAKE_PLAYER.get(), planeEntity);
    }

    @Override
    public void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
    }

    public void onApply(ItemStack itemStack, PlayerEntity playerEntity) {
        if(!this.planeEntity.level.isClientSide && this.planeEntity.getPassengers().isEmpty()) {
            fakePlayer = new WirelessModuleEntity((ServerWorld) this.planeEntity.level, new GameProfile(UUID.randomUUID(), "Wireless"));
            fakePlayer.startRiding(this.planeEntity);
            net.minecraftforge.event.entity.EntityMountEvent event = null;
            event = new net.minecraftforge.event.entity.EntityMountEvent(fakePlayer.getEntity(), this.planeEntity, this.planeEntity.level, true);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        }
    }

    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, float partialTicks) {
        matrixStack.pushPose();
        EntityType<?> entityType = this.planeEntity.getType();
        if (entityType == SimplePlanesEntities.HELICOPTER.get()) {
            matrixStack.translate(0.0, 0.0, -0.2);
        }
        PlaneLidModel.INSTANCE.renderToBuffer(matrixStack, buffer.getBuffer(PlaneLidModel.INSTANCE.renderType(TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStack.popPose();

    }


    public void writePacket(PacketBuffer buffer) {
    }

    public void readPacket(PacketBuffer buffer) {
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = this.itemStackHandler.serializeNBT();
        nbt.putUUID("fake_player_uuid", fakePlayer.getUUID());
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        this.itemStackHandler.deserializeNBT(nbt);
        if(!this.planeEntity.level.isClientSide) {
            fakePlayer = new WirelessModuleEntity((ServerWorld) this.planeEntity.level, new GameProfile(nbt.getUUID("fake_player_uuid"), "Wireless"));
            fakePlayer.startRiding(this.planeEntity);
            net.minecraftforge.event.entity.EntityMountEvent event = null;
            event = new net.minecraftforge.event.entity.EntityMountEvent(fakePlayer.getEntity(), this.planeEntity, this.planeEntity.level, true);
            net.minecraftforge.common.MinecraftForge.EVENT_BUS.post(event);
        }
    }

    public void dropItems() {
        if(!this.planeEntity.level.isClientSide) {
            fakePlayer.remove();
        }
        this.planeEntity.spawnAtLocation((IItemProvider) Registry.ModItems.WIRELESS_MODEM_NORMAL.get());
    }
}
