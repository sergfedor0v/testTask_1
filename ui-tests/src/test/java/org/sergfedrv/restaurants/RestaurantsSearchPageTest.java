package org.sergfedrv.restaurants;

import io.qameta.allure.Allure;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
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
import org.sergfedrv.data.cuisine.CuisineTypeMapper;
import org.sergfedrv.data.restaurant.RestaurantData;
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
        //Arrange - get actual restaurant cuisine type data from backend
        List<RestaurantData> actualRestaurantsData = loadActualRestaurantDataForLocation(Configuration
                .getSearchPostalCode());
        //Act
        driver.get(Configuration.getRestaurantListDirectUrl());
        String cuisineTypeName = "Italian";
        RestaurantsSearchPage page = new RestaurantsSearchPage(driver)
                .waitRestaurantListLoading()
                .filterByCuisineTopSwiper(cuisineTypeName)
                .scrollAllTheWayDown();
        //Assert
        checkCuisineCategoryOfRestaurantsFilteredByCategory(page, cuisineTypeName, actualRestaurantsData);
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
        checkAllRestaurantsFilteredByMinimalOrderAmount(restaurantsSearchPage, minimalOrderValue);
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
        checkAllRestaurantsFilteredByFreeDelivery(page);
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
        checkAllRestaurantsFilteredByOpenNow(page);
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
        driver.get(baseUrl);
        new HomePage(driver)
                .inputSearchQuery("Unter den Linden")
                .selectLocationResult(0);
        new RestaurantsSearchPage(driver).waitRestaurantListLoading();
    }

    @Step("Check that all restaurant cards from discovery filtered by cuisine category='{categoryName}' have this cuisine category")
    public void checkCuisineCategoryOfRestaurantsFilteredByCategory(
            RestaurantsSearchPage page,
            String categoryName,
            List<RestaurantData> actualRestaurantsData
    ) {
        List<RestaurantCard> restaurantCards = page.getAllRestaurantCardElements();
        CuisineTypeMapper cuisineTypeMapper = new CuisineTypeMapper();
        for (RestaurantCard card : restaurantCards) {
            List<String> actualRestaurantCuisineCategoryCodes = getRestaurantDataByNameSlug(actualRestaurantsData,
                    card.getRestaurantPrimarySlug()).cuisineTypeCodes();
            List<String> actualRestaurantCuisineTypeNames = cuisineTypeMapper
                    .getTypeNamesForCodes(actualRestaurantCuisineCategoryCodes);
            //catch card that does not match with filter and scroll to it to take a screenshot
            if (!actualRestaurantCuisineTypeNames.contains(categoryName)) {
                card.scrollBrowserWindowToTheCardAndTakeScreenshot();
            }
            assertThat(actualRestaurantCuisineTypeNames)
                    .as(String.format("Check, that restaurant %s really can cook %s food",
                            card.getRestaurantTitle(), categoryName))
                    .contains(categoryName);
        }
    }

    @Step("Check, that all opened and opening soon restaurants were filtered by minimal order amount = {minimalAmount}")
    private void checkAllRestaurantsFilteredByMinimalOrderAmount(
            RestaurantsSearchPage page,
            float minimalAmount
    ) {
        List<RestaurantCard> restaurantCards = new ArrayList<>();
        restaurantCards.addAll(page.getOpenedRestaurantCardElements());
        restaurantCards.addAll(page.getOpeningSoonRestaurantCardElements());
        for (RestaurantCard card : restaurantCards) {
            //catch card that does not match with filter and scroll to it to take a screenshot
            if (!(card.getMinimalOrderAmount() <= minimalAmount)) {
                card.scrollBrowserWindowToTheCardAndTakeScreenshot();
            }
            assertThat(card.getMinimalOrderAmount())
                    .as(String.format("Check, that restaurant %s minimal order amount is less than %f",
                            card.getRestaurantTitle(), minimalAmount))
                    .isLessThanOrEqualTo(minimalAmount);
        }
    }

    @Step("Check, that all opened and opening soon restaurants were filtered by free delivery")
    private void checkAllRestaurantsFilteredByFreeDelivery(RestaurantsSearchPage page) {
        List<RestaurantCard> restaurantCards = new ArrayList<>();
        restaurantCards.addAll(page.getOpenedRestaurantCardElements());
        restaurantCards.addAll(page.getOpeningSoonRestaurantCardElements());
        for (RestaurantCard card : restaurantCards) {
            //catch card that does not match with filter and scroll to it to take a screenshot
            if (!card.isFreeDeliveryAvailable()) {
                card.scrollBrowserWindowToTheCardAndTakeScreenshot();
            }
            assertThat(card.isFreeDeliveryAvailable())
                    .as(String.format("Check, that restaurant %s offers free delivery", card.getRestaurantTitle()))
                    .isTrue();
        }
    }

    @Step("Check, that only opened restaurants are available")
    private void checkAllRestaurantsFilteredByOpenNow(RestaurantsSearchPage page) {
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

    @Step("Load actual restaurant data from backend for postal code {postalCode}")
    private List<RestaurantData> loadActualRestaurantDataForLocation(String postalCode) {
        return apiHelper.getRestaurantInfoForLocation(postalCode).getRestaurantDataList();
    }

    private RestaurantData getRestaurantDataByNameSlug(List<RestaurantData> data, String nameSlug) {
        return data.stream().filter(r -> r.primarySlug().equals(nameSlug)).findFirst()
                .orElseThrow(() -> new RuntimeException("Cannot find a restaurant from preloaded data by primary slug "
                        + nameSlug));
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
