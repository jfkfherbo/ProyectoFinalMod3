package org.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class PimPage extends BasePage {
    private final By pimMenu = By.xpath("//span[normalize-space()='PIM']");
    private final By addEmployeeTab = By.xpath("//a[normalize-space()='Add Employee']");
    private final By firstName = By.name("firstName");
    private final By middleName = By.name("middleName");
    private final By lastName = By.name("lastName");
    private final By employeeId = By.xpath("//label[normalize-space()='Employee Id']/following::input[1]");
    private final By createLoginSwitch = By.xpath("//p[normalize-space()='Create Login Details']/following-sibling::div[contains(@class,'oxd-switch-wrapper')]//span[contains(@class,'oxd-switch-input')]");
    private final By loader = By.xpath("//div[contains(@class,'oxd-form-loader')]");
    private final By loginUsername = By.xpath("//label[normalize-space()='Username']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private final By loginPassword = By.xpath("//label[normalize-space()='Password']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private final By confirmPassword = By.xpath("//label[normalize-space()='Confirm Password']/ancestor::div[contains(@class,'oxd-input-group')]//input");
    private final By usernameExists = By.xpath("//span[normalize-space()='Username already exists']");
    private final By save = By.xpath("//form//button[@type='submit']");
    private final By cancel = By.xpath("//form//button[normalize-space()='Cancel']");

    public PimPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToPim() {
        waitForVisibility(pimMenu);
        driver.findElement(pimMenu).click();
    }

    public String createEmployee(String first, String middle, String last,
                                 boolean createLogin, String user, String pass) {
        waitForVisibility(addEmployeeTab);
        int lastSuffix = createLogin ? 99 : 0;
        for (int suffix = 0; suffix <= lastSuffix; suffix++) {
            if (suffix > 0) {
                driver.findElement(cancel).click();
                waitForUrl("viewEmployeeList");
                waitForVisibility(addEmployeeTab);
            }

            driver.findElement(addEmployeeTab).click();
            waitForVisibility(firstName);
            driver.findElement(firstName).sendKeys(first);
            driver.findElement(middleName).sendKeys(middle);
            driver.findElement(lastName).sendKeys(last);

            if (createLogin) {
                waitForVisibility(createLoginSwitch);
                waitForInvisibility(loader);
                driver.findElement(createLoginSwitch).click();
                waitForVisibility(loginUsername);
                waitForInvisibility(loader);
                String candidate = suffix == 0 ? user : user + String.format("%02d", suffix);
                driver.findElement(loginUsername).sendKeys(candidate);
                driver.findElement(loginPassword).sendKeys(pass);
                driver.findElement(confirmPassword).sendKeys(pass);
            }

            waitForVisibility(employeeId);
            String generatedId = driver.findElement(employeeId).getDomProperty("value");
            waitForInvisibility(loader);
            driver.findElement(save).click();

            if (!createLogin) {
                waitForUrl("viewPersonalDetails");
                return generatedId;
            }

            new WebDriverWait(driver, Duration.ofSeconds(15)).until(d ->
                    d.getCurrentUrl().contains("viewPersonalDetails")
                            || d.findElements(usernameExists).stream().anyMatch(WebElement::isDisplayed));

            if (driver.getCurrentUrl().contains("viewPersonalDetails")) {
                return generatedId;
            }
        }

        throw new IllegalStateException("No se encontró un username disponible entre el nombre base y los sufijos 01 a 99.");
    }
}
