package tests;

import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import utils.DriverManager;

public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    @Parameters({ "browser", "url" })
    public void setUp(@Optional("chrome") String browser, @Optional(utils.Constants.AGODA_URL) String url) {
        DriverManager.setDriver(browser);
        this.driver = DriverManager.getDriver();
        this.driver.get(url);
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}
