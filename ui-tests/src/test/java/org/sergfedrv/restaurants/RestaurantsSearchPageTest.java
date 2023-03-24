package org.sergfedrv.restaurants;

import io.qameta.allure.Allure;
import io.qameta.allure.Step;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.sergfedrv.BaseTest;
import org.sergfedrv.Configuration;
import org.sergfedrv.data.cuisine.CuisineTypeMapper;
import org.sergfedrv.data.restaurant.RestaurantData;
import org.sergfedrv.pageobjects.home.HomePage;
import org.sergfedrv.pageobjects.restaurant.RestaurantCard;
import org.sergfedrv.pageobjects.restaurant.RestaurantSearchResultCard;
import org.sergfedrv.pageobjects.restaurant.RestaurantsSearchPage;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RestaurantsSearchPageTest extends BaseTest {
    @Test
    @Tag("ui_tests")
    public void testSearchByPlaceAndCuisineType() {
        driver.get(Configuration.getRestaurantListDirectUrl());
        String category = "Italian";
        RestaurantsSearchPage page = new RestaurantsSearchPage(driver)
                .filterRestaurantsByQuery("Pizza")
                .filterByCuisineTopSwiper(category)
                .scrollAllTheWayDown();
        checkCuisineCategoryOfRestaurantsFilteredByNameAndCategory(page, "category");
    }

    @Test
    @Tag("ui_tests")
    public void testSearchByCuisineTypeOnly() {
        //Arrange - get actual restaurant data from backend
        List<RestaurantData> actualRestaurantsData = loadActualRestaurantDataForLocation(Configuration
                .getSearchPostalCode());
        //Act
        driver.get(Configuration.getRestaurantListDirectUrl());
        String cuisineTypeName = "Italian";
        RestaurantsSearchPage page = new RestaurantsSearchPage(driver)
                .filterByCuisineTopSwiper(cuisineTypeName)
                .scrollAllTheWayDown();
        //Assert
        checkCuisineCategoryOfRestaurantsFilteredByCategory(page, cuisineTypeName, actualRestaurantsData);
    }

    @Test
    @Tag("ui_tests")
    public void filterByMinimalOrderValue() {
        driver.get(Configuration.getRestaurantListDirectUrl());
        int minimalOrderAmount = 10;
        RestaurantsSearchPage restaurantsSearchPage = new RestaurantsSearchPage(driver)
                .waitRestaurantListLoading()
                .applyMinimalOrderFilter(minimalOrderAmount)
                .scrollAllTheWayDown();
        checkAllRestaurantsFilteredByMinimalOrderAmount(restaurantsSearchPage, minimalOrderAmount);
    }

    @Test
    @Tag("ui_tests")
    public void freeDeliveryFilterTest() {
        driver.get(Configuration.getRestaurantListDirectUrl());
        RestaurantsSearchPage page = new RestaurantsSearchPage(driver)
                .waitRestaurantListLoading()
                .applyFreeDeliveryFilter()
                .scrollAllTheWayDown();
        checkAllRestaurantsFilteredByFreeDelivery(page);
    }

    @Test
    @Tag("ui_tests")
    public void openNowFilterTest() {
        driver.get(Configuration.getRestaurantListDirectUrl());
        RestaurantsSearchPage page = new RestaurantsSearchPage(driver)
                .waitRestaurantListLoading()
                .applyOpenNowFilter()
                .scrollAllTheWayDown();
        checkAllRestaurantsFilteredByOpenNow(page);
    }

    @Test
    @Tag("ui_tests")
    public void cloudflareErrorsDemo() {
        driver.get(baseUrl);
        new HomePage(driver)
                .inputSearchQuery("Unter den Linden")
                .selectLocationResult(0);
        appendBrowserConsoleLogs();
    }

    @Step("Check that all restaurant cards filtered by name and cuisine category='{category}' have this category")
    public void checkCuisineCategoryOfRestaurantsFilteredByNameAndCategory(RestaurantsSearchPage page, String category) {
        List<RestaurantSearchResultCard> restaurantCards = page.getRestaurantSearchCardElements();
        for (RestaurantSearchResultCard card : restaurantCards) {
            //catch card that does not match with filter and scroll to it to take a screenshot
            if (!card.getAvailableCuisineTypes().contains(category)) {
                card.scrollBrowserWindowToTheCardAndTakeScreenshot();
            }
            assertThat(card.getAvailableCuisineTypes())
                    .as(String.format("Check, that restaurant %s really can cook %s food",
                            card.getRestaurantTitle(), category))
                    .contains(category);
        }
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
