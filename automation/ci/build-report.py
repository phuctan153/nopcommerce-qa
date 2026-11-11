"""Build the public report using an allowlist of TestNG result fields."""

from datetime import datetime, timezone
import json
import os
from pathlib import Path
import re
import shutil
import xml.etree.ElementTree as ET


ROOT = Path(__file__).resolve().parents[2]
REPORT = ROOT / "automation/target/surefire-reports/testng-results.xml"
OUTPUT = ROOT / "automation/target/report-site"
REPOSITORY = "https://github.com/phuctan153/nopcommerce-qa"


def build_report():
    data = {
        "generatedAt": datetime.now(timezone.utc).isoformat(),
        "source": "GitHub Actions" if os.environ.get("GITHUB_ACTIONS") == "true" else "Local run",
        "repositoryUrl": REPOSITORY,
        "runUrl": None,
        "commit": None,
        "workflowStatus": os.environ.get("SMOKE_OUTCOME", "unknown"),
        "available": REPORT.is_file(),
        "startedAt": None,
        "durationMs": 0,
        "summary": {"total": 0, "passed": 0, "failed": 0, "skipped": 0},
        "tests": [],
    }
    run_id = os.environ.get("GITHUB_RUN_ID", "")
    if run_id.isdigit():
        data["runUrl"] = f"{REPOSITORY}/actions/runs/{run_id}"
    commit = os.environ.get("GITHUB_SHA", "")
    if re.fullmatch(r"[0-9a-fA-F]{40}", commit):
        data["commit"] = commit

    if data["available"]:
        report = ET.parse(REPORT).getroot()
        data["summary"] = {key: int(report.get(key, "0")) for key in data["summary"]}
        suites = report.findall("suite")
        data["startedAt"] = suites[0].get("started-at") if suites else None
        data["durationMs"] = sum(int(suite.get("duration-ms", "0")) for suite in suites)
        for test_class in report.findall(".//class"):
            class_name = test_class.get("name", "").rsplit(".", 1)[-1]
            for method in test_class.findall("test-method"):
                if method.get("is-config") == "true":
                    continue
                description = method.get("description", method.get("name", "Unnamed test"))
                case = re.match(r"(TC-[A-Z]+-\d+):\s*(.*)", description)
                data["tests"].append({
                    "className": class_name,
                    "name": method.get("name", ""),
                    "caseId": case.group(1) if case else None,
                    "description": case.group(2) if case else description,
                    "status": method.get("status", "UNKNOWN"),
                    "durationMs": int(method.get("duration-ms", "0")),
                })

    OUTPUT.mkdir(parents=True, exist_ok=True)
    for filename in ("index.html", "style.css", "app.js"):
        shutil.copyfile(ROOT / "automation/report" / filename, OUTPUT / filename)
    (OUTPUT / "data.json").write_text(json.dumps(data, indent=2), encoding="utf-8")
    print(f"Built report dashboard with {len(data['tests'])} test results.")


if __name__ == "__main__":
    build_report()
