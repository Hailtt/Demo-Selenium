package pages;

import java.time.Duration;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.ExtentReportManager;

public class SearchPage extends BasePage {
    private static final Logger log = LogManager.getLogger(SearchPage.class);

    private By containerPage = By.id("reactSearchPage");

    private String itemXpath = "//ol/li[%d]//div[@dir='ltr']";
    private String titleXpath = "//ol/li[%d]//header//a";

    public SearchPage(WebDriver driver) {
        super(driver);
        isOpened();
    }

    public boolean isOpened() {
        log.info("Checking if SearchPage is opened");
        ExtentReportManager.logInfo("Checking if SearchPage is opened");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        waitForModalLoadingSpinner();
        boolean isDisplayed = wait.until(ExpectedConditions.visibilityOfElementLocated(containerPage))
                .isDisplayed();
        driver.findElement(By.id("occupancy-box")).click();
        log.info("SearchPage opened: {}", isDisplayed);
        ExtentReportManager.logInfo("SearchPage opened: " + isDisplayed);
        return isDisplayed;
    }

    public String getTitleItemByIndex(int index) {
        log.info("Getting title of hotel at index {}", index);
        ExtentReportManager.logInfo("Getting title of hotel at index " + index);
        By currentItemTitle = By.xpath(String.format(titleXpath, index));
        String title = wait.until(ExpectedConditions.visibilityOfElementLocated(currentItemTitle)).getText();
        log.info("Hotel title at index {}: {}", index, title);
        ExtentReportManager.logInfo("Hotel title at index " + index + ": " + title);
        return title;
    }

    public void clickItemByIndex(int index) {
        log.info("Clicking on hotel at index {}", index);
        ExtentReportManager.logInfo("Clicking on hotel at index " + index);
        By currentItem = By.xpath(String.format(itemXpath, index));
        wait.until(ExpectedConditions.elementToBeClickable(currentItem)).click();
    }
}
