package org.sergfedrv.model;

import com.google.gson.annotations.SerializedName;

public record UnauthorizedErrorResponse(
        String error,
        @SerializedName("error_description")
        String errorDescription,
        @SerializedName("error_message")
        String errorMessage
) {
}
