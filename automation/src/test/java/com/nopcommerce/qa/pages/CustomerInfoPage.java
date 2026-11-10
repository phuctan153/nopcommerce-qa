package com.nopcommerce.qa.pages;

import java.net.URI;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class CustomerInfoPage {

    private static final By EMAIL = By.cssSelector(".customer-info-page #Email");
    private static final By ACCOUNT_CONTENT = By.cssSelector(".customer-info-page");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public CustomerInfoPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String email() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL)).getDomProperty("value");
    }

    public URI url() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(EMAIL));
        return URI.create(driver.getCurrentUrl());
    }

    public boolean isVisible() {
        return driver.findElements(ACCOUNT_CONTENT).stream().anyMatch(WebElement::isDisplayed);
    }
}
