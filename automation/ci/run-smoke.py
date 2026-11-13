"""Provision a disposable nopCommerce storefront and run the full smoke suite."""

import os
from http.client import HTTPException
from http.cookiejar import CookieJar
from pathlib import Path
import re
import secrets
import shutil
import subprocess
import time
from urllib.parse import urlencode, urlsplit
from urllib.request import HTTPCookieProcessor, ProxyHandler, build_opener
import xml.etree.ElementTree as ET


ROOT = Path(__file__).resolve().parents[2]
REPORT = ROOT / "automation/target/surefire-reports/testng-results.xml"
TOKEN = "__RequestVerificationToken"


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


def verify_report():
    summary = ET.parse(REPORT).getroot()
    total, passed, failed, skipped = (
        int(summary.attrib[name]) for name in ("total", "passed", "failed", "skipped")
    )
    if total < 14 or passed != total or failed or skipped:
        raise RuntimeError(
            f"Expected at least 14 tests with all passing. "
            f"TestNG reported {total} total, {passed} passed, {failed} failed, {skipped} skipped."
        )
    print(f"TestNG confirmed all {total} tests passed with zero skips.", flush=True)


def run_suite(maven, base_url, env):
    private_values = [value for key, value in env.items()
                      if key.startswith("NOP_QA_") and key.endswith(("_EMAIL", "_PASSWORD")) and value]
    if os.environ.get("GITHUB_ACTIONS") == "true":
        for value in private_values:
            print(f"::add-mask::{value}", flush=True)
    try:
        subprocess.run([
            maven, "-B", "-ntp", "-f", "automation/pom.xml",
            f"-DbaseUrl={base_url}/", "-Dbrowser=chrome", "-Dheadless=true", "clean", "test",
        ], cwd=ROOT, env=env, check=True)
    finally:
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
    verify_report()


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
