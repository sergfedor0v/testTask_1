package org.sergfedrv.utils;

import com.google.gson.Gson;

public class GsonHelper {
    private static final Gson gson = new Gson();

    public static <T> T fromJsonString(String json, Class<T> tClass) {
        return gson.fromJson(json, tClass);
    }
}
