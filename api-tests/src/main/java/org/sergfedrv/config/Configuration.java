package org.sergfedrv.config;

import org.sergfedrv.client.authentication.AppCredentials;

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
        return getProperty("baseUrl");
    }

    public static AppCredentials getAppCredentials(String appScope) {
        return new AppCredentials(getProperty(String.format("%s.clientId", appScope)),
                getProperty(String.format("%s.clientSecret", appScope)));
    }

    public static String getUserId() {
        return getProperty("userId");
    }

    public static boolean debugModeEnabled() {
        return Optional.ofNullable(System.getProperty("debugMode"))
                .isPresent() && System.getProperty("debugMode").equals("enabled");
    }
}
