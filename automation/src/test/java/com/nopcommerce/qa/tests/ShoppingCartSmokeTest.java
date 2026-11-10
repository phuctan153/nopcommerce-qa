package com.nopcommerce.qa.tests;

import com.nopcommerce.qa.pages.HomePage;
import com.nopcommerce.qa.pages.ShoppingCartPage;
import com.nopcommerce.qa.pages.ShoppingCartPage.CartLine;
import com.nopcommerce.qa.support.BaseTest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ShoppingCartSmokeTest extends BaseTest {

    private static final String DEFAULT_PRODUCT = "HTC smartphone";

    @Test(groups = "smoke", description = "TC-CART-001: Add a product with a valid configuration")
    public void addingProductShowsTheRequestedQuantityInCart() {
        String productName = System.getProperty("cartProduct", DEFAULT_PRODUCT);
        int quantity = 2;

        assertSingleLine(addProductToCart(productName, quantity), productName, quantity);
    }

    @Test(groups = "smoke", description = "TC-CART-003: Save a supported cart quantity change")
    public void updatedQuantityAndLineTotalPersistWhenCartIsReopened() {
        String productName = System.getProperty("cartProduct", DEFAULT_PRODUCT);
        int initialQuantity = 1;
        int updatedQuantity = 2;

        ShoppingCartPage cart = addProductToCart(productName, initialQuantity);
        CartLine initialLine = assertSingleLine(cart, productName, initialQuantity);
        assertLineTotal(initialLine);

        cart.updateQuantity(productName, updatedQuantity);
        ShoppingCartPage reopenedCart = new HomePage(driver()).open(config().baseUrl())
                .header()
                .openCart();
        CartLine updatedLine = assertSingleLine(reopenedCart, productName, updatedQuantity);
        assertLineTotal(updatedLine);
    }

    @Test(groups = "smoke", description = "TC-CART-006: Remove a selected line and then empty the cart")
    public void removingProductsPreservesTheOtherLineUntilCartIsEmpty() {
        String firstProduct = DEFAULT_PRODUCT;
        String secondProduct = "Apple iPhone 16 128GB";
        addProductToCart(firstProduct, firstProduct, 1);
        ShoppingCartPage cart = addProductToCart("iphone", secondProduct, 1);

        List<CartLine> initialLines = cart.visibleLines();
        assertEquals(initialLines.size(), 2, "Expected two distinct cart lines: " + initialLines);
        assertEquals(Set.copyOf(initialLines.stream().map(CartLine::productName).toList()),
                Set.of(firstProduct, secondProduct), "Unexpected products in the cart.");
        CartLine remainingLine = initialLines.stream()
                .filter(line -> line.productName().equals(secondProduct))
                .findFirst()
                .orElseThrow();

        cart.removeProduct(firstProduct);
        assertEquals(cart.visibleLines(), List.of(remainingLine),
                "Removing the first product should leave the other cart line unchanged.");

        cart.removeProduct(secondProduct);
        assertEquals(cart.emptyCartMessage(), "Your Shopping Cart is empty!",
                "Removing the last product should display the empty cart message.");
        assertEquals(cart.visibleLines(), List.of(), "Expected no product lines in the empty cart.");
    }

    private ShoppingCartPage addProductToCart(String productName, int quantity) {
        return addProductToCart(System.getProperty("cartSearchQuery", DEFAULT_PRODUCT), productName, quantity);
    }

    private ShoppingCartPage addProductToCart(String query, String productName, int quantity) {
        return new HomePage(driver()).open(config().baseUrl())
                .searchFor(query)
                .openProduct(productName)
                .addToCartAndOpenCart(quantity);
    }

    private CartLine assertSingleLine(ShoppingCartPage cart, String productName, int quantity) {
        List<CartLine> lines = cart.visibleLines();
        assertEquals(lines.size(), 1, "Expected exactly one visible cart line: " + lines);
        CartLine line = lines.get(0);
        assertEquals(line.productName(), productName, "Unexpected product in the cart.");
        assertEquals(line.quantity(), quantity, "Unexpected cart quantity for " + productName);
        return line;
    }

    private void assertLineTotal(CartLine line) {
        BigDecimal expected = parseUsd(line.unitPrice()).multiply(BigDecimal.valueOf(line.quantity()));
        BigDecimal actual = parseUsd(line.lineTotal());
        assertEquals(actual.compareTo(expected), 0,
                "Expected a line total of " + expected + " for " + line);
    }

    private BigDecimal parseUsd(String amount) {
        return new BigDecimal(amount.replace("$", "").replace(",", ""));
    }
}
