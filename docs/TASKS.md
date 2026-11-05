# Project Task Tracker

| Phase | Task | Status | Deliverable |
| --- | --- | --- | --- |
| Foundation | QA-001 Initialize project structure | Complete | Repository layout, contribution guidelines, README, ignore rules, and task tracker |
| Manual test design | QA-002 Application exploration and test scope definition | Documentation complete, manual verification pending | [Application overview](manual/APPLICATION_OVERVIEW.md), [test scope](manual/TEST_SCOPE.md), and [functional checklist](manual/FUNCTIONAL_CHECKLIST.md) |
| Test planning | QA-003 Test planning and risk assessment | Complete | [Test plan](manual/TEST_PLAN.md), [risk assessment](manual/RISK_ASSESSMENT.md), and [testing strategy](manual/TEST_STRATEGY.md) |
| Manual scenario design | QA-004 Design test scenarios in Excel | Complete | [Test scenario workbook](test-cases/Test_Scenarios.xlsx) with prioritized scenarios and checklist traceability |
| Automation foundation | QA-005 Set up the Maven automation project | Planned | Java dependencies, TestNG configuration, WebDriver lifecycle, and Page Object Model structure |
| UI automation | QA-006 Automate priority smoke scenarios | Planned | A runnable smoke suite and generated test reports |
| Continuous integration | QA-007 Add GitHub Actions | Planned | Automated test execution and report artifact upload |
| Portfolio review | QA-008 Prepare the project for review | Planned | Verified documentation, traceable evidence, and repeatable run instructions |

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
- Manual verification remains pending for 32 scenarios. Confirm current demo access, account and recovery behavior, available catalog controls, cart rules, and the safe checkout path before execution. Detailed test cases and execution records remain future work.
