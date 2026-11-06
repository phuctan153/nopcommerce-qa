# Defect Management

Use the [Bug Register](Bug_Tracking.xlsx) for supported application defects and the [report template](BUG_REPORT_TEMPLATE.md) for reproducible details. Keep test outcomes in the [execution workbook](../test-execution/Test_Execution.xlsx).

## Triage observations

| Finding | How to distinguish it | Where to record it |
| --- | --- | --- |
| Confirmed application defect | Observable behavior contradicts a supported requirement under valid conditions. Reproduce safely when practical. A single observation can support a report if the evidence establishes the discrepancy and the retry limitation is documented. | Add a unique `BUG-###` record and reference it from the affected execution row. |
| Unverified requirement | The demo configuration or expected rule is still unknown. An observation differs from a provisional expectation, but the correct behavior has yet to be established. | Record the observation and question in execution notes. Use Blocked if uncertainty prevents a conclusive result. |
| Test case or fixture issue | Steps, data, or preconditions are inconsistent, or the test targets an unsupported setup. | Record the design or fixture issue and correct it with a traceable change before retesting. |
| Environment limitation | Access challenges, resets, unavailable mail, or demo restrictions prevent a reliable result. | Record the blocker and the last reliable step in execution notes. Resume once the prerequisite is restored. |

Before filing, check the related case and the basis for its expectation. Confirm the storefront, browser, operating system, session state, and relevant configuration. Recreate the synthetic fixture and retry in a clean session when safe. Search the register and any accessible Jira project for an existing report. Reuse the existing Bug ID for additional evidence. Use Duplicate if two filed records later prove to describe the same defect, and reference the canonical record.

## Lifecycle and statuses

The usual path is **New → Triaged → In Progress → Ready for Retest → Closed**. A failed retest moves the defect to **Reopened**. Record the reason for each exceptional transition in Notes.

| Status | Meaning |
| --- | --- |
| New | A supported defect has been filed and awaits triage. |
| Triaged | Impact, urgency, scope, and the next action have been reviewed. |
| In Progress | The owner is investigating or working on a fix. |
| Ready for Retest | A fix or relevant change is available in a testable environment. |
| Reopened | Retest found the discrepancy still present. |
| Closed | Retest verified the fix and relevant regression checks were reviewed. |
| Deferred | Work is postponed with a documented reason and review trigger. The defect remains unresolved. |
| Duplicate | The same defect is tracked by another record. Note the canonical Bug ID and Jira key if available. |

The dashboard's Resolved count includes Closed and Duplicate records. A duplicate disposition represents a tracking decision. Follow its canonical record for fix and retest progress.

## Severity and priority

Severity describes the observed impact on shoppers. Priority describes how urgently the defect should be addressed. Assign them separately using the affected journey, scope, workaround, and current risk.

| Severity | Impact |
| --- | --- |
| S1 | Critical. A primary flow is unusable for affected shoppers with no reasonable workaround, or serious data or security impact is evidenced. |
| S2 | Major. A significant feature fails or a core path is impaired, with limited scope or a workaround. |
| S3 | Moderate. A secondary behavior is incorrect or a limited issue causes user friction. |
| S4 | Minor. A cosmetic or wording issue has little functional effect. |

| Priority | Urgency |
| --- | --- |
| P0 | Immediate triage. Interrupt planned work because the risk requires urgent action. |
| P1 | High. Address in the next available fix cycle. |
| P2 | Normal. Schedule alongside other planned work. |
| P3 | Low. Keep in the backlog and revisit as needed. |

## Reproduction and evidence

- Include numbered steps from a known starting state. Record the related Test Case ID when available, execution ID, synthetic Test Data ID, actual date, environment, and any configuration that affects reproduction.
- State the expected result and its basis separately from the actual observation. Classify reproducibility as Always, Intermittent, or Once based on recorded attempts. For intermittent behavior, record hits and attempts under comparable conditions. Explain why a safe retry was unavailable if applicable.
- Reference the smallest useful screenshot or other evidence that shows the decisive state. Follow the [execution evidence conventions](../test-execution/evidence/README.md). Remove credentials, personal data, tokens, payment information, and sensitive URL values before sharing. If a safe screenshot cannot be kept, describe the observation and reason.

## Retest and regression

When a fix or relevant change is available, retest the original case with a fresh session and verified fixtures. Record the actual date, environment, outcome, and sanitized evidence. Run targeted checks on related behavior that the change could affect. Close the defect after the fix is verified and regression findings are reviewed. Reopen it if the same discrepancy persists. If the environment blocks retest, retain Ready for Retest and document the blocker and next action.
