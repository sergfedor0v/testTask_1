package org.sergfedrv.authentication;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.sergfedrv.config.Configuration;

import static io.restassured.RestAssured.given;

public class TokenProvider {

    private static TokenProvider instance;
    private final String fullScopeAppToken;
    private final String emptyScopeAppToken;

    private TokenProvider() {
        fullScopeAppToken = authenticateApp(Scope.FULL);
        emptyScopeAppToken = authenticateApp(Scope.EMPTY);
    }

    public static synchronized TokenProvider getInstance() {
        if (instance == null) {
            instance = new TokenProvider();
        }
        return instance;
    }

    public String getFullScopeAppToken() {
        return fullScopeAppToken;
    }

    public String getEmptyScopeAppToken() {
        return emptyScopeAppToken;
    }

    public String getInvalidToken() {
        return "Bearer invalidToken";
    }

    public String getMalformedToken() {
        return "BEAR WITH ME";
    }

    private String authenticateApp(Scope scope) {
        AppCredentials appCredentials = Configuration.getAppCredentials(scope.getValue());
        String tokenString = given().spec(getRequestLoggerSpec()).baseUri(Configuration.getBaseUrl())
                .basePath("token")
                .formParam("grant_type", "client_credentials")
                .formParam("client_id", appCredentials.clientId())
                .formParam("client_secret", appCredentials.clientSecret())
                .post()
                .then()
                .spec(getResponseLoggerSpecification())
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("access_token");
        return String.format(("Bearer %s"), tokenString);
    }

    private RequestSpecification getRequestLoggerSpec() {
        return Configuration.debugModeEnabled() ? given().log().all() : given().log().method().log().uri();
    }

    private ResponseSpecification getResponseLoggerSpecification() {
        return Configuration.debugModeEnabled() ?
                new ResponseSpecBuilder().log(LogDetail.ALL).build() :
                new ResponseSpecBuilder().log(LogDetail.STATUS).build();
    }
}
