package org.sergfedrv.utils;

import io.restassured.response.Response;
import org.sergfedrv.data.restaurant.Restaurants;

import static io.restassured.RestAssured.given;

public class ApiHelper {

    String baseUrl;

    public ApiHelper(String baseApiUrl) {
        this.baseUrl = baseApiUrl;
    }

    public Restaurants getRestaurantInfoForLocation(String locationPostalCode) {
        Response response = given().baseUri(baseUrl)
                .log().all()
                .basePath("restaurants")
                .queryParam("postalCode", locationPostalCode)
                .header("X-Country-Code", "de")
                .get();
        if (response.getStatusCode() != 200) {
            throw new RuntimeException(String.format("Cannot get restaurant info for postal code '%s'." +
                    " Api request returned status code %d", locationPostalCode, response.getStatusCode()));
        }
        return GsonHelper.fromJsonString(response.asString(), Restaurants.class);
    }
}
