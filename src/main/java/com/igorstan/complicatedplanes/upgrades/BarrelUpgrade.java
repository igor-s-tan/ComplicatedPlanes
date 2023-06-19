package com.igorstan.complicatedplanes.upgrades;

import com.igorstan.complicatedplanes.containter.BarrelStorageContainer;
import com.igorstan.complicatedplanes.impl.MetalBarrelsCompat;
import com.igorstan.complicatedplanes.registry.Upgrades;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.client.model.data.EmptyModelData;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;
import xyz.przemyk.simpleplanes.setup.SimplePlanesEntities;
import xyz.przemyk.simpleplanes.upgrades.LargeUpgrade;
import xyz.przemyk.simpleplanes.upgrades.UpgradeType;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class BarrelUpgrade extends LargeUpgrade implements INamedContainerProvider {
    public final ItemStackHandler itemStackHandler = new ItemStackHandler(27);
    public final LazyOptional<ItemStackHandler> itemHandlerLazyOptional = LazyOptional.of(() -> {
        return this.itemStackHandler;
    });
    public Item barrelType;

    public BarrelUpgrade(PlaneEntity planeEntity) {
        super((UpgradeType) Upgrades.BARREL.get(), planeEntity);
        this.barrelType = Items.BARREL;
    }

    protected void invalidateCaps() {
        super.invalidateCaps();
        this.itemHandlerLazyOptional.invalidate();
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = this.itemStackHandler.serializeNBT();
        nbt.putString("barrelType", this.barrelType.getRegistryName().toString());
        return nbt;
    }

    public void deserializeNBT(CompoundNBT nbt) {
        this.itemStackHandler.deserializeNBT(nbt);
        Item item = (Item) ForgeRegistries.ITEMS.getValue(new ResourceLocation(nbt.getString("barrelType")));
        this.barrelType = item == null ? Items.BARREL : item;
    }

    public void writePacket(PacketBuffer buffer) {
        buffer.writeRegistryId(this.barrelType);
    }

    public void readPacket(PacketBuffer buffer) {
        this.barrelType = (Item)buffer.readRegistryIdSafe(Item.class);
    }

    public void dropItems() {
        for(int i = 0; i < this.itemStackHandler.getSlots(); ++i) {
            ItemStack itemStack = this.itemStackHandler.getStackInSlot(i);
            if (!itemStack.isEmpty()) {
                this.planeEntity.spawnAtLocation(itemStack);
            }
        }

        this.planeEntity.spawnAtLocation(this.barrelType);
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
        BlockState state = this.barrelType instanceof BlockItem ? ((BlockItem)this.barrelType).getBlock().defaultBlockState() : Blocks.BARREL.defaultBlockState();
        Minecraft.getInstance().getBlockRenderer().renderBlock(state, matrixStack, buffer, packedLight, OverlayTexture.NO_OVERLAY, EmptyModelData.INSTANCE);
        matrixStack.popPose();
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("complicatedplanes:barrel");
    }

    public Container createMenu(int id, PlayerInventory playerInventoryIn, PlayerEntity playerEntity) {
        return new BarrelStorageContainer(id, playerInventoryIn, this.itemStackHandler, this.barrelType.getRegistryName().toString());
    }

    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? this.itemHandlerLazyOptional.cast() : super.getCapability(cap, side);
    }

    public void onApply(ItemStack itemStack, PlayerEntity playerEntity) {
        this.barrelType = itemStack.getItem();
        this.itemStackHandler.setSize(MetalBarrelsCompat.getSize(this.barrelType.getRegistryName().toString()));
    }
}
