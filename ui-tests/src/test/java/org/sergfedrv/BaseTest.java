package org.sergfedrv;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.sergfedrv.driver.WebDriverProvider;
import org.sergfedrv.utils.ApiHelper;
import org.sergfedrv.utils.RestaurantDataProvider;

public class BaseTest {
    protected WebDriver driver;
    protected ApiHelper apiHelper;
    protected RestaurantDataProvider restaurantDataProvider;

    @BeforeEach
    public void setDriver() {
        apiHelper = new ApiHelper(Configuration.getBaseApiUrl());
        restaurantDataProvider = RestaurantDataProvider.getInstance(apiHelper);
        driver = WebDriverProvider.getWebDriver();
    }

    @AfterEach
    public void afterTest() {
        driver.quit();
    }
}
