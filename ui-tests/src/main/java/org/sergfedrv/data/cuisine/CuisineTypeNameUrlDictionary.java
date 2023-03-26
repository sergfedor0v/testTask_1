package org.sergfedrv.data.cuisine;

import java.util.Map;
import java.util.Optional;

/**
 * Represents name and url mapping for given cuisine code
 * @param name
 * @param url
 */
public record CuisineTypeNameUrlDictionary(Map<String, String> name, Map<String, String> url) {

    public String getTypeNameForCode(CountryCode countryCode) {
        return Optional.ofNullable(name.get(countryCode.value)).orElseThrow();
    }
}
