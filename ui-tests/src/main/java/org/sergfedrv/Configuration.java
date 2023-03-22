package org.sergfedrv;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;

public class Configuration {

    private static String getProperty(String key) {
        String errorMsg = String.format("No value was found for the key '%s'. Please, run tests with argument " +
                "-D%s={value}, or add this key to the 'gradle.properties' file", key, key);
        return Optional.ofNullable(System.getProperty(key))
                .orElseThrow(() -> new IllegalArgumentException(errorMsg));
    }

    public static String getBaseUrl() {
        return getProperty("datalore_url");
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
}
