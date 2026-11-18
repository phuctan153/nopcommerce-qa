# nopCommerce QA Testing Portfolio

A hands-on QA portfolio covering test planning, manual execution, requirements traceability, defect triage, and Selenium UI automation for nopCommerce.

**[View the live test report](https://phuctan153.github.io/nopcommerce-qa/)** · [Manual follow-up results](docs/test-execution/FOLLOW_UP_2026-11-17.md) · [Automation run guide](automation/README.md)

Manual checks use the [public demo storefront](https://demo.nopcommerce.com/). Automated smoke tests run against nopCommerce 4.90.7 in a controlled Docker environment with PostgreSQL, sample products, and disposable shopper accounts.

## Verified results

Results below were verified on **2026-11-17**. Manual results and automated checks are separate sets of coverage.

| Verification | Result | Evidence |
| --- | --- | --- |
| Public storefront manual follow-up | **38 cases: 31 Pass, 6 Blocked, 1 Skipped** | [Dated execution record and evidence](docs/test-execution/FOLLOW_UP_2026-11-17.md) |
| Default automated suite | **14/14 passed:** 11 Selenium browser cases and 3 configuration checks | [Successful normal CI run](https://github.com/phuctan153/nopcommerce-qa/actions/runs/44051754267) |
| Failure screenshot demonstration | **14 Pass, 2 intentional Fail, 0 Skip;** screenshot verification and deployment succeeded | [Successful demonstration run](https://github.com/phuctan153/nopcommerce-qa/actions/runs/44052146636) |
| Report presentation | Visually checked at 320, 390, 768, and 1440px viewport widths; both screenshot viewers worked | [Responsive review and evidence](docs/manual/PORTFOLIO_REVIEW.md) |

The [historical execution workbook](docs/test-execution/Test_Execution.xlsx) retains its original **28 Pass, 3 Fail, 5 Blocked, 2 Skipped**. The later follow-up is a separate run. The three historical Fail observations lacked confirmed fixtures/preconditions; all three scenarios passed with valid new fixtures. No confirmed nopCommerce defect has been filed. [Triage findings](docs/bug-reports/QA-007.2_TRIAGE.md)

## What this project demonstrates

- **Manual QA:** risk-based planning, 33 scenarios, 38 detailed test cases, synthetic test data, requirements traceability, dated execution records, and evidence-based triage.
- **Browser automation:** Java, Selenium WebDriver, TestNG, Maven, and Page Object Model. Tests use explicit waits, an independent browser session per case, and teardown that runs after failures.
- **CI and reporting:** GitHub Actions provisions a fresh storefront/database and shopper accounts, runs the suite, uploads Surefire artifacts, and deploys a searchable report to GitHub Pages.
- **Failure investigation:** screenshots are captured before WebDriver closes, with sensitive storefront fields masked. The report provides per-test evidence viewers and separates test outcomes, automation verification, and deployment status.

The 11 Selenium cases cover login/logout, positive and empty-result search, category/subcategory navigation, product details, and cart add, quantity update, removal, and subtotal reconciliation. Manual coverage also includes registration validation and checkout through the pre-submit review.

## Reading the demonstration report

The published demonstration intentionally keeps two failed assertions so reviewers can inspect real failure screenshots. Those rows are labeled **Intentional failure**; they are not reported nopCommerce bugs.

The normal suite requires all 14 checks to pass with zero skips. Demo mode succeeds only when those 14 checks pass, the two named demo assertions fail exactly as expected, and both masked screenshots are valid. Unexpected failures still fail CI. Raw TestNG results remain unchanged, and deployment has its own status.

A later normal run can replace the demonstration with ordinary smoke results. Use the report's workflow link to identify the run being displayed.

## Review the artifacts

| Artifact | What to inspect |
| --- | --- |
| [Test plan](docs/manual/TEST_PLAN.md), [risk assessment](docs/manual/RISK_ASSESSMENT.md), and [functional checklist](docs/manual/FUNCTIONAL_CHECKLIST.md) | Scope, priorities, test approach, and observed behavior |
| [Scenarios](docs/test-cases/Test_Scenarios.xlsx), [test cases](docs/test-cases/Test_Cases.xlsx), and [test data](docs/test-cases/Test_Data.xlsx) | Preconditions, steps, expected results, and fixture rules |
| [Requirements traceability matrix](docs/test-cases/RTM.xlsx) | Links between requirements, scenarios, and cases |
| [Manual follow-up](docs/test-execution/FOLLOW_UP_2026-11-17.md) and [triage review](docs/bug-reports/QA-007.2_TRIAGE.md) | Actual results, sanitized evidence, and unresolved prerequisites |
| [Selenium tests](automation/src/test/java/com/nopcommerce/qa/tests) and [Page Objects](automation/src/test/java/com/nopcommerce/qa/pages) | Assertions, reusable browser interactions, and explicit waits |
| [GitHub Actions workflow](.github/workflows/smoke.yml) and [CI runner](automation/ci/run-smoke.py) | Disposable environment setup, outcome verification, and artifact handling |

## Run the automation

Prerequisites: **JDK 17+, Maven 3.9+, Docker, Python 3, and Chrome** for the CI reproduction script. From the repository root, with Docker running:

```powershell
python automation/ci/run-smoke.py
```

The script creates a disposable nopCommerce/PostgreSQL environment and shopper fixtures, runs the normal suite, and removes its Compose project and volumes afterward. It writes results under `automation/target/surefire-reports/`.

To use the persistent local QA environment or Microsoft Edge, follow the [full local setup and browser instructions](automation/README.md#run-the-full-suite-locally). Keep credentials in the ignored local environment file or CI secrets.

On GitHub Actions, download **storefront-smoke-reports** from a run's **Artifacts** while available (14-day retention). Extract it and open `index.html`; failure images are included when captured. [CI and report instructions](automation/README.md#run-in-github-actions)

## Scope and limitations

- The six Blocked follow-up cases comprise four mail/recovery cases without a controlled inbox, one missing-password feedback expectation requiring clarification, and one upper-quantity boundary without a safe confirmed fixture. The automatic-shipping case is Skipped because its prerequisite was absent for the selected cart/destination.
- Checkout stops before final order submission. No real payment details are used, and post-submit order confirmation is outside the public-demo scope.
- Hosted automation is verified with Chrome headless. Viewport checks are browser emulation, not physical-device or cross-browser certification.
- PostgreSQL supports the QA environment. Dedicated API and database assertion suites are outside this project's implemented coverage.

See the [task tracker](docs/TASKS.md) and [handoff](docs/HANDOFF.md) for verification history and remaining gaps.
