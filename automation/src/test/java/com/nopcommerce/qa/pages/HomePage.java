package com.nopcommerce.qa.pages;

import java.net.URI;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class HomePage {

    private static final By SEARCH_FIELD = By.id("small-searchterms");
    private final WebDriver driver;

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public HomePage open(URI baseUrl) {
        driver.get(baseUrl.toString());
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(SEARCH_FIELD));
        return this;
    }
}
