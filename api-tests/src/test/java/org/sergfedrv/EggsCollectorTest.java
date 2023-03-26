package org.sergfedrv;

import io.qameta.allure.Description;
import io.qameta.allure.Epic;
import io.qameta.allure.Step;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.*;
import org.sergfedrv.model.SuccessResponse;
import org.sergfedrv.specifications.ApiAction;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Epic("COOP egg collector tests")
public class EggsCollectorTest extends BaseTest {
    @Test
    @Order(1)
    @DisplayName("Check that we can collect eggs from our chickens.")
    @Description("""
            When: client sends /eggs-count request
            Then: client gets total collected eggs count = 'X'
            When: client sends /eggs-collect request
            And: gets amount of collected eggs Y
            And: client sends /eggs-count request
            Then: client gets total collected eggs count = 'X+Y'
            """)
    public void eggsCollectorTest() {
        SuccessResponse eggsCountBefore = coopClient.sendActionRequest(ApiAction.EGGS_COUNT);
        validateEggsResponse(eggsCountBefore, ApiAction.EGGS_COUNT);
        SuccessResponse collectedEggsResponse = coopClient.sendActionRequestUntilResponseMessageContains(ApiAction.EGGS_COLLECT,
                "eggs have been collected!");
        SuccessResponse eggsCountAfter = coopClient.sendActionRequest(ApiAction.EGGS_COUNT);
        assertThat(eggsCountAfter.data()).as("Check, that total amount of collected eggs has increased")
                .isEqualTo(eggsCountBefore.data() + collectedEggsResponse.data());
        validateEggsResponse(collectedEggsResponse, ApiAction.EGGS_COLLECT);
        validateEggsResponse(eggsCountAfter, ApiAction.EGGS_COUNT);
    }

    @Test
    @Order(2)
    @DisplayName("Check that we can't collect eggs too often and should give them a break")
    @Description("""
            Given: We have collected eggs from our chickens a moment ago
            When: We send new eggs-collect request
            Then: We get response message "Hey, give the ladies a break"
            """)
    public void giveYourChickensABreakTest() {
        SuccessResponse expectedResponse = new SuccessResponse(ApiAction.EGGS_COLLECT.value, true,
                "Hey, give the ladies a break. Makin' eggs ain't easy!", null);
        SuccessResponse actualResponse = coopClient.sendActionRequestUntilResponseMessageContains(ApiAction.EGGS_COLLECT,
                "Hey, give the ladies a break. Makin' eggs ain't easy!");
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Step("Validate response of /{action.value} request")
    public void validateEggsResponse(SuccessResponse response, ApiAction action) {
        try (AutoCloseableSoftAssertions softAssertions = new AutoCloseableSoftAssertions()) {
            softAssertions.assertThat(response.action()).as("Check response action")
                    .isEqualTo(action.value);
            softAssertions.assertThat(response.success()).as("Check response success state").isTrue();
            softAssertions.assertThat(response.data())
                    .as("Check response data contain non-zero amount of eggs")
                    .isNotNegative();
            if (action == ApiAction.EGGS_COLLECT) {
                softAssertions.assertThat(response.message()).as("Check response message")
                        .isEqualTo(String.format("Hey look at that, %d eggs have been collected!", response.data()));
            } else {
                softAssertions.assertThat(response.message()).as("Check response message")
                        .isEqualTo(String.format("You have collected a total of %d eggs today", response.data()));
            }
        }
    }
}
