package com.igorstan.complicatedplanes.screen;

import com.igorstan.complicatedplanes.containter.BarrelStorageContainer;
import com.igorstan.complicatedplanes.impl.MetalBarrelsCompat;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class BarrelStorageScreen extends ContainerScreen<BarrelStorageContainer> {
    public final ResourceLocation texture;
    public final int textureXSize;
    public final int textureYSize;

    public BarrelStorageScreen(BarrelStorageContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
        this.imageHeight = MetalBarrelsCompat.getYSize(screenContainer.barrelType);
        this.imageWidth = MetalBarrelsCompat.getXSize(screenContainer.barrelType);
        this.inventoryLabelY = this.imageHeight - 94;
        this.texture = MetalBarrelsCompat.getGuiTexture(screenContainer.barrelType);
        this.textureYSize = MetalBarrelsCompat.getTextureYSize(screenContainer.barrelType);
        this.textureXSize = MetalBarrelsCompat.getTextureXSize(screenContainer.barrelType);

    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    protected void renderBg(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(this.texture);
        int x = (this.width - this.imageWidth) / 2;
        int y = (this.height - this.imageHeight) / 2;
        blit(matrixStack, x, y, 0.0F, 0.0F, this.imageWidth, this.imageHeight, this.textureXSize, this.textureYSize);
    }
}
