package org.orangehrm.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {
    private static final Duration WAIT = Duration.ofSeconds(15);
    protected final WebDriver driver;

    public BasePage(WebDriver driver) {
        this.driver = driver;
    }

    protected void waitForVisibility(By locator) {
        new WebDriverWait(driver, WAIT)
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected void waitForPresence(By locator) {
        new WebDriverWait(driver, WAIT)
                .until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected void waitForInvisibility(By locator) {
        new WebDriverWait(driver, WAIT)
                .until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }

    protected void waitForUrl(String urlPart) {
        new WebDriverWait(driver, WAIT)
                .until(ExpectedConditions.urlContains(urlPart));
    }

    protected boolean isVisible(By locator) {
        try {
            waitForVisibility(locator);
            return true;
        } catch (TimeoutException notVisible) {
            return false;
        }
    }
}
