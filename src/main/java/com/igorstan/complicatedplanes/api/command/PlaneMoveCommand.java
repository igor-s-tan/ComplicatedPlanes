package com.igorstan.complicatedplanes.api.command;

import com.igorstan.complicatedplanes.containter.IPlaneAccess;
import com.igorstan.complicatedplanes.containter.IPlaneCommand;
import com.igorstan.complicatedplanes.containter.PlaneCommandResult;

import javax.annotation.Nonnull;

public class PlaneMoveCommand implements IPlaneCommand {
    @Nonnull
    @Override
    public PlaneCommandResult execute(@Nonnull IPlaneAccess plane) {
        plane.getPosition();
        return null;
    }
}
