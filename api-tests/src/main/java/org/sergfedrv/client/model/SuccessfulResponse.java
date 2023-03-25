package org.sergfedrv.client.model;

public record SuccessfulResponse(
        String action,
        boolean success,
        String message,
        String data
) {
}
