package org.sergfedrv.pageobjects.restaurant;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.sergfedrv.pageobjects.BasePage;
import org.sergfedrv.utils.ScreenshotHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantsSearchPage extends BasePage {
    private final By openedRestaurantsCardsLocator = By.cssSelector("section[data-qa='restaurant-list-open-section'] " +
            "ul>li");
    private final By openingSoonRestaurantCardsLocator = By.cssSelector("section[data-qa='restaurant-list-pre-order-" +
            "section'] ul>li");

    private final By closedRestaurantCardsLocator = By.cssSelector("section[data-qa='restaurant-list-open-section']" +
            " ul>li");

    private final By restaurantListHeaderLocator = By.cssSelector("h1[data-qa='restaurant-list-header']");
    private final By minimalOrderFilterShowAllLocator = By.id("radio_0");
    private final By minimalOrderFilterShowTenOrLessLocator = By.id("radio_1");
    private final By minimalOrderFilterShowFifteenOrLessLocator = By.id("radio_2");
    private final By openedNowSwitcherLocator = By.id("switch_0");
    private final By freeDeliveryFilterSwitchLocator = By.cssSelector("div[data-qa='free-delivery-filter-switch']");
    private final By restaurantListSearchInputLocator = By.id("input_5");
    private final By restaurantSearchResultLink = By.cssSelector("div[data-qa='restaurant-search-results']" +
            " ul>li a");
    private final By cuisineCategorySwiperOptionsLocator = By.cssSelector("ul.swiper-wrapper>li div._3wa4B");

    private final By footerLocator = By.cssSelector("footer[data-qa='footer']");

    public RestaurantsSearchPage(WebDriver driver) {
        super(driver);
    }

    @Step("Filter restaurants by {cuisineTypeToSelect} type from top swiper")
    public RestaurantsSearchPage filterByCuisineTopSwiper(String cuisineTypeToSelect) {
        List<WebElement> cuisineTypeOptions = getElementsBy(cuisineCategorySwiperOptionsLocator);
        WebElement optionToSelect = null;
        for (WebElement cuisineTypeOption : cuisineTypeOptions) {
            if (cuisineTypeOption.getText().equals(cuisineTypeToSelect)) {
                optionToSelect = cuisineTypeOption;
                break;
            }
        }
        if (optionToSelect == null) {
            ScreenshotHelper.takeScreenshot("Cannot find suitable option from list by name", driver);
            throw new IllegalArgumentException(String.format("Cannot filter restaurants by cuisine type %s",
                    cuisineTypeToSelect));
        }
        optionToSelect.click();
        return new RestaurantsSearchPage(driver);
    }

    @Step("Wait until restaurants list is loaded")
    public RestaurantsSearchPage waitRestaurantListLoading() {
        waitVisibilityOfElementBy(restaurantListHeaderLocator);
        return new RestaurantsSearchPage(driver);
    }

    @Step("Scroll restaurants page down until footer is not visible")
    public RestaurantsSearchPage scrollAllTheWayDown() {
        while (driver.findElements(footerLocator).isEmpty()) {
            new Actions(driver)
                    .scrollByAmount(0, 1500)
                    .perform();
        }
        return new RestaurantsSearchPage(driver);
    }

    @Step("Filter restaurants list by query '{searchQuery}'")
    public RestaurantsSearchPage filterRestaurantsByQuery(String searchQuery) {
        waitVisibilityOfElementBy(restaurantListSearchInputLocator).sendKeys(searchQuery);
        return new RestaurantsSearchPage(driver);
    }

    @Step("Apply free delivery filter")
    public RestaurantsSearchPage applyFreeDeliveryFilter() {
        performClickAction(freeDeliveryFilterSwitchLocator);
        return new RestaurantsSearchPage(driver);
    }

    @Step("Apply filtering by minimal order amount {minimalOrderAmount}")
    public RestaurantsSearchPage applyMinimalOrderFilter(int minimalOrderAmount) {
        switch (minimalOrderAmount) {
            case Integer.MAX_VALUE -> performClickAction(minimalOrderFilterShowAllLocator);
            case 10 -> performClickAction(minimalOrderFilterShowTenOrLessLocator);
            case 15 -> performClickAction(minimalOrderFilterShowFifteenOrLessLocator);
            default -> throw new IllegalArgumentException("Cannot filter by minimal order amount");
        }
        return new RestaurantsSearchPage(driver);
    }

    public List<RestaurantSearchResultCard> getRestaurantSearchCardElements() {
        List<WebElement> restaurantCards = getElementsBy(restaurantSearchResultLink);
        return restaurantCards.stream().map(card -> new RestaurantSearchResultCard(driver, card))
                .collect(Collectors.toList());
    }

    public List<RestaurantCard> getAllRestaurantCardElements() {
        List<WebElement> elements = new ArrayList<>();
        elements.addAll(getElementsBy(openedRestaurantsCardsLocator));
        elements.addAll(getElementsBy(openingSoonRestaurantCardsLocator));
        elements.addAll(getElementsBy(closedRestaurantCardsLocator));
        return elements.stream().map(element -> new RestaurantCard(driver, element))
                .collect(Collectors.toList());
    }

    public List<RestaurantCard> getOpenedRestaurantCardElements() {
        return getElementsBy(openedRestaurantsCardsLocator).stream().map(element -> new RestaurantCard(driver, element))
                .collect(Collectors.toList());
    }

    public List<RestaurantCard> getOpeningSoonRestaurantCardElements() {
        return getElementsBy(openingSoonRestaurantCardsLocator).stream()
                .map(element -> new RestaurantCard(driver, element))
                .collect(Collectors.toList());
    }
}
