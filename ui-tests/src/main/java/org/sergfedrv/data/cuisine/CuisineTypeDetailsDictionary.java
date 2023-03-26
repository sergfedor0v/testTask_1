package org.sergfedrv.data.cuisine;

import java.util.Map;

/**
 * Object representation of a file resources/org/sergfedrv/data/cuisine/cuisine_type_mapping.json
 * Map cuisine codes e.g. "italian_21" to the human friendly names, e.g "Italian"
 * @param details
 */
public record CuisineTypeDetailsDictionary(Map<String, CuisineTypeNameUrlDictionary> details) {

    /**
     * Returns name of a cuisine by its code in a human friendly format in a language of countryCode
     * E.g. for code "italian_21" and countryCode = CountryCode.EN returns string "Italian"
     * @param cuisineCode code of cuisine, e.g. "italian_21"
     * @param countryCode code of the country for translation.
     * @return Cuisine name, e.g. Italian
     */
    public String getCuisineNameInHumanFormat(String cuisineCode, CountryCode countryCode) {
        return details.get(cuisineCode).getTypeNameForCode(countryCode);
    }
}
