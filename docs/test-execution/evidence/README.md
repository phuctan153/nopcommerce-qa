# Manual Test Evidence

Store sanitized evidence captured during manual execution in this directory. The [execution workbook](../Test_Execution.xlsx) links each file to its execution row.

## Naming and links

- Use `<execution-id>_<YYYY-MM-DD>_a<attempt>_s<step>_<short-label>.png`. Use the actual capture date, a two-digit attempt and step number, and a short lowercase label with hyphens. Use step `00` for a blocker before the first case step.
- Keep the Execution ID from the workbook in the filename. Increment the attempt number on a retest and use distinct labels when recording several views of one step.
- Browser tools that return JPEG evidence may retain the same naming pattern with `.jpg`. Keep the actual file format and extension consistent. The home-access evidence under `Triage/` is referenced by the [QA-007.2 review](../../bug-reports/QA-007.2_TRIAGE.md); it is not evidence that any storefront assertion was executed.
- `Follow-up/` contains evidence for the [2026-11-17 run](../FOLLOW_UP_2026-11-17.md) and responsive report review. Sanitized `.txt` files are actual accessibility-message excerpts, explicitly distinguished from screenshots. They omit entered account values. No screenshot is claimed for the billing JavaScript alert, which the browser could not capture. Dashboard captures use task IDs because they are portfolio UI checks, not storefront test cases.
- In Evidence Reference, record the repository-relative path `docs/test-execution/evidence/<filename>`. For several files, put one path per line. Confirm each path resolves and the screenshot shows the step referenced by the execution record.

## Capture and privacy

- Capture the smallest useful view that shows the decisive UI state. Include enough page context to identify the feature and keep relevant feedback, amounts, or selections legible.
- When useful, capture before and after views for changes such as a cart update. Keep the evidence chronological and describe which step each view supports.
- Crop or redact passwords, email addresses, reset links and tokens, cookies, payment information, personal addresses, and unrelated browser tabs or notifications. Check the saved image and any visible URL before adding it to Git.
- If safe redaction would remove the relevant context, record a sanitized observation in the workbook and explain why a screenshot could not be retained. Keep any raw captures containing sensitive material outside the repository.
