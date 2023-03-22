package org.sergfedrv.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.sergfedrv.utils.ScreenshotHelper;

import java.time.Duration;
import java.util.List;

public class BasePage {
    protected final static Duration WAIT_DURATION = Duration.ofSeconds(15);
    private final WebDriverWait wait;
    protected WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, WAIT_DURATION);
    }

    public WebDriver getPageDriver() {
        return this.driver;
    }

    protected WebElement getElement(By by) {
        WebElement element;
        try {
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (TimeoutException e) {
            ScreenshotHelper.takeScreenshot(e.getMessage(), driver);
            throw new TimeoutException(e);
        }
        return element;
    }

    protected List<WebElement> getElements(By by) {
        List<WebElement> elements;
        try {
            elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
        } catch (TimeoutException e) {
            ScreenshotHelper.takeScreenshot(e.getMessage(), driver);
            throw new TimeoutException(e);
        }
        return elements;
    }
}
