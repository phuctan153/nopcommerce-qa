# nopCommerce QA Testing Portfolio

This portfolio documents manual testing and planned UI automation for a sample e-commerce storefront. It will show how test coverage is designed, how defects are documented, and how selected regression checks are automated.

The application under test is the [nopCommerce public demo storefront](https://demo.nopcommerce.com/).

## Testing objectives

- Verify that shoppers can complete core storefront journeys.
- Document repeatable manual checks, observed outcomes, and reproducible defects.
- Automate stable smoke and regression scenarios with clear failure reporting.

## Planned scope

Initial candidates include registration and sign-in, catalog browsing and search, product details, and cart updates. Checkout coverage will be defined in the test plan based on the demo's available options.

## Technology stack

Manual QA planning uses Markdown, and structured scenario design uses Excel. UI automation is planned with Java, Selenium WebDriver, TestNG, and Maven, organized using Page Object Model. GitHub Actions is planned for a later phase.

## Project status

The [test scenario workbook](docs/test-cases/Test_Scenarios.xlsx) contains 33 designed scenarios for registration, authentication, catalog, cart, and checkout, with traceability to the functional checklist. The workbook records verification status separately from test execution. Detailed test cases, manual execution, automation, and CI remain planned. See the [task tracker](docs/TASKS.md) for the project sequence.
