# nopCommerce QA Testing Portfolio

This portfolio documents manual testing and UI automation for a sample e-commerce storefront. It includes test design, manual execution records, and a Maven module for the planned storefront smoke suite.

The application under test is the [nopCommerce public demo storefront](https://demo.nopcommerce.com/).

## Testing objectives

- Verify that shoppers can complete core storefront journeys.
- Document repeatable manual checks, observed outcomes, and reproducible defects.
- Automate stable smoke and regression scenarios with clear failure reporting.

## Planned scope

Manual coverage includes registration and sign-in, catalog browsing and search, product details, and cart updates. Checkout coverage stops at the last safe pre-submit step available in the demo.

## Technology stack

Manual QA planning uses Markdown. Scenarios, detailed test cases, synthetic data rules, and traceability use Excel. The automation module uses Java, Selenium WebDriver, TestNG, Maven, and Page Object Model. GitHub Actions is planned for a later phase.

## Run the automation foundation

Install JDK 17 or later and Maven 3.9 or later. From the repository root, run:

```powershell
mvn -f automation/pom.xml test
```

QA-008 runs browser-free TestNG configuration checks. QA-009 will add storefront smoke tests. See the [automation guide](automation/README.md) for browser settings and report locations.

## Project status

All 38 designed cases have a recorded manual disposition. Evidence gaps and conditional cases are tracked separately from the recorded outcomes. See the [task tracker](docs/TASKS.md) for the project sequence and the [handoff](docs/HANDOFF.md) for the current checkpoint.

| Workbook | Contents |
| --- | --- |
| [Test scenarios](docs/test-cases/Test_Scenarios.xlsx) | QA-004 coverage, priorities, and scenario IDs |
| [Test cases](docs/test-cases/Test_Cases.xlsx) | Numbered steps, preconditions, expected results, and data references |
| [Test data](docs/test-cases/Test_Data.xlsx) | Reusable synthetic inputs and per-run fixture rules |
| [Requirements traceability](docs/test-cases/RTM.xlsx) | Functional ID to scenario to test case mappings, plus verification gaps and exclusions |
| [Manual execution](docs/test-execution/Test_Execution.xlsx) | Recorded outcomes and evidence references |
