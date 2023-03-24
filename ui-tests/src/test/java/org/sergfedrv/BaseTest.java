package org.sergfedrv;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.sergfedrv.driver.WebDriverProvider;
import org.sergfedrv.utils.ApiHelper;

public class BaseTest {
    protected WebDriver driver;
    protected ApiHelper apiHelper;
    protected String baseUrl;

    @BeforeEach
    public void setDriver() {
        apiHelper = new ApiHelper(Configuration.getBaseApiUrl());
        driver = WebDriverProvider.getWebDriver();
        baseUrl = Configuration.getBaseUrl();
    }

    @AfterEach
    public void afterTest() {
        driver.quit();
    }
}
