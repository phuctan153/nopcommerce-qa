package com.nopcommerce.qa.pages;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class CategoryPage {

    private static final By TITLE = By.cssSelector(".category-page .page-title h1");
    private static final By SUBCATEGORIES = By.cssSelector(".category-page .sub-category-item h2 a");
    private static final By PRODUCT_TITLES = By.cssSelector(".category-page .product-title a");
    private static final By BREADCRUMB = By.cssSelector(".breadcrumb li a, .breadcrumb li .current-item");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public CategoryPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public String title() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(TITLE)).getText();
    }

    public List<String> visibleSubcategoryNames() {
        return visibleTexts(SUBCATEGORIES);
    }

    public CategoryPage openSubcategory(String name) {
        wait.withMessage("The subcategory link was unavailable for " + name)
                .until(browser -> browser.findElements(SUBCATEGORIES).stream()
                        .filter(link -> link.isDisplayed() && link.isEnabled() && link.getText().equals(name))
                        .findFirst()
                        .orElse(null))
                .click();
        return new CategoryPage(driver);
    }

    public List<String> visibleProductTitles() {
        return visibleTexts(PRODUCT_TITLES);
    }

    public List<String> breadcrumb() {
        return visibleTexts(BREADCRUMB);
    }

    private List<String> visibleTexts(By locator) {
        return driver.findElements(locator).stream()
                .filter(WebElement::isDisplayed)
                .map(WebElement::getText)
                .toList();
    }
}
