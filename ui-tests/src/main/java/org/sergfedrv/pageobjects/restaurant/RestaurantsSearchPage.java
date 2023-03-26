package org.sergfedrv.pageobjects.restaurant;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.sergfedrv.pageobjects.BasePage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RestaurantsSearchPage extends BasePage {

    private final By headerLocator = By.cssSelector("header[data-qa='header']");
    private final By openedRestaurantsCardsLocator = By.cssSelector("section[data-qa='restaurant-list-open-section'] " +
            "ul>li");
    private final By openingSoonRestaurantCardsLocator = By.cssSelector("section[data-qa='restaurant-list-pre-order-" +
            "section'] ul>li");

    private final By closedRestaurantCardsLocator = By.cssSelector("section[data-qa='restaurant-list-closed-section']" +
            " ul>li");

    private final By restaurantListHeaderLocator = By.cssSelector("h1[data-qa='restaurant-list-header']");
    private final By minimalOrderFilterShowAllLocator = By.id("radio_0");
    private final By minimalOrderFilterShowTenOrLessLocator = By.id("radio_1");
    private final By minimalOrderFilterShowFifteenOrLessLocator = By.id("radio_2");
    private final By openedNowSwitcherLocator = By.cssSelector("div[data-qa='availability-filter-switch']");
    private final By freeDeliveryFilterSwitchLocator = By.cssSelector("div[data-qa='free-delivery-filter-switch']");
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
            takeScreenshot("Cannot find suitable option from list by name");
            throw new IllegalArgumentException(String.format("Cannot filter restaurants by cuisine type %s, no such " +
                            "option available.",
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
                    .scrollByAmount(0, 15000)
                    .perform();
        }
        return new RestaurantsSearchPage(driver);
    }

    @Step("Scroll restaurants list all the way up and take screenshot")
    public RestaurantsSearchPage scrollAllTheWayUpTakeScreenshot() {
        new Actions(driver).scrollToElement(waitVisibilityOfElementBy(headerLocator)).perform();
        takeScreenshot("Screenshot of the restaurant list top");
        return new RestaurantsSearchPage(driver);
    }

    @Step("Scroll restaurants page all the way down and take screenshot")
    public RestaurantsSearchPage scrollAllTheWayDownTakeScreenshot() {
        scrollAllTheWayDown();
        takeScreenshot("Screenshot of the restaurant list bottom");
        return new RestaurantsSearchPage(driver);
    }

    @Step("Apply \"Free delivery\" filter")
    public RestaurantsSearchPage applyFreeDeliveryFilter() {
        performClickAction(freeDeliveryFilterSwitchLocator);
        return new RestaurantsSearchPage(driver);
    }

    @Step("Apply \"Open now\" filter")
    public RestaurantsSearchPage applyOpenNowFilter() {
        performClickAction(openedNowSwitcherLocator);
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

    public List<RestaurantCard> getAllRestaurantCardElements() {
        List<RestaurantCard> cards = new ArrayList<>();
        cards.addAll(getOpenedRestaurantCardElements());
        cards.addAll(getOpeningSoonRestaurantCardElements());
        cards.addAll(getClosedRestaurantCardElements());
        return cards;
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

    public List<RestaurantCard> getClosedRestaurantCardElements() {
        return getElementsBy(closedRestaurantCardsLocator).stream()
                .map(element -> new RestaurantCard(driver, element))
                .collect(Collectors.toList());
    }
}
