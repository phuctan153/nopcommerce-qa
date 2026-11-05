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

Manual QA artifacts use Markdown and versioned synthetic test data. UI automation is planned with Java, Selenium WebDriver, TestNG, and Maven, organized using Page Object Model. GitHub Actions is planned for a later phase.

## Project status

QA-001 establishes the repository structure and contribution guidelines. Test design, manual execution, automation implementation, and CI setup are planned. See the [task tracker](docs/TASKS.md) for the project sequence.
