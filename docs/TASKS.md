# Project Task Tracker

| Phase | Task | Status | Deliverable |
| --- | --- | --- | --- |
| Foundation | QA-001 Initialize project structure | Complete | Repository layout, contribution guidelines, README, ignore rules, and task tracker |
| Manual test design | QA-002 Application exploration and test scope definition | Core public UI verified; mail and conditional coverage gaps remain | [Application overview](manual/APPLICATION_OVERVIEW.md), [functional checklist](manual/FUNCTIONAL_CHECKLIST.md), and [dated follow-up](test-execution/FOLLOW_UP_2026-11-17.md) |
| Test planning | QA-003 Test planning and risk assessment | Complete | [Test plan](manual/TEST_PLAN.md), [risk assessment](manual/RISK_ASSESSMENT.md), and [testing strategy](manual/TEST_STRATEGY.md) |
| Manual scenario design | QA-004 Design test scenarios in Excel | Complete | [Test scenario workbook](test-cases/Test_Scenarios.xlsx) with prioritized scenarios and checklist traceability |
| Detailed manual design | QA-005 Design detailed test cases and test data | Complete | [Test cases](test-cases/Test_Cases.xlsx), [test data](test-cases/Test_Data.xlsx), and [requirements traceability matrix](test-cases/RTM.xlsx) |
| Manual execution setup | QA-006.1 Prepare manual execution artifacts | Complete | [Execution workbook](test-execution/Test_Execution.xlsx), [execution guide](test-execution/EXECUTION_GUIDE.md), and [evidence conventions](test-execution/evidence/README.md) |
| Manual execution | QA-006.2 Verify and execute manual tests | Historical execution retained; follow-up recorded with explicit gaps | [Execution workbook](test-execution/Test_Execution.xlsx) and [new 38-case follow-up ledger](test-execution/FOLLOW_UP_2026-11-17.md) |
| Defect management setup | QA-007.1 Prepare defect management artifacts | Complete | [Bug tracking workbook](bug-reports/Bug_Tracking.xlsx), [lifecycle and severity guidelines](bug-reports/DEFECT_MANAGEMENT.md), [report template](bug-reports/BUG_REPORT_TEMPLATE.md), and [Jira reporting guide](bug-reports/BUG_REPORTING_GUIDE.md) |
| Defect management | QA-007.2 Triage, report, and retest defects | Complete for the three historical Fail observations; fresh checks passed | [Triage dispositions and fresh verification](bug-reports/QA-007.2_TRIAGE.md); no confirmed storefront defect or fix to retest |
| Automation foundation | QA-008 Set up the Maven automation project | Complete | [Maven module](../automation/pom.xml) and [automation guide](../automation/README.md) |
| UI automation | QA-009 Automate priority smoke scenarios | Complete on local nopCommerce | [Eleven browser cases and three configuration checks](../automation/README.md#run-the-full-suite-locally) |
| UI automation | QA-009.1 First real browser smoke test | Complete on local nopCommerce. Public demo blocked. | [Independent TC-CAT-001 run](../automation/README.md#qa-0091-product-search) and [local QA environment](../automation/README.md#local-nopcommerce-qa-environment) |
| UI automation | QA-009.2 Automate login and logout smoke scenarios | Complete on local nopCommerce | [TC-AUTH-001 and TC-AUTH-004 runs](../automation/README.md#qa-0092-authentication-smoke-tests) |
| UI automation | QA-009.3 Automate add-to-cart smoke scenario | Complete on local nopCommerce | [Independent TC-CART-001 run](../automation/README.md#qa-0093-add-to-cart-smoke-test) |
| UI automation | QA-009.4 Automate cart quantity update smoke scenario | Complete on local nopCommerce | [Independent TC-CART-003 run](../automation/README.md#qa-0094-cart-quantity-update-smoke-test) |
| UI automation | QA-009.5 Automate cart removal smoke scenario | Complete on local nopCommerce | [Independent TC-CART-006 run](../automation/README.md#qa-0095-cart-removal-smoke-test) |
| UI automation | QA-009.6 Automate cart subtotal smoke scenario | Complete on local nopCommerce | [Independent TC-CART-007 run](../automation/README.md#qa-0096-cart-subtotal-smoke-test) |
| UI automation | QA-009.7 Automate product details smoke scenario | Complete on local nopCommerce | [Independent TC-CAT-006 run](../automation/README.md#qa-0097-product-details-smoke-test) |
| UI automation | QA-009.8 Automate search with no results | Complete on local nopCommerce | [TC-CAT-002 run](../automation/README.md#qa-0098-search-with-no-results) |
| UI automation | QA-009.9 Automate category and subcategory navigation | Complete on local nopCommerce | [TC-CAT-004 and TC-CAT-005 runs](../automation/README.md#qa-0099-category-navigation-smoke-tests) |
| Continuous integration | QA-010 Add GitHub Actions | Hosted run passed and artifact published | [First hosted run](https://github.com/phuctan153/nopcommerce-qa/actions/runs/42780134311) |
| Portfolio review | QA-011 Prepare the project for review | Complete for the tested browser/viewport matrix; manual gaps remain explicit | [Portfolio overview](../README.md) and [responsive review / QA-011-RWD-001](manual/PORTFOLIO_REVIEW.md) |
| Report presentation | QA-011.1 Publish a live smoke report dashboard | Published on GitHub Pages | [Report dashboard](https://phuctan153.github.io/nopcommerce-qa/) |
| Failure evidence | QA-011.2 Capture Selenium failure screenshots | Complete; hosted artifacts verified | [Masked failure evidence and verification](../automation/README.md#qa-0112-failure-screenshots) |
| Report evidence | QA-011.3 Display screenshots and publish intentional failure demonstrations | Published and verified on GitHub Pages | [Screenshot viewer and demo run](../automation/README.md#qa-0113-screenshot-viewer-and-intentional-failure-demonstration) |
| CI result semantics | QA-011.4 Verify expected failures and separate deployment status | Complete and verified on hosted CI | [Verification semantics](../automation/README.md#qa-0114-expected-failure-verification-and-independent-deployment-status) |

## Current manual and portfolio follow-up

- On 2026-11-17, public UI follow-up recorded 31 Pass, 6 Blocked, 1 Skipped and 0 Not Run in a separate ledger. The historical workbook remains unchanged. All three historical Fail scenarios passed with valid new fixtures; QA-007.2's current triage scope is complete. [Run record and evidence](test-execution/FOLLOW_UP_2026-11-17.md)
- TC-AUTH-006 is now verified. TC-AUTH-005/007/008/009 remain blocked because the tester confirmed no controlled receiving inbox is available. No reset link or credential reset was fabricated.
- TC-AUTH-002 needs expectation review: an omitted password is rejected with generic incorrect-credentials feedback, while the case asks for missing-input feedback. The follow-up records Blocked pending a supported message requirement, not Pass or a filed defect.
- TC-CART-004 passed with zero removing the controlled line, and signed-in TC-CHK-001 reached Billing after restoring an unavailable account fixture. TC-CART-005 is Blocked for lack of a safe confirmed small upper limit; no large quantity was submitted. TC-CHK-006 was intentionally skipped because this fixture offers manual shipping; TC-CHK-007 passed with an omitted City.
- Guest checkout, billing validation, separate shipping validation, delivery selection and pre-submit reconciliation were observed. Post-submit confirmation remains outside the public-demo boundary.
- Live report review covered 390px and 768px without page overflow and found about 11px overflow at 320px. QA-011-RWD-001 was fixed in `e7b4476`, deployed and visually retested at 320/390/768px, plus 1440px normal-run regression. Both normal and demo workflows succeeded; the verified demo remains public. [Review](manual/PORTFOLIO_REVIEW.md)

## QA-003 planning outcome

- TP-001 prioritizes account access, finding a purchasable product, cart accuracy, and safe pre-submit checkout. The risk register distinguishes documented or observed conditions from potential risks.
- QA-003 completed planning only. The QA-002 verification items above carry forward. Before detailed case design, confirm reachability and fixtures and resolve configuration-dependent expectations where possible.

## QA-004 scenario design outcome

- The workbook contains 33 scenarios across registration, authentication, product catalog, shopping cart, and checkout. It includes 12 P0 scenarios, 21 P1 scenarios, and 10 provisional automation candidates.
- The traceability matrix maps 19 functional checklist IDs and records `FC-CHK-005` as excluded from the public demo. It preserves the registration observation as user-reported evidence from one session.
- At QA-004, 32 scenarios remained pending manual verification and one carried a user-reported observation. QA-005 maps detailed cases to all 33 scenarios.

## QA-005 detailed design outcome

- The design contains 38 unique test cases and 21 test data records. All 33 QA-004 scenarios map to cases across 19 in-scope functional IDs. `FC-CHK-005` remains outside the public-demo scope.
- All three workbooks reopened successfully. Validation checked numbered steps, expected results, source IDs, two-way data references, and workbook controls.
- The execution workbook records subsequent demo observations. Some conditional expectations still require verification before retesting or automation.

## QA-006.1 setup outcome

- The execution workbook imports all 38 QA-005 cases with one unique execution ID per case. The initial status is Not Run for every row.
- Module and overall formulas were validated against the initial state and temporary in-memory status changes. Execution fields and evidence remain empty pending manual execution.
- The setup handed off storefront access checks, clean browser sessions, and synthetic fixture preparation to the manual tester.

## QA-007.1 setup outcome

- The Bug Register contains zero defects. Its dashboard, filters, frozen headers, and dropdowns were validated. Formula checks used temporary entries held only in memory.
- QA-007.2 remains open for triage, evidence-backed reporting, and retest or regression work when applicable. Jira keys are recorded only after a real issue exists in an authorized project.

## Earlier QA-007.2 triage checkpoint

- On 2026-11-17, the three historical Fail observations were checked against their case and data preconditions. The tester confirmed the original listing/product fixture details are no longer available. None currently establishes an application defect under valid conditions; this does not establish that the features are defect-free.
- `TRIAGE-001` records missing enabled-filter/listing evidence for `TC-CAT-008`. `TRIAGE-002` records the unestablished required-option fixture for `TC-CART-002`. `TRIAGE-003` records the unestablished offered guest route for `TC-CHK-002`. See the [review](bug-reports/QA-007.2_TRIAGE.md).
- An initial accessibility observation showed Cloudflare verification, but the saved screenshot shows the storefront home page; it is home-access evidence only. Native Chrome control subsequently stopped because Computer Use could not establish the current browser URL sufficiently to enforce policy. No fresh storefront case step, local substitute, application discrepancy, or fix retest is claimed.
- Only three execution Notes cells and their row heights were updated. Original dates, actual results, Fail statuses, and blank Defect IDs were preserved. The summary remains 28 Pass, 3 Fail, 5 Blocked, 2 Skipped; the Bug Register remains unchanged with zero filed defects. Protected design/data workbooks and original Checkout screenshots were retained.
- Record review and its traceable notes are complete. QA-007.2 remains open for the blocked fresh verification, using new fixtures and the checklist. Full completion requires those checks or an explicit decision by the tester to defer that coverage.

This earlier access/control checkpoint is superseded by the successful new-fixture checks in the current follow-up above. Original workbook notes are retained as dated history.

## QA-006.2 execution checkpoint

- The tester recorded outcomes for all 38 cases across Registration, Authentication, Product Catalog, Shopping Cart, and Checkout. The workbook reports 28 Pass, 3 Fail, 5 Blocked, and 2 Skipped.
- The tester agreed to proceed with automation foundation work and defer manual evidence follow-up. The [handoff](HANDOFF.md) lists the open prerequisites and pre-submit checkout gap.

## QA-008 foundation outcome

- The Maven module compiles for Java 17 and uses Selenium WebDriver and TestNG. It supports Chrome and Edge, configurable storefront URL and headless mode, and per-method WebDriver setup and teardown.
- On 2026-11-09, `mvn -f automation/pom.xml test` passed all three browser-free configuration tests on Windows with JDK 21.0.4 and Maven 3.9.9. Browser startup and storefront behavior remain for QA-009.

## QA-009.1 verification outcome

- On 2026-11-10, the independent configuration run passed all three tests. The independent live smoke run launched Chrome 156.0.8078.80 headless on Windows 11 and recorded one failure when the demo displayed `Just a moment...` instead of the storefront. Both runs reported zero errors and zero skips.
- The full suite against a temporary localhost fixture ran four tests with zero failures, errors, or skips. An intentional wrong-product override on the same fixture ran one test with one assertion failure, zero errors, and zero skips. TestNG reported successful browser teardown for both fixture runs and the live run.
- The smoke test is linked to `TC-CAT-001`, `TD-CAT-001`, and `EXE-CAT-001`.
- On 2026-11-10, the installed nopCommerce 4.90.7 QA storefront returned the expected iPhone from its PostgreSQL sample catalog after a container restart. Chrome headless passed the independent smoke test and the full Maven suite. The runs reported one and four tests respectively, with zero failures, errors, or skips. The public demo result remains blocked.

## QA-009.2 verification outcome

- On 2026-11-10, `TC-AUTH-001` opened the expected shopper account after sign-in. `TC-AUTH-004` restored guest navigation and redirected a fresh visit to the previous account URL to sign-in, with no account content displayed.
- Each case ran independently with its own shopper fixture and browser session. The full suite on local nopCommerce 4.90.7 passed six tests with zero failures, errors, or skips.

## QA-009.3 verification outcome

- On 2026-11-10, `TC-CART-001` added two units of `HTC smartphone` in a fresh guest session. The cart showed a single line with the expected product and quantity.
- The independent smoke test and the full seven-test Maven suite passed on local nopCommerce 4.90.7 with headless Chrome on Windows 11. Both runs reported zero failures, errors, and skips.

## QA-009.4 verification outcome

- On 2026-11-10, `TC-CART-003` used `TD-CART-001` to change one `HTC smartphone` to two in a fresh guest cart. The reopened cart retained quantity two, and its line total matched the displayed unit price multiplied by two. The [manual execution workbook](test-execution/Test_Execution.xlsx) records the related `EXE-CART-003` run.
- Local nopCommerce 4.90.7 at `http://127.0.0.1:8080/` passed the independent test and all eight tests in the full Maven suite with headless Chrome 156.0.8078.80 on Windows 11. Both runs reported zero failures, errors, and skips.

## QA-009.5 verification outcome

- On 2026-11-10, `TC-CART-006` used `TD-CART-004` to add two distinct lines in a fresh guest cart. Removing the first product preserved the second line. Removing the second displayed the empty cart message and left no product lines.
- The independent test and all nine tests in the full suite passed on local nopCommerce 4.90.7 with headless Chrome on Windows 11. Both runs reported zero failures, errors, and skips. The [full suite instructions](../automation/README.md#run-the-full-suite-locally) include fixture loading and the command to watch the browser run.

## QA-009.6 verification outcome

- On 2026-11-10, `TC-CART-007` used `TD-CART-005` to reconcile the line total and cart subtotal for one and two units of an undiscounted `HTC smartphone`. Both displayed amounts matched the independently calculated USD amount at each quantity.
- The independent test and all ten tests in the full suite passed on local nopCommerce 4.90.7 with headless Chrome on Windows 11. Both runs reported zero failures, errors, and skips.

## QA-009.7 verification outcome

- On 2026-11-10, `TC-CAT-006` used `TD-CAT-001` to open `Apple iPhone 16 128GB` from search results. The details matched the selected title and URL and displayed `$799.00`, `In stock`, and an enabled Add to cart button.
- The independent test and all eleven tests in the full suite passed on local nopCommerce 4.90.7 with headless Chrome on Windows 11. Both runs reported zero failures, errors, and skips.

## QA-009.8 verification outcome

- On 2026-11-10, `TC-CAT-002` used `TD-CAT-002` to submit a generated alphanumeric search term. The results displayed zero product titles and the no-results message.
- All three `ProductSearchTest` cases passed on local nopCommerce 4.90.7 with headless Chrome on Windows 11. Maven reported zero failures, errors, and skips.

## QA-009.9 verification outcome

- On 2026-11-10, `TC-CAT-004` opened Electronics from the storefront menu and verified its heading and subcategories. `TC-CAT-005` opened Cell phones from the parent category and verified the child heading, breadcrumb, and sample iPhone listing. Both used `TD-CAT-004`.
- Both category cases and all fourteen tests in the full suite passed on local nopCommerce 4.90.7 with headless Chrome on Windows 11. Both runs reported zero failures, errors, and skips.
- All eleven cases marked for automation in the current case workbook map to distinct browser tests. QA-010 is the next phase for running the suite and publishing its reports through GitHub Actions.

## QA-010 verification outcome

- The workflow provisions a fresh nopCommerce and PostgreSQL instance, installs sample data, creates the two shopper fixtures, and runs Maven with headless Chrome. The upload step collects Surefire reports after success or failure when reports exist.
- On 2026-11-10, a fresh disposable environment passed all fourteen tests on Windows 11 with JDK 21 and headless Chrome. Both authentication cases passed. The Compose project and volumes were removed after the run.
- `actionlint` passed for the workflow. A simulated skipped test was rejected, and a simulated Maven failure retained its failure status while removing the fixture password from the report.
- On 2026-11-10, the first hosted run completed successfully on Ubuntu with JDK 17. GitHub recorded success for the smoke and upload steps and listed the `storefront-smoke-reports` artifact. [Hosted run](https://github.com/phuctan153/nopcommerce-qa/actions/runs/42780134311)

## QA-011.1 live report dashboard

- On 2026-11-11, the dashboard was published through GitHub Pages. The workflow exports selected TestNG fields after the smoke step and deploys reports from the main branch, including failed runs.
- Verification checked JavaScript syntax, status and module filters, case search, and missing-report and failure states in a DOM test harness. The public JSON exporter excluded parameters and exceptions in a synthetic failure report. Desktop browser preview was unavailable because the Computer Use helper could not confirm the browser URL, so visual browser verification remains open.
- The first dashboard deployment correctly displayed thirteen passes and one failure. Investigation found that the sign-in test read navigation state before the sign-in flow had completed. An explicit wait for the sign-out link was added, and the local suite passed fourteen tests with zero failures, errors, or skips.
- The next hosted run passed all fourteen tests and deployed the dashboard. Public HTTP checks confirmed that the HTML, CSS, JavaScript, and JSON were available, and that the JSON contained the matching commit with fourteen passes and zero failures or skips. [Verified deployment run](https://github.com/phuctan153/nopcommerce-qa/actions/runs/42975459235)

## QA-011.2 failure screenshot outcome

- `BaseTest` captures masked viewport PNGs before browser teardown for failed tests and configuration-induced skips. Missing sessions, privacy-mask rejection, and capture/write errors produce a sanitized diagnostic while retaining the original result and attempting teardown.
- Evidence is stored under Surefire reports with unique class/method filenames and a reporter-output path. The existing always-run CI upload includes screenshots in `storefront-smoke-reports` for 14 days. Pages continues to publish only the existing dashboard export.
- On 2026-11-13, four opt-in Java infrastructure checks passed. A Python regression check confirmed that failed Maven execution retains its failure, fixture email/password values are removed from text reports, and PNG bytes remain unchanged.
- The opt-in browser probe produced one expected assertion failure, zero skips, a masked PNG inspected visually, and successful teardown. A sandbox-restricted startup separately exercised the missing-browser path. The normal suite passed all fourteen tests on local nopCommerce 4.90.7 with Windows 11, JDK 21.0.4, Maven 3.9.9, and headless Chrome 157.
- The final report is regenerated with `clean test`; probe evidence is archived locally under ignored `test-output/qa-011.2-probe/`. Hosted artifact verification and desktop viewer review were subsequently completed under QA-011.3. Mobile visual review and QA-007.2 manual defect triage remain open.

## QA-011.3 screenshot viewer checkpoint

- The dashboard has an Evidence column, a modal image viewer, full-image and download links, and explicit unavailable/image-load-error states. Only masked PNGs referenced by a failed method's own reporter output are published. The exporter rejects untrusted paths and removes stale exported images during rebuilds.
- An optional `screenshot_demo` workflow input adds two independent intentional failures through existing Page Objects. Default runs remain fourteen tests; demo mode retains the failed Maven/job status and labels the demonstration on the dashboard.
- On 2026-11-13, the normal local suite passed 14/14. The demo ran sixteen tests: fourteen passed, exactly two intentional assertions failed, and zero skipped. Both failures produced masked PNGs and both images were exported with their matching result.
- All seven Python regression checks and the dashboard DOM harness passed. The harness covered filtering, both image viewers, links, image errors, missing/unsafe evidence paths, and the normal success state. Local visual browser verification was unavailable: the in-app browser could not reach localhost and native Chrome control approval timed out. Public desktop verification was subsequently completed after the authorized push; see the hosted results below.

- Hosted verification on 2026-11-13: the [normal run](https://github.com/phuctan153/nopcommerce-qa/actions/runs/43396627989) passed 14/14 and deployed successfully. The [final demonstration](https://github.com/phuctan153/nopcommerce-qa/actions/runs/43397622259) ran sixteen tests with fourteen passes, exactly two intentional failures, and zero skips. The smoke job remained failed while reporting/upload/deployment succeeded.
- Both hosted artifacts were downloaded and inspected. The demo artifact contains two valid PNGs matching the two failed methods; fixture email values were absent from text reports. Public HTTP checks confirmed matching run data, image hashes, and versioned CSS/JavaScript URLs.
- Desktop browser verification opened both live screenshot viewers and confirmed both images loaded, plus Escape dismissal and focus return. An observed stale JavaScript cache was fixed by content-versioned asset URLs and verified through the final hosted demo. Mobile visual review remains open. The current public dashboard intentionally retains the two demonstration failures.

## QA-011.4 expected-failure verification checkpoint

- Demo mode verifies all fourteen named normal methods, exactly two expected assertion failures, successful setup/teardown, and two distinct valid PNGs before accepting Maven's test-failure exit code. Ordinary test failures and any unexpected demo result still fail CI. Original TestNG results are retained.
- Stale results are removed before Maven starts. Verification status is stored separately and bound to the current XML digest; the public export includes only allowlisted status fields.
- The dashboard separates raw test outcomes, automation verification, and the actual report deployment job. GitHub API failures show an unavailable state with a workflow link instead of assuming success.
- On 2026-11-17, all 28 Python regressions and the DOM harness passed, including unexpected assertions, browser errors, skips, missing/corrupt evidence, configuration failure, stale reports, API fallback, and independent deployment failure. The fresh normal Selenium run passed 14/14. The fresh demo retained two expected Fail results with valid PNGs while the runner returned exit 0. Hosted results are recorded below.

- The [normal hosted run](https://github.com/phuctan153/nopcommerce-qa/actions/runs/44037034088) passed 14/14 and succeeded, including deployment. The [expected-failure verification run](https://github.com/phuctan153/nopcommerce-qa/actions/runs/44037419784) also succeeded, including both smoke and deploy jobs, while TestNG retained 14 Pass, 2 Fail, 0 Skip.
- Both hosted artifacts were downloaded and checked. The demo retained two failed methods and two matching PNGs, plus passed verification metadata bound to the original sanitized XML. Public report data and PNG hashes matched the hosted run.
- Desktop browser verification confirmed raw test counts, screenshot verification Passed, and deployment Succeeded together, plus the retained failure filter and working image viewer. The proof is archived locally in ignored `test-output/qa-011.4-status-live.jpg`. Historical red demo runs are preserved; mobile visual review remains open.
