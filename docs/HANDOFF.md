# Project Handoff

Recheck Git status and the workbooks when resuming. The tester may have saved further changes since this checkpoint.

## Start here

- Read the [repository guidelines](../AGENTS.md) and [task tracker](TASKS.md) before making changes.
- Communicate with the tester in Vietnamese. Write project documentation, workbook entries, evidence labels, and commit messages in English.
- The project uses the public nopCommerce demo storefront. Follow the [automation guide](../automation/README.md) for Maven, browser configuration, and report locations.

## Current state

| Work | Status at handoff |
| --- | --- |
| QA-001 through QA-005 | Foundation/design complete. Core public storefront exploration is now observed; mail, quantity-rule and conditional follow-up gaps from QA-002 remain. |
| QA-006.1 | Manual execution workbook, guide, and evidence conventions are complete. |
| QA-006.2 | Historical 38-case run retained. A separate 2026-11-17 follow-up ledger records 31 Pass, 6 Blocked, 1 Skipped, 0 Not Run, with new evidence and explicit limits. |
| QA-007.1 | Defect management setup is complete. |
| QA-007.2 | Complete for the three historical Fail observations. All three new-fixture checks passed; no confirmed storefront defect or fix retest is claimed. |
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
| QA-011 | Responsive/portfolio review complete for the tested matrix. CSS correction deployed and visually verified; manual coverage gaps remain explicit. |
| QA-011.1 | Published dashboard now passes visual review at 320/390/768px; desktop 1440px layout preserved. QA-011-RWD-001 is fixed and verified. |
| QA-011.2 | Complete. Hosted artifacts were downloaded and checked: normal smoke passed 14/14; the demo includes two masked screenshots mapped to the two intentional failures. |
| QA-011.3 | Published and verified on GitHub Pages. The current demonstration shows fourteen passes and two clearly labeled intentional failures, both with working screenshot viewers. |
| QA-011.4 | Complete and deployed. Both normal and expected-failure verification workflows succeeded. Raw test outcomes, automation verification, and deployment are shown separately. |

The manual execution commits through Checkout were pushed to `origin/main`, ending at `0bd868e`. Local changes to `Test_Cases.xlsx` and `Test_Data.xlsx` and the three original Checkout screenshots are outside those commits. Preserve them and check their contents before staging any future change.

## Manual execution checkpoint

- The [execution workbook](test-execution/Test_Execution.xlsx) reports 28 Pass, 3 Fail, 5 Blocked, and 2 Skipped. These are tester-reported dispositions. Preserve the observations and statuses unless the tester authorizes a correction.
- Before filing defects for `TC-CAT-008`, `TC-CART-002`, or `TC-CHK-002`, verify filter availability, a product with an unset required option, or an offered guest checkout route respectively. Use the [defect management guide](bug-reports/DEFECT_MANAGEMENT.md) and [Bug Register](bug-reports/Bug_Tracking.xlsx) for confirmed defects.
- The [2026-11-17 triage review](bug-reports/QA-007.2_TRIAGE.md) preserves the earlier evidence/control limitations. The tester confirmed the original listing/product fixtures are no longer available. Its initial home-page screenshot did not prove a case result. Three Notes cells and row heights were updated in the earlier committed checkpoint, preserving historical results. The subsequent [new browser run](test-execution/FOLLOW_UP_2026-11-17.md) passed all three cases with new valid fixtures and closes that triage scope without inventing a bug or fix.
- The new follow-up is recorded in Markdown; the Excel execution workbook was not changed again. Its earlier dated Notes are superseded by the linked follow-up, not silently rewritten. The tester explicitly authorized one synthetic public-demo account and the checkout terms for a pre-submit-only test. The account was signed out, the guest cart emptied, and no order was placed.
- Four recovery cases remain blocked: the tester confirmed no controlled inbox is available. TC-AUTH-006 passed. TC-AUTH-002 is blocked for expectation review because an omitted password produces generic incorrect-credentials feedback, not the case's expected missing-input feedback. TC-CHK-007 passed; TC-CHK-006 remains conditional Skipped because this fixture offers manual shipping. The zero case and signed-in checkout entry subsequently passed. The upper-limit case remains Blocked without a safe confirmed fixture; no large quantity was submitted. The original account became unavailable and the same synthetic identifier was restored before the successful signed-in check.
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
4. QA-007.2's current three-observation scope is complete. Follow the remaining manual items in the [dated ledger](test-execution/FOLLOW_UP_2026-11-17.md#remaining-work-and-evidence-limits). Do not treat an `@example.com` fixture as a controlled inbox. A real reset would also require user handoff for the credential change under Computer Use policy.
5. Preserve the two modified design/data workbooks and three original Checkout screenshots listed above; their hashes were unchanged during QA-007.2 triage. The execution workbook now additionally contains exactly three appended triage Notes entries and adjusted row heights. Do not confuse that change with the earlier design/data edits. Bug Tracking is unchanged. The tester subsequently authorized committing and pushing the QA-007.2 triage work only; the pre-existing design/data edits and original Checkout screenshots are excluded. Docker Desktop and the existing QA Compose services were started during QA-011.4; no preview server was started for that task.
6. Commit and push only on explicit request in future work. QA-011.4 continued the explicitly authorized screenshot CI deployment correction. Git pushes used `git -c http.sslBackend=schannel push origin main` successfully. The final documentation-only commit uses `[skip ci]` to retain the verified demo report.
7. [QA-011-RWD-001](manual/PORTFOLIO_REVIEW.md) is fixed and verified. The mobile CSS change in `automation/report/style.css` stacks the heading/status under 600px and allows the badge to wrap. Hosted visual retest confirmed no document overflow at 320/390/768px and preserved desktop layout at 1440px. Localhost was unreachable from the in-app browser; automatic review rejected further inspection of personal Chrome, and browser URL policy blocked an offline data-URL preview. No workaround was used. The temporary server was stopped. New shared screenshots/text are under `test-execution/evidence/Follow-up/`; invalid registration screenshot crops were discarded in favor of sanitized AX excerpts.
8. Verification for this follow-up: Python 28/28, existing dashboard DOM harness, JavaScript syntax and normal local Maven 14/14 passed. Maven emitted the existing Chrome 157 / CDP 156 compatibility warning; tests completed with zero failures/errors/skips. The follow-up was committed/pushed as `11ed63d` and the CSS fix as `e7b4476` after explicit tester authorization. Hosted normal run 44051754267 and demo run 44052146636 both succeeded, including deployment and artifact verification. Public visual retest passed; see the review for evidence. Final verification documentation uses `[skip ci]` to preserve the demo.

## Latest published report

[Normal verification](https://github.com/phuctan153/nopcommerce-qa/actions/runs/44051754267) and [screenshot demo verification](https://github.com/phuctan153/nopcommerce-qa/actions/runs/44052146636) both succeeded with code commit `e7b4476`. The public report retains the latter: 14 Pass, 2 intentional Fail, 0 Skip, two masked images, passed verification and successful deployment. Downloaded artifacts matched these outcomes and verification digests. The earlier QA-011.4 run links above are historical checkpoints.

Browser retest: document client/scroll widths match at 320px (305/305), 390px (375/375), 768px (753/753) and normal desktop 1440px (1425/1425). Both viewers loaded; search/status/module filters and Escape focus return worked. Viewport overrides and filters were reset. No personal browser profile was used for this retest. Preserved Excel and original Checkout files remain outside the new commits.

## README review — 2026-11-18

The root README now distinguishes public-demo manual work from controlled Docker-based automation, leads with the dated 31 Pass / 6 Blocked / 1 Skipped follow-up, preserves the historical workbook counts, and links the latest verified normal/demo runs. It explains intentional failures, lists implemented coverage and limits, and provides the disposable CI reproduction command. Dedicated API/database testing remains outside implemented coverage; PostgreSQL is identified as environment infrastructure.

The tester chose to handle the GitHub profile README separately and requested a local commit for the project README review. Only the root README and this handoff note belong to that commit. Verification checked relative links, Markdown fences, whitespace, and protected file hashes. No automation changed, so Maven was not rerun. No push was requested for this review.
