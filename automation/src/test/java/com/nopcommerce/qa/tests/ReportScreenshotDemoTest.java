package com.nopcommerce.qa.tests;

import com.nopcommerce.qa.pages.HomePage;
import com.nopcommerce.qa.pages.ProductDetailsPage;
import com.nopcommerce.qa.pages.SearchResultsPage;
import com.nopcommerce.qa.support.BaseTest;
import org.testng.SkipException;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

/** Explicitly selected evidence demonstrations; never included in the default smoke suite. */
public class ReportScreenshotDemoTest extends BaseTest {

    @Test(groups = "evidence-demo", description = "DEMO-001: Intentional search assertion failure")
    public void intentionalSearchFailureShowsTheResultsScreenshot() {
        requireDemoMode();
        SearchResultsPage results = new HomePage(driver()).open(config().baseUrl()).searchFor("iphone");
        assertTrue(results.visibleProductTitles().contains("QA intentional missing product"),
                "Intentional evidence demonstration: an invented product must not match the catalog.");
    }

    @Test(groups = "evidence-demo", description = "DEMO-002: Intentional product price assertion failure")
    public void intentionalPriceFailureShowsTheProductScreenshot() {
        requireDemoMode();
        ProductDetailsPage details = new HomePage(driver()).open(config().baseUrl())
                .searchFor("iphone").openProduct("Apple iPhone 16 128GB");
        assertEquals(details.price(), "$0.00",
                "Intentional evidence demonstration: the sample iPhone is not free.");
    }

    private void requireDemoMode() {
        if (!Boolean.getBoolean("screenshotDemo")) {
            throw new SkipException("Select screenshotDemo=true explicitly to run intentional failures.");
        }
    }
}
