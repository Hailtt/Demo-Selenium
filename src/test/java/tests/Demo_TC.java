package tests;

import org.testng.Assert;
import org.testng.annotations.Test;

import pages.HomePage;

public class Demo_TC extends BaseTest {

    @Test(description = "Verify price display for specific hotel search")
    public void verifyHotelPriceDisplay() {

        HomePage homePage = new HomePage(this.driver);
    }
}
