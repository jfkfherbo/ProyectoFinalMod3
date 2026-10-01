package conf;

import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.orangehrm.helpers.ReportManager;
import org.orangehrm.helpers.ScreenShotHelper;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.HashMap;
import java.util.Map;

public class BaseTest {
    protected WebDriver driver;
    private static final String URL = "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login";
    private static final Logger log = LogManager.getLogger(BaseTest.class);

    @BeforeSuite
    public static void setupSuite() {
        ReportManager.init("target/reports/orangehrm.html", "ORANGE Regression");
    }

    @BeforeMethod
    @Parameters({"browser"})
    public void setup(ITestResult result, @Optional("chrome") String browser) throws Exception {
        String name = result.getMethod().getDescription();
        if (name == null || name.isBlank()) {
            name = result.getMethod().getMethodName();
        }
        ReportManager.getInstance().startTest(name + " " + java.util.Arrays.toString(result.getParameters()));

        switch (browser.toLowerCase()) {
            case "chrome" -> driver = new ChromeDriver(chromeOptions());
            case "firefox" -> driver = new FirefoxDriver();
            default -> throw new IllegalArgumentException("Navegador no soportado: " + browser);
        }
        driver.manage().window().maximize();
        driver.get(URL);
        log.info("Navegando a {} con {}", URL, browser);
        ScreenShotHelper.takeScreenShotAndAddToHTMLReport(driver, Status.INFO, "Página de inicio de sesión");
    }

    @AfterMethod
    public void tearDown(ITestResult result) {
        try {
            var test = ReportManager.getInstance().getTest();
            if (result.getStatus() == ITestResult.FAILURE) {
                test.log(Status.FAIL, "La prueba falló: " + result.getThrowable().getMessage());
                if (driver != null) {
                    ScreenShotHelper.takeScreenShotAndAddToHTMLReport(driver, Status.FAIL, "Captura del fallo");
                }
            } else if (result.getStatus() == ITestResult.SUCCESS) {
                test.log(Status.PASS, "La prueba finalizó correctamente");
            } else {
                test.log(Status.SKIP, "La prueba fue omitida");
            }
        } catch (Exception e) {
            log.error("No se pudo actualizar el reporte", e);
        } finally {
            if (driver != null) {
                driver.quit();
                driver = null;
            }
        }
    }

    @AfterSuite
    public static void tearDownSuite() {
        ReportManager.getInstance().flush();
    }

    private ChromeOptions chromeOptions() {
        Map<String, Object> preferences = new HashMap<>();
        preferences.put("credentials_enable_service", false);
        preferences.put("profile.password_manager_enabled", false);
        preferences.put("profile.password_manager_leak_detection", false);

        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("prefs", preferences);
        options.addArguments("--disable-features=PasswordLeakDetection,AutofillServerCommunication");
        return options;
    }
}
