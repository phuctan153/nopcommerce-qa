"""Provision a disposable nopCommerce storefront and run the full smoke suite."""

import hashlib
import json
import os
from http.client import HTTPException
from http.cookiejar import CookieJar
from pathlib import Path
import re
import secrets
import shutil
import subprocess
import struct
import time
from urllib.parse import urlencode, urlsplit
from urllib.request import HTTPCookieProcessor, ProxyHandler, build_opener
import xml.etree.ElementTree as ET
import zlib


ROOT = Path(__file__).resolve().parents[2]
REPORT = ROOT / "automation/target/surefire-reports/testng-results.xml"
TOKEN = "__RequestVerificationToken"
DEMO_CLASS = "com.nopcommerce.qa.tests.ReportScreenshotDemoTest"
EXPECTED_DEMO_FAILURES = {
    "intentionalSearchFailureShowsTheResultsScreenshot":
        "Intentional evidence demonstration: an invented product must not match the catalog. "
        "expected [true] but found [false]",
    "intentionalPriceFailureShowsTheProductScreenshot":
        "Intentional evidence demonstration: the sample iPhone is not free. "
        "expected [$0.00] but found [$799.00]",
}
SMOKE_METHODS = {
    "AuthenticationSmokeTest": ("validCredentialsOpenTheCorrectAccount",
                                "signingOutProtectsThePreviouslyOpenedAccountPage"),
    "CategoryNavigationTest": ("selectingTopLevelCategoryShowsItsHeadingAndSubcategories",
                               "selectingSubcategoryShowsItsBreadcrumbAndProductListing"),
    "ProductSearchTest": ("validQueryReturnsExpectedProduct",
                          "selectingProductOpensMatchingPurchasableDetails",
                          "nonmatchingQueryShowsNoResultsMessageAndNoProducts"),
    "ShoppingCartSmokeTest": ("addingProductShowsTheRequestedQuantityInCart",
                              "updatedQuantityAndLineTotalPersistWhenCartIsReopened",
                              "removingProductsPreservesTheOtherLineUntilCartIsEmpty",
                              "subtotalMatchesCalculatedLineTotalBeforeAndAfterQuantityUpdate"),
    "TestConfigTest": ("usesDocumentedDefaultsWhenPropertiesAreOmitted",
                        "acceptsBrowserAndStorefrontOverrides", "rejectsAnInvalidHeadlessFlag"),
}


def browser_session():
    return build_opener(ProxyHandler({}), HTTPCookieProcessor(CookieJar()))


def request(session, url, fields=None, timeout=10):
    payload = urlencode(fields).encode("ascii") if fields is not None else None
    with session.open(url, data=payload, timeout=timeout) as response:
        return urlsplit(response.url).path, response.read().decode("utf-8")


def wait_for_page(session, url, marker):
    deadline = time.monotonic() + 120
    while time.monotonic() < deadline:
        try:
            _, page = request(session, url)
            if marker in page:
                return page
        except (OSError, HTTPException):
            pass
        time.sleep(2)
    raise RuntimeError(f"Timed out waiting for {url} to serve {marker}.")


def post_form(session, url, page, fields, timeout=30):
    token = re.search(r'name="__RequestVerificationToken"[^>]*value="([^"]+)"', page)
    if token is None:
        raise RuntimeError(f"Missing request verification token on {url}.")
    return request(session, url, {**fields, TOKEN: token.group(1)}, timeout)


def install_storefront(base_url, db_password):
    print("Installing nopCommerce with sample catalog data.", flush=True)
    session = browser_session()
    install_url = base_url + "/install"
    page = wait_for_page(session, install_url, 'id="installation-form"')
    provider = re.search(r'<option[^>]*value="([^"]+)"[^>]*>PostgreSQL</option>', page)
    if provider is None:
        raise RuntimeError("The installer does not offer PostgreSQL.")
    password = "Qa1!" + secrets.token_hex(16)
    post_form(session, install_url, page, {
        "AdminEmail": "ci-admin@example.com",
        "AdminPassword": password,
        "ConfirmPassword": password,
        "InstallSampleData": "true",
        "SubscribeNewsletters": "false",
        "DataProvider": provider.group(1),
        "CreateDatabaseIfNotExists": "false",
        "ServerName": "db",
        "DatabaseName": "nopcommerce",
        "Username": "nopcommerce",
        "Password": db_password,
    }, timeout=240)


def register_shopper(base_url, fixture):
    session = browser_session()
    register_url = base_url + "/register"
    _, page = request(session, register_url)
    email = f"ci-{fixture.lower()}@example.com"
    password = "Qa1!" + secrets.token_hex(16)
    path, _ = post_form(session, register_url, page, {
        "FirstName": "CI",
        "LastName": "Shopper",
        "Email": email,
        "Password": password,
        "ConfirmPassword": password,
        "NewsLetterSubscriptions[0].TypeId": "1",
        "NewsLetterSubscriptions[0].IsActive": "false",
    })
    if not path.startswith("/registerresult/"):
        raise RuntimeError(f"Registration did not complete for the {fixture} fixture (route: {path}).")
    return {f"NOP_QA_{fixture}_EMAIL": email, f"NOP_QA_{fixture}_PASSWORD": password}


def checked_methods(summary):
    methods = []
    identities = set()
    for test_class in summary.findall(".//class"):
        for method in test_class.findall("test-method"):
            if method.get("is-config") == "true":
                if method.get("status") != "PASS":
                    raise RuntimeError("A TestNG setup or teardown configuration did not pass.")
                continue
            identity = (test_class.get("name"), method.get("name"))
            if identity in identities or method.get("status") not in {"PASS", "FAIL", "SKIP"}:
                raise RuntimeError("Duplicate test identity or unsupported test status.")
            identities.add(identity)
            methods.append((identity, method))
    actual = {"total": len(methods), **{key: sum(m.get("status") == status for _, m in methods)
              for key, status in (("passed", "PASS"), ("failed", "FAIL"), ("skipped", "SKIP"))}}
    if any(actual[key] != int(summary.get(key, "-1")) for key in actual):
        raise RuntimeError("TestNG summary does not match the individual test results.")
    return methods


def verify_report():
    summary = ET.parse(REPORT).getroot()
    checked_methods(summary)
    total, passed, failed, skipped = (
        int(summary.attrib[name]) for name in ("total", "passed", "failed", "skipped")
    )
    if total < 14 or passed != total or failed or skipped:
        raise RuntimeError(
            f"Expected at least 14 tests with all passing. "
            f"TestNG reported {total} total, {passed} passed, {failed} failed, {skipped} skipped."
        )
    print(f"TestNG confirmed all {total} tests passed with zero skips.", flush=True)


def valid_png(content):
    """Validate browser PNG structure, CRCs and bounded RGB/greyscale scanline data."""
    if not content.startswith(b"\x89PNG\r\n\x1a\n") or len(content) > 10 * 1024 * 1024:
        return False
    offset, compressed, header, ended = 8, bytearray(), None, False
    while offset + 12 <= len(content):
        size = struct.unpack(">I", content[offset:offset + 4])[0]
        end = offset + 12 + size
        if end > len(content):
            return False
        kind = content[offset + 4:offset + 8]
        payload = content[offset + 8:offset + 8 + size]
        if zlib.crc32(kind + payload) != struct.unpack(">I", content[end - 4:end])[0]:
            return False
        if header is None:
            if kind != b"IHDR" or size != 13:
                return False
            header = struct.unpack(">IIBBBBB", payload)
        elif kind == b"IHDR":
            return False
        if kind == b"IDAT":
            compressed.extend(payload)
        if kind == b"IEND":
            ended = size == 0 and end == len(content)
            break
        offset = end
    if not header or not ended or not compressed:
        return False
    width, height, depth, color, compression, filtering, interlace = header
    channels = {0: 1, 2: 3, 4: 2, 6: 4}.get(color)
    if not channels or depth != 8 or compression or filtering or interlace or not width or not height:
        return False
    expected = height * (1 + width * channels)
    if expected > 64 * 1024 * 1024:
        return False
    try:
        decoder = zlib.decompressobj()
        raw = decoder.decompress(compressed, expected + 1)
        return len(raw) == expected and decoder.eof and not decoder.unused_data
    except zlib.error:
        return False


def verify_demo_report():
    summary = ET.parse(REPORT).getroot()
    methods = checked_methods(summary)
    expected_normal = {("com.nopcommerce.qa.tests." + cls, name)
                       for cls, names in SMOKE_METHODS.items() for name in names}
    normal = {identity for identity, _ in methods if identity[0] != DEMO_CLASS}
    demo = [(identity[1], method) for identity, method in methods if identity[0] == DEMO_CLASS]
    if normal != expected_normal or len(demo) != 2 or {name for name, _ in demo} != set(EXPECTED_DEMO_FAILURES):
        raise RuntimeError("Expected all fourteen normal checks and exactly the two named demo tests.")
    if any(method.get("status") != "PASS" for identity, method in methods if identity[0] != DEMO_CLASS):
        raise RuntimeError("A normal smoke test failed or skipped during screenshot verification.")
    screenshots = set()
    for name, method in demo:
        exception = method.find("exception")
        if (method.get("status") != "FAIL" or exception is None
                or exception.get("class") != "java.lang.AssertionError"
                or (exception.findtext("message") or "").strip() != EXPECTED_DEMO_FAILURES[name]):
            raise RuntimeError("A demo did not fail with its exact expected assertion.")
        references = [(line.text or "").strip() for line in method.findall("./reporter-output/line")
                      if (line.text or "").strip().startswith("Masked failure screenshot: ")]
        if len(references) != 1:
            raise RuntimeError("Each expected failure must reference exactly one masked screenshot.")
        match = re.fullmatch(r"Masked failure screenshot: screenshots/([A-Za-z0-9_-]+\.png)", references[0])
        if not match:
            raise RuntimeError("Invalid masked screenshot reference.")
        directory = REPORT.parent / "screenshots"
        image = directory / match.group(1)
        if (directory.is_symlink() or image.is_symlink() or not image.is_file()
                or image.resolve().parent != directory.resolve()
                or image.stat().st_size > 10 * 1024 * 1024 or not valid_png(image.read_bytes())
                or image in screenshots):
            raise RuntimeError("Expected a distinct, valid masked PNG for each intentional failure.")
        screenshots.add(image)
    print("Screenshot verification passed: 14 normal passes, 2 exact expected assertions, 2 valid PNGs.", flush=True)


def write_verification(mode, status):
    if REPORT.is_file():
        data = {"mode": mode, "status": status,
                "reportSha256": hashlib.sha256(REPORT.read_bytes()).hexdigest()}
        (REPORT.parent / "verification.json").write_text(json.dumps(data, indent=2), encoding="utf-8")


def run_suite(maven, base_url, env):
    private_values = [value for key, value in env.items()
                      if key.startswith("NOP_QA_") and key.endswith(("_EMAIL", "_PASSWORD")) and value]
    if os.environ.get("GITHUB_ACTIONS") == "true":
        for value in private_values:
            print(f"::add-mask::{value}", flush=True)
    demo = env.get("NOP_QA_SCREENSHOT_DEMO", "false") == "true"
    selection = (["-DscreenshotDemo=true",
                  "-Dtest=*Test,!FailureScreenshotChecksTest,!FailureScreenshotProbeTest"] if demo else [])
    mode = "screenshot-demo" if demo else "smoke"
    failure = None
    try:
        # Maven may fail before clean removes the previous run's report.
        REPORT.unlink(missing_ok=True)
        (REPORT.parent / "verification.json").unlink(missing_ok=True)
        try:
            subprocess.run([
                maven, "-B", "-ntp", "-f", "automation/pom.xml",
                f"-DbaseUrl={base_url}/", "-Dbrowser=chrome", "-Dheadless=true",
                *selection, "clean", "test",
            ], cwd=ROOT, env=env, check=True)
        except subprocess.CalledProcessError as error:
            failure = error
        finally:
            sanitize_reports(private_values)
        if demo:
            if failure is None or failure.returncode != 1:
                raise RuntimeError("Screenshot verification requires Maven test-failure exit code 1.")
            verify_demo_report()
        else:
            if failure is not None:
                raise failure
            verify_report()
        write_verification(mode, "passed")
    except Exception:
        try:
            write_verification(mode, "failed")
        except OSError:
            print("Could not persist verification status; original failure preserved.", flush=True)
        raise


def sanitize_reports(private_values):
    # A failed browser command can include submitted credentials in a report.
    for path in REPORT.parent.rglob("*"):
        if path.is_file():
            # PNG evidence is already masked in the browser. Byte replacement can corrupt it.
            if path.suffix.lower() not in {".xml", ".html", ".txt", ".json"}:
                continue
            original = path.read_bytes()
            sanitized = original
            for value in private_values:
                sanitized = sanitized.replace(value.encode("utf-8"), b"[redacted]")
            if sanitized != original:
                path.write_bytes(sanitized)


def main():
    maven = shutil.which("mvn")
    if maven is None:
        raise RuntimeError("Maven is required on PATH.")

    port = int(os.environ.get("NOP_QA_CI_PORT", "18080"))
    base_url = f"http://127.0.0.1:{port}"
    project = f"nopcommerce-qa-ci-{secrets.token_hex(6)}"
    env = {**os.environ, "NOP_QA_PORT": str(port), "NOP_QA_DB_PASSWORD": secrets.token_hex(24)}
    compose = ["docker", "compose", "-f", "automation/compose.qa.yaml", "-p", project]
    try:
        subprocess.run([*compose, "up", "-d", "--wait", "--wait-timeout", "120"],
                       cwd=ROOT, env=env, check=True)
        install_storefront(base_url, env["NOP_QA_DB_PASSWORD"])
        subprocess.run([*compose, "restart", "storefront"], cwd=ROOT, env=env, check=True)
        wait_for_page(browser_session(), base_url + "/", 'id="small-searchterms"')
        print("Creating the login and logout shopper fixtures.", flush=True)
        for fixture in ("LOGIN", "LOGOUT"):
            env.update(register_shopper(base_url, fixture))

        print("Running the full smoke suite in headless Chrome.", flush=True)
        run_suite(maven, base_url, env)
    finally:
        subprocess.run([*compose, "down", "--volumes"], cwd=ROOT, env=env, check=True)


if __name__ == "__main__":
    main()
