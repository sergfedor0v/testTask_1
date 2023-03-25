package org.sergfedrv.barnlock;

import io.qameta.allure.Description;
import org.junit.jupiter.api.Test;
import org.sergfedrv.BaseTest;
import org.sergfedrv.model.SuccessfulResponse;
import org.sergfedrv.specifications.ApiAction;

import static org.assertj.core.api.Assertions.assertThat;

public class BarnLockTest extends BaseTest {

    @Test
    @Description("""
            Check that it's been a while since the last time we opened a barn, we got "You just unlocked your barn!
             Watch out for strangers!" as a response message.
            """)
    public void testBarnUnlockPositive() {
        SuccessfulResponse expectedResponse = new SuccessfulResponse(ApiAction.BARN_UNLOCK.value, true,
                "You just unlocked your barn! Watch out for strangers!", null);
        SuccessfulResponse actualResponse = coopClient.sendActionRequestUntilResponseMessageIs(ApiAction.BARN_UNLOCK,
                expectedResponse.message());
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }

    @Test
    @Description("""
            Check that when we open already opened barn, we got "The barn is already wide open! Let's throw a party!" as
            a response message.
            """)
    public void testBarnUnlockAlreadyUnlocked() {
        SuccessfulResponse expectedResponse = new SuccessfulResponse(ApiAction.BARN_UNLOCK.value, true,
                "The barn is already wide open! Let's throw a party!", null);
        SuccessfulResponse actualResponse = coopClient.sendActionRequestUntilResponseMessageIs(ApiAction.BARN_UNLOCK,
                expectedResponse.message());
        assertThat(actualResponse).isEqualTo(expectedResponse);
    }
}
