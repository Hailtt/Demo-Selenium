package pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import utils.DateTimeUtils;
import utils.ExtentReportManager;

import java.time.Duration;
import java.util.Set;

public class BasePage {
    protected static final Logger baseLog = LogManager.getLogger(BasePage.class);
    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public WebElement waitForElementClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    public WebElement waitForElementVisible(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    public void setValue(By locator, String text) {
        baseLog.debug("Setting value '{}' to element: {}", text, locator);
        WebElement element = waitForElementVisible(locator);
        element.clear();
        element.sendKeys(text);
    }

    public void clickElement(By locator) {
        baseLog.debug("Clicking on element: {}", locator);
        waitForElementClickable(locator).click();
    }

    public void pressEscKey() {
        Actions action = new Actions(driver);
        action.sendKeys(Keys.ESCAPE).build().perform();
    }

    public void switchToNewTab() {
        baseLog.info("Switching to new tab");
        ExtentReportManager.logInfo("Switching to new tab");
        String originalWindow = driver.getWindowHandle();
        Set<String> allWindows = driver.getWindowHandles();

        for (String windowHandle : allWindows) {
            if (!originalWindow.contentEquals(windowHandle)) {
                driver.switchTo().window(windowHandle);
                break;
            }
        }
        baseLog.info("Switched to new tab: {}", driver.getTitle());
        ExtentReportManager.logInfo("Switched to new tab: " + driver.getTitle());
    }

    protected void waitForModalLoadingSpinner() {
        By spinner = By.id("ModalLoadingSpinner");
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        wait.until(ExpectedConditions.invisibilityOfElementLocated(spinner));
    }

}
