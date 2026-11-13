package com.nopcommerce.qa.tests;

import com.nopcommerce.qa.support.BaseTest;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import org.testng.annotations.Test;

import static org.testng.Assert.fail;

/** Intentionally fails only when selected explicitly with -Dtest=FailureScreenshotProbeTest. */
public class FailureScreenshotProbeTest extends BaseTest {

    @Test
    public void controlledFailureProducesMaskedEvidenceBeforeTeardown() {
        String html = """
                <html><body style="background:white;font:24px sans-serif">
                <h1>QA-011.2 controlled failure</h1>
                <p>Public diagnostic content remains visible.</p>
                <input value="synthetic-private-input">
                <input type="password" value="synthetic-private-password">
                <p>synthetic-shopper@example.com</p>
                <div class="customer-info-page">Synthetic private account details</div>
                <div class="checkout-page">Synthetic private billing address</div>
                </body></html>
                """;
        driver().get("data:text/html;charset=utf-8," + URLEncoder.encode(html, StandardCharsets.UTF_8)
                .replace("+", "%20"));
        fail("QA-011.2 intentional evidence probe; expected failure, not a storefront defect.");
    }
}
