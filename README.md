# nopCommerce QA Testing Portfolio

This portfolio documents manual testing and UI automation for a sample e-commerce storefront. It includes test design, manual execution records, and a Maven smoke suite for shopper authentication, catalog navigation and search, and shopping cart behavior.

The application under test is the [nopCommerce public demo storefront](https://demo.nopcommerce.com/).

## Testing objectives

- Verify that shoppers can complete core storefront journeys.
- Document repeatable manual checks, observed outcomes, and reproducible defects.
- Automate stable smoke and regression scenarios with clear failure reporting.

## Planned scope

Manual coverage includes registration and sign-in, catalog browsing and search, product details, and cart updates. Checkout coverage stops at the last safe pre-submit step available in the demo.

## Technology stack

Manual QA planning uses Markdown. Scenarios, detailed test cases, synthetic data rules, and traceability use Excel. The automation module uses Java, Selenium WebDriver, TestNG, Maven, and Page Object Model. The GitHub Actions workflow provisions the test storefront and runs the smoke suite.

## Run the smoke suite

Install JDK 17 or later, Maven 3.9 or later, Docker Desktop, and Chrome or Microsoft Edge. The suite includes 11 browser cases and three browser-free configuration checks. Follow the [local run instructions](automation/README.md#run-the-full-suite-locally) to start nopCommerce, load the shopper fixtures, run the tests, and open the report.

See the [GitHub Actions run instructions](automation/README.md#run-in-github-actions) to reproduce the CI setup or download its reports.

## Project status

All 38 designed cases have a recorded manual disposition. Evidence gaps and conditional cases are tracked separately from the recorded outcomes. See the [task tracker](docs/TASKS.md) for the project sequence and the [handoff](docs/HANDOFF.md) for the current checkpoint.

| Workbook | Contents |
| --- | --- |
| [Test scenarios](docs/test-cases/Test_Scenarios.xlsx) | QA-004 coverage, priorities, and scenario IDs |
| [Test cases](docs/test-cases/Test_Cases.xlsx) | Numbered steps, preconditions, expected results, and data references |
| [Test data](docs/test-cases/Test_Data.xlsx) | Reusable synthetic inputs and per-run fixture rules |
| [Requirements traceability](docs/test-cases/RTM.xlsx) | Functional ID to scenario to test case mappings, plus verification gaps and exclusions |
| [Manual execution](docs/test-execution/Test_Execution.xlsx) | Recorded outcomes and evidence references |
