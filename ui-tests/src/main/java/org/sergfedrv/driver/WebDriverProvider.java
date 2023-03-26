package org.sergfedrv.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.sergfedrv.Configuration;

import java.util.Locale;
import java.util.Optional;

public class WebDriverProvider {
    private static final String CHROME = "CHROME";
    private static final String FIREFOX = "FIREFOX";
    private static final String GRID_CHROME = "GRID_CHROME";

    public static WebDriver getWebDriver() {
        String webDriverType = Configuration.getDriverName();
        Optional.ofNullable(webDriverType)
                .orElseThrow(
                        () -> new IllegalArgumentException("Cannot choose browser for the test launch. \n" +
                                "Please, run test script with -Dbrowser=someValue option")
                );
        WebDriver driver;
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--remote-allow-origins=*");
        switch (webDriverType.toUpperCase(Locale.ENGLISH)) {
            case CHROME -> driver = WebDriverManager.chromedriver().capabilities(chromeOptions).create();
            case GRID_CHROME -> driver = new RemoteWebDriver(Configuration.getRemoteDriverUrl(), chromeOptions);
            case FIREFOX -> driver = WebDriverManager.firefoxdriver().create();
            default -> throw new IllegalArgumentException(String.format("Cannot find suitable browser for option %s. " +
                            "Supported values are: chrome, firefox and grid_chrome (for launching tests in Selenium Grid)",
                    webDriverType));
        }
        return driver;
    }
}
