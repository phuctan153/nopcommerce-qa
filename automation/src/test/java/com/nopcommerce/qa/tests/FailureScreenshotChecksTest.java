package com.nopcommerce.qa.tests;

import com.nopcommerce.qa.support.FailureScreenshot;
import java.lang.reflect.Proxy;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.Test;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

/** Opt-in infrastructure checks; not part of the storefront smoke case count. */
public class FailureScreenshotChecksTest {

    @Test
    public void missingBrowserPreservesTheOriginalFailure() {
        verifyUnavailable(null);
    }

    @Test
    public void maskingFailurePreventsScreenshotCapture() {
        verifyUnavailable(browser(false, false));
    }

    @Test
    public void closedBrowserPreservesTheOriginalFailure() {
        verifyUnavailable(browser(true, false));
    }

    @Test
    public void screenshotFailurePreservesTheOriginalFailure() {
        verifyUnavailable(browser(false, true));
    }

    private void verifyUnavailable(WebDriver driver) {
        ITestResult result = Reporter.getCurrentTestResult();
        int originalStatus = result.getStatus();
        Throwable originalThrowable = result.getThrowable();
        try {
            result.setStatus(ITestResult.FAILURE);
            Throwable failure = new AssertionError("Synthetic original failure");
            result.setThrowable(failure);
            FailureScreenshot.capture(driver, result);
            assertEquals(result.getStatus(), ITestResult.FAILURE);
            assertEquals(result.getThrowable(), failure);
            assertNull(result.getAttribute("failureScreenshot"));
        } finally {
            result.setStatus(originalStatus);
            result.setThrowable(originalThrowable);
        }
    }

    private WebDriver browser(boolean scriptThrows, boolean screenshotThrows) {
        return (WebDriver) Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class<?>[]{WebDriver.class, JavascriptExecutor.class, TakesScreenshot.class},
                (proxy, method, args) -> {
                    if (method.getName().equals("executeScript")) {
                        if (scriptThrows) {
                            throw new IllegalStateException("Synthetic closed browser");
                        }
                        return screenshotThrows;
                    }
                    if (method.getName().equals("getScreenshotAs")) {
                        if (screenshotThrows) {
                            throw new IllegalStateException("Synthetic screenshot error");
                        }
                        throw new AssertionError("Screenshot must not run before masking succeeds.");
                    }
                    throw new UnsupportedOperationException(method.getName());
                });
    }
}
