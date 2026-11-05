# Repository Guidelines

## Purpose and stack

This portfolio demonstrates manual testing and browser UI automation for the public nopCommerce demo. The planned automation stack is Java, Selenium WebDriver, TestNG, Maven, and Page Object Model. GitHub Actions will run the suite in a later phase. Check [the task tracker](docs/TASKS.md) before expanding the current scope.

## Java standards

- Use four spaces for indentation. Use UpperCamelCase for classes, lowerCamelCase for methods and variables, and lowercase for package names.
- Use the base package `com.nopcommerce.qa`. Keep page objects in `pages`, TestNG test classes in `tests`, and shared browser setup and configuration in `support`.
- Give test classes a `Test` suffix and name test methods for the behavior and expected outcome. Keep methods focused and reuse helpers when they serve multiple callers.

## Test automation

- Use TestNG lifecycle hooks to start and quit WebDriver. Give each test its own browser session and ensure teardown runs even when setup or a test fails.
- Keep tests independent of execution order. Use synthetic test data, isolate records where possible, and account for shared demo state.
- Keep TestNG assertions in test classes. Use explicit waits for observable UI conditions and avoid fixed sleeps.
- Prefer stable IDs or attributes for locators, followed by readable CSS selectors. Use XPath when a simpler stable locator is unavailable.
- Centralize configurable values such as the base URL and browser. Read secrets from local environment variables or CI secrets and exclude them from version control and evidence.

## Page Object Model

- Represent each page or reusable UI component with a focused class. Pass the current WebDriver into the object and keep locators private.
- Expose user actions and observable state through meaningful methods. Keep Selenium calls and UI synchronization inside page or component objects.
- Return the destination page object when an action reliably navigates to another page. Leave scenario-specific assertions and test data in the test layer.

## Documentation

- Write clear English in Markdown. Use stable identifiers such as `TC-001` for test cases, `BUG-001` for defects, and `QA-001` for project tasks.
- Link defects and execution records to related test cases when available. Record the environment, reproducible steps, expected and actual behavior, and sanitized evidence for observed defects.
- Keep planned coverage separate from observed results. Record actual outcomes with the run date and environment. Use synthetic data and redact sensitive information from shared evidence.

## Git workflow

- Inspect Git status before editing and preserve existing work. Keep each change focused on its task.
- Use a task-specific branch when the workflow calls for one, for example `codex/qa-002-test-plan`.
- Review the diff before handoff.

## Git Commit Policy

- Commit and push only when explicitly requested. Treat a proposed commit plan as review material.
- Group related changes into small, meaningful commits. Include the relevant task ID in each concise commit subject.
- Stage only the intended paths. Review `git diff --cached` and run `git diff --cached --check` before committing. Check staged files for credentials, sensitive data, and unintended artifacts.
- Describe the work and verification accurately. Keep pending checks visible in documentation and commit messages when relevant.
- Avoid empty commits and artificial splits. Use Git's default timestamps.

## Verification

- For documentation and scaffolding, verify relative links, directory paths, and ignore rules. Run `git diff --check` for tracked changes and check new files for whitespace issues.
- Once the Maven module exists, run `mvn -f automation/pom.xml test` for automation changes. Review TestNG results and confirm failures have enough evidence to investigate.
- Report the checks actually performed, their outcomes, and any verification gaps. Update the task tracker when the task status changes.
