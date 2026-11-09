package com.nopcommerce.qa.support;

import java.net.URI;
import java.util.Locale;
import java.util.Properties;

public record TestConfig(URI baseUrl, Browser browser, boolean headless) {

    public enum Browser {
        CHROME,
        EDGE
    }

    public static TestConfig from(Properties properties) {
        return new TestConfig(
                readBaseUrl(properties.getProperty("baseUrl", "https://demo.nopcommerce.com/")),
                readBrowser(properties.getProperty("browser", "chrome")),
                readHeadless(properties.getProperty("headless", "false")));
    }

    private static URI readBaseUrl(String value) {
        String message = "baseUrl must be an absolute HTTP or HTTPS URL without credentials.";
        URI url;
        try {
            url = URI.create(value.strip());
        } catch (IllegalArgumentException exception) {
            throw new IllegalArgumentException(message);
        }

        if (!("http".equalsIgnoreCase(url.getScheme()) || "https".equalsIgnoreCase(url.getScheme()))
                || url.getHost() == null || url.getUserInfo() != null) {
            throw new IllegalArgumentException(message);
        }
        return url;
    }

    private static Browser readBrowser(String value) {
        return switch (value.strip().toLowerCase(Locale.ROOT)) {
            case "chrome" -> Browser.CHROME;
            case "edge" -> Browser.EDGE;
            default -> throw new IllegalArgumentException("browser must be chrome or edge.");
        };
    }

    private static boolean readHeadless(String value) {
        return switch (value.strip().toLowerCase(Locale.ROOT)) {
            case "true" -> true;
            case "false" -> false;
            default -> throw new IllegalArgumentException("headless must be true or false.");
        };
    }
}
