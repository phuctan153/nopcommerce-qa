package com.nopcommerce.qa.pages;

import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class ProductDetailsPage {

    private static final By TITLE = By.cssSelector("#product-details-form .product-name h1");
    private static final By PRICE = By.cssSelector("#product-details-form .product-price span[id^='price-value-']");
    private static final By STOCK_STATUS = By.cssSelector("#product-details-form .stock .value");
    private static final By QUANTITY = By.cssSelector("#product-details-form .add-to-cart .qty-input");
    private static final By ADD_TO_CART = By.cssSelector("#product-details-form button.add-to-cart-button");
    private static final By CART_CONFIRMATION_LINK = By.cssSelector("#bar-notification .success a");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public ProductDetailsPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String title() {
        return visibleText(TITLE);
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }

    public String price() {
        return visibleText(PRICE);
    }

    public String stockStatus() {
        return visibleText(STOCK_STATUS);
    }

    public boolean canAddToCart() {
        return driver.findElements(ADD_TO_CART).stream()
                .anyMatch(button -> button.isDisplayed() && button.isEnabled());
    }

    public ShoppingCartPage addToCartAndOpenCart(int quantity) {
        WebElement quantityField = wait.until(ExpectedConditions.elementToBeClickable(QUANTITY));
        quantityField.clear();
        quantityField.sendKeys(Integer.toString(quantity));
        wait.until(ExpectedConditions.elementToBeClickable(ADD_TO_CART)).click();
        wait.withMessage("Adding the product did not show the shopping cart confirmation link.")
                .until(ExpectedConditions.elementToBeClickable(CART_CONFIRMATION_LINK)).click();
        return new ShoppingCartPage(driver);
    }

    private String visibleText(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator)).getText();
    }
}
