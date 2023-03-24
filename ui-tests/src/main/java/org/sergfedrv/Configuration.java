package org.sergfedrv;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class Configuration {

    private static String getProperty(String key) {
        return Optional.ofNullable(System.getProperty(key))
                .orElseThrow(
                        () -> new IllegalArgumentException(String.format("No value was found for the key '%s'. " +
                                "Please, run tests with argument -D%s={value}, or add this key to the " +
                                "'gradle.properties' file", key, key))
                );
    }

    public static String getBaseUrl() {
        return getProperty("base_url");
    }

    public static String getRestaurantListDirectUrl() {
        return String.format("%s%s", getBaseUrl(), getProperty("restaurant_search_url"));
    }

    public static String getDriverName() {
        return getProperty("browser");
    }

    public static URL getRemoteDriverUrl() {
        try {
            return new URL(getProperty("remote_driver_url"));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getBaseApiUrl() {
        return getProperty("base_api_url");
    }

    public static String getSearchPostalCode() {
        return getProperty("search_postal_code");
    }
}
