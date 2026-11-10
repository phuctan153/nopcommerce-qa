# Automation

Use JDK 17 or later and Maven 3.9 or later. Install Chrome or Microsoft Edge when running browser tests. Selenium Manager resolves the matching driver when a local driver is unavailable. The first run can require network access for Maven dependencies or browser drivers. [Selenium Manager documentation](https://www.selenium.dev/documentation/selenium_manager/)

## Run

From the repository root:

```powershell
mvn -f automation/pom.xml test
```

Maven Surefire discovers TestNG classes named `*Test.java`. The full run includes the configuration checks, browser search, and authentication smoke tests. Authentication requires the local storefront and shopper fixtures described below. Find the test counts, failures, and stack traces in `automation/target/surefire-reports/`.

Run the configuration checks without opening a browser:

```powershell
mvn -f automation/pom.xml "-Dtest=TestConfigTest" test
```

Run the search smoke test independently:

```powershell
mvn -f automation/pom.xml "-Dtest=ProductSearchTest" "-Dheadless=true" test
```

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

## Source layout

| Package | Responsibility |
| --- | --- |
| `com.nopcommerce.qa.pages` | Storefront page objects, locators, and explicit waits |
| `com.nopcommerce.qa.support` | Configuration and one WebDriver session per TestNG test method, with teardown after a failure or skip |
| `com.nopcommerce.qa.tests` | TestNG tests, scenario data, and assertions |

Extend `BaseTest` in UI test classes and pass `driver()` into page objects. Map additional checks to the [test case workbook](../docs/test-cases/Test_Cases.xlsx). Keep checkout automation within the [pre-submit boundary](../docs/test-execution/EXECUTION_GUIDE.md).
