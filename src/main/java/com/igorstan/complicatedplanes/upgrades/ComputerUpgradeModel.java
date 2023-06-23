package com.igorstan.complicatedplanes.upgrades;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.ModelRenderer;

import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;
import xyz.przemyk.simpleplanes.upgrades.floating.FloatingModel;


public class ComputerUpgradeModel extends EntityModel<PlaneEntity> {


    public static final ComputerUpgradeModel INSTANCE = new ComputerUpgradeModel();
    private final ModelRenderer bb_main;
    private final ModelRenderer computer_r1;

    public ComputerUpgradeModel() {
        texWidth = 32;
        texHeight = 32;

        bb_main = new ModelRenderer(this);
        bb_main.setPos(0.0F, 24.0F, 0.0F);


        computer_r1 = new ModelRenderer(this);
        computer_r1.setPos(0.0F, 0.0F, 0.0F);
        bb_main.addChild(computer_r1);
        setRotationAngle(computer_r1, -0.48F, 0.0F, 0.0F);
        computer_r1.texOffs(0, 0).addBox(-6.0001F, -15.25F, -20.0F, 12.0F, 2.0F, 4.0F, 0.0F, false);
    }
    @Override
    public void setupAnim(PlaneEntity p_225597_1_, float p_225597_2_, float p_225597_3_, float p_225597_4_, float p_225597_5_, float p_225597_6_) {

    }
    public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
        modelRenderer.xRot = x;
        modelRenderer.yRot = y;
        modelRenderer.zRot = z;
    }


    @Override
    public void renderToBuffer(MatrixStack matrixStack, IVertexBuilder buffer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        this.bb_main.render(matrixStack, buffer, packedLight, packedOverlay);

    }
}