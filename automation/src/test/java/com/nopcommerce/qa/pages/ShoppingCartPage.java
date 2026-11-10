package com.nopcommerce.qa.pages;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class ShoppingCartPage {

    private static final By CART = By.cssSelector(".shopping-cart-page .order-summary-content");
    private static final By CART_ROWS = By.cssSelector("table.cart tbody tr");
    private static final By PRODUCT_NAME = By.cssSelector("a.product-name");
    private static final By QUANTITY = By.cssSelector("input.qty-input");

    private final WebDriver driver;

    public ShoppingCartPage(WebDriver driver) {
        this.driver = driver;
    }

    public List<CartLine> visibleLines() {
        WebElement cart = new WebDriverWait(driver, Duration.ofSeconds(10))
                .withMessage("The shopping cart did not become visible.")
                .until(ExpectedConditions.visibilityOfElementLocated(CART));

        return cart.findElements(CART_ROWS).stream()
                .filter(WebElement::isDisplayed)
                .map(row -> new CartLine(
                        row.findElement(PRODUCT_NAME).getText(),
                        Integer.parseInt(row.findElement(QUANTITY).getDomProperty("value"))))
                .toList();
    }

    public record CartLine(String productName, int quantity) {
    }
}
