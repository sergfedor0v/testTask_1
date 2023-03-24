package org.sergfedrv.data.cuisine;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public record CuisineTypeNameMappingObject(Map<String, CuisineTypeNameUrl> details) {
    public List<String> getTypeNamesForTypeCode(String codeString) {
        CuisineTypeNameUrl cuisineTypeNameUrlObj = Optional.ofNullable(details.get(codeString)).orElseThrow();
        return cuisineTypeNameUrlObj.getTypeNamesList();
    }
}
