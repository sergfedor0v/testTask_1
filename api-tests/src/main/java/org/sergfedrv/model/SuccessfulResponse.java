package org.sergfedrv.model;

public record SuccessfulResponse(
        String action,
        boolean success,
        String message,
        Integer data
) {
}
