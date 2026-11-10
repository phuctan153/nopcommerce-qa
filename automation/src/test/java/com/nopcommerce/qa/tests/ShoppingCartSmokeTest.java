package com.nopcommerce.qa.tests;

import com.nopcommerce.qa.pages.HomePage;
import com.nopcommerce.qa.pages.ShoppingCartPage;
import com.nopcommerce.qa.pages.ShoppingCartPage.CartLine;
import com.nopcommerce.qa.support.BaseTest;
import java.math.BigDecimal;
import java.util.List;
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

    private ShoppingCartPage addProductToCart(String productName, int quantity) {
        String query = System.getProperty("cartSearchQuery", DEFAULT_PRODUCT);
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
