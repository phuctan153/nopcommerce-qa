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
| QA-009.5 | Complete on local nopCommerce. Cart removal passed independently and in the nine-test suite. |
| QA-009.6 | Complete on local nopCommerce. Cart subtotal reconciliation passed independently and in the ten-test suite. |
| QA-009.7 | Complete on local nopCommerce. Product details passed independently and in the eleven-test suite. |
| QA-009.8 | Complete on local nopCommerce. Search with no results passed in the three-case search and details run. |
| QA-009.9 | Complete on local nopCommerce. Both category navigation cases passed. |
| QA-009 | All eleven workbook automation candidates are covered. The full suite passed all fourteen tests. |
| QA-010 | The local run passed all fourteen tests. The first hosted run succeeded and published the Surefire artifact. |
| QA-011 | The repository README was reviewed for portfolio use. |
| QA-011.1 | The dashboard is published on GitHub Pages with fourteen passing tests from the hosted run. Its public data export and filter behavior are verified. Visual browser verification remains open. |
| QA-011.2 | Masked Selenium failure screenshots are implemented and verified locally. The normal suite still passes fourteen tests. Hosted artifact verification awaits an explicitly authorized push. |
| QA-011.3 | The screenshot viewer and optional two-failure CI demonstration are verified locally. Push and hosted verification are authorized for this task. |

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
- On 2026-11-10, [the cart removal smoke test](../automation/README.md#qa-0095-cart-removal-smoke-test) confirmed that removing the first line preserved the other line and removing the last line emptied the cart. The independent run passed one test and the full run passed all nine with zero failures, errors, and skips on local nopCommerce 4.90.7 with headless Chrome.
- On 2026-11-10, [the cart subtotal smoke test](../automation/README.md#qa-0096-cart-subtotal-smoke-test) matched the calculated amounts at quantities one and two. The independent run passed one test and the full run passed all ten with zero failures, errors, and skips on local nopCommerce 4.90.7 with headless Chrome.
- On 2026-11-10, [the product details smoke test](../automation/README.md#qa-0097-product-details-smoke-test) verified the selected listing's title and URL, price, stock status, and enabled Add to cart button. The independent run passed one test and the full run passed eleven with zero failures, errors, and skips on local nopCommerce 4.90.7 with headless Chrome.
- On 2026-11-10, [the no-results smoke test](../automation/README.md#qa-0098-search-with-no-results) verified that a nonmatching query showed the empty results message and zero product titles. All three search and details tests passed on local nopCommerce 4.90.7 with headless Chrome.
- On 2026-11-10, [the category navigation smoke tests](../automation/README.md#qa-0099-category-navigation-smoke-tests) verified the Electronics landing page and the Cell phones heading, breadcrumb, and product listing. The focused run passed both category tests. The full suite passed all fourteen tests with zero failures, errors, and skips on local nopCommerce 4.90.7 with headless Chrome.
- On 2026-11-10, [the CI reproduction script](../automation/README.md#run-in-github-actions) installed nopCommerce and provisioned two fresh shopper accounts in a disposable Compose project. Maven passed all fourteen tests with zero failures, errors, and skips on Windows 11 with JDK 21 and headless Chrome. The workflow passed `actionlint`.

## Next session

QA-011.3 extends the evidence work with a public screenshot viewer. See the [viewer/demo guide](../automation/README.md#qa-0113-screenshot-viewer-and-intentional-failure-demonstration). On 2026-11-13, normal local smoke passed 14/14 and demo mode produced exactly 14 passes, two intentional failures, and zero skips, with two exported images. Seven Python checks and the DOM harness passed. The local browser preview was unavailable (localhost timeout and native Chrome approval timeout); hosted visual verification remains pending at this checkpoint. The tester explicitly requested commit, push, and a published intentional-failure demonstration. Preserve the Excel changes and original Checkout PNGs. The old QA-011.2 note below records the earlier checkpoint; QA-011.3 now publishes allowlisted masked screenshots on Pages.

QA-011.2 is implemented: `BaseTest` attempts masked screenshots before quitting on test/configuration failure. See the [failure evidence guide](../automation/README.md#qa-0112-failure-screenshots). On 2026-11-13, four opt-in Java checks and one Python sanitization check passed. A controlled browser failure generated a visually inspected masked PNG, preserved the intended assertion failure, and completed teardown. The local fourteen-test regression passed with zero failures, errors, or skips on Windows 11, JDK 21.0.4, Maven 3.9.9, and headless Chrome 157 against nopCommerce 4.90.7. Probe reports were preserved in ignored `test-output/qa-011.2-probe/` before the final clean run. Screenshots are included by the existing always-run Surefire artifact upload, but this update has not been pushed or verified on a hosted runner. After an authorized push, verify a failed-run artifact contains `screenshots/` and that Pages still excludes raw evidence. Review privacy selectors when adding new pages; use synthetic data and inspect evidence before sharing. Docker Desktop and the existing local QA Compose services were started for verification.

1. Use the [portfolio README](../README.md) as the entry point for the project results, test evidence, and run instructions.
   The [live dashboard](https://phuctan153.github.io/nopcommerce-qa/) presents the latest main-branch smoke report.
   Review its desktop and mobile layout in a browser. Computer Use could not confirm the browser URL during this session, so visual verification was not completed.
   The [verified deployment run](https://github.com/phuctan153/nopcommerce-qa/actions/runs/42975459235) passed all fourteen tests on 2026-11-11. The public report JSON matched that run and commit.
2. The [first hosted run](https://github.com/phuctan153/nopcommerce-qa/actions/runs/42780134311) succeeded on 2026-11-10. The GitHub API confirmed the `storefront-smoke-reports` artifact listing. The artifact contents still need a download check if required for the portfolio review.
3. Refresh this handoff and the task tracker when the next task changes the project state. Commit or push only on explicit request. If Git for Windows encounters a certificate trust error, previous pushes succeeded with the per-command option `-c http.sslBackend=schannel`.
