# nopCommerce QA Testing Portfolio

This portfolio documents manual testing and planned UI automation for a sample e-commerce storefront. It will show how test coverage is designed, how defects are documented, and how selected regression checks are automated.

The application under test is the [nopCommerce public demo storefront](https://demo.nopcommerce.com/).

## Testing objectives

- Verify that shoppers can complete core storefront journeys.
- Document repeatable manual checks, observed outcomes, and reproducible defects.
- Automate stable smoke and regression scenarios with clear failure reporting.

## Planned scope

Manual coverage includes registration and sign-in, catalog browsing and search, product details, and cart updates. Checkout coverage stops at the last safe pre-submit step available in the demo.

## Technology stack

Manual QA planning uses Markdown. Scenarios, detailed test cases, synthetic data rules, and traceability use Excel. UI automation is planned with Java, Selenium WebDriver, TestNG, and Maven, organized using Page Object Model. GitHub Actions is planned for a later phase.

## Project status

QA-005 produced 38 detailed cases mapped to all 33 QA-004 scenarios. Manual verification and execution remain pending. See the [task tracker](docs/TASKS.md) for the project sequence.

| Workbook | Contents |
| --- | --- |
| [Test scenarios](docs/test-cases/Test_Scenarios.xlsx) | QA-004 coverage, priorities, and scenario IDs |
| [Test cases](docs/test-cases/Test_Cases.xlsx) | Numbered steps, preconditions, expected results, and data references |
| [Test data](docs/test-cases/Test_Data.xlsx) | Reusable synthetic inputs and per-run fixture rules |
| [Requirements traceability](docs/test-cases/RTM.xlsx) | Functional ID to scenario to test case mappings, plus verification gaps and exclusions |
