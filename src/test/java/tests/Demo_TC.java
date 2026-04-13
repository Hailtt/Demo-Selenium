package tests;

import org.apache.poi.ss.usermodel.DateUtil;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import pages.HomePage;
import utils.DateTimeUtils;

public class Demo_TC extends BaseTest {

    private HomePage homePage;

    private String searchValue = "Muong Thanh Saigon Centre Hotel";
    private String formatDayPicker = "MMM dd yyyy";
    private String formatCheckingDay = "dd MMM yyyy";
    private int startDayFromToday = 2;
    private int endDayFromToday = 3;

    @BeforeMethod
    void init() {
        homePage = new HomePage(this.driver);
    }

    @Test(description = "Verify price display for specific hotel search")
    public void verifyHotelPriceDisplay() {

        // STEP 1: input into Srearch box
        homePage.searchHotel(searchValue);

        // STEP 2
        selecAndVerifyDayPicker();

        // STEP 3
        selecAndVerifyAdult();

        homePage.clickSearchButton();
    }

    private void selecAndVerifyDayPicker() {
        String startDateStr = DateTimeUtils.getTargetDate(startDayFromToday, formatDayPicker);
        String endDateStr = DateTimeUtils.getTargetDate(endDayFromToday, formatDayPicker);
        homePage.selectDayPicker(startDateStr, endDateStr);

        // Format Day
        startDateStr = DateTimeUtils.formatStringDate(startDateStr, formatDayPicker, formatCheckingDay);
        endDateStr = DateTimeUtils.formatStringDate(endDateStr, formatDayPicker, formatCheckingDay);

        String actualStartDaypicker = homePage.getTextCheckInDayPicker();
        String actualEndDaypicker = homePage.getTextCheckOutDayPicker();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(actualStartDaypicker.contains(startDateStr), "Check-in date is WRONG!");
        softAssert.assertTrue(actualEndDaypicker.contains(endDateStr), "Check-out date is WRONG!");
        softAssert.assertAll();
    }

    private void selecAndVerifyAdult() {

        homePage.openOccupancyBox();
        homePage.setOccupancyQuantity(1, 1);
        homePage.setOccupancyQuantity(2, 4);
        homePage.setOccupancyQuantity(3, 2);
        homePage.pressEscKey();

        String actualOccupancy = homePage.getTextOccupancyBox();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(actualOccupancy.contains("1 room"), "Room is WRONG!");
        softAssert.assertTrue(actualOccupancy.contains("4 adults"), "Adults is WRONG!");
        softAssert.assertTrue(actualOccupancy.contains("2 children"), "Children is WRONG!");
        softAssert.assertAll();
    }

}
