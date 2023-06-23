package com.igorstan.complicatedplanes.event;


import com.igorstan.complicatedplanes.containter.ComputerContainerProvider;
import com.igorstan.complicatedplanes.network.Networking;
import com.igorstan.complicatedplanes.network.OpenBarrelInventoryPacket;
import com.igorstan.complicatedplanes.network.OpenComputerPacket;
import com.igorstan.complicatedplanes.registry.Upgrades;
import com.igorstan.complicatedplanes.upgrades.ComputerUpgrade;
import com.mojang.blaze3d.matrix.MatrixStack;
import dan200.computercraft.core.computer.Computer;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.network.container.ComputerContainerData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.settings.PointOfView;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;
import xyz.przemyk.simpleplanes.network.OpenEngineInventoryPacket;
import xyz.przemyk.simpleplanes.network.PlaneNetworking;


@Mod.EventBusSubscriber({Dist.CLIENT})
public class ClientEventHandler {
    @OnlyIn(Dist.CLIENT)
    public static KeyBinding openComputerKey;
    @SubscribeEvent(
            priority = EventPriority.HIGH
    )
    public static void planeInventory(GuiOpenEvent event) {
        ClientPlayerEntity player = Minecraft.getInstance().player;
        if (event.getGui() instanceof InventoryScreen && player.getVehicle() instanceof PlaneEntity) {
            PlaneEntity plane = (PlaneEntity)player.getVehicle();
            if (plane.upgrades.containsKey(Upgrades.BARREL.getId())) {
                event.setCanceled(true);
                Networking.INSTANCE.sendToServer(new OpenBarrelInventoryPacket());
            }
        }

    }
    public static void renderHotbarItem(MatrixStack matrixStack, int x, int y, float partialTicks, ItemStack stack, Minecraft mc) {
        ItemRenderer itemRenderer = mc.getItemRenderer();
        if (!stack.isEmpty()) {
            float f = (float)stack.getUseDuration() - partialTicks;
            if (f > 0.0F) {
                matrixStack.pushPose();
                float f1 = 1.0F + f / 5.0F;
                matrixStack.translate((double)((float)(x + 8)), (double)((float)(y + 12)), 0.0);
                matrixStack.scale(1.0F / f1, (f1 + 1.0F) / 2.0F, 1.0F);
                matrixStack.translate((double)((float)(-(x + 8))), (double)((float)(-(y + 12))), 0.0);
            }

            itemRenderer.renderAndDecorateItem(mc.player, stack, x, y);
            if (f > 0.0F) {
                matrixStack.popPose();
            }

            itemRenderer.renderGuiItemDecorations(mc.font, stack, x, y);
        }

    }

    public static void blit(MatrixStack matrixStack, int blitOffset, int x, int y, int uOffset, int vOffset, int uWidth, int vHeight) {
        AbstractGui.blit(matrixStack, x, y, blitOffset, (float)uOffset, (float)vOffset, uWidth, vHeight, 256, 256);
    }

    public static void clientSetup() {
        openComputerKey = new KeyBinding("key.plane_computer_open.desc", 67, "key.complicatedplanes.category");
        ClientRegistry.registerKeyBinding(openComputerKey);
    }

    @SubscribeEvent (
         priority =  EventPriority.LOWEST
    )
    public static void openComputer(TickEvent.PlayerTickEvent event) {
        PlayerEntity player = event.player;
        if(event.phase == TickEvent.Phase.END && player instanceof ClientPlayerEntity) {
            if (player.getVehicle() instanceof PlaneEntity) {
                PlaneEntity planeEntity = (PlaneEntity) player.getVehicle();
                Minecraft mc = Minecraft.getInstance();
                if (planeEntity.upgrades.containsKey(new ResourceLocation("complicatedplanes:computer")) && mc.screen == null && mc.overlay == null && openComputerKey.consumeClick()) {
                    Networking.INSTANCE.sendToServer(new OpenComputerPacket());
                }
            }
        }
    }
}
