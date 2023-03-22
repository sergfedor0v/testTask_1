package org.sergfedrv.utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.ByteArrayInputStream;

public class ScreenshotHelper {
    public static void takeScreenshot(String description, WebDriver driver) {
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        Allure.addAttachment(description,
                new ByteArrayInputStream(screenshot.getScreenshotAs(OutputType.BYTES)));
    }
}
