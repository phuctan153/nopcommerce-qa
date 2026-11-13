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

Maven Surefire discovers TestNG classes named `*Test.java`. The full suite contains three configuration checks and eleven automated browser cases.

| Test class | Coverage |
| --- | --- |
| `TestConfigTest` | Default settings, overrides, and invalid headless flag |
| `ProductSearchTest` | `TC-CAT-001` product search, `TC-CAT-002` no results, and `TC-CAT-006` product details |
| `CategoryNavigationTest` | `TC-CAT-004` top-level category and `TC-CAT-005` subcategory |
| `AuthenticationSmokeTest` | `TC-AUTH-001` login and `TC-AUTH-004` logout |
| `ShoppingCartSmokeTest` | `TC-CART-001` add, `TC-CART-003` update quantity, `TC-CART-006` remove, and `TC-CART-007` reconcile subtotal |

Check the Maven summary for fourteen tests with zero failures, errors, or skips. Open `automation/target/surefire-reports/index.html` for per-test results or inspect `testng-results.xml` in that directory. For manual browser cases in the workbook, follow the [manual execution guide](../docs/test-execution/EXECUTION_GUIDE.md).

### Run in GitHub Actions

Open the [live report dashboard](https://phuctan153.github.io/nopcommerce-qa/) to view the latest published main-branch run. It includes per-module totals, case results, durations, and filters. The page links to the workflow run that produced the data.

The [Storefront smoke suite workflow](../.github/workflows/smoke.yml) runs on pushes to `main`, pull requests targeting `main`, and manual runs. It uses Ubuntu 24.04, Temurin JDK 17, Docker Compose, and headless Chrome.

Each run starts a fresh nopCommerce 4.90.7 and PostgreSQL instance, installs the sample catalog, and creates separate shopper accounts for login and logout. The script generates disposable passwords for that run. The job fails if Maven fails or TestNG reports skips or fewer than fourteen tests. The script removes its Compose project and volumes at the end.

Open the workflow run in the repository's **Actions** tab. Download **storefront-smoke-reports** from **Artifacts** and open `index.html`. Failure images are included in its `screenshots/` directory; the test's reporter output identifies the filename. The upload step runs after a failure and keeps any reports Maven produced for 14 days. If setup failed before Maven, inspect the provisioning step's log.

To reproduce the job from the repository root, start Docker and use Python 3 with Maven, Java, and Chrome on the machine.

```powershell
python automation/ci/run-smoke.py
```

The script uses port `18080` by default. Set `NOP_QA_CI_PORT` before running if that port is in use. The disposable Compose project uses its own volumes and credentials. Maven runs `clean test` and writes fresh Surefire reports to `automation/target/surefire-reports/`.

The workflow builds the dashboard after the smoke step, including when tests fail. Main-branch runs upload the static dashboard and deploy it through GitHub Pages. Pull requests build the report without publishing it. If no TestNG XML exists, the dashboard shows that results are unavailable and links to the workflow for diagnosis.

The public JSON includes only case IDs, names, statuses, durations, and run metadata. Raw exceptions, parameters, and system properties remain outside the public dashboard. Generate a local preview after a test run with the commands below, then open `http://127.0.0.1:8766/`.

```powershell
python automation/ci/build-report.py
python -m http.server 8766 --bind 127.0.0.1 --directory automation/target/report-site
```

### Run selected checks

Run the configuration checks without opening a browser:

```powershell
mvn -f automation/pom.xml "-Dtest=TestConfigTest" test
```

Run the search smoke test independently:

```powershell
mvn -f automation/pom.xml "-Dtest=ProductSearchTest#validQueryReturnsExpectedProduct" "-DbaseUrl=http://127.0.0.1:8080/" "-Dheadless=true" test
```

If Maven reports a `PKIX path building failed` error on Windows and the required certificate is trusted by Windows, run Maven with the Windows root store in a temporary PowerShell session:

```powershell
$env:MAVEN_OPTS = "$env:MAVEN_OPTS -Djavax.net.ssl.trustStoreType=Windows-ROOT -Djavax.net.ssl.trustStore=NONE"
mvn -f automation/pom.xml "-DbaseUrl=http://127.0.0.1:8080/" "-Dheadless=true" test
```

## QA-011.2 failure screenshots

`BaseTest` captures a viewport PNG from the current Selenium session in its always-run teardown, before quitting WebDriver. Failed test methods and skips caused by failed configuration methods trigger an attempt. Successful tests and ordinary fixture skips do not. Images are written under `automation/target/surefire-reports/screenshots/` with the class, method, and a unique ID. TestNG reporter output records the relative path. A missing browser, rejected privacy mask, or capture/write error produces a generic diagnostic and preserves the original result; teardown still runs. No raw browser exception, URL, page source, or test parameters are added to the evidence log.

Before capture, the helper hides form controls, editable content, frames, canvases, account navigation, account/login/registration/checkout/order/address regions, validation errors, email text, and text containing nonempty `NOP_QA_` environment values. If masking throws or cannot be confirmed, it omits the image. CI also redacts fixture email/password values from text reports, leaving PNG bytes untouched. Screenshots remain in the downloadable report artifact. QA-011.3 also publishes explicitly marked masked failure images to the Pages dashboard; see the next section.

Use only synthetic data. This mask covers the current storefront and fixtures; review it when new pages or sensitive components are introduced. It is not a general detector for personal data in arbitrary images or custom widgets. Inspect evidence before sharing it. Local raw Surefire reports may contain Selenium exception details; the CI text-sanitization step does not run during plain Maven commands.

Run the opt-in infrastructure checks (four Java checks and one Python check):

```powershell
mvn -f automation/pom.xml "-Dtest=FailureScreenshotChecksTest" test
python automation/ci/test_run_smoke.py
```

Run the controlled browser probe. **This command must fail** with one intentional assertion failure and no skips. It uses a local data page containing synthetic private fields and does not contact the storefront:

```powershell
mvn -f automation/pom.xml "-Dtest=FailureScreenshotProbeTest" "-Dheadless=true" clean test
```

Open the generated PNG and confirm that only the heading and public diagnostic paragraph remain visible. Check `testng-results.xml` for the failed probe, its screenshot path, and successful `quitBrowser` configuration. Surefire explicitly excludes the infrastructure and probe classes from normal discovery; `-Dtest` selects them for verification. The smoke suite remains fourteen tests.

Archive any evidence you need before `clean`, which removes `automation/target/`. Then load the local shopper fixtures using the [full-suite instructions](#run-the-full-suite-locally) and restore a fresh normal report:

```powershell
mvn -f automation/pom.xml "-DbaseUrl=http://127.0.0.1:8080/" "-Dheadless=true" clean test
```

On 2026-11-13, the four infrastructure checks and the Python sanitization check passed. The browser probe failed exactly once as intended, produced a visually inspected masked PNG, and completed teardown successfully. The normal suite then passed fourteen tests with zero failures, errors, or skips on Windows 11, JDK 21.0.4, Maven 3.9.9, headless Chrome 157, and local nopCommerce 4.90.7. A sandbox-restricted browser startup also confirmed that missing WebDriver produces a safe unavailable-evidence message. The updated workflow has not been pushed or run on GitHub; hosted artifact download verification remains pending.

## QA-011.3 screenshot viewer and intentional failure demonstration

The dashboard includes an **Evidence** column. Failed cases with published masked PNGs offer **View screenshot**, which opens an accessible image dialog with full-image and download links. Close it with the close button, Escape, or the backdrop. A failed case without evidence shows **Unavailable**. Filters and case search continue to work with the viewer.

The exporter reads each failed method's own `Masked failure screenshot:` reporter line, accepts only PNG basenames under the Surefire `screenshots/` directory, rejects missing/non-PNG/oversized or symbolic-link files, and publishes images using content hashes. It exports neither raw exception messages nor test parameters. A rebuild removes previously exported screenshots. Public images are accessible to anyone who can view GitHub Pages, so use synthetic storefront data and keep the privacy mask current.

Normal push and pull-request runs still execute fourteen tests. Two additional tests in `ReportScreenshotDemoTest` are excluded by default. They deliberately assert an invented product name and a zero price through the existing Page Objects. They are evidence demonstrations, not confirmed storefront defects.

To publish a demonstration on GitHub, open **Actions → Storefront smoke suite → Run workflow**, select `main`, and enable **screenshot_demo**. This runs the fourteen normal checks plus `DEMO-001` and `DEMO-002`. The expected result is **16 tests: 14 passed, 2 failed, 0 skipped**. Maven and the smoke job remain failed; the always-run report build and Pages deployment still publish the evidence. The dashboard labels these cases **Intentional failure** and displays a demonstration notice. A later ordinary run replaces this demonstration with the latest results.

To reproduce locally after loading shopper fixtures:

```powershell
mvn -f automation/pom.xml "-DbaseUrl=http://127.0.0.1:8080/" "-Dheadless=true" "-DscreenshotDemo=true" "-Dtest=*Test,!FailureScreenshotChecksTest,!FailureScreenshotProbeTest" clean test
# Expected nonzero exit: inspect exactly two intentional failures.
python automation/ci/build-report.py
python -m http.server 8766 --bind 127.0.0.1 --directory automation/target/report-site
```

Run exporter and CI sanitization checks with `python -m unittest discover -s automation/ci -p "test_*.py"`; check JavaScript syntax with `node --check automation/report/app.js`. For the optional DOM harness, install jsdom into the ignored test-output directory and run:

```powershell
npm.cmd install --prefix test-output/qa-011.3-dom --no-save --package-lock=false jsdom
node automation/report/test-app.cjs test-output/qa-011.3-dom
```

Use the ordinary full-suite command without demo properties to return to fourteen normal tests. Preserve reports before `clean` when comparing runs.

On 2026-11-13, the normal local suite passed 14/14 and all seven Python regression checks passed. Local browser demonstration, visual viewer verification, and hosted deployment results are recorded in the [handoff](../docs/HANDOFF.md) as they complete.

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
mvn -f automation/pom.xml "-Dtest=ProductSearchTest#validQueryReturnsExpectedProduct" "-DbaseUrl=http://127.0.0.1:8080/" "-Dheadless=true" "-DsearchQuery=iphone" "-DexpectedProduct=Apple iPhone 16 128GB" test
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

Add `"-Dtest=ProductSearchTest#validQueryReturnsExpectedProduct"` to run only the search smoke test.

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

On 2026-11-10, the independent run passed one test and the full suite passed nine tests on local nopCommerce 4.90.7 with headless Chrome on Windows 11. Both runs reported zero failures, errors, and skips.

## QA-009.6 cart subtotal smoke test

`ShoppingCartSmokeTest` covers `TC-CART-007` using `TD-CART-005`. It adds one `HTC smartphone` to a fresh guest cart, then updates the quantity to two. At both quantities it multiplies the displayed USD unit price by the observed quantity and compares the result with the line total and cart subtotal. The fixture uses a constant unit price and requires no displayed line or subtotal discount.

```powershell
mvn -f automation/pom.xml "-Dtest=ShoppingCartSmokeTest#subtotalMatchesCalculatedLineTotalBeforeAndAfterQuantityUpdate" "-DbaseUrl=http://127.0.0.1:8080/" "-Dheadless=true" test
```

Use `"-Dtest=ShoppingCartSmokeTest"` to run all four cart cases.

On 2026-11-10, the independent run passed one test and the full suite passed ten tests on local nopCommerce 4.90.7 with headless Chrome on Windows 11. Both runs reported zero failures, errors, and skips.

## QA-009.7 product details smoke test

`ProductSearchTest` covers `TC-CAT-006` using `TD-CAT-001`. It selects `Apple iPhone 16 128GB` from the local search results and compares the destination URL and title with the selected listing. It verifies the local fixture's `$799.00` price, `In stock` status, and enabled Add to cart button. The sample iPhone has no configurable purchase options. If the local catalog changes, recheck this fixture and its expected price and stock before updating the test.

```powershell
mvn -f automation/pom.xml "-Dtest=ProductSearchTest#selectingProductOpensMatchingPurchasableDetails" "-DbaseUrl=http://127.0.0.1:8080/" "-Dheadless=true" test
```

On 2026-11-10, the independent run passed one test and the full suite passed eleven tests on local nopCommerce 4.90.7 with headless Chrome on Windows 11. Both runs reported zero failures, errors, and skips. The [manual screenshot](../docs/test-execution/evidence/Product/TC-CAT-006_20261109_01.png) records the earlier public demo observation for `EXE-CAT-006`.

## QA-009.8 search with no results

`ProductSearchTest` covers `TC-CAT-002` using `TD-CAT-002`. The test generates an uncommon alphanumeric term, submits it through the storefront search box, and verifies that the results contain no products and display `No products were found that matched your criteria.`

```powershell
mvn -f automation/pom.xml "-Dtest=ProductSearchTest#nonmatchingQueryShowsNoResultsMessageAndNoProducts" "-DbaseUrl=http://127.0.0.1:8080/" "-Dheadless=true" test
```

Use `"-Dtest=ProductSearchTest"` to run all three search and product details cases. On 2026-11-10, all three passed on local nopCommerce 4.90.7 with headless Chrome.

## QA-009.9 category navigation smoke tests

`CategoryNavigationTest` covers `TC-CAT-004` and `TC-CAT-005` using `TD-CAT-004`. The first case opens Electronics through the storefront menu and checks its heading and sample subcategories. The second opens Cell phones through the Electronics landing page and checks its heading, the Home / Electronics / Cell phones breadcrumb, and the sample iPhone listing.

```powershell
mvn -f automation/pom.xml "-Dtest=CategoryNavigationTest" "-DbaseUrl=http://127.0.0.1:8080/" "-Dheadless=true" test
```

On 2026-11-10, both category tests passed in the focused run, and all fourteen tests passed in the full suite on local nopCommerce 4.90.7 with headless Chrome on Windows 11. Both runs reported zero failures, errors, and skips.

## Source layout

| Package | Responsibility |
| --- | --- |
| `com.nopcommerce.qa.pages` | Storefront page objects, locators, and explicit waits |
| `com.nopcommerce.qa.support` | Configuration and one WebDriver session per TestNG test method, with teardown after a failure or skip |
| `com.nopcommerce.qa.tests` | TestNG tests, scenario data, and assertions |

Extend `BaseTest` in UI test classes and pass `driver()` into page objects. Map additional checks to the [test case workbook](../docs/test-cases/Test_Cases.xlsx). Keep checkout automation within the [pre-submit boundary](../docs/test-execution/EXECUTION_GUIDE.md).
