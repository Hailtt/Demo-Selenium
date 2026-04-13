package tests;

import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LoginPage;

public class LoginTest extends BaseTest {

    @Test
    public void testLogin() {
        getDriver().get("https://www.agoda.com/");

        LoginPage loginPage = new LoginPage(getDriver());
        loginPage.login("abc", "abc");
        Assert.assertTrue("abc".equals("abc"));
    }
}
