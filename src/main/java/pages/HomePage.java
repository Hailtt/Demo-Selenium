package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class HomePage extends BasePage {
    // Khai báo các Locators
    private By searchInput = By.id("textInput-search");
    private By searchButton = By.xpath("//button[@data-selenium='searchButton']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public void searchHotel(String hotelName) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchInput));
        sendKey(searchInput, hotelName);
    }
}