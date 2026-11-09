package com.nopcommerce.qa.tests;

import com.nopcommerce.qa.support.TestConfig;
import java.net.URI;
import java.util.Properties;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertThrows;
import static org.testng.Assert.assertTrue;

public class TestConfigTest {

    @Test
    public void usesDocumentedDefaultsWhenPropertiesAreOmitted() {
        TestConfig config = TestConfig.from(new Properties());

        assertEquals(config.baseUrl(), URI.create("https://demo.nopcommerce.com/"));
        assertEquals(config.browser(), TestConfig.Browser.CHROME);
        assertFalse(config.headless());
    }

    @Test
    public void acceptsBrowserAndStorefrontOverrides() {
        Properties properties = new Properties();
        properties.setProperty("baseUrl", "http://localhost:8080/");
        properties.setProperty("browser", "Edge");
        properties.setProperty("headless", "true");

        TestConfig config = TestConfig.from(properties);

        assertEquals(config.baseUrl(), URI.create("http://localhost:8080/"));
        assertEquals(config.browser(), TestConfig.Browser.EDGE);
        assertTrue(config.headless());
    }

    @Test
    public void rejectsAnInvalidHeadlessFlag() {
        Properties properties = new Properties();
        properties.setProperty("headless", "tru");

        assertThrows(IllegalArgumentException.class, () -> TestConfig.from(properties));
    }
}
