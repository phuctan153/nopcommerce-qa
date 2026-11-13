"""Build the public report using an allowlist of TestNG result fields."""

from datetime import datetime, timezone
import hashlib
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


def export_screenshots(method):
    """Publish only existing PNGs attested by the masking helper for this failed method."""
    if method.get("status") != "FAIL":
        return []
    images = []
    for line in method.findall("./reporter-output/line"):
        match = re.fullmatch(r"Masked failure screenshot: screenshots/([A-Za-z0-9_-]+\.png)",
                             (line.text or "").strip())
        if not match:
            continue
        directory = REPORT.parent / "screenshots"
        source = directory / match.group(1)
        if directory.is_symlink() or source.is_symlink() or not source.is_file():
            continue
        if source.resolve().parent != directory.resolve() or source.stat().st_size > 10 * 1024 * 1024:
            continue
        content = source.read_bytes()
        if not content.startswith(b"\x89PNG\r\n\x1a\n"):
            continue
        relative = "screenshots/" + hashlib.sha256(content).hexdigest() + ".png"
        (OUTPUT / "screenshots").mkdir(exist_ok=True)
        (OUTPUT / relative).write_bytes(content)
        if relative not in images:
            images.append(relative)
    return images


def build_report():
    OUTPUT.mkdir(parents=True, exist_ok=True)
    evidence = OUTPUT / "screenshots"
    if OUTPUT.is_symlink() or evidence.is_symlink():
        raise RuntimeError("Report output must not be a symbolic link.")
    # Remove only generated evidence, so a later successful run cannot show stale failures.
    if evidence.exists():
        if evidence.resolve().parent != OUTPUT.resolve():
            raise RuntimeError("Evidence output escaped the report directory.")
        for previous in evidence.glob("*.png"):
            previous.unlink()
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
        "demonstration": False,
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
                case = re.match(r"(TC-[A-Z]+-\d+|DEMO-\d+):\s*(.*)", description)
                data["tests"].append({
                    "className": class_name,
                    "name": method.get("name", ""),
                    "caseId": case.group(1) if case else None,
                    "description": case.group(2) if case else description,
                    "status": method.get("status", "UNKNOWN"),
                    "durationMs": int(method.get("duration-ms", "0")),
                    "screenshots": export_screenshots(method),
                    "intentionalFailure": test_class.get("name")
                    == "com.nopcommerce.qa.tests.ReportScreenshotDemoTest",
                })

    data["demonstration"] = any(test["intentionalFailure"] for test in data["tests"])

    OUTPUT.mkdir(parents=True, exist_ok=True)
    for filename in ("index.html", "style.css", "app.js"):
        shutil.copyfile(ROOT / "automation/report" / filename, OUTPUT / filename)
    # Pages and browsers cache assets across deployments. New HTML must request matching code.
    index = OUTPUT / "index.html"
    html = index.read_text(encoding="utf-8")
    for filename in ("style.css", "app.js"):
        version = hashlib.sha256((OUTPUT / filename).read_bytes()).hexdigest()[:12]
        html = html.replace('"' + filename + '"', '"' + filename + '?v=' + version + '"')
    index.write_text(html, encoding="utf-8")
    (OUTPUT / "data.json").write_text(json.dumps(data, indent=2), encoding="utf-8")
    print(f"Built report dashboard with {len(data['tests'])} test results.")


if __name__ == "__main__":
    build_report()
