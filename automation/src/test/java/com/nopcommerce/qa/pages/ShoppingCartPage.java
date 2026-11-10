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
    private static final By REMOVE = By.cssSelector("button.remove-btn[name='updatecart']");
    private static final By EMPTY_CART = By.cssSelector(".shopping-cart-page .order-summary-content .no-data");
    private static final By SUBTOTAL = By.cssSelector("table.cart-total .order-subtotal .value-summary");
    private static final By SUBTOTAL_DISCOUNTS = By.cssSelector("td.subtotal .discount, .order-subtotal-discount");

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
        WebElement row = rowFor(productName);
        WebElement quantityField = row.findElement(QUANTITY);
        wait.withMessage("The cart quantity field was not editable.")
                .until(ExpectedConditions.elementToBeClickable(quantityField));
        // Leaving the edited field submits the cart update.
        quantityField.sendKeys(Keys.chord(Keys.CONTROL, "a"), Integer.toString(quantity), Keys.TAB);
        wait.withMessage("Updating the quantity did not reload the cart.")
                .until(ExpectedConditions.stalenessOf(row));
    }

    public void removeProduct(String productName) {
        WebElement row = rowFor(productName);
        wait.withMessage("The remove button was not clickable for " + productName)
                .until(ExpectedConditions.elementToBeClickable(row.findElement(REMOVE))).click();
        wait.withMessage("Removing " + productName + " did not reload the cart.")
                .until(ExpectedConditions.stalenessOf(row));
    }

    public String emptyCartMessage() {
        return wait.withMessage("The empty cart message did not become visible.")
                .until(ExpectedConditions.visibilityOfElementLocated(EMPTY_CART)).getText();
    }

    public String visibleSubtotal() {
        return wait.withMessage("The cart subtotal did not become visible.")
                .until(ExpectedConditions.visibilityOfNestedElementsLocatedBy(CART, SUBTOTAL)).get(0).getText();
    }

    public boolean hasVisibleSubtotalDiscount() {
        return visibleCart().findElements(SUBTOTAL_DISCOUNTS).stream().anyMatch(WebElement::isDisplayed);
    }

    private WebElement rowFor(String productName) {
        return visibleRows().stream()
                .filter(item -> item.findElement(PRODUCT_NAME).getText().equals(productName))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No cart line found for " + productName));
    }

    private List<WebElement> visibleRows() {
        return visibleCart().findElements(CART_ROWS).stream().filter(WebElement::isDisplayed).toList();
    }

    private WebElement visibleCart() {
        return wait.withMessage("The shopping cart did not become visible.")
                .until(ExpectedConditions.visibilityOfElementLocated(CART));
    }

    public record CartLine(String productName, int quantity, String unitPrice, String lineTotal) {
    }
}
