package com.nopcommerce.qa.tests;

import com.nopcommerce.qa.pages.HomePage;
import com.nopcommerce.qa.pages.SearchResultsPage;
import com.nopcommerce.qa.support.BaseTest;
import java.util.List;
import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

public class ProductSearchTest extends BaseTest {

    @Test(groups = "smoke", description = "TC-CAT-001: Find a current product by a valid query")
    public void validQueryReturnsExpectedProduct() {
        String query = System.getProperty("searchQuery", "iphone");
        String expectedProduct = System.getProperty("expectedProduct", "Apple iPhone 16 128GB");

        SearchResultsPage results = new HomePage(driver()).open(config().baseUrl()).searchFor(query);
        List<String> visibleTitles = results.visibleProductTitles();

        assertTrue(visibleTitles.contains(expectedProduct),
                "Expected search results for '%s' to include '%s'. Visible product titles: %s"
                        .formatted(query, expectedProduct, visibleTitles));
    }
}
