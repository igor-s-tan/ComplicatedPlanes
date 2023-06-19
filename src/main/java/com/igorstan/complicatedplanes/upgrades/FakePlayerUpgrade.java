package com.igorstan.complicatedplanes.upgrades;

import com.igorstan.complicatedplanes.registry.Items;
import com.igorstan.complicatedplanes.registry.Upgrades;
import com.mojang.authlib.GameProfile;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
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
import xyz.przemyk.simpleplanes.entities.PlaneEntity;
import xyz.przemyk.simpleplanes.setup.SimplePlanesEntities;
import xyz.przemyk.simpleplanes.upgrades.Upgrade;
import xyz.przemyk.simpleplanes.upgrades.floating.FloatingModel;
import xyz.przemyk.simpleplanes.upgrades.floating.HelicopterFloatingModel;
import xyz.przemyk.simpleplanes.upgrades.floating.LargeFloatingModel;

import java.util.UUID;

public class FakePlayerUpgrade extends Upgrade {

    public FakePlayerUpgrade(PlaneEntity planeEntity) {
        super(Upgrades.FAKE_PLAYER.get(), planeEntity);

    }

    @Override
    public void onItemRightClick(PlayerInteractEvent.RightClickItem event) {
        System.out.println(this.planeEntity.getControllingPassenger());
    }

    public void onApply(ItemStack itemStack, PlayerEntity playerEntity) {
        if(!this.planeEntity.level.isClientSide) {
            FakePlayer fakePlayer = new FakePlayer((ServerWorld) this.planeEntity.level, new GameProfile(UUID.randomUUID(), "Wireless"));
            fakePlayer.setGameMode(GameType.SURVIVAL);
            fakePlayer.startRiding(this.planeEntity);
            System.out.println(this.planeEntity.getControllingPassenger());
        }
    }

    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, float partialTicks) {

    }

    public void writePacket(PacketBuffer buffer) {
    }

    public void readPacket(PacketBuffer buffer) {
    }

    public void dropItems() {

    }
}
