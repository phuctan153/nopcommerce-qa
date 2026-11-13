"""Regression checks for the public evidence allowlist and stale screenshot cleanup."""

import importlib.util
import json
from pathlib import Path
import tempfile
import unittest
from unittest.mock import patch


spec = importlib.util.spec_from_file_location("build_report", Path(__file__).with_name("build-report.py"))
reporter = importlib.util.module_from_spec(spec)
spec.loader.exec_module(reporter)


class PublicEvidenceTest(unittest.TestCase):
    def setUp(self):
        self.temporary = tempfile.TemporaryDirectory()
        self.addCleanup(self.temporary.cleanup)
        self.directory = Path(self.temporary.name)
        self.report = self.directory / "surefire-reports/testng-results.xml"
        self.report.parent.mkdir()
        self.output = self.directory / "site"
        for name, value in (("REPORT", self.report), ("OUTPUT", self.output)):
            patcher = patch.object(reporter, name, value)
            patcher.start()
            self.addCleanup(patcher.stop)

    def write_report(self, message, status="FAIL", class_name="ReportScreenshotDemoTest"):
        self.report.write_text(f"""<testng-results total="1" passed="0" failed="1" skipped="0">
            <suite duration-ms="10"><test><class name="com.nopcommerce.qa.tests.{class_name}">
            <test-method name="example" description="DEMO-001: Intentional failure" status="{status}">
            <params><param>PRIVATE_PARAMETER</param></params>
            <exception><message>PRIVATE_EXCEPTION</message></exception>
            <reporter-output><line>{message}</line></reporter-output>
            </test-method></class></test></suite></testng-results>""", encoding="utf-8")

    def image(self, name="example.png", content=b"\x89PNG\r\n\x1a\nsynthetic-masked-content"):
        directory = self.report.parent / "screenshots"
        directory.mkdir(exist_ok=True)
        path = directory / name
        path.write_bytes(content)
        return path

    def build(self):
        reporter.build_report()
        return json.loads((self.output / "data.json").read_text(encoding="utf-8"))

    def test_masked_image_is_linked_without_exporting_private_report_fields(self):
        source = self.image()
        self.write_report("Masked failure screenshot: screenshots/example.png")
        data = self.build()
        self.assertTrue(data["demonstration"])
        self.assertTrue(data["tests"][0]["intentionalFailure"])
        images = data["tests"][0]["screenshots"]
        self.assertEqual(len(images), 1)
        self.assertRegex(images[0], r"^screenshots/[a-f0-9]{64}\.png$")
        self.assertEqual((self.output / images[0]).read_bytes(), source.read_bytes())
        self.assertNotIn("PRIVATE_", json.dumps(data))

    def test_untrusted_missing_or_legacy_paths_are_not_published(self):
        self.image()
        for message in ("Failure screenshot: screenshots/example.png",
                        "Masked failure screenshot: screenshots/../example.png",
                        "Masked failure screenshot: https://example.com/example.png",
                        "Masked failure screenshot: screenshots/missing.png"):
            with self.subTest(message=message):
                self.write_report(message)
                self.assertEqual(self.build()["tests"][0]["screenshots"], [])

    def test_non_png_file_is_not_published(self):
        self.image(content=b"PRIVATE_NOT_AN_IMAGE")
        self.write_report("Masked failure screenshot: screenshots/example.png")
        self.assertEqual(self.build()["tests"][0]["screenshots"], [])

    def test_passing_method_cannot_publish_a_screenshot(self):
        self.image()
        self.write_report("Masked failure screenshot: screenshots/example.png", status="PASS")
        self.assertEqual(self.build()["tests"][0]["screenshots"], [])

    def test_successful_rebuild_removes_previous_failure_images(self):
        self.image()
        self.write_report("Masked failure screenshot: screenshots/example.png")
        self.build()
        self.write_report("", status="PASS", class_name="ProductSearchTest")
        data = self.build()
        self.assertFalse(data["demonstration"])
        self.assertEqual(list((self.output / "screenshots").glob("*.png")), [])

    def test_missing_report_removes_old_evidence_and_shows_unavailable_results(self):
        self.image()
        self.write_report("Masked failure screenshot: screenshots/example.png")
        self.build()
        self.report.unlink()
        data = self.build()
        self.assertFalse(data["available"])
        self.assertEqual(data["tests"], [])
        self.assertEqual(list((self.output / "screenshots").glob("*.png")), [])


if __name__ == "__main__":
    unittest.main()
