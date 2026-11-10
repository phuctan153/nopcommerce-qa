package com.nopcommerce.qa.pages;

import java.net.URI;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class HomePage {

    private static final By SEARCH_FIELD = By.id("small-searchterms");
    private static final By SEARCH_BUTTON = By.cssSelector("#small-search-box-form button[type='submit']");
    private final WebDriver driver;
    private final WebDriverWait wait;

    public HomePage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.withMessage(() -> "Storefront search controls were unavailable. Page title: " + driver.getTitle());
    }

    public HomePage open(URI baseUrl) {
        driver.get(baseUrl.toString());
        wait.until(ExpectedConditions.visibilityOfElementLocated(SEARCH_FIELD));
        return this;
    }

    public SearchResultsPage searchFor(String query) {
        WebElement searchField = wait.until(ExpectedConditions.elementToBeClickable(SEARCH_FIELD));
        searchField.clear();
        searchField.sendKeys(query);
        wait.until(ExpectedConditions.elementToBeClickable(SEARCH_BUTTON)).click();
        return new SearchResultsPage(driver);
    }
}
