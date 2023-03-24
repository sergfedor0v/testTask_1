package org.sergfedrv.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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

    protected WebElement waitVisibilityOfElementBy(By by) {
        WebElement element;
        try {
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (TimeoutException e) {
            ScreenshotHelper.takeScreenshot(e.getMessage(), driver);
            throw new TimeoutException(e);
        }
        return element;
    }

    protected List<WebElement> waitVisibilityOfElementsBy(By by) {
        List<WebElement> elements;
        try {
            elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
        } catch (TimeoutException e) {
            ScreenshotHelper.takeScreenshot(e.getMessage(), driver);
            throw new TimeoutException(e);
        }
        return elements;
    }

    protected void performClickAction(By by) {
        WebElement clickableElement = waitClickabilityOfElementBy(by);
        new Actions(driver).click(clickableElement).perform();
    }

    protected WebElement waitClickabilityOfElementBy(By by) {
        WebElement element;
        try {
            element = wait.until(ExpectedConditions.elementToBeClickable(by));
        } catch (TimeoutException e) {
            ScreenshotHelper.takeScreenshot(e.getMessage(), driver);
            throw new TimeoutException(e);
        }
        return element;
    }

    protected List<WebElement> getElementsBy(By by) {
        return driver.findElements(by);
    }
}
