# Test Plan

| Field | Value |
| --- | --- |
| Identifier | TP-001 |
| Version | 1.0 |
| Project | nopCommerce QA Testing Portfolio |
| Test target | [Public nopCommerce demo storefront](https://demo.nopcommerce.com/) |
| Status | Planning baseline. Live verification pending. |

## Overview and objectives

This solo portfolio project evaluates the shopper-facing storefront through manual browser testing and prepares a small suite for later UI automation. The objectives are to verify account access, product discovery, cart accuracy, and safe checkout progression, and to document evidence and limitations that another tester can follow.

## Requirements and evidence

The [application overview](APPLICATION_OVERVIEW.md) provides the workflow model. The [test scope](TEST_SCOPE.md) defines coverage and priorities. The [functional checklist](FUNCTIONAL_CHECKLIST.md) provides traceable expectations in place of a formal requirements specification. The [task tracker](../TASKS.md) records milestones.

QA-002 recorded 18 checklist items pending manual verification, one user-reported registration that required no email verification click, and one post-submit check unavailable within the public-demo scope. The web request returned HTTP 403 and the browser showed a security verification page during that session. Confirm current access before executing tests. Required fields, product options, mail delivery, and checkout branches are still open questions.

## Test items and priorities

P0 covers the primary shopper journeys. P1 covers secondary paths, input validation, and configuration-dependent behavior. Checklist ranges identify the planning references.

| Test item | P0 | P1 | Checklist references |
| --- | --- | --- | --- |
| User account | Registration, login, logout | Invalid registration and login inputs, password recovery | FC-ACC-001 through FC-ACC-006 |
| Product catalog | Find and open a purchasable product | Category navigation, search feedback, sorting and filtering where offered | FC-CAT-001 through FC-CAT-005 |
| Shopping cart | Add a product, update quantity, reconcile subtotal | Remove lines, empty state, unsupported quantities | FC-CART-001 through FC-CART-004 |
| Checkout | Reach available billing, shipping, and pre-submit order review | Guest path where enabled and field validation | FC-CHK-001 through FC-CHK-004 |

Excluded coverage includes final order placement and post-submit confirmation on the public demo, real payments and payment information, external payment providers, administration, fulfillment, APIs, database and unit testing, security probing, load testing, and destructive testing. Post-submit confirmation (FC-CHK-005) requires an authorized test-order sandbox. Detailed cases and automation are later milestones.

## Environment and test data

- Start with one local desktop browser in a fresh profile or isolated session. Record browser version, operating system, storefront URL, run date, currency, and any relevant configuration visible in the UI. Browser selection and current access will be confirmed at execution.
- Check the demo's reachability and the required product and account state at the start of each session. Shared use and resets can change the available data.
- Use a disposable synthetic account with a run-specific identifier, a controlled inbox for recovery checks, and synthetic addresses for the destinations the demo accepts. Store credentials and recovery links outside Git.
- Select a currently purchasable product with simple pricing for cart totals and a shippable item for checkout. Record the product and options actually used. Change only the cart items created in the test session.

## Activities and deliverables

| Milestone | Activity and output |
| --- | --- |
| Planning baseline | Review scope and evidence. Publish the test plan, risk assessment, and testing strategy. |
| Manual design | Resolve reachable checklist questions, then draft prioritized cases under `docs/test-cases/` and reusable synthetic data under `docs/test-data/`. |
| Manual execution | Run a P0 smoke pass, then targeted P1 and regression checks as access permits. Save execution records under `docs/test-execution/` and evidence-backed defects under `docs/bug-reports/`. |
| Automation foundation and smoke suite | Set up the Java, Selenium WebDriver, TestNG, and Maven module in the later automation phase. Automate a small set of stable checks after manual verification. |
| CI and portfolio review | Add GitHub Actions in the CI phase. Review traceability, run instructions, evidence, and known coverage gaps before portfolio handoff. |

## Roles and responsibilities

The portfolio author owns planning, data preparation, execution, evidence, and defect triage. The author reviews evidence and staged documentation before publishing it. A controlled mailbox or an authorized sandbox is a dependency for the relevant checks and can be recorded as unavailable when access is absent.

## Entry and exit criteria

- **Entry.** Scope and priorities are agreed for the session. The storefront loads in the chosen browser. A suitable product, isolated session, and synthetic account are available for the journeys that need them. Recovery also requires a controlled inbox and safe delivery. Checkout requires an eligible cart and a safe stopping point.
- **Exit.** Every planned check has an evidence-backed result or a documented reason it was blocked, deferred, or unavailable. P0 gaps and open defects are summarized with impact and next action. Save sanitized evidence and link results to checklist or case IDs. A session with unresolved P0 coverage is reported as incomplete.

## Suspension and resumption

Suspend affected checks when access is blocked, a reset or stock change invalidates the fixture, the controlled inbox is unavailable, or checkout reaches a step that requires real data or final order placement. Record the last reliable checkpoint and continue independent safe checks when possible. Resume with a fresh session once the dependency is restored, revalidate fixtures, and rerun from the last reliable checkpoint. Reserve post-submit checks for an authorized sandbox.

## Defect management

Before filing, reproduce the discrepancy when safe, confirm the expectation against the agreed scope or reliable evidence, and rule out a reset, invalid fixture, or access problem. Assign a `BUG-` ID and record the related checklist or case ID, environment, steps, expected and actual behavior, impact, and sanitized evidence. Record environment blockers in the run notes. Retest reproducible defects when a fix or relevant configuration change is available.
