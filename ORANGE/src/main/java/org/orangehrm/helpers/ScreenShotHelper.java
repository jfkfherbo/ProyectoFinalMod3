package org.orangehrm.helpers;

import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;

public class ScreenShotHelper {
    private ScreenShotHelper() { }

    public static String takeScreenShot(WebDriver driver) {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
    }

    public static void takeScreenShotAndAddToHTMLReport(
            WebDriver driver, Status status, String details) throws IOException {
        String image = takeScreenShot(driver);
        ReportManager.getInstance().getTest().log(status, details,
                MediaEntityBuilder.createScreenCaptureFromBase64String(image).build());
    }
}
