# Application Overview

The [nopCommerce public demo](https://demo.nopcommerce.com/) is the shopper-facing storefront for this portfolio. The exploration covers the journey from finding a product to reviewing an order before submission.

## Evidence and access

- **Directly observed on 2026-11-05.** The web request returned HTTP 403, and the in-app browser displayed Cloudflare's security verification page. The storefront UI did not load during exploration, so this establishes an access limitation for the session.
- **User-reported observation.** The user manually registered an account on the public demo and reported that no email verification click was required during that registration. This applies to the reported registration only. The product supports configurable registration methods. [nopCommerce customer settings](https://docs.nopcommerce.com/en/running-your-store/customer-management/customer-settings.html)
- **Documentation-based workflow model.** The actions and flows below describe expected product capabilities. Except for the narrow registration observation above, storefront availability, exact UI labels, and behavior are **Pending Manual Verification**.

## User roles

| Role | Storefront responsibilities to explore |
| --- | --- |
| Guest shopper | Browse and search the catalog, inspect products, and explore cart access. Guest checkout depends on store configuration. |
| Registered customer | Sign in and out, request account recovery, and use an authenticated cart and checkout session. |
| Store administrator | Configures storefront behavior, catalog, and checkout options. Administration is outside the portfolio scope. |

## User account

Purpose is to let shoppers establish an identity, start and end a session, and recover access.

| Feature | Actions and main flow | Validations to investigate | Preconditions and risks |
| --- | --- | --- | --- |
| Registration | Open registration, enter customer details and credentials, submit, and inspect the completion or activation state. | Required fields, email format and uniqueness, password rules, and confirmation mismatch. | Registration must be enabled. Use unique synthetic data. Activation mode and required fields can vary by store. |
| Login | Enter the configured identifier and password, submit, and inspect the signed-in state. | Valid credentials establish a session. Missing or incorrect credentials leave the user signed out with useful feedback. | Requires an active test account and a signed-out session. Demo resets can invalidate credentials. Keep failed attempts limited. |
| Logout | Use the sign-out control and revisit a page that requires authentication. | Authenticated navigation clears and protected pages require sign-in again. | Requires a signed-in session. Cached content and multiple tabs can obscure the actual session state. |
| Password recovery | Open the recovery form, request a reset for a controlled test account, and use the link if safe mail delivery is available. | Email input feedback, delivery to the controlled inbox, and valid, expired, or reused link behavior where feasible. | Requires a disposable test account, a controlled inbox, and working outbound mail for end-to-end coverage. Recovery links are sensitive and delivery may be unavailable. |

Product reference. [Customer settings](https://docs.nopcommerce.com/en/running-your-store/customer-management/customer-settings.html)

## Product catalog

Purpose is to help shoppers discover products, compare the available choices, and identify an item they can add to the cart.

| Feature | Actions and main flow | Validations to investigate | Preconditions and risks |
| --- | --- | --- | --- |
| Search | Enter a search term, inspect matching products or the empty state, and open a result. | Known products can be found, unmatched terms show an informative state, and short or empty terms receive feedback appropriate to the configured rules. | Search must be enabled. Use a product visible in the current catalog. Search settings and changing demo data affect results. |
| Categories | Open a category or subcategory and browse its product listing. | Navigation lands on the selected category and displays its products, subcategories, or a useful empty state. | Requires visible categories and representative products. Category contents can change between sessions. |
| Product details | Open a product and inspect its title, price, purchase availability, and available options. | The details match the selected product. Required options receive feedback before the item can be added. | Requires a published product. Stock, prices, and option requirements are product-specific. |
| Sorting | Select an available sort option on a listing and inspect the resulting order. | Visible products follow the selected direction or criterion, accounting for ties. | Requires an enabled sort control and distinct comparison values. A short listing or equal values can hide errors. |
| Filtering | Apply and clear a supported filter on a suitable listing. | Results match the active filter and clearing it restores the relevant broader listing. | Requires an enabled filter and products with suitable attributes or price values. Filter options may vary across categories. |

Product references. [Catalog settings](https://docs.nopcommerce.com/en/running-your-store/catalog/catalog-settings.html) · [Products](https://docs.nopcommerce.com/en/running-your-store/catalog/products/add-products.html)

## Shopping cart

Purpose is to let shoppers review and change their selections before checkout.

| Feature | Actions and main flow | Validations to investigate | Preconditions and risks |
| --- | --- | --- | --- |
| Add item | Select any required options, add a purchasable product, and open the cart. | The cart shows the chosen product, options, and quantity. Unsupported selections receive feedback. | Requires a purchasable item and an isolated session. Stock or option changes can invalidate the chosen item. |
| Update quantity | Change the quantity on an existing line, apply the update if required, and inspect the line. | A supported quantity is saved and the amount updates. Investigate how zero and values outside supported limits are handled. | Requires an item in the current cart. Stock, per-item limits, and the meaning of zero can vary. |
| Remove item | Remove a line from the current session's cart and inspect the resulting contents. | The selected line disappears, other lines remain, and removing the last item shows an empty cart. | Use only items added in the test session. Duplicate products with different options can make the intended line ambiguous. |
| Subtotal | Compare the displayed subtotal with line amounts before and after cart changes. | The subtotal reconciles with the displayed quantities, prices, and applicable adjustments. | Prefer a simple item with known pricing. Tax display, discounts, rounding, and currency can affect reconciliation. |

Product reference. [Shopping carts and wishlists](https://docs.nopcommerce.com/en/running-your-store/order-management/shopping-carts-and-wishlists.html)

## Checkout

Purpose is to gather billing and delivery information and let the shopper review the purchase before it is placed.

| Feature | Actions and main flow | Validations to investigate | Preconditions and risks |
| --- | --- | --- | --- |
| Checkout entry | Open checkout from a populated cart and inspect the sign-in or guest path offered. | An eligible cart can proceed through the offered path. Record any terms or minimum-cart requirements shown. | Requires a checkout-eligible cart. Guest access and checkout gates are configurable. |
| Billing | Select or enter a synthetic billing address if prompted, then continue. | Required fields receive feedback and country-dependent fields update when applicable. | Requires an eligible cart and any required sign-in. Billing may be prefilled or skipped by configuration. |
| Shipping | Select or enter a shipping address when required and inspect the delivery methods offered for it. | Incomplete addresses receive feedback where applicable. Eligible methods and displayed charges reflect the destination and cart. | Requires a shippable item and an eligible synthetic destination. A method may be selected automatically or an address step skipped. |
| Order review and confirmation | Inspect the pre-submit order summary when reachable with a safe payment option. A completed-order view or redirect follows submission in supported configurations. | Compare the pre-submit items, addresses, shipping, and totals with earlier selections. Reserve post-submit order reference and completion checks for an authorized sandbox. | Requires a valid cart, addresses, and a safe payment option if prompted. Stop before final order placement on the public demo. |

Product references. [Order settings](https://docs.nopcommerce.com/en/running-your-store/order-management/order-settings.html) · [Shipping settings](https://docs.nopcommerce.com/en/getting-started/configure-shipping/shipping-settings.html)

## Environment limitations

nopCommerce describes the demo as shared and reset hourly. It also says the storefront and admin demos are separate. Accounts, inventory, and configuration should be checked again before relying on them in a test session. [Official demo notes](https://www.nopcommerce.com/en/demo)

Use synthetic data and record the date, browser, and relevant storefront state with future evidence. The access challenge leaves catalog state, enabled options, mail delivery, and checkout progression unconfirmed. See the [functional checklist](FUNCTIONAL_CHECKLIST.md) for the remaining observations.
