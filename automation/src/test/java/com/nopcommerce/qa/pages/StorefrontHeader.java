package com.nopcommerce.qa.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class StorefrontHeader {

    private static final By SIGN_IN = By.cssSelector(".header-links a.ico-login");
    private static final By ACCOUNT = By.cssSelector(".header-links a.ico-account");
    private static final By SIGN_OUT = By.cssSelector(".header-links a.ico-logout");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public StorefrontHeader(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public LoginPage openSignIn() {
        wait.until(ExpectedConditions.elementToBeClickable(SIGN_IN)).click();
        return new LoginPage(driver);
    }

    public CustomerInfoPage openAccount() {
        wait.until(ExpectedConditions.elementToBeClickable(ACCOUNT)).click();
        return new CustomerInfoPage(driver);
    }

    public HomePage signOut() {
        wait.until(ExpectedConditions.elementToBeClickable(SIGN_OUT)).click();
        return new HomePage(driver);
    }

    public boolean isSignedIn() {
        return isVisible(ACCOUNT) && isVisible(SIGN_OUT) && !isVisible(SIGN_IN);
    }

    public boolean isGuest() {
        return isVisible(SIGN_IN) && !isVisible(ACCOUNT) && !isVisible(SIGN_OUT);
    }

    private boolean isVisible(By locator) {
        return driver.findElements(locator).stream().anyMatch(WebElement::isDisplayed);
    }
}
