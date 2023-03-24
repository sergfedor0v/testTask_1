package org.sergfedrv.data.cuisine;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public record CuisineTypeNameUrl(Map<String, String> name, Map<String, String> url) {
    public List<String> getTypeNamesList() {
        return new ArrayList<>(name.values());
    }
}
