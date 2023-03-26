package org.sergfedrv.model;

public record SuccessResponse(
        String action,
        boolean success,
        String message,
        Integer data
) {
}
