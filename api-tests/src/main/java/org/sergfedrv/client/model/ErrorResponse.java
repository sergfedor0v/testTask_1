package org.sergfedrv.client.model;

import com.google.gson.annotations.SerializedName;

public record ErrorResponse(
        String error,
        @SerializedName("error_message")
        String errorMessage
) {
}
