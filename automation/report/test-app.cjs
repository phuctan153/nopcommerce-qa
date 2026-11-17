// Run with: node automation/report/test-app.cjs <directory-containing-node_modules>
const assert = require("node:assert/strict");
const fs = require("node:fs");
const path = require("node:path");
const { JSDOM } = require(require.resolve("jsdom", { paths: [path.resolve(process.argv[2] || ".")] }));
const html = fs.readFileSync(path.join(__dirname, "index.html"), "utf8");
const script = fs.readFileSync(path.join(__dirname, "app.js"), "utf8");
const image = "screenshots/" + "a".repeat(64) + ".png";
const cases = [
  { className: "ProductSearchTest", name: "search", caseId: "TC-CAT-001", description: "Find a product", status: "PASS", durationMs: 100 },
  { className: "ReportScreenshotDemoTest", name: "price", caseId: "DEMO-002", description: "Intentional price failure", status: "FAIL", durationMs: 100, screenshots: [image], intentionalFailure: true },
  { className: "ReportScreenshotDemoTest", name: "searchDemo", caseId: "DEMO-001", description: "Intentional search failure", status: "FAIL", durationMs: 100, screenshots: [image], intentionalFailure: true },
];

async function load(tests, extra = {}) {
  const dom = new JSDOM(html, { url: "https://example.com/nopcommerce-qa/", runScripts: "outside-only" });
  const { window } = dom;
  window.fetch = async (url) => {
    if (url.startsWith("https://api.github.com/")) {
      if (extra.deploymentUnavailable) return {ok: false};
      return {ok: true, json: async () => ({jobs: [{name: "deploy-report", conclusion: extra.deploymentConclusion || "success"}]})};
    }
    return { ok: true, json: async () => ({ tests, available: true, source: "Local test", generatedAt: new Date().toISOString(), durationMs: 1000, summary: { total: tests.length, passed: 1, failed: tests.length - 1, skipped: 0 }, automationStatus: "success", demonstration: true, verification: {mode: "screenshot-demo", status: "passed"}, runUrl: "https://github.com/phuctan153/nopcommerce-qa/actions/runs/123", ...extra }) };
  };
  window.HTMLDialogElement.prototype.showModal = function () { this.open = true; };
  window.HTMLDialogElement.prototype.close = function () { this.open = false; };
  window.eval(script);
  await new Promise(setImmediate);
  return window;
}

(async () => {
  let window = await load(cases);
  const doc = window.document;
  assert.equal(doc.querySelectorAll("#test-rows tr").length, 3);
  assert.equal(doc.querySelectorAll(".evidence-button").length, 2);
  assert.match(doc.querySelector("#notice").textContent, /demonstration/);
  assert.equal(doc.querySelector("#failed").textContent, "2");
  assert.equal(doc.querySelector("#run-status").textContent, "Screenshot verification passed");
  assert.equal(doc.querySelector("#case-status").textContent, "1 passed · 2 failed · 0 skipped");
  assert.equal(doc.querySelector("#verification-status").textContent, "Screenshot check passed");
  assert.equal(doc.querySelector("#deployment-status").textContent, "Succeeded");
  doc.querySelector('[data-status="FAIL"]').click();
  assert.equal(doc.querySelectorAll("#test-rows tr").length, 2);
  doc.querySelector(".evidence-button").click();
  assert.equal(doc.querySelector("#evidence-dialog").open, true);
  assert.match(doc.querySelector("#evidence-title").textContent, /DEMO-002/);
  assert.equal(doc.querySelector("#evidence-image").getAttribute("src"), image);
  assert.equal(doc.querySelector("#open-evidence").getAttribute("href"), image);
  assert.equal(doc.querySelector("#download-evidence").getAttribute("href"), image);
  doc.querySelector("#evidence-image").dispatchEvent(new window.Event("error"));
  assert.equal(doc.querySelector("#evidence-error").hidden, false);
  doc.querySelector("#close-evidence").click();
  assert.equal(doc.querySelector("#evidence-dialog").open, false);
  doc.querySelectorAll(".evidence-button")[1].click();
  assert.match(doc.querySelector("#evidence-title").textContent, /DEMO-001/);
  assert.equal(doc.querySelector("#evidence-error").hidden, true);
  assert.equal(doc.querySelector("#evidence-image").hidden, false);
  doc.querySelector("#search").value = "price";
  doc.querySelector("#search").dispatchEvent(new window.Event("input"));
  assert.equal(doc.querySelectorAll("#test-rows tr").length, 1);
  doc.querySelector("#search").value = "absent";
  doc.querySelector("#search").dispatchEvent(new window.Event("input"));
  assert.equal(doc.querySelector("#empty").hidden, false);
  window.close();

  window = await load([{ ...cases[1], screenshots: ["../private.png", "https://example.com/private.png"] }]);
  assert.equal(window.document.querySelectorAll(".evidence-button").length, 0);
  assert.match(window.document.querySelector(".evidence-cell").textContent, /Unavailable/);
  window.close();

  window = await load([{ ...cases[0], screenshots: [image] }], { demonstration: false, verification: {mode: "smoke", status: "passed"}, summary: { total: 1, passed: 1, failed: 0, skipped: 0 } });
  assert.equal(window.document.querySelectorAll(".evidence-button").length, 0);
  assert.equal(window.document.querySelector("#notice").hidden, true);
  assert.match(window.document.querySelector("#run-status").textContent, /Test suite passed/);
  assert.equal(window.document.querySelector("#verification-status").textContent, "Normal suite verified");
  window.close();

  window = await load(cases, {verification: {mode: "screenshot-demo", status: "failed"}, automationStatus: "failure"});
  assert.equal(window.document.querySelector("#run-status").textContent, "Automation needs attention");
  assert.match(window.document.querySelector("#notice").textContent, /not verified/);
  assert.equal(window.document.querySelector("#deployment-status").textContent, "Succeeded");
  window.close();

  window = await load(cases, {deploymentConclusion: "failure"});
  assert.equal(window.document.querySelector("#verification-status").textContent, "Screenshot check passed");
  assert.equal(window.document.querySelector("#deployment-status").textContent, "Failed");
  window.close();

  window = await load(cases, {deploymentUnavailable: true});
  assert.equal(window.document.querySelector("#deployment-status").textContent, "Status unavailable · check workflow");
  window.close();

  window = await load(cases, {verification: {status: "unavailable", mode: "unknown"}});
  assert.equal(window.document.querySelector("#verification-status").textContent, "Not verified");
  assert.notEqual(window.document.querySelector("#run-status").textContent, "Screenshot verification passed");
  window.close();

  window = await load(cases, {automationStatus: "failure"});
  assert.match(window.document.querySelector("#verification-status").textContent, /execution failed/);
  assert.equal(window.document.querySelector("#run-status").textContent, "Automation needs attention");
  window.close();
  console.log("Dashboard DOM checks passed: raw Fail results, verification/deployment independence, API fallback, filters, images, and failure states.");
})().catch((error) => { console.error(error); process.exitCode = 1; });
