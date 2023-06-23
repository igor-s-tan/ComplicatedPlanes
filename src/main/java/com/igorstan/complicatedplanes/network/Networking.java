package com.igorstan.complicatedplanes.network;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;


import java.util.Optional;

public class Networking {
    private static final String PROTOCOL_VERSION = "4";
    public static SimpleChannel INSTANCE;

    public static void init() {
        INSTANCE = NetworkRegistry.newSimpleChannel(new ResourceLocation("complicatedplanes", "main"), () -> {
            return "4";
        }, "4"::equals, "4"::equals);
        int id = -1;
        ++id;
        INSTANCE.registerMessage(id, OpenBarrelInventoryPacket.class, OpenBarrelInventoryPacket::toBytes, OpenBarrelInventoryPacket::new, OpenBarrelInventoryPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
        ++id;
        INSTANCE.registerMessage(id, MovementPacket.class, MovementPacket::encode, MovementPacket::decode, MovementPacket::handle);
        ++id;
        INSTANCE.registerMessage(id, OpenComputerPacket.class, OpenComputerPacket::toBytes, OpenComputerPacket::new, OpenComputerPacket::handle, Optional.of(NetworkDirection.PLAY_TO_SERVER));
    }

}
