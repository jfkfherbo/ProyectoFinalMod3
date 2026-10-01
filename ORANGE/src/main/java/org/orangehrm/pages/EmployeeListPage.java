package org.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class EmployeeListPage extends BasePage {
    private final By employeeListTab = By.xpath("//a[normalize-space()='Employee List']");
    private final By employeeIdSearch = By.xpath("//label[normalize-space()='Employee Id']/following::input[1]");
    private final By search = By.xpath("//form[.//label[normalize-space()='Employee Id']]//button[@type='submit']");
    private final By firstRow = By.xpath("//div[contains(@class,'oxd-table-body')]//div[@role='row'][.//div[@role='cell']][1]");
    private final By firstRowId = By.xpath("//div[contains(@class,'oxd-table-body')]//div[@role='row'][.//div[@role='cell']][1]//div[@role='cell'][2]");
    private final By editFirstRow = By.xpath("//div[contains(@class,'oxd-table-body')]//div[@role='row'][.//div[@role='cell']][1]//button[.//i[contains(@class,'bi-pencil-fill')]]");

    public EmployeeListPage(WebDriver driver) {
        super(driver);
    }

    public void searchEmployeeById(String id) {
        waitForVisibility(employeeListTab);
        driver.findElement(employeeListTab).click();
        waitForVisibility(employeeIdSearch);
        driver.findElement(employeeIdSearch).clear();
        driver.findElement(employeeIdSearch).sendKeys(id);
        driver.findElement(search).click();
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(d -> id.equals(d.findElement(firstRowId).getText().trim()));
    }

    public boolean hasResults() {
        return isVisible(firstRow);
    }

    public String getFirstResultId() {
        waitForVisibility(firstRowId);
        return driver.findElement(firstRowId).getText();
    }

    public void editFirstEmployee() {
        waitForVisibility(editFirstRow);
        driver.findElement(editFirstRow).click();
        waitForUrl("viewPersonalDetails");
    }
}
