package com.igorstan.complicatedplanes.containter;

import dan200.computercraft.api.turtle.ITurtleAccess;
import dan200.computercraft.api.turtle.TurtleCommandResult;

import javax.annotation.Nonnull;

public interface IPlaneCommand {
    @Nonnull
    PlaneCommandResult execute(@Nonnull IPlaneAccess var1);
}
