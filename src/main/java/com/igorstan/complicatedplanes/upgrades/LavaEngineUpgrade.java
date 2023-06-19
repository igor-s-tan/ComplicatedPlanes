package com.igorstan.complicatedplanes.upgrades;

import com.igorstan.complicatedplanes.event.ClientEventHandler;
import com.igorstan.complicatedplanes.registry.Items;
import com.igorstan.complicatedplanes.containter.LavaEngineContainer;
import com.igorstan.complicatedplanes.registry.Upgrades;
import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.Direction;
import net.minecraft.util.HandSide;
import net.minecraft.util.IIntArray;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.network.NetworkHooks;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import xyz.przemyk.simpleplanes.client.ClientUtil;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;
import xyz.przemyk.simpleplanes.setup.SimplePlanesEntities;
import xyz.przemyk.simpleplanes.upgrades.UpgradeType;
import xyz.przemyk.simpleplanes.upgrades.engines.EngineUpgrade;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;



public class LavaEngineUpgrade extends EngineUpgrade implements INamedContainerProvider {
    public final ItemStackHandler itemStackHandler = new ItemStackHandler();
    public final LazyOptional<ItemStackHandler> itemHandlerLazyOptional = LazyOptional.of(() -> {
        return this.itemStackHandler;
    });
    public int burnTime;
    public int burnTimeTotal;

    public LavaEngineUpgrade(PlaneEntity planeEntity) {
        super((UpgradeType) Upgrades.LAVA_ENGINE.get(), planeEntity);
    }

    public void tick() {
        if (this.burnTime > 0) {
            this.burnTime -= this.planeEntity.getFuelCost();
            this.updateClient();
        } else {
            ItemStack itemStack = this.itemStackHandler.getStackInSlot(0);
            int itemBurnTime = ForgeHooks.getBurnTime(itemStack);
            if (itemBurnTime > 0) {
                this.burnTimeTotal = itemBurnTime;
                this.burnTime = itemBurnTime;
                if (itemStack.hasContainerItem()) {
                    this.itemStackHandler.setStackInSlot(0, itemStack.getContainerItem());
                } else {
                    this.itemStackHandler.extractItem(0, 1, false);
                }

                this.updateClient();
            }
        }

    }

    public boolean isPowered() {
        return this.burnTime > 0;
    }

    public void render(MatrixStack matrixStack, IRenderTypeBuffer buffer, int packedLight, float partialTicks) {
        matrixStack.pushPose();
        EntityType<?> entityType = this.planeEntity.getType();
        if (entityType == SimplePlanesEntities.HELICOPTER.get()) {
            matrixStack.translate(0.0, -0.8, 0.65);
        } else if (entityType == SimplePlanesEntities.LARGE_PLANE.get()) {
            matrixStack.translate(0.0, 0.0, 1.1);
        }

        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        matrixStack.translate(-0.4, -1.0, 0.3);
        matrixStack.scale(0.82F, 0.82F, 0.82F);
        ClientUtil.renderItemModelAsBlock(matrixStack, Minecraft.getInstance(), buffer, packedLight, (Item) Items.LAVA_ENGINE.get());
        matrixStack.popPose();
    }

    protected void invalidateCaps() {
        super.invalidateCaps();
        this.itemHandlerLazyOptional.invalidate();
    }

    public CompoundNBT serializeNBT() {
        CompoundNBT compound = new CompoundNBT();
        compound.put("item", this.itemStackHandler.serializeNBT());
        compound.putInt("burnTime", this.burnTime);
        compound.putInt("burnTimeTotal", this.burnTimeTotal);
        return compound;
    }

    public void deserializeNBT(CompoundNBT compound) {
        this.itemStackHandler.deserializeNBT(compound.getCompound("item"));
        this.burnTime = compound.getInt("burnTime");
        this.burnTimeTotal = compound.getInt("burnTimeTotal");
    }

    public void writePacket(PacketBuffer buffer) {
        buffer.writeItem(this.itemStackHandler.getStackInSlot(0));
        buffer.writeVarInt(this.burnTime);
        buffer.writeVarInt(this.burnTimeTotal);
    }

    public void readPacket(PacketBuffer buffer) {
        this.itemStackHandler.setStackInSlot(0, buffer.readItem());
        this.burnTime = buffer.readVarInt();
        this.burnTimeTotal = buffer.readVarInt();
    }

    public boolean canOpenGui() {
        return true;
    }

    public void openGui(ServerPlayerEntity playerEntity) {
        NetworkHooks.openGui(playerEntity, this);
    }

    public ITextComponent getDisplayName() {
        return new TranslationTextComponent("complicatedplanes.lava_engine_container");
    }

    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return new LavaEngineContainer(id, playerInventory, this.itemStackHandler, new IIntArray() {
            public int get(int index) {
                return index == 0 ? LavaEngineUpgrade.this.burnTime : LavaEngineUpgrade.this.burnTimeTotal;
            }

            public void set(int index, int value) {
            }

            public int getCount() {
                return 2;
            }
        });
    }

    @Nonnull
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
        return cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY ? this.itemHandlerLazyOptional.cast() : super.getCapability(cap, side);
    }

    public void dropItems() {
        this.planeEntity.spawnAtLocation((IItemProvider)Items.LAVA_ENGINE.get());
        this.planeEntity.spawnAtLocation(this.itemStackHandler.getStackInSlot(0));
    }

    public void renderPowerHUD(MatrixStack matrixStack, HandSide side, int scaledWidth, int scaledHeight, float partialTicks) {
        int i = scaledWidth / 2;
        Minecraft mc = Minecraft.getInstance();
        if (side == HandSide.LEFT) {
            ClientEventHandler.blit(matrixStack, -90, i - 91 - 29, scaledHeight - 40, 0, 44, 22, 40);
        } else {
            ClientEventHandler.blit(matrixStack, -90, i + 91, scaledHeight - 40, 0, 44, 22, 40);
        }

        int i2;
        if (this.burnTime > 0) {
            int burnTimeTotal2 = this.burnTimeTotal == 0 ? 200 : this.burnTimeTotal;
            i2 = this.burnTime * 13 / burnTimeTotal2;
            if (side == HandSide.LEFT) {
                ClientEventHandler.blit(matrixStack, -90, i - 91 - 29 + 4, scaledHeight - 40 + 16 - i2, 22, 56 - i2, 14, i2 + 1);
            } else {
                ClientEventHandler.blit(matrixStack, -90, i + 91 + 4, scaledHeight - 40 + 16 - i2, 22, 56 - i2, 14, i2 + 1);
            }
        }

        ItemStack fuelStack = this.itemStackHandler.getStackInSlot(0);
        if (!fuelStack.isEmpty()) {
            i2 = scaledHeight - 16 - 3;
            if (side == HandSide.LEFT) {
                ClientEventHandler.renderHotbarItem(matrixStack, i - 91 - 26, i2, partialTicks, fuelStack, mc);
            } else {
                ClientEventHandler.renderHotbarItem(matrixStack, i + 91 + 3, i2, partialTicks, fuelStack, mc);
            }
        }
    }
}


