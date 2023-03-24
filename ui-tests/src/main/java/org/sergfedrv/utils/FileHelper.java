package org.sergfedrv.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileHelper {

    public static String getResourceFileAsString(Class<?> c, String fileName) {
        InputStream is = Optional.ofNullable(c.getResourceAsStream(fileName))
                .orElseThrow();
        try (InputStreamReader isr = new InputStreamReader(is);
             BufferedReader reader = new BufferedReader(isr)) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
