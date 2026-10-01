package org.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.nio.file.Path;

public class AttachmentsPage extends BasePage {
    private final By add = By.xpath("//button[contains(@class,'oxd-button') and contains(normalize-space(.),'Add')]");
    private final By file = By.xpath("//input[@type='file']");
    private final By comment = By.xpath("//textarea[@placeholder='Type comment here']");
    private final By save = By.xpath("//div[contains(@class,'orangehrm-attachment')]//button[@type='submit']");
    private final By attachmentList = By.xpath("//div[contains(@class,'orangehrm-attachment')]");

    public AttachmentsPage(WebDriver driver) {
        super(driver);
    }

    public void addAttachment(String filePath, String note) {
        waitForVisibility(add);
        driver.findElement(add).click();
        waitForPresence(file);
        driver.findElement(file).sendKeys(Path.of(filePath).toAbsolutePath().normalize().toString());
        driver.findElement(comment).sendKeys(note);
        driver.findElement(save).click();
    }

    public boolean attachmentIsDisplayed(String fileName) {
        By uploadedFile = By.xpath("//div[contains(@class,'orangehrm-attachment')]//*[contains(normalize-space(.),'" + fileName + "')]");
        return isVisible(attachmentList) && isVisible(uploadedFile);
    }
}
