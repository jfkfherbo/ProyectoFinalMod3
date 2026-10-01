package org.orangehrm.pages;

import org.orangehrm.models.EmployeeData;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PersonalDetailsPage extends BasePage {
    private final By employeeId = By.xpath("//label[normalize-space()='Employee Id']/following::input[1]");
    private final By otherId = By.xpath("//label[normalize-space()='Other Id']/following::input[1]");
    private final By driverLicense = By.xpath("//label[contains(normalize-space(),'Driver') and contains(normalize-space(),'License Number')]/following::input[1]");
    private final By licenseExpiry = By.xpath("//label[normalize-space()='License Expiry Date']/following::input[1]");
    private final By nationality = By.xpath("//label[normalize-space()='Nationality']/following::div[contains(@class,'oxd-select-text')][1]");
    private final By maritalStatus = By.xpath("//label[normalize-space()='Marital Status']/following::div[contains(@class,'oxd-select-text')][1]");
    private final By dateOfBirth = By.xpath("//label[normalize-space()='Date of Birth']/following::input[1]");
    private final By bloodType = By.xpath("//label[normalize-space()='Blood Type']/following::div[contains(@class,'oxd-select-text')][1]");
    private final By testField = By.xpath("//label[normalize-space()='Test_Field']/following::input[1]");
    private final By savePersonal = By.xpath("//form[.//label[normalize-space()='Nationality']]//button[@type='submit']");
    private final By saveCustom = By.xpath("//form[.//label[normalize-space()='Blood Type']]//button[@type='submit']");
    private final By loader = By.xpath("//div[contains(@class,'oxd-form-loader')]");

    public PersonalDetailsPage(WebDriver driver) {
        super(driver);
    }

    public String getEmployeeId() {
        waitForVisibility(employeeId);
        return getValue(employeeId);
    }


    public void waitForEmployeeId(String expectedId) {
        new WebDriverWait(driver, Duration.ofSeconds(15))
                .until(d -> expectedId.equals(d.findElement(employeeId).getDomProperty("value")));
    }
    public String getOtherId() { return getValue(otherId); }
    public String getNationality() { return getText(nationality); }
    public String getMaritalStatus() { return getText(maritalStatus); }
    public String getBloodType() { return getText(bloodType); }
    public String getCustomField() { return getValue(testField); }

    public void completePersonalDetails(EmployeeData data) {
        replace(otherId, data.getOtherId());
        replace(driverLicense, data.getDriverLicense());
        replace(licenseExpiry, data.getLicenseExpiry());
        choose(nationality, data.getNationality());
        choose(maritalStatus, data.getMaritalStatus());
        replace(dateOfBirth, data.getDateOfBirth());
        chooseGender(data.getGender());
        waitForInvisibility(loader);
        driver.findElement(savePersonal).click();
    }

    public void completeCustomFields(EmployeeData data) {
        choose(bloodType, data.getBloodType());
        replace(testField, data.getCustomField());
        waitForInvisibility(loader);
        driver.findElement(saveCustom).click();
    }

    private void replace(By locator, String value) {
        waitForVisibility(locator);
        waitForInvisibility(loader);
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(value);
    }

    private void choose(By dropdown, String value) {
        waitForVisibility(dropdown);
        waitForInvisibility(loader);
        driver.findElement(dropdown).click();
        By option = By.xpath("//div[@role='option']//span[normalize-space()='" + value + "']");
        waitForVisibility(option);
        waitForInvisibility(loader);
        driver.findElement(option).click();
    }

    private String getValue(By locator) {
        waitForVisibility(locator);
        return driver.findElement(locator).getDomProperty("value");
    }

    private String getText(By locator) {
        waitForVisibility(locator);
        return driver.findElement(locator).getText().trim();
    }

    private void chooseGender(String gender) {
        By option = By.xpath("//label[normalize-space()='" + gender + "']");
        waitForVisibility(option);
        waitForInvisibility(loader);
        driver.findElement(option).click();
    }
}
