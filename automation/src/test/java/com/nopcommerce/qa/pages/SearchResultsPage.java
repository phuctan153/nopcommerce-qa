package com.nopcommerce.qa.pages;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class SearchResultsPage {

    private static final By RESULTS = By.cssSelector(".search-results .products-wrapper");
    private static final By PRODUCT_TITLE = By.cssSelector(".product-title a");
    private final WebDriver driver;

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
    }

    public List<String> visibleProductTitles() {
        return visibleProductLinks().stream().map(WebElement::getText).toList();
    }

    public ProductDetailsPage openProduct(String title) {
        WebElement productLink = visibleProductLinks().stream()
                .filter(link -> link.getText().equals(title))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Product was not visible in search results: " + title));
        productLink.click();
        return new ProductDetailsPage(driver);
    }

    private List<WebElement> visibleProductLinks() {
        WebElement results = new WebDriverWait(driver, Duration.ofSeconds(10))
                .withMessage(() -> "Search results did not become visible. Page title: " + driver.getTitle())
                .until(ExpectedConditions.visibilityOfElementLocated(RESULTS));

        return results.findElements(PRODUCT_TITLE).stream()
                .filter(WebElement::isDisplayed)
                .toList();
    }
}
