"""Browser-free regression check for failed-run artifact sanitization."""

import importlib.util
from pathlib import Path
import subprocess
import tempfile
import unittest
from unittest.mock import patch


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
            with patch.object(smoke, "REPORT", report), \
                    patch.object(smoke.subprocess, "run", side_effect=failure), \
                    patch.object(smoke, "verify_report") as verify, \
                    patch.dict(smoke.os.environ, {"GITHUB_ACTIONS": "false"}):
                with self.assertRaises(subprocess.CalledProcessError) as caught:
                    smoke.run_suite("mvn", "http://127.0.0.1:18080", env)
                self.assertIs(caught.exception, failure)
                verify.assert_not_called()
            self.assertEqual(report.read_text(encoding="utf-8"), "[redacted] [redacted]")
            self.assertEqual(image.read_bytes(), original)


if __name__ == "__main__":
    unittest.main()
