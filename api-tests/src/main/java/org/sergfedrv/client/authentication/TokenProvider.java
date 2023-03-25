package org.sergfedrv.client.authentication;

import org.sergfedrv.config.Configuration;

import static io.restassured.RestAssured.given;

public class TokenProvider {

    private static TokenProvider instance;
    private final String fullScopeAppToken;
    private final String emptyScopeAppToken;
    private final String invalidToken = "IAMINVALID";

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

    private String authenticateApp(Scope scope) {
        AppCredentials appCredentials = Configuration.getAppCredentials(scope.getValue());
        return given()
                .baseUri(Configuration.getBaseUrl())
                .basePath("token")
                .formParam("grant_type", "client_credentials")
                .formParam("client_id", appCredentials.clientId())
                .formParam("client_secret", appCredentials.clientSecret())
                .post()
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("access_token");
    }

    public String getFullScopeAppToken() {
        return fullScopeAppToken;
    }

    public String getEmptyScopeAppToken() {
        return emptyScopeAppToken;
    }

    public String getInvalidToken() {
        return invalidToken;
    }
}
