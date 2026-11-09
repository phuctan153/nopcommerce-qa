# Automation

Use JDK 17 or later and Maven 3.9 or later. Install Chrome or Microsoft Edge when running browser tests. Selenium Manager resolves the matching driver when a local driver is unavailable. The first run can require network access for Maven dependencies or browser drivers. [Selenium Manager documentation](https://www.selenium.dev/documentation/selenium_manager/)

## Run

From the repository root:

```powershell
mvn -f automation/pom.xml test
```

Maven Surefire discovers TestNG classes named `*Test.java`. QA-008 includes configuration checks that run without a browser. Find the test counts, failures, and stack traces in `automation/target/surefire-reports/`.

If Maven reports a `PKIX path building failed` error on Windows and the required certificate is trusted by Windows, run Maven with the Windows root store in a temporary PowerShell session:

```powershell
$env:MAVEN_OPTS = "$env:MAVEN_OPTS -Djavax.net.ssl.trustStoreType=Windows-ROOT -Djavax.net.ssl.trustStore=NONE"
mvn -f automation/pom.xml test
```

## Browser configuration

Tests extending `BaseTest` read these JVM properties:

| Property | Default | Accepted value |
| --- | --- | --- |
| `baseUrl` | `https://demo.nopcommerce.com/` | Absolute HTTP or HTTPS storefront URL without embedded credentials |
| `browser` | `chrome` | `chrome` or `edge` |
| `headless` | `false` | `true` or `false` |

Example for a suite that contains browser tests:

```powershell
mvn -f automation/pom.xml "-Dbrowser=edge" "-Dheadless=true" test
```

Keep any test credentials in local environment variables or CI secrets.

## Source layout

| Package | Responsibility |
| --- | --- |
| `com.nopcommerce.qa.pages` | Storefront page objects, locators, and explicit waits |
| `com.nopcommerce.qa.support` | Configuration and one WebDriver session per TestNG test method, with teardown after a failure or skip |
| `com.nopcommerce.qa.tests` | TestNG tests, scenario data, and assertions |

For QA-009, extend `BaseTest` in UI test classes and pass `driver()` into page objects. Start with a stable public storefront case from the [test case workbook](../docs/test-cases/Test_Cases.xlsx). Keep checkout automation within the [pre-submit boundary](../docs/test-execution/EXECUTION_GUIDE.md).
