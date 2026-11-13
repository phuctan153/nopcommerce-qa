package com.nopcommerce.qa.support;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;

public final class FailureScreenshot {

    private static final String REDACT = """
            const style = document.createElement('style');
            style.textContent = `
                input, textarea, select, [contenteditable], iframe, canvas,
                .header-links, .customer-info-page, .login-page, .registration-page,
                .checkout-page, .order-details-page, .order-list-page,
                .address-list-page, .message-error, .validation-summary-errors,
                [data-qa-evidence-private] { visibility: hidden !important; }
            `;
            document.documentElement.appendChild(style);
            const secrets = arguments[0];
            const walker = document.createTreeWalker(document.body, NodeFilter.SHOW_TEXT);
            const privateElements = [];
            while (walker.nextNode()) {
                const text = walker.currentNode.textContent;
                if (secrets.some(value => text.includes(value))
                        || /[\\w.+-]+@[\\w.-]+\\.[a-z]{2,}/i.test(text)) {
                    privateElements.push(walker.currentNode.parentElement);
                }
            }
            privateElements.forEach(element => element.setAttribute('data-qa-evidence-private', ''));
            // Fail closed when styling is blocked: never save an unredacted fallback.
            return [...document.querySelectorAll(
                'input, textarea, select, [contenteditable], iframe, canvas, '
                + '.header-links, .customer-info-page, .login-page, .registration-page, '
                + '.checkout-page, .order-details-page, .order-list-page, .address-list-page, '
                + '.message-error, .validation-summary-errors, [data-qa-evidence-private]'
            )].every(element => getComputedStyle(element).visibility === 'hidden');
            """;

    private FailureScreenshot() {
    }

    public static void capture(WebDriver driver, ITestResult result) {
        if (!(driver instanceof TakesScreenshot screenshot)
                || !(driver instanceof JavascriptExecutor javascript)) {
            report(result, "Failure screenshot unavailable: browser is not initialized or supported.");
            return;
        }
        try {
            List<String> secrets = System.getenv().entrySet().stream()
                    .filter(entry -> entry.getKey().startsWith("NOP_QA_"))
                    .map(entry -> entry.getValue()).filter(value -> !value.isBlank()).toList();
            if (!Boolean.TRUE.equals(javascript.executeScript(REDACT, secrets))) {
                report(result, "Failure screenshot omitted: privacy masking could not be verified.");
                return;
            }
            byte[] image = screenshot.getScreenshotAs(OutputType.BYTES);
            String name = result.getTestClass().getRealClass().getSimpleName() + "-"
                    + result.getMethod().getMethodName();
            name = name.replaceAll("[^A-Za-z0-9_-]", "_") + "-" + UUID.randomUUID() + ".png";
            Path directory = Path.of(result.getTestContext().getOutputDirectory())
                    .getParent().resolve("screenshots");
            Files.createDirectories(directory);
            Files.write(directory.resolve(name), image);
            result.setAttribute("failureScreenshot", "screenshots/" + name);
            report(result, "Failure screenshot: screenshots/" + name);
        } catch (IOException | RuntimeException exception) {
            // Browser errors can contain credentials or page content. Do not log their messages.
            report(result, "Failure screenshot unavailable: capture or privacy masking failed."
                    + " Original test result preserved.");
        }
    }

    private static void report(ITestResult result, String message) {
        // @AfterMethod output otherwise belongs to the configuration method in TestNG reports.
        ITestResult previous = Reporter.getCurrentTestResult();
        try {
            Reporter.setCurrentTestResult(result);
            Reporter.log(message, true);
        } finally {
            Reporter.setCurrentTestResult(previous);
        }
    }
}
