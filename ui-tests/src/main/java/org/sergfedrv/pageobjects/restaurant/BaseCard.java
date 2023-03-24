package org.sergfedrv.pageobjects.restaurant;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.sergfedrv.utils.ScreenshotHelper;

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
            ScreenshotHelper.takeScreenshot("Cannot find child element of root element", driver);
            throw new NoSuchElementException("Cannot find child element.", e);
        }
        return element;
    }

    public void scrollBrowserWindowToTheCardAndTakeScreenshot() {
        new Actions(driver)
                .scrollToElement(rootElement)
                .perform();
        ScreenshotHelper.takeScreenshot("Screenshot of the page with element", driver);
    }
}
