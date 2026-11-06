# Reporting Defects in Jira

Use the [defect management rules](DEFECT_MANAGEMENT.md) to decide whether an observation is ready to file. Prepare the report with the [bug report template](BUG_REPORT_TEMPLATE.md) and reserve a unique portfolio Bug ID in the [Bug Register](Bug_Tracking.xlsx). Excel can track a confirmed defect while Jira access is pending. Leave Jira Issue Key blank until an issue exists.

## Create the issue manually

1. In an authorized Jira project, search for the problem using the affected feature, symptoms, or portfolio Bug ID. If an issue already tracks the same defect, reference it and add useful evidence to the existing record where permitted.
2. Choose **Create**, select the appropriate project or space, and choose the **Bug** type if available. If access or the issue type is missing, keep the Excel record and document the reason in Notes. [Atlassian's Jira creation guide](https://support.atlassian.com/jira-software-cloud/docs/create-a-work-item-and-a-subtask)
3. Enter the summary, environment, preconditions, numbered steps, expected and actual results, reproducibility, and shopper impact. Include the local Bug ID, existing Test Case ID, and execution ID in the description. Use the project's severity and priority fields where available, or write the portfolio classifications in the description.
4. Attach sanitized screenshots when the project permits it. Name the relevant step and visible state in the description, and use the same filename as the repository copy when retained. Otherwise, provide an evidence link accessible to the intended readers. A repository-relative path is useful in Excel, but a Jira reader also needs the attachment or permission to open its link. [Atlassian's attachment guide](https://support.atlassian.com/jira-software-cloud/docs/add-an-attachment-to-an-issue/)
5. After creation, open the issue and copy the exact assigned key into **Jira Issue Key** on the matching Excel row. Keep the portfolio Bug ID as the stable local identifier. If the issue is linked to another Jira work item for testing, record the relationship in Jira as well.

## Keep the links and status current

- Use the existing case ID from [Test Cases](../test-cases/Test_Cases.xlsx) in the register and Jira description. Reference the relevant execution row in both places and enter the portfolio Bug ID in that row's **Defect ID** field. Use the [evidence conventions](../test-execution/evidence/README.md) for screenshot names and repository-relative references.
- During triage and after each update, open Jira by the assigned key. Compare its workflow and resolution with the Excel lifecycle, map the current stage to the applicable register status, and note any difference with the actual sync date and next action.
- Keep Jira's status and the QA decision distinct if the project workflow differs. A Jira item marked done because a fix was delivered can remain **Ready for Retest** in Excel until QA has verified it. Record retest evidence and the final disposition in both places when authorized.
- Preserve the original Bug ID during retest or reopening. For a duplicate, reference the canonical issue and Bug ID in Notes. If Jira becomes unavailable, keep tracking in Excel and reconcile on the next authorized review.
