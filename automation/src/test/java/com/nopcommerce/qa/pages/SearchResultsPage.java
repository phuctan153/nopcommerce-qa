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

    private static final By RESULTS = By.cssSelector(".search-results");
    private static final By NO_RESULTS = By.cssSelector(".search-results .no-result");
    private static final By PRODUCT_TITLE = By.cssSelector(".product-title a");
    private final WebDriver driver;
    private final WebDriverWait wait;

    public SearchResultsPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public List<String> visibleProductTitles() {
        return visibleProductLinks().stream().map(WebElement::getText).toList();
    }

    public String noResultsMessage() {
        return wait.withMessage("The no-results message did not appear.")
                .until(ExpectedConditions.visibilityOfElementLocated(NO_RESULTS)).getText();
    }

    public ProductDetailsPage openProduct(String title) {
        productLink(title).click();
        return new ProductDetailsPage(driver);
    }

    public String productUrl(String title) {
        return productLink(title).getDomProperty("href");
    }

    private WebElement productLink(String title) {
        return visibleProductLinks().stream()
                .filter(link -> link.getText().equals(title))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("Product was not visible in search results: " + title));
    }

    private List<WebElement> visibleProductLinks() {
        WebElement results = wait.withMessage(() -> "Search results did not become visible. Page title: " + driver.getTitle())
                .until(ExpectedConditions.visibilityOfElementLocated(RESULTS));

        return results.findElements(PRODUCT_TITLE).stream()
                .filter(WebElement::isDisplayed)
                .toList();
    }
}
