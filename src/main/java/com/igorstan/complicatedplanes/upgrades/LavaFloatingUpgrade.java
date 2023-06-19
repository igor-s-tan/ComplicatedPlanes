package com.igorstan.complicatedplanes.upgrades;

import com.igorstan.complicatedplanes.registry.Items;
import com.igorstan.complicatedplanes.registry.Upgrades;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.Blocks;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.EntityType;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3d;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;
import xyz.przemyk.simpleplanes.setup.SimplePlanesEntities;
import xyz.przemyk.simpleplanes.upgrades.Upgrade;
import xyz.przemyk.simpleplanes.upgrades.floating.FloatingModel;
import xyz.przemyk.simpleplanes.upgrades.floating.HelicopterFloatingModel;
import xyz.przemyk.simpleplanes.upgrades.floating.LargeFloatingModel;

public class LavaFloatingUpgrade extends Upgrade {
    public LavaFloatingUpgrade(PlaneEntity planeEntity) {
        super(Upgrades.LAVA_FLOATY_BEDDING.get(), planeEntity);
    }

    public static final ResourceLocation LAVA_TEXTURE = new ResourceLocation("complicatedplanes", "textures/plane_upgrades/lava_floating.png");
    public static final ResourceLocation LAVA_LARGE_TEXTURE = new ResourceLocation("complicatedplanes", "textures/plane_upgrades/lava_floating_large.png");
    public static final ResourceLocation LAVA_HELICOPTER_TEXTURE = new ResourceLocation("complicatedplanes", "textures/plane_upgrades/lava_floating_helicopter.png");


    public void tick() {
        if (this.planeEntity.level.getBlockState(new BlockPos(this.planeEntity.position().add(0.0, 0.4, 0.0))).getBlock() == Blocks.LAVA) {
            Vector3d motion = this.planeEntity.getDeltaMovement();
            double f = 1.0;
            double y = MathHelper.lerp(1.0, motion.y, Math.max(motion.y, 0.0));
            this.planeEntity.setDeltaMovement(motion.x * f, y, motion.z * f);
            if (this.planeEntity.level.getBlockState(new BlockPos(this.planeEntity.position().add(0.0, 0.5, 0.0))).getBlock() == Blocks.LAVA) {
                this.planeEntity.setDeltaMovement(this.planeEntity.getDeltaMovement().add(0.0, 0.04, 0.0));
            }
        }

    }

    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, float partialTicks) {
        EntityType<?> entityType = this.planeEntity.getType();
        if (entityType == SimplePlanesEntities.HELICOPTER.get()) {
            HelicopterFloatingModel.INSTANCE.renderToBuffer(matrixStack, buffer.getBuffer(LargeFloatingModel.INSTANCE.renderType(LAVA_HELICOPTER_TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        } else if (entityType == SimplePlanesEntities.LARGE_PLANE.get()) {
            LargeFloatingModel.INSTANCE.renderToBuffer(matrixStack, buffer.getBuffer(LargeFloatingModel.INSTANCE.renderType(LAVA_LARGE_TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        } else {
            FloatingModel.INSTANCE.renderToBuffer(matrixStack, buffer.getBuffer(FloatingModel.INSTANCE.renderType(LAVA_TEXTURE)), packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }

    }

    public void writePacket(PacketBuffer buffer) {
    }

    public void readPacket(PacketBuffer buffer) {
    }

    public void dropItems() {
        this.planeEntity.spawnAtLocation((IItemProvider) Items.LAVA_FLOATY_BEDDING.get());
    }
}
