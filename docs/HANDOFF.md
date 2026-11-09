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
| QA-007.2 | Open for triage, evidence-backed defect reports, and retesting when applicable. |
| QA-008 | Complete. The Maven module, WebDriver lifecycle, and first page object compile. All three browser-free configuration tests pass. |
| QA-009 through QA-011 | Storefront smoke tests, GitHub Actions, and portfolio review are planned. |

The manual execution commits through Checkout were pushed to `origin/main`, ending at `0bd868e`. Local changes to `Test_Cases.xlsx` and `Test_Data.xlsx` and the three original Checkout screenshots are outside those commits. Preserve them and check their contents before staging any future change.

## Manual execution checkpoint

- The [execution workbook](test-execution/Test_Execution.xlsx) reports 28 Pass, 3 Fail, 5 Blocked, and 2 Skipped. These are tester-reported dispositions. Preserve the observations and statuses unless the tester authorizes a correction.
- Before filing defects for `TC-CAT-008`, `TC-CART-002`, or `TC-CHK-002`, verify filter availability, a product with an unset required option, or an offered guest checkout route respectively. Use the [defect management guide](bug-reports/DEFECT_MANAGEMENT.md) and [Bug Register](bug-reports/Bug_Tracking.xlsx) for confirmed defects.
- The `TC-REG-002` attempt left several fields blank. Its case requires exactly one. The `TC-CHK-008` evidence was captured after order placement. If those cases are revisited, preserve the original attempts and stop checkout before final submission.
- The tester runs manual browser checks. Record actual dates and observations, then link sanitized evidence using the [execution guide](test-execution/EXECUTION_GUIDE.md) and [evidence conventions](test-execution/evidence/README.md).

## Automation checkpoint

- On 2026-11-09, `mvn -f automation/pom.xml test` passed on Windows with JDK 21.0.4 and Maven 3.9.9. Surefire reported three tests, zero failures, zero errors, and zero skips.
- The initial dependency download hit a Java certificate trust error. A per-process Windows trust-store setting let Maven populate its cache. The plain command passed on the next run. See the [automation guide](../automation/README.md) for the command used.
- Browser launch, WebDriver session behavior, and live storefront access still need verification in QA-009.

## Next session

1. Start QA-009 with a stable storefront smoke candidate. Check the [case workbook](test-cases/Test_Cases.xlsx), [test data workbook](test-cases/Test_Data.xlsx), [traceability matrix](test-cases/RTM.xlsx), and recorded execution evidence before selecting cases.
2. Verify browser startup and storefront access when running the first smoke test. Keep the scenarios independent and stop checkout at the last safe pre-submit step.
3. Refresh this handoff and the task tracker when the next task changes the project state. Commit or push only on explicit request. If Git for Windows encounters a certificate trust error, previous pushes succeeded with the per-command option `-c http.sslBackend=schannel`.
