package org.sergfedrv.utils;

import org.sergfedrv.Configuration;
import org.sergfedrv.data.cuisine.CountryCode;
import org.sergfedrv.data.cuisine.CuisineTypeDetailsDictionary;
import org.sergfedrv.data.restaurant.RestaurantData;

import java.util.ArrayList;
import java.util.List;

/**
 * Loads data about restaurants from backend and provide access to this data to tests
 */
public class RestaurantDataProvider {

    private static RestaurantDataProvider instance;
    private final List<RestaurantData> restaurantDataList;
    private final CuisineTypeDetailsDictionary cuisineTypeDetailsDictionary;
    private RestaurantDataProvider(ApiHelper helper) {
        cuisineTypeDetailsDictionary = GsonHelper.fromJsonString(
                FileHelper.getResourceFileAsString(CuisineTypeDetailsDictionary.class, "cuisine_type_mapping.json"),
                CuisineTypeDetailsDictionary.class
        );
        restaurantDataList = helper.getRestaurantInfoForLocation(Configuration.getSearchPostalCode())
                .getRestaurantDataList();
    }

    public static RestaurantDataProvider getInstance(ApiHelper helper) {
        if(instance == null) {
            instance = new RestaurantDataProvider(helper);
        }
        return instance;
    }

    public List<String> getRestaurantCuisineTypeNames(String restaurantPrimarySlug, CountryCode countryCode) {
        List<String> cuisineTypes = new ArrayList<>();
        RestaurantData restaurant = getRestaurantByPrimaryNameSlug(restaurantPrimarySlug);
        for (String cuisineCode : restaurant.cuisineTypeCodes()) {
            cuisineTypes.add(cuisineTypeDetailsDictionary.getCuisineNameInHumanFormat(cuisineCode, countryCode));
        }
        return cuisineTypes;
    }

    public float getRestaurantMinimalOrderAmount(String restaurantPrimarySlug) {
        return getRestaurantByPrimaryNameSlug(restaurantPrimarySlug).getMinimalOrderAmount();
    }

    public float getRestaurantDeliveryFee(String restaurantPrimarySlug) {
        return getRestaurantByPrimaryNameSlug(restaurantPrimarySlug).getDeliveryFee();
    }

    private RestaurantData getRestaurantByPrimaryNameSlug(String nameSlug) {
        return restaurantDataList.stream().filter(r -> r.primarySlug().equals(nameSlug)).findFirst()
                .orElseThrow();
    }
 }
