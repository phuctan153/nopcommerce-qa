# QA-011 portfolio and responsive report review

Review date: 2026-11-17. Target: [published dashboard](https://phuctan153.github.io/nopcommerce-qa/), report run `44037419784`, report commit `445ad79`.

## Browser observations

The in-app browser was used with explicit viewport overrides, not a physical phone. Its exact browser version/OS was not exposed. Mobile observations do not establish Safari/iOS or real-device coverage.

| Viewport | Observed result |
| --- | --- |
| 390 × 844 | Overview, demonstration notice and three separate process states were readable. Document client/scroll widths both 375px: no page overflow. Failed filter retained two demo rows. Search `DEMO-001` plus Evidence demo module retained one result. |
| 320 × 568 | Both the filter controls and screenshot dialog worked; Escape closed the dialog and returned focus to its opener. Found page overflow: client width 305px, scroll width 316px. The nonwrapping run-status badge extended to x=316.47. The results table's own horizontal scrolling is intentional and separate. |
| 768 × 1024 | Overview, metrics, two insight panels and independent process states rendered without page overflow (client/scroll widths both 753px). |

Both demo screenshot viewers loaded on mobile. DEMO-002's image reported 1440 × 757 natural dimensions; the image, close control and full-image/download links fitted the dialog. The published report retained 14 Pass, 2 intentional Fail, 0 Skip with screenshot verification passed and deployment succeeded.

Evidence: [390px overview](../test-execution/evidence/Follow-up/QA-011_2026-11-17_mobile-390-overview.jpg), [390px image viewer](../test-execution/evidence/Follow-up/QA-011_2026-11-17_mobile-390-evidence.jpg), [320px overflow](../test-execution/evidence/Follow-up/QA-011_2026-11-17_mobile-320-heading-overflow.jpg), [768px overview](../test-execution/evidence/Follow-up/QA-011_2026-11-17_tablet-768-overview.jpg).

## Finding QA-011-RWD-001

- **Component:** this portfolio's report dashboard, not the nopCommerce storefront.
- **Reproduction:** open the current screenshot demonstration report at 320 × 568, navigate to Test overview, and inspect the run-status badge and page horizontal scrollbar.
- **Expected:** heading and status fit the page; only the wide result table needs horizontal scrolling.
- **Actual:** the heading and nonwrapping badge remain on one flex row. The badge causes about 11px of document overflow and is clipped at the right edge.
- **Impact:** low-severity responsive layout defect on narrow mobile viewports. Results and evidence remain reachable.
- **Change prepared:** under the existing 600px breakpoint, stack the heading/status vertically, bound the badge width and allow its text to wrap. Desktop rules and test-result semantics remain unchanged.
- **Verification:** Python 28/28, the existing DOM harness, JavaScript syntax, and normal Maven 14/14 passed. These checks do not prove browser layout. Visual retest of the modified CSS is **pending**, and the fix is **not deployed**.

The in-app browser could not reach the loopback preview server. Reading further from the user's personal Chrome was rejected by automatic review because of unrelated private tabs/account information. An isolated offline layout URL was also blocked by browser URL policy; no alternative browser or protocol workaround was used for that blocked preview. Do not mark the CSS fix visually verified until an approved HTTP(S) preview/deployment is accessible. The temporary loopback server was stopped.

## Portfolio review

- README presents the project scope, 11 browser cases plus 3 configuration checks, artifact links, execution instructions and explicit intentional-failure semantics.
- Historical manual counts and the new [dated follow-up](../test-execution/FOLLOW_UP_2026-11-17.md) are kept separate. Missing original fixtures, recovery limitations and the generic missing-password response remain visible. No confirmed storefront bug is invented to fill the Bug Register.
- Public demonstration results, verification and deployment are distinct. Reviewers can inspect source, workflow and masked screenshots.
- Checkout evidence stops before final submission. Account credentials, receiving inboxes and recovery tokens are absent from the new shared evidence.
- Final readiness remains conditional on the narrow-mobile fix retest and the documented manual gaps; no claim of complete end-to-end, cross-browser or real-device coverage is made.
