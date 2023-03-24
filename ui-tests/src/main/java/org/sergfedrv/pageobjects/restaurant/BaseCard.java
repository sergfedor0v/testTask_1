package org.sergfedrv.pageobjects.restaurant;

import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.io.ByteArrayInputStream;

public class BaseCard {

    protected WebElement rootElement;
    protected WebDriver driver;

    public BaseCard(WebDriver driver, WebElement rootElement) {
        this.rootElement = rootElement;
        this.driver = driver;
    }

    protected WebElement getElementBy(By by) {
        WebElement element;
        try {
            element = rootElement.findElement(by);
        } catch (NoSuchElementException e) {
            new Actions(driver)
                    .scrollToElement(rootElement)
                    .perform();
            takeScreenshot("Cannot find child element of root element");
            throw new NoSuchElementException("Cannot find child element.", e);
        }
        return element;
    }

    protected void takeScreenshot(String description) {
        TakesScreenshot screenshot = (TakesScreenshot) driver;
        Allure.addAttachment(description,
                new ByteArrayInputStream(screenshot.getScreenshotAs(OutputType.BYTES)));
    }

    public void scrollBrowserWindowToTheCardAndTakeScreenshot() {
        new Actions(driver)
                .scrollToElement(rootElement)
                .perform();
        takeScreenshot("Screenshot of the page with the restaurant card");
    }
}
