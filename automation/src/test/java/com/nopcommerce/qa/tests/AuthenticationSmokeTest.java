package com.nopcommerce.qa.tests;

import com.nopcommerce.qa.pages.CustomerInfoPage;
import com.nopcommerce.qa.pages.HomePage;
import com.nopcommerce.qa.pages.LoginPage;
import com.nopcommerce.qa.pages.StorefrontHeader;
import com.nopcommerce.qa.support.BaseTest;
import java.net.URI;
import org.testng.SkipException;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

public class AuthenticationSmokeTest extends BaseTest {

    @Test(groups = "smoke", description = "TC-AUTH-001: Sign in with valid disposable credentials")
    public void validCredentialsOpenTheCorrectAccount() {
        String email = requiredEnvironmentValue("NOP_QA_LOGIN_EMAIL");
        String password = requiredEnvironmentValue("NOP_QA_LOGIN_PASSWORD");
        StorefrontHeader header = signInAs(email, password);

        assertTrue(header.isSignedIn(), "Expected account and sign-out links after signing in.");
        assertTrue(email.equalsIgnoreCase(header.openAccount().email()),
                "Expected the account page to belong to the signed-in QA shopper.");
    }

    @Test(groups = "smoke", description = "TC-AUTH-004: End the session and revisit a protected page")
    public void signingOutProtectsThePreviouslyOpenedAccountPage() {
        StorefrontHeader header = signInAs(
                requiredEnvironmentValue("NOP_QA_LOGOUT_EMAIL"),
                requiredEnvironmentValue("NOP_QA_LOGOUT_PASSWORD"));
        CustomerInfoPage account = header.openAccount();
        URI protectedUrl = account.url();

        assertTrue(header.signOut().header().isGuest(), "Expected guest navigation after signing out.");

        LoginPage signIn = new LoginPage(driver()).openFromProtectedUrl(protectedUrl);

        assertTrue(signIn.isSignInFormVisible(), "Expected sign-in to be required after revisiting the account URL.");
        assertFalse(account.isVisible(), "Protected account content was visible after signing out.");
    }

    private StorefrontHeader signInAs(String email, String password) {
        URI baseUrl = config().baseUrl();
        String host = baseUrl.getHost();
        if (!"127.0.0.1".equals(host) && !"localhost".equalsIgnoreCase(host)) {
            throw new SkipException("Authentication smoke tests require a loopback nopCommerce QA storefront.");
        }
        return new HomePage(driver()).open(baseUrl).header().openSignIn().signIn(email, password).header();
    }

    private static String requiredEnvironmentValue(String name) {
        String value = System.getenv(name);
        if (value == null || value.isBlank()) {
            throw new SkipException("Missing QA fixture environment variable: " + name);
        }
        return value;
    }
}
