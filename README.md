# nopCommerce QA Testing Portfolio

A QA portfolio for nopCommerce covering risk-based test planning, manual execution, requirements traceability, and automated storefront smoke tests. The automated suite uses a disposable Docker environment with sample data. [Public demo used for manual testing](https://demo.nopcommerce.com/)

Open the live report dashboard for the latest smoke run, module summaries, and searchable test results. [Live test report](https://phuctan153.github.io/nopcommerce-qa/)

## Results

- **Manual testing.** Recorded outcomes for 38 test cases across registration, authentication, product catalog, shopping cart, and checkout. The execution workbook reports 28 Pass, 3 Fail, 5 Blocked, and 2 Skipped. [Execution workbook](docs/test-execution/Test_Execution.xlsx)
- **UI automation.** Implemented 11 Selenium browser cases and 3 configuration checks using Java, TestNG, Maven, and the Page Object Model. The full suite passed **14/14** on local nopCommerce 4.90.7 with Chrome headless. [Automation guide](automation/README.md)
- **Continuous integration.** GitHub Actions provisions nopCommerce and PostgreSQL, creates disposable shopper accounts, runs the smoke suite, and uploads Surefire reports. The first hosted run passed and published the report artifact. [Successful GitHub Actions run](https://github.com/phuctan153/nopcommerce-qa/actions/runs/42780134311)

The browser suite covers sign-in and sign-out, catalog search and navigation, product details, and cart add, update, removal, and subtotal behavior. The manual checkout scope ends at the pre-submit review.

## Review the portfolio

| Artifact | What it shows |
| --- | --- |
| [Test plan](docs/manual/TEST_PLAN.md) and [risk assessment](docs/manual/RISK_ASSESSMENT.md) | Scope, priorities, and test approach |
| [Test scenarios](docs/test-cases/Test_Scenarios.xlsx), [test cases](docs/test-cases/Test_Cases.xlsx), and [test data](docs/test-cases/Test_Data.xlsx) | Preconditions, steps, expected results, and synthetic data |
| [Requirements traceability matrix](docs/test-cases/RTM.xlsx) | Links between requirements, scenarios, and cases |
| [Sample cart evidence](docs/test-execution/evidence/Cart/TC-CART-007_20261109_01.png) | Manual subtotal reconciliation for `TC-CART-007` |
| [Selenium tests](automation/src/test/java/com/nopcommerce/qa/tests) and [Page Objects](automation/src/test/java/com/nopcommerce/qa/pages) | Test assertions, browser interactions, and explicit waits |
| [GitHub Actions workflow](.github/workflows/smoke.yml) and [CI script](automation/ci/run-smoke.py) | Runner setup, storefront provisioning, and report upload |

## Run the smoke suite

Use JDK 17 or later, Maven 3.9 or later, Docker, and Chrome or Microsoft Edge. Follow the local instructions to start nopCommerce, load the shopper fixtures, and view the Surefire report. [Local run instructions](automation/README.md#run-the-full-suite-locally)

For a CI run, open the workflow summary and download `storefront-smoke-reports` under **Artifacts** while it is available. Open `index.html` after extracting it. [GitHub Actions run instructions](automation/README.md#run-in-github-actions)
