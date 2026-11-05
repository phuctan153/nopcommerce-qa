# Testing Strategy

This strategy applies the priorities in [TP-001](TEST_PLAN.md) and the mitigations in the [risk assessment](RISK_ASSESSMENT.md). It starts with manual storefront verification and introduces a small automated suite once the selected behavior is repeatable.

## Test levels and coverage

| Level | Focus |
| --- | --- |
| Storefront system testing | Exercise shopper-facing functionality through the browser. Check observable account state, catalog results, cart lines, and checkout summaries. |
| End-to-end journey testing | Exercise handoffs across account, catalog, cart, and checkout. End the purchase journey at the last safe pre-submit step. Post-submit confirmation requires an authorized test-order sandbox. |

Trace each planned case to a [functional checklist](FUNCTIONAL_CHECKLIST.md) ID and the relevant priority in the test plan. Track design and execution separately. Report which P0 and P1 items were exercised and which remain pending, blocked, or unavailable. Keep the single user-reported registration observation identified as such. Exact storefront rules remain pending where QA-002 had no direct evidence.

## Manual testing approach

- **Exploratory testing.** Use short sessions focused on one module or handoff. Check access and fixtures first, then record visible controls, configuration branches, and open questions. Keep observations separate from expected behavior.
- **Functional testing.** Start with representative valid inputs and check shopper-visible outcomes. Compare cart amounts with displayed pricing and quantities, allowing for observed adjustments. Confirm checkout selections carry through to review where reachable.
- **Negative and boundary testing.** Use representative missing, malformed, and conflicting inputs. Explore password, search, and quantity boundaries after confirming supported rules. Limit invalid login attempts to protect the disposable account and keep traffic low.
- **Smoke testing.** Run a small P0 set first to check account access, product discovery, cart changes, and the safe checkout path. Record blocked paths and continue independent checks where possible.
- **Regression testing.** Retest a verified defect or changed behavior, then cover affected neighboring flows. For example, follow a cart quantity change with a subtotal and checkout review check where reachable.

## Test data management

Use synthetic names and addresses, run-specific identifiers, a disposable account, and a controlled inbox for email-dependent checks. Store reusable non-sensitive data and fixture selection notes under `docs/test-data/`. Keep credentials, reset links, and private inbox contents outside Git. Start each session with clean browser state and verify the current product, availability, pricing, and options before testing. Recreate disposable data after a reset.

## Future UI automation

Use Java, Selenium WebDriver, TestNG, and Maven in the planned `automation/` module. Keep selectors and UI actions in Page Object Model classes, assertions in TestNG tests, and browser setup in shared support code. Give each test its own WebDriver session, close it in teardown, and wait explicitly for observable UI state changes. Use a configurable storefront URL and browser. Review generated TestNG reports and retain sanitized failure evidence. GitHub Actions is a later phase.

Select candidates with high shopper impact, repeatable preconditions, safe isolated data, and a clear observable result. Prefer checks with stable UI controls and low maintenance cost. Product search and details, cart quantity updates, and subtotal recalculation are initial candidates once confirmed manually. Consider login only if a disposable account can be provisioned reliably. Keep mail delivery, volatile product selections, and configuration-dependent checkout branches manual until their dependencies are controllable. Keep final order placement outside public-demo automation.

## Defect and test reporting

- Record each run under `docs/test-execution/` with the run date, environment, fixtures, checklist or case IDs, outcomes, and sanitized evidence. Use **Passed**, **Failed**, **Blocked**, or **Not Run** for execution outcomes. Report scope exclusions separately.
- Use **Blocked** when a prerequisite fails and **Failed** when observed application behavior disagrees with a supported expectation. A planned check or a prior user report does not count as a QA execution result.
- File a report under `docs/bug-reports/` when evidence supports a defect. Include a stable ID, impact, expected and actual behavior, reproduction steps, and links to the related run and case. Record access challenges and resets in the execution notes.
- Summarize results by priority with unresolved gaps and defect impact. In later automation runs, distinguish product discrepancies from environment or test failures and retain the original outcome if a diagnostic rerun is needed.
