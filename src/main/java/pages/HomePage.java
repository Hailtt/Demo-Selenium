package pages;

import org.apache.poi.ss.usermodel.DateUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {

    private By searchInput = By.id("textInput");
    private By checkInBox = By.id("check-in-box");
    private By checkOutBox = By.id("check-out-box");
    private By occupancyBox = By.id("occupancy-box");

    private By occupancyText = By.xpath("//div[@data-selenium=\"occupancyBox\"]");
    private By searchButton = By.xpath("//button[@data-element-name='search-button']");
    private By sreachContentPopup = By.xpath("//div[contains(@class,\"Popup__content_Occupancy\")]");

    private String dayPicker = "//div[contains(@aria-label,'%s')]";
    private String occupancyOption = "//div[@data-element-name='occupancy-selector-panel']/div/div[%s]";

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void searchHotel(String hotelName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        setValue(searchInput, hotelName);
        wait.until(ExpectedConditions.visibilityOfElementLocated(sreachContentPopup));
        pressEscKey();
    }

    public void openCheckInBox() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        clickElement(checkInBox);
    }

    public void openOccupancyBox() {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        clickElement(occupancyBox);
    }

    public String getTextCheckInDayPicker() {
        return waitForElementVisible(checkInBox).getText();
    }

    public String getTextCheckOutDayPicker() {
        return waitForElementVisible(checkOutBox).getText();
    }

    public String getTextOccupancyBox() {
        return waitForElementVisible(occupancyText).getText().replace("&npsp;", " ");
    }

    public void clickSearchButton() {
        waitForElementClickable(searchButton);
    }

    public void selectDayPicker(String startDate, String endDate) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(checkInBox));
        clickElement(checkInBox);
        By startElement = By.xpath(String.format(dayPicker, startDate));
        By endElement = By.xpath(String.format(dayPicker, endDate));
        clickElement(startElement);
        clickElement(endElement);
        pressEscKey();
        pressEscKey();
    }

    public void setOccupancyQuantity(int indexOption, int targetNumber) {
        By rowLocator = By.xpath(String.format(occupancyOption, indexOption));
        WebElement rowElement = waitForElementVisible(rowLocator);

        String currentText = rowElement.findElement(By.xpath(".//p")).getText().trim();
        int currentNumber = Integer.parseInt(currentText);

        while (currentNumber < targetNumber) {
            rowElement.findElement(By.xpath(".//button[@data-selenium='plus']")).click();
            currentNumber++;
        }

        while (currentNumber > targetNumber) {
            rowElement.findElement(By.xpath(".//button[@data-selenium='minus']")).click();
            currentNumber--;
        }
    }
}