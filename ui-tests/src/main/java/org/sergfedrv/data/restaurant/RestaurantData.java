package org.sergfedrv.data.restaurant;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public record RestaurantData(
        String primarySlug,
        @SerializedName("cuisineTypes")
        List<String> cuisineTypeCodes,

        ShippingInfo shippingInfo
) {
    public float getMinimalOrderAmount() {
        return (float) shippingInfo.delivery().minOrderValue() / 100;
    }

    public float getDeliveryFee() {
        return (float) shippingInfo.delivery().deliveryFeeDefault() / 100;
    }
}
