# Project Handoff

Recheck Git status and the workbooks when resuming. The tester may have saved further changes since this checkpoint.

## Start here

- Read the [repository guidelines](../AGENTS.md) and [task tracker](TASKS.md) before making changes.
- Communicate with the tester in Vietnamese. Write project documentation, workbook entries, evidence labels, and commit messages in English.
- The project uses the public nopCommerce demo storefront. Follow the [automation guide](../automation/README.md) for Maven, browser configuration, and report locations.

## Current state

| Work | Status at handoff |
| --- | --- |
| QA-001 through QA-005 | Project foundation and test design are complete. Some demo requirements from QA-002 still need manual verification. |
| QA-006.1 | Manual execution workbook, guide, and evidence conventions are complete. |
| QA-006.2 | All 38 cases have recorded statuses. The tester agreed to defer evidence follow-up and proceed with automation. |
| QA-007.1 | Defect management setup is complete. |
| QA-007.2 | Open for triage, evidence-backed defect reports, and retesting when applicable. |
| QA-008 | Complete. The Maven module, WebDriver lifecycle, and first page object compile. All three browser-free configuration tests pass. |
| QA-009.1 | Complete on installed nopCommerce 4.90.7. The public demo run remains blocked by its verification page. |
| QA-009.2 | Complete on local nopCommerce. Login and logout passed independently and in the six-test suite. |
| QA-009.3 | Complete on local nopCommerce. Add-to-cart passed independently and in the seven-test suite. |
| QA-009.4 | Complete on local nopCommerce. Cart quantity update passed independently and in the eight-test suite. |
| Remaining QA-009, QA-010, and QA-011 | Additional smoke tests, GitHub Actions, and portfolio review are planned. |

The manual execution commits through Checkout were pushed to `origin/main`, ending at `0bd868e`. Local changes to `Test_Cases.xlsx` and `Test_Data.xlsx` and the three original Checkout screenshots are outside those commits. Preserve them and check their contents before staging any future change.

## Manual execution checkpoint

- The [execution workbook](test-execution/Test_Execution.xlsx) reports 28 Pass, 3 Fail, 5 Blocked, and 2 Skipped. These are tester-reported dispositions. Preserve the observations and statuses unless the tester authorizes a correction.
- Before filing defects for `TC-CAT-008`, `TC-CART-002`, or `TC-CHK-002`, verify filter availability, a product with an unset required option, or an offered guest checkout route respectively. Use the [defect management guide](bug-reports/DEFECT_MANAGEMENT.md) and [Bug Register](bug-reports/Bug_Tracking.xlsx) for confirmed defects.
- The `TC-REG-002` attempt left several fields blank. Its case requires exactly one. The `TC-CHK-008` evidence was captured after order placement. If those cases are revisited, preserve the original attempts and stop checkout before final submission.
- The tester runs manual browser checks. Record actual dates and observations, then link sanitized evidence using the [execution guide](test-execution/EXECUTION_GUIDE.md) and [evidence conventions](test-execution/evidence/README.md).

## Automation checkpoint

- On 2026-11-09, `mvn -f automation/pom.xml test` passed on Windows with JDK 21.0.4 and Maven 3.9.9. Surefire reported three tests, zero failures, zero errors, and zero skips.
- The initial dependency download hit a Java certificate trust error. A per-process Windows trust-store setting let Maven populate its cache. The plain command passed on the next run. See the [automation guide](../automation/README.md) for the command used.
- On 2026-11-10, `ProductSearchTest` launched Chrome 156.0.8078.80 headless. The public demo returned `Just a moment...` and the test failed before submitting a search. The full suite passed all four tests against a temporary localhost fixture, and a wrong-product override failed as expected. TestNG recorded successful teardown after the failed runs. See the [automation guide](../automation/README.md#qa-0091-product-search) for the case, fixture settings, and run commands.
- The real [local QA instance](../automation/README.md#local-nopcommerce-qa-environment) runs on `http://127.0.0.1:8080/` using Docker Compose and PostgreSQL. The sample search result persisted after restart. Chrome headless passed the independent search test and all four tests in the full suite. Local admin credentials are in the ignored `automation/.env`. The admin account is for setup.
- Two separate shopper fixtures are provisioned for [authentication smoke tests](../automation/README.md#qa-0092-authentication-smoke-tests). Their credentials are in the ignored `automation/.env`. On 2026-11-10, Chrome headless passed each authentication case independently and all six tests in the full suite.
- On 2026-11-10, [the add-to-cart smoke test](../automation/README.md#qa-0093-add-to-cart-smoke-test) verified one `HTC smartphone` cart line with quantity two. The independent run and the full seven-test suite passed on local nopCommerce with headless Chrome.
- On 2026-11-10, [the cart quantity update smoke test](../automation/README.md#qa-0094-cart-quantity-update-smoke-test) confirmed that the reopened cart kept the updated quantity and displayed the corresponding line total. The independent run passed one test and the full run passed eight, including login and logout. Both runs used local nopCommerce 4.90.7 and headless Chrome on Windows 11 and reported zero failures, errors, and skips.

## Next session

1. Start Docker Desktop and resume the local QA instance if needed. Load the shopper fixture variables and run the current smoke tests before expanding QA-009.
2. Review `TC-CART-006` for the next shopping cart smoke test. Use the [case workbook](test-cases/Test_Cases.xlsx), [test data workbook](test-cases/Test_Data.xlsx), [traceability matrix](test-cases/RTM.xlsx), and manual execution evidence. Keep scenarios independent and stop checkout at the last safe pre-submit step.
3. Refresh this handoff and the task tracker when the next task changes the project state. Commit or push only on explicit request. If Git for Windows encounters a certificate trust error, previous pushes succeeded with the per-command option `-c http.sslBackend=schannel`.
