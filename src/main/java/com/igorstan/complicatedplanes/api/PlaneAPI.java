package com.igorstan.complicatedplanes.api;

import com.igorstan.complicatedplanes.api.command.PlaneMoveCommand;
import com.igorstan.complicatedplanes.containter.IPlaneAccess;
import com.igorstan.complicatedplanes.containter.IPlaneCommand;
import com.igorstan.complicatedplanes.containter.PlaneCommandResult;
import com.igorstan.complicatedplanes.network.MovementPacket;
import com.igorstan.complicatedplanes.network.Networking;
import com.igorstan.complicatedplanes.upgrades.ComputerUpgrade;
import dan200.computercraft.api.detail.DetailRegistries;
import dan200.computercraft.api.lua.*;
import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.ITurtleCommand;
import dan200.computercraft.api.turtle.TurtleCommandResult;
import dan200.computercraft.api.turtle.TurtleSide;
import dan200.computercraft.api.turtle.event.TurtleActionEvent;
import dan200.computercraft.api.turtle.event.TurtleInspectItemEvent;
import dan200.computercraft.core.apis.IAPIEnvironment;
import dan200.computercraft.core.metrics.Metrics;
import dan200.computercraft.shared.computer.core.ServerComputer;
import dan200.computercraft.shared.peripheral.generic.data.ItemData;
import dan200.computercraft.shared.pocket.core.PocketServerComputer;
import dan200.computercraft.shared.turtle.core.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.network.NetworkDirection;
import xyz.przemyk.simpleplanes.MathUtil;
import xyz.przemyk.simpleplanes.client.PlaneSound;
import xyz.przemyk.simpleplanes.entities.LargePlaneEntity;
import xyz.przemyk.simpleplanes.entities.PlaneEntity;
import xyz.przemyk.simpleplanes.network.PlaneNetworking;
import xyz.przemyk.simpleplanes.network.RotationPacket;
import xyz.przemyk.simpleplanes.setup.SimplePlanesConfig;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class PlaneAPI extends PlaneEntity implements ILuaAPI {

    private final ServerComputer computer;
    private final PlaneEntity plane;


    public PlaneAPI(EntityType<? extends PlaneEntity> entityTypeIn, ServerComputer computer, PlaneEntity plane) {
        super(entityTypeIn, plane.level);
        this.computer = computer;
        this.plane = plane;

    }


    @Override
    public String[] getNames() {
        return new String[]{"plane"};
    }

    @LuaFunction
    public final Object getXc() {
        return this.plane.getX();
    }
    @LuaFunction
    public final Object getYc() {
        return this.plane.getY();
    }
    @LuaFunction
    public final Object getZc() {
        return this.plane.getZ();
    }

    @LuaFunction
    public final Object moveForward() {
        if(plane.getPlayer() != null && !plane.isOnGround()) {
            ServerPlayerEntity player = (ServerPlayerEntity) plane.getPlayer();
            Networking.INSTANCE.sendTo(new MovementPacket(1), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            return PlaneCommandResult.success();
        }
        else {
            return PlaneCommandResult.failure();
        }
    }

    @LuaFunction
    public final Object moveLeft() {
        if((plane.getPlayer() != null) || (plane.getPlayer() instanceof FakePlayer)) {
            ServerPlayerEntity player = (ServerPlayerEntity) plane.getPlayer();
            System.out.println(player);
            ComputerUpgrade upgrade = (ComputerUpgrade) this.plane.upgrades.get(new ResourceLocation("complicatedplanes:computer"));
            upgrade.xxaa = 1f;
            //Networking.INSTANCE.sendTo(new MovementPacket(2), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            return PlaneCommandResult.success();
        }
        else {
            return PlaneCommandResult.failure();
        }
    }

    @LuaFunction
    public final Object moveRight() {
        if (plane.getPlayer() != null && !plane.isOnGround()) {
            ServerPlayerEntity player = (ServerPlayerEntity) plane.getPlayer();
            Networking.INSTANCE.sendTo(new MovementPacket(3), player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
            return PlaneCommandResult.success();
        } else {
            return PlaneCommandResult.failure();
        }
    }
}

