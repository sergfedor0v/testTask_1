package org.sergfedrv.pageobjects;

import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.ByteArrayInputStream;
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

    protected void takeScreenshot(String description) {
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        Allure.addAttachment(description,
                new ByteArrayInputStream(screenshot.getScreenshotAs(OutputType.BYTES)));
    }

    protected WebElement waitVisibilityOfElementBy(By by) {
        WebElement element;
        try {
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));
        } catch (TimeoutException e) {
            takeScreenshot("Wait for element failed screenshot");
            throw new TimeoutException(e);
        }
        return element;
    }

    protected List<WebElement> waitVisibilityOfElementsBy(By by) {
        List<WebElement> elements;
        try {
            elements = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(by));
        } catch (TimeoutException e) {
            takeScreenshot("Wait for element failed screenshot");
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
            takeScreenshot("Wait for element failed screenshot");
            throw new TimeoutException(e);
        }
        return element;
    }

    protected List<WebElement> getElementsBy(By by) {
        return driver.findElements(by);
    }
}
