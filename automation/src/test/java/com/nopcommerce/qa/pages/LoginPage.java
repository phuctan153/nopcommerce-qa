package com.nopcommerce.qa.pages;

import java.net.URI;
import java.time.Duration;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class LoginPage {

    private static final By EMAIL = By.cssSelector(".login-page #Email");
    private static final By PASSWORD = By.cssSelector(".login-page #Password");
    private static final By SUBMIT = By.cssSelector(".login-page button.login-button");

    private final WebDriver driver;
    private final WebDriverWait wait;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public HomePage signIn(String email, String password) {
        WebElement emailField = wait.until(ExpectedConditions.elementToBeClickable(EMAIL));
        WebElement passwordField = wait.until(ExpectedConditions.elementToBeClickable(PASSWORD));
        try {
            emailField.sendKeys(email);
            passwordField.sendKeys(password);
        } catch (WebDriverException exception) {
            // WebDriver command details can contain the values entered in the form.
            throw new IllegalStateException("Could not enter QA credentials: " + exception.getClass().getSimpleName());
        }
        wait.until(ExpectedConditions.elementToBeClickable(SUBMIT)).click();
        return new HomePage(driver);
    }

    public LoginPage openFromProtectedUrl(URI url) {
        driver.get(url.toString());
        wait.withMessage("The sign-in form did not appear after revisiting the protected page.")
                .until(ExpectedConditions.visibilityOfElementLocated(SUBMIT));
        return this;
    }

    public boolean isSignInFormVisible() {
        return driver.findElements(EMAIL).stream().anyMatch(WebElement::isDisplayed)
                && driver.findElements(PASSWORD).stream().anyMatch(WebElement::isDisplayed);
    }
}
