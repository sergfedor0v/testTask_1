package org.sergfedrv.restaurants;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import org.assertj.core.api.AutoCloseableSoftAssertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.sergfedrv.BaseTest;
import org.sergfedrv.Configuration;
import org.sergfedrv.data.cuisine.CountryCode;
import org.sergfedrv.pageobjects.home.HomePage;
import org.sergfedrv.pageobjects.restaurant.RestaurantCard;
import org.sergfedrv.pageobjects.restaurant.RestaurantsSearchPage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantsSearchPageTest extends BaseTest {

    @AfterEach
    public void afterTest() {
        appendBrowserConsoleLogs();
        super.afterTest();
    }

    @Test
    @DisplayName("Check that restaurants can be filtered by cuisine type")
    @Description("""
            Given: User opens restaurant search page
            When: User select category "Italian" from top swiper panel of the page
            Then: Restaurants list is filtered by this category.
            """)
    public void testSearchByCuisineType() {
        driver.get(Configuration.getRestaurantListDirectUrl());
        String cuisineTypeName = "Italian";
        RestaurantsSearchPage page = new RestaurantsSearchPage(driver)
                .waitRestaurantListLoading()
                .filterByCuisineTopSwiper(cuisineTypeName)
                .scrollAllTheWayDown();
        //Assert
        checkRestaurantsFilteredByCuisineCategory(page, cuisineTypeName);
    }

    @ParameterizedTest
    @ValueSource(ints = {10, 15})
    @DisplayName("Check that restaurants can be filtered by minimal order value")
    @Description("""
            Given: User opens restaurant search page
            When: User press minimal order amount radio button ([10 or less] or [15 or less])
            Then: Restaurants list is filtered by minimal order amount
            """)
    public void filterByMinimalOrderValue(int minimalOrderValue) {
        driver.get(Configuration.getRestaurantListDirectUrl());
        RestaurantsSearchPage restaurantsSearchPage = new RestaurantsSearchPage(driver)
                .waitRestaurantListLoading()
                .applyMinimalOrderFilter(minimalOrderValue)
                .scrollAllTheWayDown();
        checkFilteringByMinimalOrderAmount(restaurantsSearchPage, minimalOrderValue);
    }

    @Test
    @DisplayName("Check that restaurants can be filtered by [Free delivery] filter switcher")
    @Description("""
            Given: User opens restaurant search page
            When: User apply [Free delivery] filter.
            Then: Restaurants list shows only restaurants with free delivery
            """)
    public void freeDeliveryFilterTest() {
        driver.get(Configuration.getRestaurantListDirectUrl());
        RestaurantsSearchPage page = new RestaurantsSearchPage(driver)
                .waitRestaurantListLoading()
                .applyFreeDeliveryFilter()
                .scrollAllTheWayDown();
        checkFilteringByFreeDelivery(page);
    }

    @Test
    @DisplayName("Check that restaurants can be filtered by [Open now] filter switcher.")
    @Description("""
            Given: User opens restaurant search page
            When: User apply [Free delivery] filter.
            Then: Restaurants list shows only restaurants with free delivery
            """)
    public void openNowFilterTest() {
        driver.get(Configuration.getRestaurantListDirectUrl());
        RestaurantsSearchPage page = new RestaurantsSearchPage(driver)
                .waitRestaurantListLoading()
                .applyOpenNowFilter()
                .scrollAllTheWayDown();
        checkFilteringByOpenNow(page);
    }

    @Test
    @DisplayName("Check that it's possible to search restaurants for location")
    @Description("""
            Given: User opens main page
            When: User fills valid address in search field
            Then: User sees list of suggested locations
            When: User clicks on suggested location option
            Then: Opens page with the restaurant list for this location.
            """)
    public void cloudflareErrorsDemo() {
        driver.get(Configuration.getBaseUrl());
        new HomePage(driver)
                .inputSearchQuery("Unter den Linden")
                .selectLocationResult(0);
        new RestaurantsSearchPage(driver).waitRestaurantListLoading();
    }

    @Step("Check that all restaurants are filtered by cuisine category='{categoryName}'")
    public void checkRestaurantsFilteredByCuisineCategory(
            RestaurantsSearchPage page,
            String categoryName
    ) {
        List<RestaurantCard> restaurantCards = page.getAllRestaurantCardElements();
        try (AutoCloseableSoftAssertions softAssertions = new AutoCloseableSoftAssertions()) {
            for (RestaurantCard card : restaurantCards) {
                List<String> restaurantAvailableCuisines = restaurantDataProvider
                        .getRestaurantCuisineTypeNames(card.getRestaurantPrimarySlug(), CountryCode.EN);
                softAssertions.assertThat(restaurantAvailableCuisines)
                        .as(String.format("Check, that restaurant %s really can cook %s food",
                                card.getRestaurantTitle(), categoryName))
                        .contains(categoryName);
            }
            page.scrollAllTheWayUpTakeScreenshot();
        }
    }

    @Step("Check that all restaurants were filtered by minimal order amount = {minimalAmount}")
    private void checkFilteringByMinimalOrderAmount(RestaurantsSearchPage page, float minimalAmount) {
        List<RestaurantCard> openedAndOpeningSoonRestaurants = new ArrayList<>();
        openedAndOpeningSoonRestaurants.addAll(page.getOpenedRestaurantCardElements());
        openedAndOpeningSoonRestaurants.addAll(page.getOpeningSoonRestaurantCardElements());
        try (AutoCloseableSoftAssertions softAssertions = new AutoCloseableSoftAssertions()) {
            for (RestaurantCard card : openedAndOpeningSoonRestaurants) {
                softAssertions.assertThat(card.getMinimalOrderAmount())
                        .as(String.format("Check, that restaurant %s minimal order amount is less than %f",
                                card.getRestaurantTitle(), minimalAmount))
                        .isLessThanOrEqualTo(minimalAmount);
            }
            page.scrollAllTheWayUpTakeScreenshot();
        }
        if (!page.getClosedRestaurantCardElements().isEmpty()) {
           checkClosedRestaurantsFilteringByMinimalOrderAmount(page, minimalAmount);
        }
    }
    @Step("Check that restaurants closed for delivery filtered by minimal order amount")
    private void checkClosedRestaurantsFilteringByMinimalOrderAmount(RestaurantsSearchPage page, float minimalAmount) {
        List<RestaurantCard> cards = page.getClosedRestaurantCardElements();
        try (AutoCloseableSoftAssertions softAssertions = new AutoCloseableSoftAssertions()) {
            for (RestaurantCard card : cards) {
                float restaurantMinimalOrderAmount = restaurantDataProvider
                        .getRestaurantMinimalOrderAmount(card.getRestaurantPrimarySlug());
                softAssertions.assertThat(restaurantMinimalOrderAmount)
                        .as(String.format("Check, that restaurant %s minimal order amount is less than %f",
                                card.getRestaurantTitle(), minimalAmount))
                        .isLessThanOrEqualTo(minimalAmount);
            }
            page.scrollAllTheWayDownTakeScreenshot();
        }
    }

    @Step("Check, that all restaurants were filtered by free delivery")
    private void checkFilteringByFreeDelivery(RestaurantsSearchPage page) {
        List<RestaurantCard> openedAndOpeningSoonRestaurants = new ArrayList<>();
        openedAndOpeningSoonRestaurants.addAll(page.getOpenedRestaurantCardElements());
        openedAndOpeningSoonRestaurants.addAll(page.getOpeningSoonRestaurantCardElements());
        try (AutoCloseableSoftAssertions softAssertions = new AutoCloseableSoftAssertions()) {
            for (RestaurantCard card : openedAndOpeningSoonRestaurants) {
                softAssertions.assertThat(card.isFreeDeliveryIndicatorAvailable())
                        .as(String.format("Check, that restaurant %s offers free delivery", card.getRestaurantTitle()))
                        .isTrue();
            }
            page.scrollAllTheWayUpTakeScreenshot();
        }
        if (!page.getClosedRestaurantCardElements().isEmpty()) {
            checkClosedRestaurantsFilteringByFreeDelivery(page);
        }
    }

    @Step("Check that restaurants closed for delivery filtered by free delivery availability")
    private void checkClosedRestaurantsFilteringByFreeDelivery(RestaurantsSearchPage page) {
        List<RestaurantCard> cards = page.getClosedRestaurantCardElements();
        try (AutoCloseableSoftAssertions softAssertions = new AutoCloseableSoftAssertions()) {
            for (RestaurantCard card : cards) {
                float restaurantDeliveryFee = restaurantDataProvider
                        .getRestaurantDeliveryFee(card.getRestaurantPrimarySlug());
                softAssertions.assertThat(restaurantDeliveryFee)
                        .as(String.format("Check, that restaurant %s offers free delivery", card.getRestaurantTitle()))
                        .isEqualTo(0);
            }
            page.scrollAllTheWayDownTakeScreenshot();
        }
    }

    @Step("Check, that only opened restaurants are displayed")
    private void checkFilteringByOpenNow(RestaurantsSearchPage page) {
        List<RestaurantCard> openedRestaurantCards = page.getOpenedRestaurantCardElements();
        List<RestaurantCard> openingSoonRestaurantCards = page.getOpeningSoonRestaurantCardElements();
        List<RestaurantCard> closedRestaurantCards = page.getClosedRestaurantCardElements();
        assertThat(openedRestaurantCards).as("Check that opened restaurants are displayed")
                .isNotEmpty();
        assertThat(openingSoonRestaurantCards).as("Check that opening soon restaurants are not displayed")
                .isEmpty();
        assertThat(closedRestaurantCards).as("Check that closed restaurants are not displayed")
                .isEmpty();
    }

    @Step("Browser console logs")
    public void appendBrowserConsoleLogs() {
        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
        StringBuilder builder = new StringBuilder();
        for (LogEntry entry : logEntries) {
            builder.append(new Date(entry.getTimestamp())).append(" ").append(entry.getLevel())
                    .append(" ").append(entry.getMessage()).append("\n");
        }
        Allure.addAttachment("Browser console logs", builder.toString());
    }
}
