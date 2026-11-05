# Test Scope

## Objectives

- Verify that shoppers can manage account access, find products, and build an accurate cart.
- Verify that an eligible cart can progress through the available checkout steps up to the last safe review point.
- Record environment-dependent behavior and evidence for prioritizing later manual cases and smoke automation.

## In scope

| Module | Coverage boundary |
| --- | --- |
| User account | Registration and activation behavior, login, logout, and password recovery. Recovery delivery and reset depend on a controlled inbox and an isolated account. |
| Product catalog | Search, category and subcategory navigation, product details, and sorting and filtering where offered. |
| Shopping cart | Add an item, update its quantity, remove it from the current session, and reconcile line totals and subtotal. |
| Checkout | Entry and available guest or signed-in paths, billing, shipping, and pre-submit order review where safely reachable. Document post-submit confirmation as future sandbox coverage. |

## Out of scope

- Admin storefront configuration, fulfillment, cancellations, refunds, and other back-office workflows.
- Real payments, real payment information, final order placement on the public demo, and validation of external payment providers.
- API, database, and unit testing, load or stress testing, destructive testing, and security probing.
- Detailed test case design, Selenium implementation, CI setup, and formal manual execution for QA-002.

## Testing levels and types

| Level or type | Planned application |
| --- | --- |
| Storefront system testing | Check user-visible behavior through the public browser interface. |
| End-to-end journey coverage | Follow account, product, cart, and checkout handoffs up to the safe pre-submit boundary. |
| Exploratory testing | Identify visible controls, configuration-dependent branches, and evidence gaps. |
| Functional testing | Cover representative valid and invalid inputs, applicable boundaries, state changes, and totals. |
| Smoke and regression testing | Use stable, high-priority journeys for a later repeatable suite. |

## Priorities

| Priority | Coverage |
| --- | --- |
| P0 | Registration, login and logout, find and open a purchasable product, add and update cart lines, check subtotal, and reach the available billing, shipping, and order review steps. |
| P1 | Password recovery, category navigation, sorting and filtering, cart removal and empty state, invalid inputs, and guest checkout where enabled. |

Priority reflects shopper impact and expected frequency. Verification status and dependencies are tracked in the [functional checklist](FUNCTIONAL_CHECKLIST.md).

## Assumptions, risks, and constraints

| Assumption or risk | Effect on coverage |
| --- | --- |
| Demo access can vary by browsing environment. | Establish access in a normal manual browser before capturing behavior. Track access challenges as environment limitations. |
| Public demo data can reset or change through shared use. | Recheck fixtures and account state at the start of each session. Record the products, currency, and availability relevant to observations. [Official demo notes](https://www.nopcommerce.com/en/demo) |
| Registration, catalog controls, and checkout are configuration-dependent. | Record observed options before setting exact assertions. The reported registration without an email verification click represents one session. [Customer settings](https://docs.nopcommerce.com/en/running-your-store/customer-management/customer-settings.html) · [Catalog settings](https://docs.nopcommerce.com/en/running-your-store/catalog/catalog-settings.html) · [Order settings](https://docs.nopcommerce.com/en/running-your-store/order-management/order-settings.html) |
| Mail delivery, shipping eligibility, and payment options may limit exploration. | Use a controlled inbox and synthetic addresses. Stop at the last safe reachable checkout step. Post-submit checks require a sandbox authorized for test orders. |

## Manual verification criteria

- **Entry.** The demo is reachable in a normal browser. A synthetic account, controlled inbox, and suitable in-stock product are available for the checks that need them.
- **Exit.** Each checklist item has an evidence-backed status or a documented reason it remains pending or unavailable. Record the environment and sanitize evidence. Prioritize unresolved P0 items for a follow-up session.
