package org.sergfedrv.barnlock;

import io.qameta.allure.Step;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.Test;
import org.sergfedrv.BaseTest;
import org.sergfedrv.client.model.SuccessfulResponse;
import org.sergfedrv.client.specifications.ApiAction;
import org.sergfedrv.client.specifications.SpecificationProvider;
import org.sergfedrv.config.Configuration;

public class BarnLockTest extends BaseTest {

    @Test
    public void testBarnUnlockPositive() {
        SuccessfulResponse as = executeRequest(SpecificationProvider.getRequestSpec(ApiAction.BARN_UNLOCK,
                tokenProvider.getFullScopeAppToken()));
    }

    @Step("Execute '{action.value}' request with valid request parameters and check response code")
    public SuccessfulResponse executeRequest(RequestSpecification requestSpecification) {
        return requestSpecification.post().then().log().all().statusCode(200)
                .extract().as(SuccessfulResponse.class);
    }
}
