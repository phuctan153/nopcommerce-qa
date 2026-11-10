# Automation

Use JDK 17 or later and Maven 3.9 or later. Install Chrome or Microsoft Edge when running browser tests. Selenium Manager resolves the matching driver when a local driver is unavailable. The first run can require network access for Maven dependencies or browser drivers. [Selenium Manager documentation](https://www.selenium.dev/documentation/selenium_manager/)

## Run

### Run the full suite locally

Start Docker Desktop with the Linux engine and open PowerShell at the repository root. For a first run, complete the [local storefront setup](#local-nopcommerce-qa-environment) and create the two [shopper fixtures](#qa-0092-authentication-smoke-tests). Store their values in the Git-ignored `automation/.env`.

```powershell
docker compose --env-file automation/.env -f automation/compose.qa.yaml up -d --wait
if ($LASTEXITCODE -ne 0) { throw 'The QA storefront failed to start.' }

$fixtures = @{}
foreach ($line in Get-Content -LiteralPath automation/.env) {
    if ($line -match '^(NOP_QA_(?:LOGIN|LOGOUT)_(?:EMAIL|PASSWORD))=(.+)$') {
        $fixtures[$Matches[1]] = $Matches[2]
    }
}
foreach ($name in 'NOP_QA_LOGIN_EMAIL', 'NOP_QA_LOGIN_PASSWORD', 'NOP_QA_LOGOUT_EMAIL', 'NOP_QA_LOGOUT_PASSWORD') {
    if (-not $fixtures[$name]) { throw "Missing $name in automation/.env" }
    [Environment]::SetEnvironmentVariable($name, $fixtures[$name], 'Process')
}

mvn -f automation/pom.xml "-DbaseUrl=http://127.0.0.1:8080/" "-Dbrowser=chrome" "-Dheadless=false" test
```

Use `"-Dheadless=true"` to run with the browser window hidden. Each UI test opens and closes its own browser session.

Maven Surefire discovers TestNG classes named `*Test.java`. The full suite contains three configuration checks and six automated browser cases:

| Test class | Coverage |
| --- | --- |
| `TestConfigTest` | Default settings, overrides, and invalid headless flag |
| `ProductSearchTest` | `TC-CAT-001` product search |
| `AuthenticationSmokeTest` | `TC-AUTH-001` login and `TC-AUTH-004` logout |
| `ShoppingCartSmokeTest` | `TC-CART-001` add, `TC-CART-003` update quantity, and `TC-CART-006` remove |

Check the Maven summary for nine tests with zero failures, errors, or skips. Open `automation/target/surefire-reports/index.html` for per-test results or inspect `testng-results.xml` in that directory. For manual browser cases in the workbook, follow the [manual execution guide](../docs/test-execution/EXECUTION_GUIDE.md).

### Run selected checks

Run the configuration checks without opening a browser:

```powershell
mvn -f automation/pom.xml "-Dtest=TestConfigTest" test
```

Run the search smoke test independently:

```powershell
mvn -f automation/pom.xml "-Dtest=ProductSearchTest" "-DbaseUrl=http://127.0.0.1:8080/" "-Dheadless=true" test
```

If Maven reports a `PKIX path building failed` error on Windows and the required certificate is trusted by Windows, run Maven with the Windows root store in a temporary PowerShell session:

```powershell
$env:MAVEN_OPTS = "$env:MAVEN_OPTS -Djavax.net.ssl.trustStoreType=Windows-ROOT -Djavax.net.ssl.trustStore=NONE"
mvn -f automation/pom.xml "-DbaseUrl=http://127.0.0.1:8080/" "-Dheadless=true" test
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

## QA-009.1 product search

`ProductSearchTest` covers the positive search case `TC-CAT-001` using `TD-CAT-001`. The [manual execution record](../docs/test-execution/Test_Execution.xlsx) and [screenshot](../docs/test-execution/evidence/Product/TC-CAT-001_20261109_01.png) show that `iphone` returned `Apple iPhone 16 128GB`.

The smoke test checks for the configured product title among the visible results on the first page. It makes no assertion about result order, count, or price. If the catalog changes, verify a current published product and choose a query that returns it on the first page. Override both properties together, for example:

```powershell
mvn -f automation/pom.xml "-Dtest=ProductSearchTest" "-Dheadless=true" "-DsearchQuery=iphone" "-DexpectedProduct=Apple iPhone 16 128GB" test
```

On 2026-11-10, Chrome 156.0.8078.80 launched in headless mode on Windows 11. The public demo displayed a security verification page titled `Just a moment...`. The test timed out waiting for the storefront search field and Maven recorded a failure. A separate run against a temporary fixture verified form submission and product matching. The same smoke test later passed against the installed nopCommerce QA instance below. Search on the public demo remains unverified by Selenium.

## Local nopCommerce QA environment

Start Docker Desktop with the Linux engine. From the repository root, create the ignored `automation/.env` on first setup. Keep the same database password when reusing an existing volume.

```powershell
if (-not (Test-Path automation/.env)) {
    $qaDbPassword = [guid]::NewGuid().ToString('N')
    Set-Content -LiteralPath automation/.env -Value "NOP_QA_DB_PASSWORD=$qaDbPassword"
}
docker compose --env-file automation/.env -f automation/compose.qa.yaml up -d --wait
```

Open `http://127.0.0.1:8080/`. On the first install, create a disposable admin account, enable sample data, and clear the newsletter option. Select PostgreSQL and use server `db`, database `nopcommerce`, user `nopcommerce`, and the database password from `automation/.env`. The Compose database already exists. The [database init script](qa-postgres-init.sql) enables `citext` when PostgreSQL initializes a fresh volume. Keep credentials in the ignored file or a local password manager. [nopCommerce installation guide](https://docs.nopcommerce.com/en/installation-and-upgrading/installing-nopcommerce/installing-local.html)

If the installer completes and the browser still returns to `/install`, restart the storefront so it reloads its saved configuration:

```powershell
docker compose --env-file automation/.env -f automation/compose.qa.yaml restart storefront
```

The [Compose file](compose.qa.yaml) publishes the storefront on loopback. Named volumes retain the database, application configuration, and product images. Stop the services with `docker compose --env-file automation/.env -f automation/compose.qa.yaml stop`. Run `up -d --wait` again to resume the same instance.

On 2026-11-10, the nopCommerce 4.90.7 sample catalog returned `Apple iPhone 16 128GB` for `iphone` after a container restart. The independent smoke test passed and the full suite passed all four tests.

```powershell
mvn -f automation/pom.xml "-DbaseUrl=http://127.0.0.1:8080/" "-Dbrowser=chrome" "-Dheadless=true" test
```

Add `"-Dtest=ProductSearchTest"` to run only the browser smoke test.

## QA-009.2 authentication smoke tests

`AuthenticationSmokeTest` covers `TC-AUTH-001` and `TC-AUTH-004` using `TD-AUTH-001`. Each case opens a fresh browser session. The login case checks that the account page belongs to the signed-in shopper. The logout case revisits the previously opened account URL and checks that the sign-in form appears and account content is hidden.

On the local QA storefront, create two separate registered shopper accounts through the registration page or the admin customer list. Keep the admin account for environment setup. Add the shopper values to the Git-ignored `automation/.env`:

| Test case | Environment variables |
| --- | --- |
| `TC-AUTH-001` | `NOP_QA_LOGIN_EMAIL`, `NOP_QA_LOGIN_PASSWORD` |
| `TC-AUTH-004` | `NOP_QA_LOGOUT_EMAIL`, `NOP_QA_LOGOUT_PASSWORD` |

Load only those variables into the current PowerShell session and run the authentication tests:

```powershell
foreach ($line in Get-Content -LiteralPath automation/.env) {
    if ($line -match '^(NOP_QA_(?:LOGIN|LOGOUT)_(?:EMAIL|PASSWORD))=(.+)$') {
        [Environment]::SetEnvironmentVariable($Matches[1], $Matches[2], 'Process')
    }
}
mvn -f automation/pom.xml "-Dtest=AuthenticationSmokeTest" "-DbaseUrl=http://127.0.0.1:8080/" "-Dheadless=true" test
```

Omit `-Dtest` to run the full suite with those fixtures. Authentication reports a skip when its fixture variables are missing or `baseUrl` does not use `127.0.0.1` or `localhost`. A configured fixture that fails to sign in produces a test failure.

On 2026-11-10, each authentication test passed independently on local nopCommerce 4.90.7 with Chrome 156.0.8078.80 headless on Windows 11. The full suite passed six tests with zero failures, errors, or skips.

## QA-009.3 add-to-cart smoke test

`ShoppingCartSmokeTest` covers `TC-CART-001` using `TD-CART-001` in a fresh guest session. The local sample fixture is `HTC smartphone`, a purchasable product with no required options. The test opens its details page, adds two units, and checks that the cart contains one line for that product with quantity two.

```powershell
mvn -f automation/pom.xml "-Dtest=ShoppingCartSmokeTest#addingProductShowsTheRequestedQuantityInCart" "-DbaseUrl=http://127.0.0.1:8080/" "-Dheadless=true" test
```

To use another catalog fixture, override `cartSearchQuery` and `cartProduct` together. Use a published product with no required options that supports quantity two.

On 2026-11-10, the independent cart test and all seven tests in the full suite passed on local nopCommerce 4.90.7 with headless Chrome on Windows 11. Both runs reported zero failures, errors, and skips.

## QA-009.4 cart quantity update smoke test

`ShoppingCartSmokeTest` covers `TC-CART-003` using `TD-CART-001`. See the [test case workbook](../docs/test-cases/Test_Cases.xlsx) and the [manual execution record](../docs/test-execution/Test_Execution.xlsx) for `EXE-CART-003`.

In a fresh guest session, the test adds one `HTC smartphone` and checks its initial quantity and line total. It changes the quantity to two, leaves the field to submit the update, returns to the storefront home page, and reopens the cart from the header. It checks that the same product still appears on a single line with quantity two and that the line total equals the displayed unit price multiplied by two.

```powershell
mvn -f automation/pom.xml "-Dtest=ShoppingCartSmokeTest#updatedQuantityAndLineTotalPersistWhenCartIsReopened" "-DbaseUrl=http://127.0.0.1:8080/" "-Dbrowser=chrome" "-Dheadless=true" test
```

For a fixture override, use a product that supports quantities one and two with an editable quantity field and USD prices where the displayed unit price times the quantity equals the line total.

On 2026-11-10, the independent run passed one test and the full suite passed eight tests on local nopCommerce 4.90.7 with headless Chrome 156.0.8078.80 on Windows 11. Both runs reported zero failures, errors, and skips. The full run included both authentication cases with the local shopper fixtures.

## QA-009.5 cart removal smoke test

`ShoppingCartSmokeTest` covers `TC-CART-006` using `TD-CART-004`. The test adds one `HTC smartphone` and one `Apple iPhone 16 128GB` to a fresh guest cart. It removes the HTC and verifies that the iPhone line retains its product name, quantity, unit price, and line total. It then removes the iPhone and verifies the empty cart message and absence of product lines. The removal case uses these two sample products independently of the fixture overrides for the other cart cases.

```powershell
mvn -f automation/pom.xml "-Dtest=ShoppingCartSmokeTest#removingProductsPreservesTheOtherLineUntilCartIsEmpty" "-DbaseUrl=http://127.0.0.1:8080/" "-Dheadless=true" test
```

Use `"-Dtest=ShoppingCartSmokeTest"` to run all three cart cases.

On 2026-11-10, the independent run passed one test and the full suite passed nine tests on local nopCommerce 4.90.7 with headless Chrome on Windows 11. Both runs reported zero failures, errors, and skips.

## Source layout

| Package | Responsibility |
| --- | --- |
| `com.nopcommerce.qa.pages` | Storefront page objects, locators, and explicit waits |
| `com.nopcommerce.qa.support` | Configuration and one WebDriver session per TestNG test method, with teardown after a failure or skip |
| `com.nopcommerce.qa.tests` | TestNG tests, scenario data, and assertions |

Extend `BaseTest` in UI test classes and pass `driver()` into page objects. Map additional checks to the [test case workbook](../docs/test-cases/Test_Cases.xlsx). Keep checkout automation within the [pre-submit boundary](../docs/test-execution/EXECUTION_GUIDE.md).
