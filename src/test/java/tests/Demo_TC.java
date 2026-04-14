package tests;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.DataProvider;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import pages.HomePage;
import pages.NorthstarPage;
import pages.SearchPage;
import utils.DateTimeUtils;
import utils.ExtentReportManager;
import utils.JsonUtils;

import java.util.List;
import java.util.Map;

public class Demo_TC extends BaseTest {

    private static final Logger log = LogManager.getLogger(Demo_TC.class);
    private HomePage homePage;
    private SearchPage searchPage;
    private NorthstarPage northstarPage;

    private String formatDayPicker = "MMM dd yyyy";
    private String formatCheckingDay = "dd MMM yyyy";
    private String titleFirstItem;

    @BeforeMethod
    public void init() {
        homePage = new HomePage(this.driver);
    }

    @DataProvider(name = "hotelData")
    public Object[][] getHotelData() {
        String path = "src/test/resources/testdata/hotels.json";
        List<Map<String, Object>> dataList = JsonUtils.getData(path);

        Object[][] data = new Object[dataList.size()][1];
        for (int i = 0; i < dataList.size(); i++) {
            data[i][0] = dataList.get(i);
        }
        return data;
    }

    @Test(description = "Verify price display for specific hotel search", dataProvider = "hotelData")
    public void verifyHotelPriceDisplay(Map<String, Object> data) {
        String searchValue = data.get("hotelName").toString();
        int startDayFromToday = Integer.parseInt(data.get("startDayFromToday").toString());
        int endDayFromToday = Integer.parseInt(data.get("endDayFromToday").toString());

        ExtentReportManager.logInfoWithScreenshot("START TEST: verifyHotelPriceDisplay with hotel: " + searchValue);

        // STEP 1: Input into Search Box
        ExtentReportManager.logInfoWithScreenshot("Step 1: Searching for hotel: " + searchValue);
        homePage.searchHotel(searchValue);

        // STEP 2: Select CheckInDay and CheckOutDay
        ExtentReportManager.logInfoWithScreenshot("Step 2: Selecting dates");
        selecAndVerifyDayPicker(startDayFromToday, endDayFromToday);

        // STEP 3: Change value of Adults
        ExtentReportManager.logInfoWithScreenshot("Step 3: Setting occupancy");
        selecAndVerifyAdult(data);

        // STEP 4: Click Search button
        ExtentReportManager.logInfoWithScreenshot("Step 4: Clicking search button");
        searchPage = homePage.clickSearchButton();

        // STEP 5: Click on the first available option
        ExtentReportManager.logInfoWithScreenshot("Step 5: Clicking on first item");
        clickOnTheFirstItem();

        // STEP 6: Switch new tab and verify
        ExtentReportManager.logInfoWithScreenshot("Step 6: Verifying hotel details in new tab");
        switchToNewTabAndVerify();
        
        ExtentReportManager.logInfoWithScreenshot("TEST CASE FINISHED SUCCESSFULLY");
    }

    private void selecAndVerifyDayPicker(int startDay, int endDay) {
        String startDateStr = DateTimeUtils.getTargetDate(startDay, formatDayPicker);
        String endDateStr = DateTimeUtils.getTargetDate(endDay, formatDayPicker);
        homePage.selectDayPicker(startDateStr, endDateStr);

        startDateStr = DateTimeUtils.formatStringDate(startDateStr, formatDayPicker, formatCheckingDay);
        endDateStr = DateTimeUtils.formatStringDate(endDateStr, formatDayPicker, formatCheckingDay);

        String actualStartDaypicker = homePage.getTextCheckInDayPicker();
        String actualEndDaypicker = homePage.getTextCheckOutDayPicker();

        ExtentReportManager.logInfo("Verify Check-in: Expected [" + startDateStr + "], Actual [" + actualStartDaypicker + "]");
        ExtentReportManager.logInfo("Verify Check-out: Expected [" + endDateStr + "], Actual [" + actualEndDaypicker + "]");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(actualStartDaypicker.contains(startDateStr), "Check-in date mismatch!");
        softAssert.assertTrue(actualEndDaypicker.contains(endDateStr), "Check-out date mismatch!");
        softAssert.assertAll();
    }

    private void selecAndVerifyAdult(Map<String, Object> data) {
        int rooms = Integer.parseInt(data.get("rooms").toString());
        int adults = Integer.parseInt(data.get("adults").toString());
        int children = Integer.parseInt(data.get("children").toString());

        homePage.openOccupancyBox();
        homePage.setOccupancyQuantity(1, rooms);
        homePage.setOccupancyQuantity(2, adults);
        homePage.setOccupancyQuantity(3, children);
        homePage.pressEscKey();

        String actualOccupancy = homePage.getTextOccupancyBox();

        ExtentReportManager.logInfo("Verify Rooms: Expected [" + rooms + "], Actual [" + actualOccupancy + "]");
        ExtentReportManager.logInfo("Verify Adults: Expected [" + adults + "], Actual [" + actualOccupancy + "]");
        ExtentReportManager.logInfo("Verify Children: Expected [" + children + "], Actual [" + actualOccupancy + "]");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(actualOccupancy.contains(rooms + " room"), "Room count mismatch!");
        softAssert.assertTrue(actualOccupancy.contains(adults + " adults"), "Adult count mismatch!");
        softAssert.assertTrue(actualOccupancy.contains(children + " children"), "Children count mismatch!");
        softAssert.assertAll();
    }

    private void clickOnTheFirstItem() {
        titleFirstItem = searchPage.getTitleItemByIndex(1).trim();
        searchPage.clickItemByIndex(1);
    }

    private void switchToNewTabAndVerify() {
        northstarPage = new NorthstarPage(this.driver);
        northstarPage.switchToNewTab();
        northstarPage.isOpened();

        String actualTitle = northstarPage.getTitleHotel().trim();
        String actualPrice = northstarPage.getPriceHotel().trim();

        ExtentReportManager.logInfo("Verify Hotel Title: Expected [" + titleFirstItem + "], Actual [" + actualTitle + "]");
        ExtentReportManager.logInfo("Verify Hotel Price: Expected [Not Empty], Actual [" + actualPrice + "]");

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(actualTitle.equals(titleFirstItem), "Hotel Title mismatch!");
        softAssert.assertFalse(actualPrice.isEmpty(), "Hotel Price is missing!");
        softAssert.assertAll();
    }
}
