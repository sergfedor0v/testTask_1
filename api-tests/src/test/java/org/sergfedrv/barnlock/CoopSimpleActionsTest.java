package org.sergfedrv.barnlock;

import io.qameta.allure.Allure;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.sergfedrv.BaseTest;
import org.sergfedrv.model.SuccessfulResponse;
import org.sergfedrv.specifications.ApiAction;

import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

public class CoopSimpleActionsTest extends BaseTest {

    private static Stream<Arguments> stateChangedDataProvider() {
        return Stream.of(
                Arguments.of(
                        ApiAction.BARN_UNLOCK,
                        new SuccessfulResponse(ApiAction.BARN_UNLOCK.value, true,
                                "You just unlocked your barn! Watch out for strangers!", null)
                ),
                Arguments.of(
                        ApiAction.BARN_UNLOCK,
                        new SuccessfulResponse(ApiAction.BARN_UNLOCK.value, true,
                                "The barn is already wide open! Let's throw a party!", null)
                ),
                Arguments.of(
                        ApiAction.CHICKENS_FEED,
                        new SuccessfulResponse(ApiAction.CHICKENS_FEED.value, true,
                                "Your chickens are now full and happy", null)
                ),
                Arguments.of(
                        ApiAction.CHICKENS_FEED,
                        new SuccessfulResponse(ApiAction.CHICKENS_FEED.value, true,
                                "You just fed them! Do you want them to explode??", null)
                ),
                Arguments.of(
                        ApiAction.TOILETSEAT_DOWN,
                        new SuccessfulResponse(ApiAction.TOILETSEAT_DOWN.value, true,
                                "You just put the toilet seat down. You're a wonderful roommate!", null)
                ),
                Arguments.of(
                        ApiAction.TOILETSEAT_DOWN,
                        new SuccessfulResponse(ApiAction.TOILETSEAT_DOWN.value, true,
                                "Yea, the toilet seat is already down... you slob!", null)
                )
        );
    }

    @ParameterizedTest
    @MethodSource("stateChangedDataProvider")
    @DisplayName("Check simple COOP app actions")
    public void testCoopActionStateChanged(ApiAction action, SuccessfulResponse expectedResponse) {
        Allure.description(String.format("Check, that we can successfully execute request POST /api/{userId}/%s" +
                " request and get message \"%s\" in the response body.", action.value, expectedResponse.message()));
        SuccessfulResponse actualResponse = coopClient.sendActionRequestUntilResponseMessageIs(action,
                expectedResponse.message());
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
