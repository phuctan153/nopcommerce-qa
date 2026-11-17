# Functional Checklist

These investigation prompts define coverage for the public demo. The status applies to the specific behavior in each row.

- **Verified** means the behavior was observed directly or explicitly reported by the user. The evidence column identifies the source and limits of the observation.
- **Pending** means **Pending Manual Verification**. Vendor documentation informs the expected behavior, while availability and results in the demo remain to be checked.
- **Not Available** means the check cannot be exercised within the current environment or scope. The evidence column states the reason.

The 2026-11-05 web request returned HTTP 403 and the browser showed a Cloudflare security verification page. See the [application overview](APPLICATION_OVERVIEW.md) for the access record and product references.

The [2026-11-17 public UI follow-up](../test-execution/FOLLOW_UP_2026-11-17.md) supplies direct observations below. Verified applies to the described fixtures and behavior, not every configuration or browser. The Excel run remains a separate historical record.

## User account

| ID | Expected behavior to investigate | Status | Evidence or observation notes |
| --- | --- | --- | --- |
| FC-ACC-001 | Determine whether completing registration requires an email verification click. | Verified | 2026-11-17: Direct observation: disposable registration completed and signed in without an email click, consistent with the earlier user report. Applies to this run only. |
| FC-ACC-002 | Registration provides useful feedback for required fields, malformed or duplicate email, and invalid or mismatched passwords. | Verified | 2026-11-17: Only First name omitted, malformed/existing email, matching 5-character password below exposed minimum 6, and mismatched confirmation were each rejected. No exhaustive length/character claim. |
| FC-ACC-003 | Valid credentials establish a signed-in session. Missing or invalid credentials leave the shopper signed out. | Verified | 2026-11-17: Correct credentials opened the synthetic account; one wrong-password attempt and an omitted password were rejected. Omitted-password feedback is generic; the narrower TC-AUTH-002 expectation remains under review. |
| FC-ACC-004 | Logout ends the session and subsequent protected navigation requires sign-in. | Verified | 2026-11-17: Logout restored guest navigation; /customer/info required sign-in without revealing account content. |
| FC-ACC-005 | The recovery form accepts a valid request and gives useful feedback for malformed input. | Pending | 2026-11-17: Form available and malformed email rejected (TC-AUTH-006 Pass). Valid request remains untested: tester confirmed no controlled receiving inbox. |
| FC-ACC-006 | A received reset link supports a reset for the intended account. Expired or reused links are handled when feasible. | Pending | 2026-11-17: No controlled inbox or legitimate fresh/expired/consumed links. TC-AUTH-007/008/009 remain Blocked. Credential-change completion requires user handoff. |

## Product catalog

| ID | Expected behavior to investigate | Status | Evidence or observation notes |
| --- | --- | --- | --- |
| FC-CAT-001 | Search finds a known product and provides an informative state for unmatched or invalid queries. | Verified | 2026-11-17: HTC returned both named HTC fixtures; generated query returned no products; ab was constrained with an exposed minimum of 3 characters. |
| FC-CAT-002 | Category and subcategory navigation opens the expected listing. | Verified | 2026-11-17: Computers displayed its subcategories; selecting Notebooks opened the child heading and listing. |
| FC-CAT-003 | A selected listing opens the correct product details with pricing, available options, and purchase availability. | Verified | 2026-11-17: HTC smartphone matched search title/URL, $245 and available Add to cart action. Build your own computer exposed required configuration fields. |
| FC-CAT-004 | An available sort option orders listings by the selected criterion. | Verified | 2026-11-17: Notebooks Price: Low to High produced six increasing distinct prices ($1350 through $1800). Other sort criteria were not repeated. |
| FC-CAT-005 | An available filter narrows results as shown, and clearing it restores the applicable listing. | Verified | 2026-11-17: HP manufacturer filter on Notebooks retained two HP products, excluded Apple MacBook Pro and restored six original names when cleared. No global filter-availability claim. |

## Shopping cart

| ID | Expected behavior to investigate | Status | Evidence or observation notes |
| --- | --- | --- | --- |
| FC-CART-001 | Adding a purchasable product places the correct item, options, and quantity in the cart. Required options are enforced where applicable. | Verified | 2026-11-17: HTC smartphone quantity 1 appeared at $245; only required RAM omitted on Build your own computer produced feedback and left the cart empty. |
| FC-CART-002 | A supported quantity update is saved. Invalid or out-of-range quantities receive appropriate handling. | Pending | 2026-11-17: Quantity 1 to 2 persisted after reload with $490 line/subtotal. Zero removed the controlled HTC line in the final check. Limit-plus-one remains blocked without a safe confirmed fixture. |
| FC-CART-003 | Removing a selected line preserves other lines. Removing the last line shows an empty cart. | Verified | 2026-11-17: Removing HTC smartphone preserved HTC One Mini Blue at quantity 1 / $100; removing the final line showed empty cart. |
| FC-CART-004 | Subtotal reconciles with the displayed lines and updates after cart changes. | Verified | 2026-11-17: Undiscounted USD fixture: $245 x 1 = $245, $245 x 2 = $490. Gift wrapping No, displayed shipping/tax $0. |

## Checkout

| ID | Expected behavior to investigate | Status | Evidence or observation notes |
| --- | --- | --- | --- |
| FC-CHK-001 | An eligible cart reaches checkout through the available guest or authenticated path. | Verified | 2026-11-17: Explicit guest button reached Billing with guest navigation after approved terms acceptance. Signed-in entry also reached Billing after restoring the unavailable disposable fixture. |
| FC-CHK-002 | Billing accepts applicable synthetic details and flags incomplete required fields. | Verified | 2026-11-17: Only street omitted produced Street address is required; supplying it advanced to Shipping address. |
| FC-CHK-003 | Shipping uses the applicable destination and shows eligible delivery options and charges when required. | Verified | 2026-11-17: Separate synthetic US/NY address: missing City blocked progress; valid address exposed three manual methods. Ground $0 matched review. Automatic-shipping condition absent for this fixture. |
| FC-CHK-004 | The pre-submit order summary matches the cart and selected billing and delivery information. | Verified | 2026-11-17: Quantity 1 HTC, $245 subtotal/total, Ground $0, tax $0 and entered addresses/payment choice matched. Stopped before CONFIRM; no card details or order submission. |
| FC-CHK-005 | After a permitted test submission, the shopper can identify the completed order or reach its details. | Not Available | Outside the public-demo verification boundary. Requires an authorized test-order sandbox. |
