package com.nopcommerce.qa.tests;

import com.nopcommerce.qa.pages.HomePage;
import com.nopcommerce.qa.pages.ProductDetailsPage;
import com.nopcommerce.qa.pages.SearchResultsPage;
import com.nopcommerce.qa.support.BaseTest;
import java.util.List;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class ProductSearchTest extends BaseTest {

    private static final String SAMPLE_QUERY = "iphone";
    private static final String SAMPLE_PRODUCT = "Apple iPhone 16 128GB";

    @Test(groups = "smoke", description = "TC-CAT-001: Find a current product by a valid query")
    public void validQueryReturnsExpectedProduct() {
        String query = System.getProperty("searchQuery", SAMPLE_QUERY);
        String expectedProduct = System.getProperty("expectedProduct", SAMPLE_PRODUCT);

        SearchResultsPage results = new HomePage(driver()).open(config().baseUrl()).searchFor(query);
        List<String> visibleTitles = results.visibleProductTitles();

        assertTrue(visibleTitles.contains(expectedProduct),
                "Expected search results for '%s' to include '%s'. Visible product titles: %s"
                        .formatted(query, expectedProduct, visibleTitles));
    }

    @Test(groups = "smoke", description = "TC-CAT-006: Open details for a known listing item")
    public void selectingProductOpensMatchingPurchasableDetails() {
        SearchResultsPage results = new HomePage(driver()).open(config().baseUrl()).searchFor(SAMPLE_QUERY);
        String productUrl = results.productUrl(SAMPLE_PRODUCT);
        ProductDetailsPage details = results.openProduct(SAMPLE_PRODUCT);

        assertEquals(details.title(), SAMPLE_PRODUCT, "The details title should match the selected listing.");
        assertEquals(details.currentUrl(), productUrl, "The details URL should match the listing link.");
        assertEquals(details.price(), "$799.00", "Unexpected price for the local iPhone fixture.");
        assertEquals(details.stockStatus(), "In stock", "The local iPhone fixture should be in stock.");
        assertTrue(details.canAddToCart(), "The Add to cart button should be visible and enabled.");
    }
}
