package org.sergfedrv.client;

import io.qameta.allure.Step;
import io.restassured.mapper.ObjectMapperType;
import org.sergfedrv.authentication.TokenProvider;
import org.sergfedrv.model.SuccessResponse;
import org.sergfedrv.model.UnauthorizedErrorResponse;
import org.sergfedrv.specifications.ApiAction;
import org.sergfedrv.specifications.SpecificationProvider;

import java.time.Duration;
import java.time.Instant;

public class CoopClient {
    private final SpecificationProvider specificationProvider;
    private final TokenProvider tokenProvider;
    private final Duration defaultWaitDuration;

    public CoopClient(SpecificationProvider provider, TokenProvider tokenProvider) {
        this.specificationProvider = provider;
        this.tokenProvider = tokenProvider;
        defaultWaitDuration = Duration.ofSeconds(30);
    }

    @Step("Send POST /api/{userId}/{action.value} request and check that response code is 401 Unauthorized.")
    public UnauthorizedErrorResponse sendUnauthorizedActionRequest(
            ApiAction action,
            String userId,
            String accessToken
    ) {
        return specificationProvider.getRequestSpec(action, userId, accessToken).post().then().log().body()
                .statusCode(401).extract().as(UnauthorizedErrorResponse.class, ObjectMapperType.GSON);
    }

    @Step("Send POST /api/{userId}/{action.value} request and check that response code is 200 OK.")
    public UnauthorizedErrorResponse sendActionRequestWithoutToken(ApiAction action) {
        return specificationProvider.getRequestSpecWithoutAuthHeader(action).post().then().log().body()
                .statusCode(200).extract().as(UnauthorizedErrorResponse.class, ObjectMapperType.GSON);
    }

    @Step("Send POST /api/{userId}/{action.value} request with valid parameters and check response is 200 OK.")
    public SuccessResponse sendActionRequest(ApiAction action) {
        return specificationProvider.getRequestSpec(action, tokenProvider.getFullScopeAppToken())
                .post().then().log().body().statusCode(200)
                .extract().as(SuccessResponse.class, ObjectMapperType.GSON);
    }

    @Step("Send POST /api/{userId}/{action.value} requests until response body.message is {expectedMessageText}")
    public SuccessResponse sendActionRequestUntilResponseMessageContains(
            ApiAction action,
            String expectedMessageText
    ) {
        Instant timeout = Instant.now().plus(defaultWaitDuration);
        SuccessResponse response = sendActionRequest(action);
        while ((Instant.now().compareTo(timeout) <= 0) && !(response.message().contains(expectedMessageText))) {
            response = sendActionRequest(action);
        }
        if (!response.message().contains(expectedMessageText)) {
            throw new RuntimeException(String.format("Timeout occurred during waiting for 'body.message' of POST " +
                            "/api/{userId}/%s is equal to expected. Expected value is ['%s'], actual was ['%s'].",
                    action.value, expectedMessageText, response.message()
            ));
        }
        return response;
    }
}
