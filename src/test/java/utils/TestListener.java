package utils;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Test Suite started: " + context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.getInstance().flush();
        System.out.println("Test Suite finished: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        ExtentTest test = ExtentReportManager.getInstance().createTest(result.getTestClass().getName() + " - " + result.getMethod().getMethodName());
        ExtentReportManager.setTest(test);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.logPass("Test Passed");
    }

    @Override
    public void onTestFailure(ITestResult result) {
        String methodName = result.getMethod().getMethodName();
        WebDriver driver = DriverManager.getDriver();
        
        if (driver != null) {
            String fileName = captureScreenshot(driver, methodName);
            // Use relative path for HTML report to find images: ../screenshots/fileName
            String relativePath = "../screenshots/" + fileName;
            ExtentReportManager.getTest().log(Status.FAIL, "Test Failed: " + result.getThrowable())
                    .addScreenCaptureFromPath(relativePath);
        } else {
            ExtentReportManager.logFail("Test Failed: " + result.getThrowable());
        }
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getTest().log(Status.SKIP, "Test Skipped");
    }

    private String captureScreenshot(WebDriver driver, String name) {
        String timeStamp = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
        String fileName = name + "_" + timeStamp + ".png";
        String directory = System.getProperty("user.dir") + "/target/screenshots/";
        
        File folder = new File(directory);
        if (!folder.exists()) folder.mkdirs();

        File srcFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        String destination = directory + fileName;
        try {
            FileUtils.copyFile(srcFile, new File(destination));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fileName; // Return ONLY the filename for relative path construction
    }
}
