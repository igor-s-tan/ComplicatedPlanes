package com.igorstan.complicatedplanes.network;

import com.igorstan.complicatedplanes.registry.Upgrades;
import com.igorstan.complicatedplanes.upgrades.BarrelUpgrade;
import com.sun.jna.platform.win32.WinUser;
import dan200.computercraft.api.turtle.event.TurtleBlockEvent;
import net.java.games.input.Component;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.gui.widget.list.KeyBindingList;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;
import xyz.przemyk.simpleplanes.upgrades.Upgrade;

import javax.swing.plaf.basic.BasicTreeUI;
import javax.swing.text.JTextComponent;
import java.awt.event.KeyEvent;
import java.util.function.Supplier;

public class MovementPacket {
    public float xxaa;

    public MovementPacket(float xxaa) {
        this.xxaa = xxaa;
    }

    public static void encode(MovementPacket movementPacket, PacketBuffer buffer) {
        buffer.writeFloat(movementPacket.xxaa);
    }

    public static MovementPacket decode(PacketBuffer buffer) {
        return new MovementPacket(buffer.readFloat());
    }

    public static void handle(MovementPacket movementPacket, Supplier<NetworkEvent.Context> ctxSup) {
        NetworkEvent.Context ctx = (NetworkEvent.Context)ctxSup.get();
        ctx.enqueueWork(() -> DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> handlePacket(movementPacket, ctxSup)));
        ctx.setPacketHandled(true);
    }
    public static void handlePacket(MovementPacket movementPacket, Supplier<NetworkEvent.Context> contextSupplier) {
        Minecraft mc = Minecraft.getInstance();
        if(mc.player != null) {
            mc.player.xxa = movementPacket.xxaa;
        }
    }
}
