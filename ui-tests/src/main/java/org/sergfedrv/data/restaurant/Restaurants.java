package org.sergfedrv.data.restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record Restaurants(Map<String, RestaurantData> restaurants) {
    public List<RestaurantData> getRestaurantDataList() {
        return new ArrayList<>(restaurants.values());
    }
}
