package com.nopcommerce.qa.support;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;

final class DriverFactory {

    private DriverFactory() {
    }

    static WebDriver create(TestConfig config) {
        return switch (config.browser()) {
            case CHROME -> {
                ChromeOptions options = new ChromeOptions();
                if (config.headless()) {
                    options.addArguments("--headless=new", "--window-size=1440,900");
                }
                yield new ChromeDriver(options);
            }
            case EDGE -> {
                EdgeOptions options = new EdgeOptions();
                if (config.headless()) {
                    options.addArguments("--headless=new", "--window-size=1440,900");
                }
                yield new EdgeDriver(options);
            }
        };
    }
}
