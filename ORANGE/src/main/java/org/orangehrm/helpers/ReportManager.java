package org.orangehrm.helpers;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;

public class ReportManager {
    private static ReportManager instance;
    private final ExtentReports report;
    private final ConcurrentHashMap<Long, ExtentTest> tests = new ConcurrentHashMap<>();

    private ReportManager(String filePath, String reportName) {
        File parent = new File(filePath).getParentFile();
        if (parent != null) {
            parent.mkdirs();
        }
        report = new ExtentReports();
        ExtentSparkReporter html = new ExtentSparkReporter(filePath);
        html.config().setDocumentTitle("Automation Report " + reportName);
        html.config().setReportName(reportName);
        html.config().setTheme(Theme.STANDARD);
        html.config().setEncoding("utf-8");
        report.attachReporter(html);
    }

    public static synchronized void init(String filePath, String reportName) {
        if (instance != null) {
            throw new IllegalStateException("ExtentReports ya fue inicializado");
        }
        instance = new ReportManager(filePath, reportName);
    }

    public static ReportManager getInstance() {
        if (instance == null) {
            throw new IllegalStateException("Inicializa ReportManager antes de usarlo");
        }
        return instance;
    }

    public ExtentTest startTest(String name) {
        ExtentTest test = report.createTest(name);
        tests.put(Thread.currentThread().getId(), test);
        return test;
    }

    public ExtentTest getTest() {
        return tests.get(Thread.currentThread().getId());
    }

    public void flush() {
        report.flush();
    }
}
