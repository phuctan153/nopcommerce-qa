# QA-011 portfolio and responsive report review

Review date: 2026-11-17. Target: [published dashboard](https://phuctan153.github.io/nopcommerce-qa/), report run `44037419784`, report commit `445ad79`.

## Initial browser observations before the fix

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
- **Original reproduction:** on report commit `445ad79`, open the screenshot demonstration at 320 × 568, navigate to Test overview, and inspect the run-status badge and page horizontal scrollbar.
- **Expected:** heading and status fit the page; only the wide result table needs horizontal scrolling.
- **Original actual:** the heading and nonwrapping badge remain on one flex row. The badge causes about 11px of document overflow and is clipped at the right edge.
- **Impact:** low-severity responsive layout defect on narrow mobile viewports. Results and evidence remain reachable.
- **Fix in `e7b4476`:** under the existing 600px breakpoint, stack the heading/status vertically, bound the badge width and allow its text to wrap. Desktop rules and test-result semantics remain unchanged.
- **Verification:** Python 28/28, the existing DOM harness, JavaScript syntax, and normal Maven 14/14 passed. Subsequent hosted normal and demo runs succeeded, including Pages deployment. The modified CSS was visually retested on the public report; status: **fixed and verified**.

The in-app browser could not reach the loopback preview server. Reading further from the user's personal Chrome was rejected by automatic review because of unrelated private tabs/account information. An isolated offline layout URL was also blocked by browser URL policy; no alternative browser or protocol workaround was used for that blocked preview. The temporary loopback server was stopped. The tester later authorized committing/pushing the fix. Verification then used the public HTTP(S) deployment in an isolated in-app tab, without interacting further with personal Chrome.

## Portfolio review

- README presents the project scope, 11 browser cases plus 3 configuration checks, artifact links, execution instructions and explicit intentional-failure semantics.
- Historical manual counts and the new [dated follow-up](../test-execution/FOLLOW_UP_2026-11-17.md) are kept separate. Missing original fixtures, recovery limitations and the generic missing-password response remain visible. No confirmed storefront bug is invented to fill the Bug Register.
- Public demonstration results, verification and deployment are distinct. Reviewers can inspect source, workflow and masked screenshots.
- Checkout evidence stops before final submission. Account credentials, receiving inboxes and recovery tokens are absent from the new shared evidence.
- Responsive report review is complete for the tested browser/viewport matrix. The documented manual gaps remain; no claim of complete end-to-end, cross-browser or real-device coverage is made.

## Hosted deployment and visual retest

Both runs used report code commit `e7b4476a52550b5faa6be6211fd18a89ed4f633e`:

- [Normal run 44051754267](https://github.com/phuctan153/nopcommerce-qa/actions/runs/44051754267): smoke and deployment succeeded. Downloaded artifact 14313044948 retained 14 Pass, 0 Fail, 0 Skip, no failure screenshots and passed smoke verification.
- [Demo run 44052146636](https://github.com/phuctan153/nopcommerce-qa/actions/runs/44052146636): smoke and deployment succeeded. Downloaded artifact 14313993043 retained 14 Pass, 2 exact expected Fail, 0 Skip, two matching screenshots and passed screenshot-demo verification bound to the sanitized XML digest. The public dashboard was verified against this run/commit and retains the demo.

| Retest | Observed result |
| --- | --- |
| 320 × 568, normal and demo | Client/scroll widths both 305px. Heading/status stack vertically; the long demo badge ends at x=182.41, within the page. No document overflow. |
| 390 × 844, demo | Client/scroll widths both 375px. DEMO-002 viewer loaded and fitted; close control worked. |
| 768 × 1024, demo | Client/scroll widths both 753px; overview, metrics and insight panels remained readable. |
| 1440 × 900, normal | Client/scroll widths both 1425px. Heading/status remain on one row; desktop layout is preserved. |
| Filters and viewers | Failed + Evidence demo + DEMO-001 search retained one result. Both DEMO-001 and DEMO-002 images loaded at natural 1440 × 757. Escape closed DEMO-001 and returned focus to its opener. No privacy fields were exposed in the masked images. |

Evidence: [320px normal](../test-execution/evidence/Follow-up/QA-011_2026-11-17_mobile-320-fixed-normal.jpg), [320px demo](../test-execution/evidence/Follow-up/QA-011_2026-11-17_mobile-320-fixed-demo.jpg), [320px viewer](../test-execution/evidence/Follow-up/QA-011_2026-11-17_mobile-320-fixed-viewer.jpg), [390px viewer](../test-execution/evidence/Follow-up/QA-011_2026-11-17_mobile-390-fixed-viewer.jpg), [768px demo](../test-execution/evidence/Follow-up/QA-011_2026-11-17_tablet-768-fixed-demo.jpg), [desktop normal](../test-execution/evidence/Follow-up/QA-011_2026-11-17_desktop-fixed-normal.jpg).

Viewport overrides were reset and filters cleared after verification. Real phones and other browser engines were not tested. A final documentation-only commit uses `[skip ci]` to preserve the verified hosted demo.
