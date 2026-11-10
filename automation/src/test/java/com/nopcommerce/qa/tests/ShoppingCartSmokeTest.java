package com.nopcommerce.qa.tests;

import com.nopcommerce.qa.pages.HomePage;
import com.nopcommerce.qa.pages.ShoppingCartPage;
import com.nopcommerce.qa.pages.ShoppingCartPage.CartLine;
import com.nopcommerce.qa.support.BaseTest;
import java.util.List;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;

public class ShoppingCartSmokeTest extends BaseTest {

    @Test(groups = "smoke", description = "TC-CART-001: Add a product with a valid configuration")
    public void addingProductShowsTheRequestedQuantityInCart() {
        String query = System.getProperty("cartSearchQuery", "HTC smartphone");
        String productName = System.getProperty("cartProduct", "HTC smartphone");
        int quantity = 2;

        ShoppingCartPage cart = new HomePage(driver()).open(config().baseUrl())
                .searchFor(query)
                .openProduct(productName)
                .addToCartAndOpenCart(quantity);

        assertEquals(cart.visibleLines(), List.of(new CartLine(productName, quantity)),
                "Expected the fresh cart to contain the selected product with the requested quantity.");
    }
}
