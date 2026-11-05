# Functional Checklist

These investigation prompts define coverage for the public demo. The status applies to the specific behavior in each row.

- **Verified** means the behavior was observed directly or explicitly reported by the user. The evidence column identifies the source and limits of the observation.
- **Pending** means **Pending Manual Verification**. Vendor documentation informs the expected behavior, while availability and results in the demo remain to be checked.
- **Not Available** means the check cannot be exercised within the current environment or scope. The evidence column states the reason.

The 2026-11-05 web request returned HTTP 403 and the browser showed a Cloudflare security verification page. See the [application overview](APPLICATION_OVERVIEW.md) for the access record and product references.

## User account

| ID | Expected behavior to investigate | Status | Evidence or observation notes |
| --- | --- | --- | --- |
| FC-ACC-001 | Determine whether completing registration requires an email verification click. | Verified | User-reported only. No email verification click was required in the user's manual registration on the public demo. |
| FC-ACC-002 | Registration provides useful feedback for required fields, malformed or duplicate email, and invalid or mismatched passwords. | Pending | Exact fields, rules, and messages require storefront access. |
| FC-ACC-003 | Valid credentials establish a signed-in session. Missing or invalid credentials leave the shopper signed out. | Pending | Requires an active synthetic account and a limited number of invalid attempts. |
| FC-ACC-004 | Logout ends the session and subsequent protected navigation requires sign-in. | Pending | Requires a signed-in session. |
| FC-ACC-005 | The recovery form accepts a valid request and gives useful feedback for malformed input. | Pending | Confirm form availability and use only an account and inbox controlled for testing. |
| FC-ACC-006 | A received reset link supports a reset for the intended account. Expired or reused links are handled when feasible. | Pending | Depends on controlled mail delivery and link settings. Keep links and credentials out of evidence. |

## Product catalog

| ID | Expected behavior to investigate | Status | Evidence or observation notes |
| --- | --- | --- | --- |
| FC-CAT-001 | Search finds a known product and provides an informative state for unmatched or invalid queries. | Pending | Select a product from the current demo catalog. Confirm minimum term rules if exposed. |
| FC-CAT-002 | Category and subcategory navigation opens the expected listing. | Pending | Record the visible menu and category structure. |
| FC-CAT-003 | A selected listing opens the correct product details with pricing, available options, and purchase availability. | Pending | Use a currently published product. |
| FC-CAT-004 | An available sort option orders listings by the selected criterion. | Pending | Use a listing with distinct values and record the offered sort options. |
| FC-CAT-005 | An available filter narrows results as shown, and clearing it restores the applicable listing. | Pending | Record which filters and categories are available. |

## Shopping cart

| ID | Expected behavior to investigate | Status | Evidence or observation notes |
| --- | --- | --- | --- |
| FC-CART-001 | Adding a purchasable product places the correct item, options, and quantity in the cart. Required options are enforced where applicable. | Pending | Requires a purchasable product and an isolated session. |
| FC-CART-002 | A supported quantity update is saved. Invalid or out-of-range quantities receive appropriate handling. | Pending | Confirm stock and quantity rules, including how zero is treated. |
| FC-CART-003 | Removing a selected line preserves other lines. Removing the last line shows an empty cart. | Pending | Use only items added during the test session. |
| FC-CART-004 | Subtotal reconciles with the displayed lines and updates after cart changes. | Pending | Record currency and any discount, tax display, or rounding that affects the comparison. |

## Checkout

| ID | Expected behavior to investigate | Status | Evidence or observation notes |
| --- | --- | --- | --- |
| FC-CHK-001 | An eligible cart reaches checkout through the available guest or authenticated path. | Pending | Confirm guest availability and any cart or sign-in requirements. |
| FC-CHK-002 | Billing accepts applicable synthetic details and flags incomplete required fields. | Pending | Confirm which fields appear and whether billing is skipped or prefilled. |
| FC-CHK-003 | Shipping uses the applicable destination and shows eligible delivery options and charges when required. | Pending | Requires a shippable product and eligible synthetic address. Note skipped steps. |
| FC-CHK-004 | The pre-submit order summary matches the cart and selected billing and delivery information. | Pending | Inspect only if reachable without real payment information. Stop before final order placement. |
| FC-CHK-005 | After a permitted test submission, the shopper can identify the completed order or reach its details. | Not Available | Outside the public-demo verification boundary. Requires an authorized test-order sandbox. |
