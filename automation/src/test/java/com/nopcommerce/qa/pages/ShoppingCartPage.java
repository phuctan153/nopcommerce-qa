package com.nopcommerce.qa.pages;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class ShoppingCartPage {

    private static final By CART = By.cssSelector(".shopping-cart-page .order-summary-content");
    private static final By CART_ROWS = By.cssSelector("table.cart tbody tr");
    private static final By PRODUCT_NAME = By.cssSelector("a.product-name");
    private static final By QUANTITY = By.cssSelector("input.qty-input");
    private static final By UNIT_PRICE = By.cssSelector(".product-unit-price");
    private static final By LINE_TOTAL = By.cssSelector(".product-subtotal");

    private final WebDriverWait wait;

    public ShoppingCartPage(WebDriver driver) {
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public List<CartLine> visibleLines() {
        return visibleRows().stream()
                .map(row -> new CartLine(
                        row.findElement(PRODUCT_NAME).getText(),
                        Integer.parseInt(row.findElement(QUANTITY).getDomProperty("value")),
                        row.findElement(UNIT_PRICE).getText(),
                        row.findElement(LINE_TOTAL).getText()))
                .toList();
    }

    public void updateQuantity(String productName, int quantity) {
        WebElement row = visibleRows().stream()
                .filter(item -> item.findElement(PRODUCT_NAME).getText().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No cart line found for " + productName));
        WebElement quantityField = row.findElement(QUANTITY);
        wait.withMessage("The cart quantity field was not editable.")
                .until(ExpectedConditions.elementToBeClickable(quantityField));
        // Leaving the edited field submits the cart update.
        quantityField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Integer.toString(quantity), Keys.TAB);
        wait.withMessage("Updating the quantity did not reload the cart.")
                .until(ExpectedConditions.stalenessOf(row));
    }

    private List<WebElement> visibleRows() {
        WebElement cart = wait.withMessage("The shopping cart did not become visible.")
                .until(ExpectedConditions.visibilityOfElementLocated(CART));
        return cart.findElements(CART_ROWS).stream().filter(WebElement::isDisplayed).toList();
    }

    public record CartLine(String productName, int quantity, String unitPrice, String lineTotal) {
    }
}
