package org.sergfedrv.pageobjects.home;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.sergfedrv.pageobjects.BasePage;
import org.sergfedrv.pageobjects.restaurant.RestaurantsSearchPage;
import org.sergfedrv.utils.ScreenshotHelper;

import java.util.List;

public class HomePage extends BasePage {

    private final By searchInputLocator = By.cssSelector("input[data-qa='location-panel-search-input-address-element']");
    private final By locationPanelResultListElements = By.cssSelector("div[data-qa='location-panel-search-panel']" +
            ">div>div>ul>li[data-qa='location-panel-results-item-element']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    @Step("Send '{query} search query string into the search field'")
    public HomePage inputSearchQuery(String query) {
        waitVisibilityOfElementBy(searchInputLocator).sendKeys(query);
        return new HomePage(driver);
    }

    @Step("Select option of index ({resultIndex}) from the location panel list items.")
    public void selectLocationResult(int resultIndex) {
        List<WebElement> results = waitVisibilityOfElementsBy(locationPanelResultListElements);
        if (results.size() < resultIndex) {
            ScreenshotHelper.takeScreenshot("Not enough options to select from screenshot", driver);
            throw new IndexOutOfBoundsException(String.format("Option index %d out of bounds for length %d",
                    resultIndex, results.size()));
        }
        results.get(resultIndex).click();
    }
}
