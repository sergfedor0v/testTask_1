package org.sergfedrv.driver;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.sergfedrv.Configuration;

import java.util.Collections;
import java.util.Locale;
import java.util.Optional;

public class WebDriverProvider {
    private static final String CHROME = "CHROME";
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
        chromeOptions.addArguments("--disable-blink-features=AutomationControlled");
        chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));
        chromeOptions.setExperimentalOption("useAutomationExtension", null);
        chromeOptions.addArguments("start-maximized");
        chromeOptions.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/74.0.3729.169 Safari/537.36");
        chromeOptions.addArguments("disable-infobars");
        switch (webDriverType.toUpperCase(Locale.ENGLISH)) {
            case CHROME -> driver = WebDriverManager.chromedriver().capabilities(chromeOptions).create();
            case GRID_CHROME -> driver = new RemoteWebDriver(Configuration.getRemoteDriverUrl(), chromeOptions);
            default -> throw new IllegalArgumentException(String.format("Cannot find suitable browser for option %s. " +
                            "Supported values are: chrome and grid_chrome (for launching tests in Selenium Grid)",
                    webDriverType));
        }
        return driver;
    }
}
