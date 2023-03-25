package org.sergfedrv;

import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import org.sergfedrv.client.model.SuccessfulResponse;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

public class RequestValidator {

    @Step("Execute '{action.value}' request with valid request parameters and check response code")
    public static SuccessfulResponse validateRequestSuccess(RequestSpecification requestSpecification) {
        return requestSpecification.post().then().log().body().statusCode(200)
                .extract().as(SuccessfulResponse.class);
    }

    @Step("Execute '{action.value}' request with valid request parameters and check response code")
    public static SuccessfulResponse waitForResponseMessageText(
            RequestSpecification requestSpecification,
            String expectedMessageText,
            Duration duration
    ) {
        SuccessfulResponse awaitedResponse = null;
        Instant timeout = Instant.now().plus(duration);
        boolean textMatch = false;
        while (Instant.now().compareTo(timeout) <= 0 && !textMatch) {
            SuccessfulResponse response = requestSpecification.post().then().log().body().statusCode(200)
                    .extract().as(SuccessfulResponse.class);
            if (response.message().equals(expectedMessageText)) {
                textMatch = true;
                awaitedResponse = response;
            }
        }
        return Optional.ofNullable(awaitedResponse)
                .orElseThrow(() -> new RuntimeException(String.format("Timeout occurred while waiting for response " +
                        "message is equal to '%s'", expectedMessageText)));
    }
}
