package org.sergfedrv.data.cuisine;

import org.sergfedrv.utils.FileHelper;
import org.sergfedrv.utils.GsonHelper;

import java.util.ArrayList;
import java.util.List;

public class CuisineTypeMapper {
    private final CuisineTypeNameMappingObject mappingObject;

    public CuisineTypeMapper() {
        String jsonString = FileHelper.getResourceFileAsString(this.getClass(), "cuisine_type_mapping.json");
        mappingObject = GsonHelper.fromJsonString(jsonString, CuisineTypeNameMappingObject.class);
    }

    public List<String> getTypeNamesForCodes(List<String> codes) {
        List<String> names = new ArrayList<>();
        codes.forEach(code -> names.addAll(mappingObject.getTypeNamesForTypeCode(code)));
        return names;
    }
}

