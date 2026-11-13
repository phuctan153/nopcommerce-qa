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
| QA-011.1 | The dashboard is published on GitHub Pages. Desktop layout and the screenshot viewer are visually verified; mobile visual review remains open. |
| QA-011.2 | Complete. Hosted artifacts were downloaded and checked: normal smoke passed 14/14; the demo includes two masked screenshots mapped to the two intentional failures. |
| QA-011.3 | Published and verified on GitHub Pages. The current demonstration shows fourteen passes and two clearly labeled intentional failures, both with working screenshot viewers. |

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

1. Open the [live dashboard](https://phuctan153.github.io/nopcommerce-qa/), filter **Failed**, and use **View screenshot**. The current report deliberately contains 14 passes, two intentional failures, and zero skips. These are evidence demonstrations, not reported nopCommerce defects. A future ordinary run replaces the demo with normal smoke results; leave the current demo visible unless the tester requests another run.
2. The [normal hosted run](https://github.com/phuctan153/nopcommerce-qa/actions/runs/43396627989) passed 14/14 on 2026-11-13 and deployed successfully. Its `storefront-smoke-reports` artifact was downloaded and checked: fourteen passes, zero screenshots, and no fixture email values in text reports.
3. The [final demonstration run](https://github.com/phuctan153/nopcommerce-qa/actions/runs/43397622259), using commit `8a445ff`, produced exactly fourteen passes, two intentional failures, and zero skips. The smoke job correctly failed; report build, artifact uploads, and Pages deployment succeeded. The downloaded artifact contained both PNGs referenced by the failed methods. Public HTTP checks verified the matching run, result counts, versioned CSS/JavaScript, and both PNG content hashes. The asset-version fix was tested by a full manually dispatched run rather than a duplicate push-triggered run.
4. Desktop browser verification opened both screenshot dialogs on the public dashboard, confirmed that both images loaded at 1440 × 757, and checked Escape closing and focus return. The price screenshot viewer proof is saved locally in ignored `test-output/qa-011.3-viewer-live.png`. Local reports are archived in `test-output/qa-011.3-normal/` and `test-output/qa-011.3-demo/`. A stale browser JavaScript cache initially hid the new buttons; content-versioned asset URLs fixed this and the live browser verified the deployed correction. Mobile visual review remains open. The local in-app browser could not reach localhost and native Chrome approval timed out; public desktop verification subsequently completed successfully.
5. See the [viewer/demo run guide](../automation/README.md#qa-0113-screenshot-viewer-and-intentional-failure-demonstration). Seven Python regression checks, the dashboard DOM harness, JavaScript syntax, documentation links, ignore rules, and whitespace checks passed. Only explicitly marked masked failure PNGs are published; keep privacy selectors current as coverage expands, use synthetic data, and inspect evidence before sharing.
6. Continue QA-007.2 manual defect triage when requested. Preserve the two modified Excel workbooks and three original Checkout screenshots listed above; they were not staged or committed during the screenshot work. Docker Desktop and the existing QA Compose services are running. The task-owned localhost preview server was stopped after verification.
7. Commit and push only on explicit request in future work. This screenshot-viewer task was explicitly authorized for commit, push, and public deployment. Previous pushes used `git -c http.sslBackend=schannel push origin main` successfully.
