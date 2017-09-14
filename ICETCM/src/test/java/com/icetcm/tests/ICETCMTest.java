package com.icetcm.tests;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

public class ICETCMTest {
	
	ExtentReports extent;
    ExtentTest test;
    WebDriver driver;
   
    @BeforeTest
    public void init()
    {
    	driver = new FirefoxDriver();
        extent = new ExtentReports(System.getProperty("user.dir") + "/test-output/MonitoringResults.html", true);
    }
     
    @Test
    public void icetTest() throws InterruptedException
    {
        test = extent.startTest("monitoringTest");        
        test.log(LogStatus.PASS, "Browser started");
        driver.get("http://bldbz5200.boulder.ibm.com:9080/icet/app/login.html");
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        
        WebElement ele=driver.findElement(By.xpath("//*[@id='ibm-leadspace-body']/div/div/h1"));
        String content=ele.getText();
        System.out.println("Page header message :  "+content);
        Assert.assertEquals("Welcome to ICET Login", content);
        test.log(LogStatus.PASS, "Header message");
        boolean searchEmailPresence = driver.findElement(By.xpath("//input[@name='j_username']")).isDisplayed();
        boolean searchPasswordPresence = driver.findElement(By.xpath("//input[@name='j_password']")).isDisplayed();
        
        if (searchEmailPresence==true && searchPasswordPresence==true)
        {
        	WebElement button=driver.findElement(By.xpath("//div/p/input[@value='Submit']"));
        	button.click();
        	
        } 
        WebElement warn=driver.findElement(By.xpath("//*[@id='ibm-content-main']/div[1]/div/form/p[2]"));
        String res=warn.getText();
    	System.out.println("Login button text is : "+res);
    	Assert.assertEquals("You are not authorized to work with ICET application or your credentials is not correct.", res);
    	test.log(LogStatus.PASS, "Warn Message");
    }
           
    @AfterMethod
    public void getResult(ITestResult result) throws IOException
    {
        if(result.getStatus() == ITestResult.FAILURE)
        {
            String screenShotPath = GetScreenShot.capture(driver, "Monitoring");
            test.log(LogStatus.FAIL, result.getThrowable());
            test.log(LogStatus.FAIL, "Snapshot below: " + test.addScreenCapture(screenShotPath));
        }
        extent.endTest(test);
    }
     
         
    @AfterTest
    public void endreport()
    {
        driver.close();
        extent.flush();
        extent.close();
    }

}
