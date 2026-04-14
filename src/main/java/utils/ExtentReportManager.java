package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ExtentReportManager {
    private static ExtentReports extent;
    private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentReports getInstance() {
        if (extent == null) {
            String reportPath = System.getProperty("user.dir") + "/target/ExtentReports/AgodaTestReport.html";
            
            // Xóa folder screenshots cũ trước khi bắt đầu suite mới
            cleanOldScreenshots();

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("Agoda Automation Report");
            spark.config().setReportName("Test Execution Report");
            
            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Project", "Demo Selenium Agoda");
            extent.setSystemInfo("Environment", "QA");
            extent.setSystemInfo("User", "Tester");
        }
        return extent;
    }

    private static void cleanOldScreenshots() {
        String directory = System.getProperty("user.dir") + "/target/screenshots/";
        File folder = new File(directory);
        if (folder.exists()) {
            try {
                FileUtils.cleanDirectory(folder);
            } catch (IOException e) {
                System.out.println("Could not clean old screenshots: " + e.getMessage());
            }
        }
    }

    public static ExtentTest getTest() {
        return test.get();
    }

    public static void setTest(ExtentTest extentTest) {
        test.set(extentTest);
    }
    
    public static void logInfo(String message) {
        if (getTest() != null) {
            getTest().info(message);
        }
    }

    public static void logInfoWithScreenshot(String message) {
        if (getTest() != null) {
            WebDriver driver = DriverManager.getDriver();
            if (driver != null) {
                String fileName = captureScreenshot(driver, "Step");
                String relativePath = "../screenshots/" + fileName;
                getTest().info(message, MediaEntityBuilder.createScreenCaptureFromPath(relativePath).build());
            } else {
                getTest().info(message);
            }
        }
    }
    
    public static void logPass(String message) {
        if (getTest() != null) {
            getTest().pass(message);
        }
    }
    
    public static void logFail(String message) {
        if (getTest() != null) {
            getTest().fail(message);
        }
    }

    private static String captureScreenshot(WebDriver driver, String name) {
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
        return fileName;
    }
}
