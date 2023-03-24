package org.sergfedrv.pageobjects.restaurant;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class RestaurantCard extends BaseCard {

    private final By minimalOrderAmountLocator = By.cssSelector("div[data-qa='mov-indicator-content']" +
            " span[class='_2PRj3E']");
    private final float minimalOrderAmount;
    private final String restaurantPrimarySlug;
    private final String restaurantTitle;
    private final boolean isFreeDeliveryAvailable;

    public RestaurantCard(WebDriver driver, WebElement rootElement) {
        super(driver, rootElement);
        minimalOrderAmount = calculateMinimalOrderAmount();
        restaurantPrimarySlug = getElementBy(By.cssSelector("div[data-qa^='restaurant-card-']"))
                .getAttribute("data-qa")
                .replaceAll("restaurant-card-", "");
        restaurantTitle = getElementBy(By.cssSelector("div>a")).getAttribute("title");
        isFreeDeliveryAvailable = getElementBy(By.cssSelector("div[data-qa='delivery-costs-indicator-content']"))
                .getText().equals("Free");
    }

    public String getRestaurantPrimarySlug() {
        return restaurantPrimarySlug;
    }

    public String getRestaurantTitle() {
        return restaurantTitle;
    }

    private float calculateMinimalOrderAmount() {
        List<WebElement> elements = driver.findElements(minimalOrderAmountLocator);
        if (elements.isEmpty()) {
            return 0;
        }
        String priceString = elements.get(0).getText();
        return Float.parseFloat(priceString.replaceAll("\\p{Sc}", "").replaceAll(",", "."));
    }

    public float getMinimalOrderAmount() {
        return minimalOrderAmount;
    }

    public boolean isFreeDeliveryAvailable() {
        return isFreeDeliveryAvailable;
    }
}
