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
    private static final By CART = By.cssSelector(".header-links a.ico-cart");
    private static final By TOP_LEVEL_CATEGORY = By.cssSelector(
            ".header-menu .menu > .menu__item > a.menu__link, "
                    + ".header-menu .menu > .menu__item > .menu__item-toggle > a.menu__link");

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

    public ShoppingCartPage openCart() {
        wait.until(ExpectedConditions.elementToBeClickable(CART)).click();
        return new ShoppingCartPage(driver);
    }

    public CategoryPage openTopLevelCategory(String name) {
        wait.withMessage("The top-level category link was unavailable for " + name)
                .until(browser -> browser.findElements(TOP_LEVEL_CATEGORY).stream()
                        .filter(link -> link.isDisplayed() && link.isEnabled() && link.getText().equals(name))
                        .findFirst()
                        .orElse(null))
                .click();
        return new CategoryPage(driver);
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
