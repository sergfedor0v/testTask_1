package org.sergfedrv.specifications;

import io.restassured.specification.RequestSpecification;
import org.sergfedrv.config.Configuration;

import static io.restassured.RestAssured.given;

public class SpecificationProvider {

    public SpecificationProvider() {

    }

    public RequestSpecification getRequestSpec(ApiAction action, String userId, String accessToken) {
        return given()
                .spec(getRequestLogSpec())
                .baseUri(Configuration.getBaseUrl())
                .basePath(String.format("api/%s/%s", userId, action.value))
                .header("Authorization", "Bearer " + accessToken);
    }

    public RequestSpecification getRequestSpec(ApiAction action, String accessToken) {
        return getRequestSpec(action, Configuration.getDefaultUserId(), accessToken);
    }

    private RequestSpecification getRequestLogSpec() {
        return Configuration.debugModeEnabled() ? given().log().all() : given().log().method().log().uri().log().body();
    }
}
