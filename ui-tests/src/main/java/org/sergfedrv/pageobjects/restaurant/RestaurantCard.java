package org.sergfedrv.pageobjects.restaurant;

import io.qameta.allure.Allure;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;

import java.io.ByteArrayInputStream;
import java.util.List;

public class RestaurantCard {

    protected WebElement rootElement;
    protected WebDriver driver;

    private final By minimalOrderAmountLocator = By.cssSelector("div[data-qa='mov-indicator-content']" +
            " span[class='_2PRj3E']");
    private final By deliveryCostIndicator = By.cssSelector("div[data-qa='delivery-costs-indicator-content']");
    private final String restaurantPrimarySlug;
    private final String restaurantTitle;

    public RestaurantCard(WebDriver driver, WebElement rootElement) {
        this.rootElement = rootElement;
        this.driver = driver;
        restaurantTitle = getElementBy(By.cssSelector("div>a")).getAttribute("title");
        restaurantPrimarySlug = getElementBy(By.cssSelector("div[data-qa^='restaurant-card-']"))
                .getAttribute("data-qa")
                .replaceAll("restaurant-card-", "");
    }

    private WebElement getElementBy(By by) {
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

    public String getRestaurantPrimarySlug() {
        return restaurantPrimarySlug;
    }

    public String getRestaurantTitle() {
        return restaurantTitle;
    }

    public float getMinimalOrderAmount() {
        List<WebElement> elements = driver.findElements(minimalOrderAmountLocator);
        if (elements.isEmpty()) {
            return 0;
        }
        String priceString = elements.get(0).getText();
        return Float.parseFloat(priceString.replaceAll("\\p{Sc}", "").replaceAll(",", "."));
    }

    public boolean isFreeDeliveryIndicatorAvailable() {
        return getElementBy(deliveryCostIndicator).getText().equals("Free");
    }
}
