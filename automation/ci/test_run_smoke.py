"""Browser-free regression check for failed-run artifact sanitization."""

import importlib.util
import copy
import json
from pathlib import Path
import struct
import subprocess
import tempfile
import unittest
from unittest.mock import patch
import xml.etree.ElementTree as ET
import zlib


spec = importlib.util.spec_from_file_location("run_smoke", Path(__file__).with_name("run-smoke.py"))
smoke = importlib.util.module_from_spec(spec)
spec.loader.exec_module(smoke)


class ReportSanitizationTest(unittest.TestCase):
    def test_failed_maven_redacts_text_preserves_png_and_original_failure(self):
        with tempfile.TemporaryDirectory() as temporary:
            directory = Path(temporary)
            report = directory / "testng-results.xml"
            report.write_text("synthetic@example.com SyntheticPassword", encoding="utf-8")
            screenshots = directory / "screenshots"
            screenshots.mkdir()
            image = screenshots / "probe.png"
            original = b"\x89PNG\r\nSyntheticPassword"
            image.write_bytes(original)
            env = {"NOP_QA_LOGIN_EMAIL": "synthetic@example.com",
                   "NOP_QA_LOGIN_PASSWORD": "SyntheticPassword"}
            failure = subprocess.CalledProcessError(1, "mvn")
            def failed_maven(*args, **kwargs):
                report.write_text("synthetic@example.com SyntheticPassword", encoding="utf-8")
                raise failure
            with patch.object(smoke, "REPORT", report), \
                    patch.object(smoke.subprocess, "run", side_effect=failed_maven), \
                    patch.object(smoke, "verify_report") as verify, \
                    patch.dict(smoke.os.environ, {"GITHUB_ACTIONS": "false"}):
                with self.assertRaises(subprocess.CalledProcessError) as caught:
                    smoke.run_suite("mvn", "http://127.0.0.1:18080", env)
                self.assertIs(caught.exception, failure)
                verify.assert_not_called()
            self.assertEqual(report.read_text(encoding="utf-8"), "[redacted] [redacted]")
            self.assertEqual(image.read_bytes(), original)


class ExpectedFailureVerificationTest(unittest.TestCase):
    def setUp(self):
        self.temporary = tempfile.TemporaryDirectory()
        self.addCleanup(self.temporary.cleanup)
        self.directory = Path(self.temporary.name)
        self.report = self.directory / "testng-results.xml"
        patcher = patch.object(smoke, "REPORT", self.report)
        patcher.start()
        self.addCleanup(patcher.stop)
        self.root = ET.Element("testng-results")
        suite = ET.SubElement(self.root, "suite")
        test = ET.SubElement(suite, "test")
        for cls, names in smoke.SMOKE_METHODS.items():
            group = ET.SubElement(test, "class", name="com.nopcommerce.qa.tests." + cls)
            for name in names:
                ET.SubElement(group, "test-method", name=name, status="PASS")
        self.demo_class = ET.SubElement(test, "class", name=smoke.DEMO_CLASS)
        self.demos = []
        self.images = self.directory / "screenshots"
        self.images.mkdir()
        for index, (name, message) in enumerate(smoke.EXPECTED_DEMO_FAILURES.items()):
            method = ET.SubElement(self.demo_class, "test-method", name=name, status="FAIL")
            exception = ET.SubElement(method, "exception", {"class": "java.lang.AssertionError"})
            ET.SubElement(exception, "message").text = message
            output = ET.SubElement(method, "reporter-output")
            ET.SubElement(output, "line").text = f"Masked failure screenshot: screenshots/demo-{index}.png"
            (self.images / f"demo-{index}.png").write_bytes(self.png(index))
            self.demos.append(method)
        self.configuration = ET.SubElement(self.demo_class, "test-method",
                                           {"name": "quitBrowser", "status": "PASS", "is-config": "true"})

    @staticmethod
    def png(color):
        def chunk(kind, data):
            return struct.pack(">I", len(data)) + kind + data + struct.pack(">I", zlib.crc32(kind + data))
        return (b"\x89PNG\r\n\x1a\n" + chunk(b"IHDR", struct.pack(">IIBBBBB", 1, 1, 8, 2, 0, 0, 0))
                + chunk(b"IDAT", zlib.compress(bytes([0, color, 0, 0]))) + chunk(b"IEND", b""))

    def save(self):
        methods = [m for m in self.root.findall(".//test-method") if m.get("is-config") != "true"]
        self.root.set("total", str(len(methods)))
        for key, status in (("passed", "PASS"), ("failed", "FAIL"), ("skipped", "SKIP")):
            self.root.set(key, str(sum(m.get("status") == status for m in methods)))
        ET.ElementTree(self.root).write(self.report, encoding="utf-8")

    def run_maven(self, exit_code=1, demo=True):
        def maven(*args, **kwargs):
            self.save()
            if exit_code:
                raise subprocess.CalledProcessError(exit_code, "mvn")
        env = {"NOP_QA_SCREENSHOT_DEMO": "true" if demo else "false"}
        with patch.object(smoke.subprocess, "run", side_effect=maven):
            smoke.run_suite("mvn", "http://127.0.0.1:18080", env)

    def assert_rejected(self, exit_code=1):
        with self.assertRaises(RuntimeError):
            self.run_maven(exit_code)
        status = json.loads((self.directory / "verification.json").read_text())
        self.assertEqual(status["status"], "failed")

    def test_exact_expected_failures_pass_verification_without_changing_test_results(self):
        self.run_maven()
        result = ET.parse(self.report).getroot()
        self.assertEqual(result.get("failed"), "2")
        self.assertEqual(result.get("passed"), "14")
        self.assertEqual(json.loads((self.directory / "verification.json").read_text())["status"], "passed")

    def test_normal_failure_is_not_accepted_in_demo_mode(self):
        self.root.find(".//test-method").set("status", "FAIL")
        self.assert_rejected()

    def test_missing_normal_check_is_rejected(self):
        group = self.root.find(".//class")
        group.remove(group.find("test-method"))
        self.assert_rejected()

    def test_unexpected_assertion_message_is_rejected(self):
        self.demos[0].find("exception/message").text = "Unexpected assertion failure"
        self.assert_rejected()

    def test_browser_exception_is_rejected(self):
        self.demos[0].find("exception").set("class", "org.openqa.selenium.TimeoutException")
        self.assert_rejected()

    def test_demo_pass_is_rejected(self):
        self.demos[0].set("status", "PASS")
        self.assert_rejected()

    def test_demo_skip_is_rejected(self):
        self.demos[0].set("status", "SKIP")
        self.assert_rejected()

    def test_setup_or_teardown_failure_is_rejected(self):
        self.configuration.set("status", "FAIL")
        self.assert_rejected()

    def test_wrong_method_name_is_rejected(self):
        self.demos[0].set("name", "unexpectedMethod")
        self.assert_rejected()

    def test_duplicate_method_is_rejected(self):
        self.demo_class.append(copy.deepcopy(self.demos[0]))
        self.assert_rejected()

    def test_missing_screenshot_is_rejected(self):
        (self.images / "demo-0.png").unlink()
        self.assert_rejected()

    def test_corrupt_screenshot_is_rejected(self):
        (self.images / "demo-0.png").write_bytes(self.png(0)[:-5])
        self.assert_rejected()

    def test_untrusted_screenshot_path_is_rejected(self):
        self.demos[0].find("reporter-output/line").text = "Masked failure screenshot: screenshots/../private.png"
        self.assert_rejected()

    def test_same_screenshot_reference_for_two_tests_is_rejected(self):
        self.demos[1].find("reporter-output/line").text = self.demos[0].findtext("reporter-output/line")
        self.assert_rejected()

    def test_unexpected_maven_exit_code_is_rejected(self):
        self.assert_rejected(exit_code=2)

    def test_successful_maven_cannot_claim_expected_failures(self):
        self.assert_rejected(exit_code=0)

    def test_demo_results_remain_a_failure_in_normal_mode(self):
        with self.assertRaises(subprocess.CalledProcessError):
            self.run_maven(demo=False)

    def test_normal_suite_still_passes_without_a_demo(self):
        self.root.find(".//test").remove(self.demo_class)
        self.run_maven(exit_code=0, demo=False)
        self.assertEqual(json.loads((self.directory / "verification.json").read_text())["status"], "passed")

    def test_build_failure_cannot_reuse_a_previous_good_demo_report(self):
        self.save()
        (self.directory / "verification.json").write_text('{"status":"passed"}')
        with patch.object(smoke.subprocess, "run", side_effect=subprocess.CalledProcessError(1, "mvn")):
            with self.assertRaises(FileNotFoundError):
                smoke.run_suite("mvn", "http://127.0.0.1:18080", {"NOP_QA_SCREENSHOT_DEMO": "true"})
        self.assertFalse(self.report.exists())
        self.assertFalse((self.directory / "verification.json").exists())


if __name__ == "__main__":
    unittest.main()
