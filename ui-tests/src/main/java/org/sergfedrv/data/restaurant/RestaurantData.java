package org.sergfedrv.data.restaurant;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record RestaurantData(
        String primarySlug,
        @SerializedName("cuisineTypes")
        List<String> cuisineTypeCodes
) { }
