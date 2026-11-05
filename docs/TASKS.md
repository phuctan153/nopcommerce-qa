# Project Task Tracker

| Phase | Task | Status | Deliverable |
| --- | --- | --- | --- |
| Foundation | QA-001 Initialize project structure | Complete | Repository layout, contribution guidelines, README, ignore rules, and task tracker |
| Manual test design | QA-002 Application exploration and test scope definition | Documentation complete, manual verification pending | [Application overview](manual/APPLICATION_OVERVIEW.md), [test scope](manual/TEST_SCOPE.md), and [functional checklist](manual/FUNCTIONAL_CHECKLIST.md) |
| Manual test design | QA-003 Design manual test cases and data | Planned | Prioritized test cases and reusable synthetic data |
| Manual execution | QA-004 Execute manual tests | Planned | Execution records, evidence, and defect reports for any observed issues |
| Automation foundation | QA-005 Set up the Maven automation project | Planned | Java dependencies, TestNG configuration, WebDriver lifecycle, and Page Object Model structure |
| UI automation | QA-006 Automate priority smoke scenarios | Planned | A runnable smoke suite and generated test reports |
| Continuous integration | QA-007 Add GitHub Actions | Planned | Automated test execution and report artifact upload |
| Portfolio review | QA-008 Prepare the project for review | Planned | Verified documentation, traceable evidence, and repeatable run instructions |

## QA-002 verification remaining

- Retry the public demo in a normal manual browser. On 2026-11-05 the web request returned HTTP 403 and the in-app browser showed a security verification page.
- Confirm account field and credential validation, login and logout, and recovery with a controlled inbox if available. The user-reported registration without an email verification click is recorded under `FC-ACC-001`.
- Confirm search, category navigation, product details, available sorting and filtering, and cart add, update, remove, and subtotal behavior.
- Confirm checkout entry, billing, shipping, and pre-submit review up to the last safe point. Post-submit confirmation requires an authorized test-order sandbox.
