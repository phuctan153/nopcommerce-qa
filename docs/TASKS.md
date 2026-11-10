# Project Task Tracker

| Phase | Task | Status | Deliverable |
| --- | --- | --- | --- |
| Foundation | QA-001 Initialize project structure | Complete | Repository layout, contribution guidelines, README, ignore rules, and task tracker |
| Manual test design | QA-002 Application exploration and test scope definition | Documentation complete, manual verification pending | [Application overview](manual/APPLICATION_OVERVIEW.md), [test scope](manual/TEST_SCOPE.md), and [functional checklist](manual/FUNCTIONAL_CHECKLIST.md) |
| Test planning | QA-003 Test planning and risk assessment | Complete | [Test plan](manual/TEST_PLAN.md), [risk assessment](manual/RISK_ASSESSMENT.md), and [testing strategy](manual/TEST_STRATEGY.md) |
| Manual scenario design | QA-004 Design test scenarios in Excel | Complete | [Test scenario workbook](test-cases/Test_Scenarios.xlsx) with prioritized scenarios and checklist traceability |
| Detailed manual design | QA-005 Design detailed test cases and test data | Complete | [Test cases](test-cases/Test_Cases.xlsx), [test data](test-cases/Test_Data.xlsx), and [requirements traceability matrix](test-cases/RTM.xlsx) |
| Manual execution setup | QA-006.1 Prepare manual execution artifacts | Complete | [Execution workbook](test-execution/Test_Execution.xlsx), [execution guide](test-execution/EXECUTION_GUIDE.md), and [evidence conventions](test-execution/evidence/README.md) |
| Manual execution | QA-006.2 Verify and execute manual tests | Execution recorded, follow-up deferred | [Execution workbook](test-execution/Test_Execution.xlsx) and [handoff notes](HANDOFF.md) |
| Defect management setup | QA-007.1 Prepare defect management artifacts | Complete | [Bug tracking workbook](bug-reports/Bug_Tracking.xlsx), [lifecycle and severity guidelines](bug-reports/DEFECT_MANAGEMENT.md), [report template](bug-reports/BUG_REPORT_TEMPLATE.md), and [Jira reporting guide](bug-reports/BUG_REPORTING_GUIDE.md) |
| Defect management | QA-007.2 Triage, report, and retest defects | Open | Evidence-backed defect records and retest or regression results when applicable |
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
| Continuous integration | QA-010 Add GitHub Actions | Planned | Automated test execution and report artifact upload |
| Portfolio review | QA-011 Prepare the project for review | Planned | Verified documentation, traceable evidence, and repeatable run instructions |

## QA-002 verification remaining

- Retry the public demo in a normal manual browser. On 2026-11-05 the web request returned HTTP 403 and the in-app browser showed a security verification page.
- Confirm account field and credential validation, login and logout, and recovery with a controlled inbox if available. The user-reported registration without an email verification click is recorded under `FC-ACC-001`.
- Confirm search, category navigation, product details, available sorting and filtering, and cart add, update, remove, and subtotal behavior.
- Confirm checkout entry, billing, shipping, and pre-submit review up to the last safe point. Post-submit confirmation requires an authorized test-order sandbox.

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
