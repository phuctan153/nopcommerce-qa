package com.nopcommerce.qa.support;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    private final ThreadLocal<WebDriver> browserSession = new ThreadLocal<>();
    private final TestConfig config = TestConfig.from(System.getProperties());

    @BeforeMethod
    public final void startBrowser() {
        browserSession.set(DriverFactory.create(config));
    }

    @AfterMethod(alwaysRun = true)
    public final void quitBrowser() {
        WebDriver driver = browserSession.get();
        browserSession.remove();
        if (driver != null) {
            driver.quit();
        }
    }

    protected final WebDriver driver() {
        return browserSession.get();
    }

    protected final TestConfig config() {
        return config;
    }
}
