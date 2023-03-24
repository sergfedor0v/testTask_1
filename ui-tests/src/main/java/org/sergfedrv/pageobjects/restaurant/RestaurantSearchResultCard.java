package org.sergfedrv.pageobjects.restaurant;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Arrays;
import java.util.List;

public class RestaurantSearchResultCard extends BaseCard {

    private final List<String> cuisineTypes;
    private final String restaurantTitle;

    public RestaurantSearchResultCard(WebDriver driver, WebElement rootElement) {
        super(driver, rootElement);
        cuisineTypes = Arrays.asList(getElementBy(By.cssSelector("div[data-qa='restaurant-cuisines']"))
                .getText().trim().split("\\s*,\\s*"));
        restaurantTitle = getElementBy(By.cssSelector("h3[data-qa='search-result-card-title']")).getText();
    }

    public List<String> getAvailableCuisineTypes() {
        return cuisineTypes;
    }

    public String getRestaurantTitle() {
        return restaurantTitle;
    }
}
