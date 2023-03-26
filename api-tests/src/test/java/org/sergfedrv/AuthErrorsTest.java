package org.sergfedrv;

import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import io.qameta.allure.Story;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.sergfedrv.config.Configuration;
import org.sergfedrv.model.UnauthorizedErrorResponse;
import org.sergfedrv.specifications.ApiAction;

import java.util.Optional;
import java.util.stream.Stream;

@Epic("Authentication errors tests")
public class AuthErrorsTest extends BaseTest {

    private static Stream<Arguments> apiActionsDataProvider() {
        return Stream.of(
                Arguments.of(ApiAction.BARN_UNLOCK),
                Arguments.of(ApiAction.EGGS_COLLECT),
                Arguments.of(ApiAction.EGGS_COUNT),
                Arguments.of(ApiAction.CHICKENS_FEED),
                Arguments.of(ApiAction.TOILETSEAT_DOWN)
        );
    }

    @ParameterizedTest
    @MethodSource("apiActionsDataProvider")
    @DisplayName("Send action request with token that does not have rights to perform this action")
    @Story("Send action request with token that does not have rights to perform this action")
    public void insufficientScopeResponseTest(ApiAction action) {
        UnauthorizedErrorResponse response = coopClient.sendUnauthorizedActionRequest(action,
                Configuration.getDefaultUserId(), tokenProvider.getEmptyScopeAppToken());
        validateErrorResponse(response, "insufficient_scope",
                "The request requires higher privileges than provided by the access token");
    }

    @ParameterizedTest
    @MethodSource("apiActionsDataProvider")
    @DisplayName("Send action request with invalid token")
    @Story("Send action request with invalid token")
    public void invalidTokenResponseTest(ApiAction action) {
        UnauthorizedErrorResponse response = coopClient.sendUnauthorizedActionRequest(action,
                Configuration.getDefaultUserId(), tokenProvider.getInvalidToken());
        validateErrorResponse(response, "invalid_token",
                "The access token provided is invalid");
    }

    @ParameterizedTest
    @MethodSource("apiActionsDataProvider")
    @DisplayName("Send action request on behalf of the user who did not give permissions")
    @Story("Send action request on behalf of the user who did not give permissions")
    public void noAccessOnBehalfOfThisUserTest(ApiAction action) {
        UnauthorizedErrorResponse response = coopClient.sendUnauthorizedActionRequest(action,
                Configuration.getAccessDeniedUser(), tokenProvider.getFullScopeAppToken());
        validateErrorResponse(response, "access_denied",
                "You do not have access to take this action on behalf of this user");
    }

    @ParameterizedTest
    @MethodSource("apiActionsDataProvider")
    @DisplayName("Send action request with malformed token value")
    @Story("Send action request with malformed token value")
    public void malformedAuthHeaderTest(ApiAction action) {
        UnauthorizedErrorResponse response = coopClient.sendUnauthorizedActionRequest(action,
                Configuration.getAccessDeniedUser(), tokenProvider.getMalformedToken());
        validateErrorResponse(response, "invalid_request",
                "Malformed auth header");
    }

    @ParameterizedTest
    @MethodSource("apiActionsDataProvider")
    @DisplayName("Send action without auth header")
    @Story("Send action without auth header")
    public void noAuthHeaderTest(ApiAction action) {
        UnauthorizedErrorResponse response = coopClient.sendActionRequestWithoutToken(action);
        validateErrorResponse(response, "access_denied",
                "an access token is required");
    }

    @Step("Validate error response")
    private void validateErrorResponse(
            UnauthorizedErrorResponse response,
            String expectedError,
            String expectedErrorMessage
    ) {
        //Most error responses have error description in field "error_description", but
        //when we send request with valid token on behalf og other user, error description is in
        //"error_message" field.
        String errorDescriptionOrMessage = Optional.ofNullable(response.errorDescription())
                .orElse(response.errorMessage());
        try (AutoCloseableSoftAssertions softAssertions = new AutoCloseableSoftAssertions()) {
            softAssertions.assertThat(response.error()).as("Check response error").isEqualTo(expectedError);
            softAssertions.assertThat(errorDescriptionOrMessage).as("Check response error message")
                    .isEqualTo(expectedErrorMessage);
        }
    }
}
