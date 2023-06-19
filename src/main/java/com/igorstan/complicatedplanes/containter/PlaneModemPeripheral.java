package com.igorstan.complicatedplanes.containter;

import dan200.computercraft.api.peripheral.IPeripheral;
import dan200.computercraft.shared.peripheral.modem.ModemState;
import dan200.computercraft.shared.peripheral.modem.wireless.WirelessModemPeripheral;
import dan200.computercraft.shared.pocket.peripherals.PocketModemPeripheral;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class PlaneModemPeripheral extends WirelessModemPeripheral {
    private World world = null;
    private Vector3d position;

    public PlaneModemPeripheral(boolean advanced) {
        super(new ModemState(), advanced);
        this.position = Vector3d.ZERO;
    }

    void setLocation(World world, Vector3d position) {
        this.position = position;
        this.world = world;
    }

    @Nonnull
    public World getWorld() {
        return this.world;
    }

    @Nonnull
    public Vector3d getPosition() {
        return this.position;
    }

    public boolean equals(IPeripheral other) {
        return other instanceof PlaneModemPeripheral;
    }
}
