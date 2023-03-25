package org.sergfedrv.client.specifications;

import io.restassured.specification.RequestSpecification;
import org.sergfedrv.config.Configuration;

import static io.restassured.RestAssured.given;

public class SpecificationProvider {
    private static final RequestSpecification baseUrlSpec = given().baseUri(Configuration.getBaseUrl());
    private static final RequestSpecification fullRequestLogSpec = given().log().all();
    private static final RequestSpecification requestLogNoHeadersSpec = given().log().method().log().uri().log()
            .parameters().log().body().log().cookies();

    public static RequestSpecification getRequestSpec(ApiAction action, String userId, String accessToken) {
        return given()
                .spec(getRequestLogSpec())
                .baseUri(Configuration.getBaseUrl())
                .basePath(String.format("api/%s/%s", userId, action.getValue()))
//                .spec(new RequestSpecBuilder().log(LogDetail.ALL).build())
                .header("Authorization", "Bearer " + accessToken);
    }

    public static RequestSpecification getRequestSpec(ApiAction action, String accessToken) {
        return getRequestSpec(action, Configuration.getUserId(), accessToken);
    }

    private static RequestSpecification getRequestLogSpec() {
        return Configuration.debugModeEnabled() ? given().log().all() : given().log().method().log().uri().log().body();
    }
}
