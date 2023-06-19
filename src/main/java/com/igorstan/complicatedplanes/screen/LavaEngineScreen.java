package com.igorstan.complicatedplanes.screen;

import com.igorstan.complicatedplanes.containter.LavaEngineContainer;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

public class LavaEngineScreen extends ContainerScreen<LavaEngineContainer> {
    private static final ResourceLocation GUI = new ResourceLocation("complicatedplanes", "textures/gui/lava_engine.png");

    public LavaEngineScreen(LavaEngineContainer screenContainer, PlayerInventory inv, ITextComponent titleIn) {
        super(screenContainer, inv, titleIn);
    }

    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(matrixStack);
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrixStack, mouseX, mouseY);
    }

    protected void renderBg(MatrixStack matrixStack, float partialTicks, int x, int y) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(GUI);
        this.blit(matrixStack, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        if (((LavaEngineContainer)this.menu).engineData.get(0) > 0) {
            int burnLeftScaled = this.getBurnLeftScaled();
            this.blit(matrixStack, this.leftPos + 80, this.topPos + 57 - burnLeftScaled, 176, 12 - burnLeftScaled, 14, burnLeftScaled + 1);
        }
    }

    private int getBurnLeftScaled() {
        int burnTimeTotal = ((LavaEngineContainer)this.menu).engineData.get(1);
        if (burnTimeTotal == 0) {
            burnTimeTotal = 200;
        }

        return ((LavaEngineContainer)this.menu).engineData.get(0) * 13 / burnTimeTotal;
    }
}

