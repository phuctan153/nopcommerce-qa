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
| QA-007.2 | Historical observations triaged and execution notes updated. Fresh verification remains blocked; no confirmed application defect has been filed. See the triage review. |
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
| QA-011.4 | Complete and deployed. Both normal and expected-failure verification workflows succeeded. Raw test outcomes, automation verification, and deployment are shown separately. |

The manual execution commits through Checkout were pushed to `origin/main`, ending at `0bd868e`. Local changes to `Test_Cases.xlsx` and `Test_Data.xlsx` and the three original Checkout screenshots are outside those commits. Preserve them and check their contents before staging any future change.

## Manual execution checkpoint

- The [execution workbook](test-execution/Test_Execution.xlsx) reports 28 Pass, 3 Fail, 5 Blocked, and 2 Skipped. These are tester-reported dispositions. Preserve the observations and statuses unless the tester authorizes a correction.
- Before filing defects for `TC-CAT-008`, `TC-CART-002`, or `TC-CHK-002`, verify filter availability, a product with an unset required option, or an offered guest checkout route respectively. Use the [defect management guide](bug-reports/DEFECT_MANAGEMENT.md) and [Bug Register](bug-reports/Bug_Tracking.xlsx) for confirmed defects.
- The [2026-11-17 triage review](bug-reports/QA-007.2_TRIAGE.md) documents each precondition/evidence gap. The tester confirmed the original listing/product fixtures are no longer available. The initial browser observation showed Cloudflare, but the saved screenshot shows the storefront home page; continuing Cloudflare blockage must not be inferred from that image. Computer Use subsequently stopped native Chrome control because it could not establish the current URL. Fresh storefront verification was not performed. No bug report, Pass result, or successful retest was invented. Three Notes cells and their row heights were updated without changing historical results or other workbook features.
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

## QA-011.4 verification outcome

On 2026-11-17, all 28 Python regressions, the dashboard DOM harness, JavaScript syntax, documentation links, ignore rules, and whitespace checks passed. The fresh local normal suite passed 14/14. The fresh local demo retained 14 Pass, 2 Fail, 0 Skip and two valid PNGs while the runner returned exit 0 only after verifying the expected outcomes. Regression checks rejected wrong assertions, browser/configuration failures, missing tests, duplicate results, skips, corrupt/missing evidence, unexpected Maven exit codes, and attempts to reuse stale reports after build failure.

The [normal hosted run](https://github.com/phuctan153/nopcommerce-qa/actions/runs/44037034088) and the [expected-failure verification run](https://github.com/phuctan153/nopcommerce-qa/actions/runs/44037419784) both completed with **success**, including the smoke and deploy jobs. Both `storefront-smoke-reports` artifacts were downloaded and checked. The verification artifact retains two failed test methods and two matching PNGs; its separate `verification.json` records `screenshot-demo` / `passed` and matches the sanitized XML digest. The normal artifact records fourteen passes and `smoke` / `passed`. The public JSON and PNG hashes matched the verification run and commit `445ad79`.

Desktop browser verification confirmed the three visible states together: **14 passed · 2 failed · 0 skipped**, **Screenshot check passed**, and **Succeeded**. Deployment status came from the actual report run's GitHub Actions API. Filtering to Failed retained both intentional failures and their evidence buttons; the product screenshot loaded successfully at 1440 × 757. The proof is saved locally in ignored `test-output/qa-011.4-status-live.jpg`. Local reports are archived under `test-output/qa-011.4/normal/` and `test-output/qa-011.4/demo/`.

## Next session

1. Open the [live dashboard](https://phuctan153.github.io/nopcommerce-qa/). The current report deliberately retains 14 Pass, 2 Fail, 0 Skip while the screenshot verification and deployment are successful. These are expected demonstration assertions, not reported nopCommerce defects. A future ordinary run replaces the demo with normal results.
2. See the [verification run guide](../automation/README.md#qa-0114-expected-failure-verification-and-independent-deployment-status). Default smoke tests still require all tests to pass. Demo mode accepts only the two exact named assertion failures with valid masked evidence and all fourteen baseline checks passing; unexpected failures still fail CI. The historical red demonstration runs from 2026-11-13 predate QA-011.4 and are preserved.
3. The public deployment field reads the actual `deploy-report` job through the public GitHub API. API errors show an unavailable state with a workflow link rather than a fabricated success. Missing/stale verification metadata is displayed as unverified. Keep the baseline method manifest and expected demo messages aligned with intentional coverage or fixture changes.
4. Resume QA-007.2 fresh verification once browser access/control is available, using the [new-fixture checklist](bug-reports/QA-007.2_TRIAGE.md#fresh-verification-checklist). Historical triage is documented; the task is not fully complete. Mobile dashboard visual review also remains open. Keep privacy selectors current, use synthetic data, and inspect evidence before sharing.
5. Preserve the two modified design/data workbooks and three original Checkout screenshots listed above; their hashes were unchanged during QA-007.2 triage. The execution workbook now additionally contains exactly three appended triage Notes entries and adjusted row heights. Do not confuse that change with the earlier design/data edits. Bug Tracking is unchanged. The tester subsequently authorized committing and pushing the QA-007.2 triage work only; the pre-existing design/data edits and original Checkout screenshots are excluded. Docker Desktop and the existing QA Compose services were started during QA-011.4; no preview server was started for that task.
6. Commit and push only on explicit request in future work. QA-011.4 continued the explicitly authorized screenshot CI deployment correction. Git pushes used `git -c http.sslBackend=schannel push origin main` successfully. The final documentation-only commit uses `[skip ci]` to retain the verified demo report.
