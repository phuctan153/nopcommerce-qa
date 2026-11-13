const byId = (id) => document.getElementById(id);
const moduleNames = {
  AuthenticationSmokeTest: "Authentication",
  CategoryNavigationTest: "Catalog",
  ProductSearchTest: "Catalog",
  ShoppingCartSmokeTest: "Shopping cart",
  TestConfigTest: "Configuration",
  ReportScreenshotDemoTest: "Evidence demo",
};
let tests = [];
let selectedStatus = "ALL";

function screenshotPaths(test) {
  return test.status === "FAIL" && Array.isArray(test.screenshots)
    ? test.screenshots.filter((path) => /^screenshots\/[a-f0-9]{64}\.png$/.test(path)) : [];
}

function openEvidence(test, path) {
  const image = byId("evidence-image");
  byId("evidence-title").textContent = (test.caseId || test.name) + " · " + test.description;
  byId("evidence-error").hidden = true;
  image.hidden = false;
  image.alt = "Masked failure screenshot for " + (test.caseId || test.name);
  image.onerror = () => {
    image.hidden = true;
    byId("evidence-error").hidden = false;
  };
  image.src = path;
  byId("open-evidence").href = path;
  byId("download-evidence").href = path;
  byId("evidence-dialog").showModal();
}

function duration(milliseconds) {
  const seconds = milliseconds / 1000;
  if (seconds < 60) return seconds.toFixed(1) + "s";
  return Math.floor(seconds / 60) + "m " + Math.round(seconds % 60) + "s";
}

function element(tag, className, text) {
  const node = document.createElement(tag);
  if (className) node.className = className;
  if (text !== undefined) node.textContent = text;
  return node;
}

function renderTests() {
  const query = byId("search").value.trim().toLowerCase();
  const module = byId("module").value;
  const visible = tests.filter((test) =>
    (selectedStatus === "ALL" || test.status === selectedStatus) &&
    (module === "ALL" || test.module === module) &&
    [test.caseId, test.description, test.name, test.module].join(" ").toLowerCase().includes(query)
  );
  byId("test-rows").replaceChildren(...visible.map((test) => {
    const row = element("tr");
    const name = element("td", "test-name", test.description);
    name.append(element("span", "test-id", test.caseId || test.name));
    if (test.intentionalFailure) name.append(element("span", "demo-label", "Intentional failure"));
    const status = element("td");
    const knownStatus = ["PASS", "FAIL", "SKIP"].includes(test.status) ? test.status : "UNKNOWN";
    const label = {PASS: "Passed", FAIL: "Failed", SKIP: "Skipped"}[knownStatus] || "Unknown";
    status.append(element("span", "badge " + knownStatus, label));
    row.append(name, element("td", "", test.module), status, element("td", "numeric", duration(test.durationMs)));
    const evidence = element("td", "evidence-cell");
    const images = screenshotPaths(test);
    if (images.length) {
      for (const [index, path] of images.entries()) {
        const button = element("button", "evidence-button", images.length > 1 ? "Screenshot " + (index + 1) : "View screenshot");
        button.type = "button";
        button.setAttribute("aria-label", "View screenshot for " + (test.caseId || test.name));
        button.addEventListener("click", () => openEvidence(test, path));
        evidence.append(button);
      }
    } else {
      evidence.append(element("span", "evidence-unavailable", test.status === "FAIL" ? "Unavailable" : "—"));
    }
    row.append(evidence);
    return row;
  }));
  byId("empty").hidden = visible.length !== 0;
  byId("showing").textContent = "Showing " + visible.length + " of " + tests.length + " tests";
}

function renderModules() {
  const modules = [...new Set(tests.map((test) => test.module))].sort();
  byId("modules").replaceChildren(...modules.map((name) => {
    const items = tests.filter((test) => test.module === name);
    const passed = items.filter((test) => test.status === "PASS").length;
    const row = element("div", "module-row");
    const bar = element("div", "module-bar");
    for (const [status, className] of [["PASS", "pass"], ["FAIL", "fail"], ["SKIP", "skip"]]) {
      const segment = element("span", className);
      segment.style.width = items.filter((test) => test.status === status).length / items.length * 100 + "%";
      bar.append(segment);
    }
    row.append(element("span", "", name), bar, element("span", "module-score", passed + "/" + items.length));
    return row;
  }));
  for (const name of modules) {
    const option = element("option", "", name);
    option.value = name;
    byId("module").append(option);
  }
}

function showNotice(message) {
  byId("notice").textContent = message;
  byId("notice").hidden = false;
}

async function loadReport() {
  try {
    const response = await fetch("data.json", {cache: "no-store"});
    if (!response.ok) throw new Error("Could not load report data.");
    const data = await response.json();
    tests = data.tests.map((test) => ({...test, module: moduleNames[test.className] || test.className}));
    const {total, passed, failed, skipped} = data.summary;
    const rate = total ? Math.round(passed / total * 100) : null;
    const rateText = rate === null ? "—" : rate + "%";
    byId("source").textContent = data.source;
    byId("started-at").textContent = data.startedAt || "No test start time";
    byId("total").textContent = data.available ? total : "—";
    byId("passed").textContent = data.available ? passed : "—";
    byId("failed").textContent = data.available ? failed : "—";
    byId("skipped").textContent = data.available ? skipped : "—";
    byId("pass-rate").textContent = rateText + " pass rate";
    byId("ring-rate").textContent = rateText;
    byId("duration").textContent = data.available ? duration(data.durationMs) : "—";
    byId("result-count").textContent = tests.length;
    byId("generated-at").textContent = "Updated " + new Date(data.generatedAt).toLocaleString();
    if (data.runUrl) byId("run-link").href = data.runUrl;
    if (data.commit) {
      byId("commit").textContent = data.commit.slice(0, 7);
      byId("commit").href = data.repositoryUrl + "/commit/" + data.commit;
      byId("commit").hidden = false;
    }
    const workflowFailed = ["failure", "cancelled"].includes(data.workflowStatus);
    const healthy = data.available && total > 0 && passed === total && !failed && !skipped && !workflowFailed;
    byId("run-status").className = "run-state " + (healthy ? "" : data.available ? "failed" : "warning");
    byId("run-status").textContent = healthy ? "All checks passed" : data.available ? "Run needs attention" : "Results unavailable";
    byId("health-title").textContent = healthy ? "Looking good. All tests passed." : data.available ? "This run needs a closer look." : "Tests did not produce a report.";
    byId("health-copy").textContent = healthy ? passed + " checks passed across " + new Set(tests.map((test) => test.module)).size + " modules." : "Open the workflow to inspect the failed or incomplete step.";
    if (total) {
      const passEnd = passed / total * 100;
      const failEnd = passEnd + failed / total * 100;
      byId("ring").style.background = "conic-gradient(#259c7b 0% " + passEnd + "%, #c74949 " + passEnd + "% " + failEnd + "%, #d4a344 " + failEnd + "% 100%)";
    }
    if (!data.available) showNotice("No TestNG results were produced for this run. Storefront setup may have failed. Open the workflow for details.");
    else if (data.demonstration) showNotice("Screenshot demonstration: DEMO-001 and DEMO-002 fail intentionally to show browser evidence. These are not reported nopCommerce defects. Normal smoke cases are listed separately; the workflow remains failed.");
    else if (workflowFailed) showNotice("The workflow failed or was cancelled. Test results below may be partial, or a provisioning or cleanup step may have failed.");
    renderModules();
    renderTests();
  } catch (error) {
    byId("run-status").className = "run-state warning";
    byId("run-status").textContent = "Report could not load";
    byId("showing").textContent = "No results loaded";
    showNotice("The report data could not be loaded. Refresh the page or open the GitHub workflow to check the latest run.");
  }
}

for (const button of document.querySelectorAll("[data-status]")) {
  button.addEventListener("click", () => {
    selectedStatus = button.dataset.status;
    for (const filter of document.querySelectorAll("[data-status]")) {
      const active = filter === button;
      filter.classList.toggle("active", active);
      filter.setAttribute("aria-pressed", String(active));
    }
    renderTests();
  });
}
byId("search").addEventListener("input", renderTests);
byId("module").addEventListener("change", renderTests);
byId("close-evidence").addEventListener("click", () => byId("evidence-dialog").close());
byId("evidence-dialog").addEventListener("click", (event) => {
  if (event.target === byId("evidence-dialog")) {
    const bounds = event.target.getBoundingClientRect();
    if (event.clientX < bounds.left || event.clientX > bounds.right
        || event.clientY < bounds.top || event.clientY > bounds.bottom) event.target.close();
  }
});
loadReport();
