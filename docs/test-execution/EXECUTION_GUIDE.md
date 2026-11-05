# Manual Execution Guide

Use the [execution workbook](Test_Execution.xlsx) for run records, the [test cases](../test-cases/Test_Cases.xlsx) for steps and expected results, and the [test data workbook](../test-cases/Test_Data.xlsx) for fixture rules. The [traceability matrix](../test-cases/RTM.xlsx) identifies conditional cases and scope exclusions.

## Prepare a session

1. Select the cases for the session. Start with reachable P0 cases, then run the planned P1 cases as prerequisites allow.
2. Open a fresh private window or a dedicated QA browser profile. Start from a signed-out state with an empty cart unless the case specifies another precondition. Keep separate cases independent.
3. Verify that the public demo storefront loads normally. Record the environment, including the storefront, browser version, operating system, and any relevant configuration. If a security challenge prevents access, record the blocker and stop affected work.
4. Resolve each Test Data ID in the data workbook. Generate a fresh synthetic registration identifier and provision disposable accounts as needed. Use an approved controlled inbox for recovery or activation when required. Verify stock, cart eligibility, and synthetic address suitability before the relevant cases. Keep actual credentials and reset links outside Git.

## Execute and record

1. Find the row by Test Case ID. Keep the imported IDs, priority, and title intact. Follow the numbered steps in the case workbook and use its Expected Result as the comparison.
2. For an actual attempt, enter the test environment, local execution date in `YYYY-MM-DD` format, and tester identifier. Keep Actual Result factual. Describe what the UI showed or how its state changed, including the step where it diverged. Use Notes for assumptions, blockers, and the next action.
3. Set the status using the dropdown. Record any sanitized evidence path and enter a Defect ID only after a defect has been filed. For an intentional skip without an attempt, record the reason in Notes and leave execution date and actual result blank.
4. Stop checkout at the last safe pre-submit step. If reaching the next expected checkpoint requires real payment data or final order placement, record the safety constraint and mark the affected case Blocked.
5. Review the Execution Summary at the end of the session. Executed Cases is Pass plus Fail. Execution Progress divides Executed Cases by Total Test Cases. Pass Rate divides Pass by Pass plus Fail and shows N/A when there are no conclusive outcomes. Blocked, Skipped, and Not Run are counted separately. Filters affect the visible rows while overall metrics include all imported cases.

| Status | Use when |
| --- | --- |
| Not Run | No attempt or intentional disposition has been recorded. |
| Pass | The observable result satisfies the case's expected result. |
| Fail | An observable result contradicts a supported expectation under valid preconditions. |
| Blocked | A missing prerequisite, access issue, reset, unconfirmed rule, or safety boundary prevents a conclusive result. Record what prevented execution and the last reliable step. |
| Skipped | The case was intentionally omitted from the session. Record the reason and leave the attempt fields blank if no attempt took place. |

## Recover after a demo reset

If the session loses its account, cart, or product fixture, pause affected cases and record the last reliable checkpoint. Keep the interrupted attempt as Blocked if the reset prevented a conclusive result. Start a fresh browser session, recreate disposable accounts and carts, and verify current products, prices, and options. Rerun from a reproducible starting point. Preserve a dated snapshot of the prior run before updating an execution row, and use a new attempt number for its evidence files.

## Collect evidence and report defects

- Follow the [evidence conventions](evidence/README.md). Capture the decisive UI state for a failure or blocker when safe. Use a sanitized path in Evidence Reference and record the relevant step in Actual Result or Notes.
- Before reporting a potential defect, recheck the expectation, fixture, account state, and environment. Reproduce once in a clean session when safe. Record configuration uncertainty or access issues as blockers while investigating.
- For a supported defect, record the test case and execution IDs, environment, reproduction steps, expected and actual results, shopper impact, and evidence under [bug reports](../bug-reports/). Link the filed defect's `BUG-` ID from the execution row.
