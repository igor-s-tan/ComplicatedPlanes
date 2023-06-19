package com.igorstan.complicatedplanes.containter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class PlaneCommandResult {
    private static final PlaneCommandResult EMPTY_SUCCESS = new PlaneCommandResult(true, (String)null, (Object[])null);
    private static final PlaneCommandResult EMPTY_FAILURE = new PlaneCommandResult(false, (String)null, (Object[])null);
    private final boolean success;
    private final String errorMessage;
    private final Object[] results;

    @Nonnull
    public static PlaneCommandResult success() {
        return EMPTY_SUCCESS;
    }

    @Nonnull
    public static PlaneCommandResult success(@Nullable Object[] results) {
        return results != null && results.length != 0 ? new PlaneCommandResult(true, (String)null, results) : EMPTY_SUCCESS;
    }

    @Nonnull
    public static PlaneCommandResult failure() {
        return EMPTY_FAILURE;
    }

    @Nonnull
    public static PlaneCommandResult failure(@Nullable String errorMessage) {
        return errorMessage == null ? EMPTY_FAILURE : new PlaneCommandResult(false, errorMessage, (Object[])null);
    }

    private PlaneCommandResult(boolean success, String errorMessage, Object[] results) {
        this.success = success;
        this.errorMessage = errorMessage;
        this.results = results;
    }

    public boolean isSuccess() {
        return this.success;
    }

    @Nullable
    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Nullable
    public Object[] getResults() {
        return this.results;
    }
}
