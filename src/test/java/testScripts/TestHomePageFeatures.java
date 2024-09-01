package testScripts;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.aventstack.extentreports.Status;

import annotations.FrameworkAnnotation;
import commonUtility.BaseTest;
import commonUtility.dataProvider;
import enums.CategoryType;
import helperTestUtility.ReportLogs;
import helperTestUtility.RetryAnalyzer;
import pageObjectModels.BasePage;
import pageObjectModels.HomePage;
import pageObjectModels.LoginPage;
import pageObjectModels.SearchPage;

/**
 * Test Login Page
 * 
 * @author Niraj.Tiwari
 */
public class TestHomePageFeatures extends BaseTest{
	public static final Logger logger = LogManager.getLogger(TestHomePageFeatures.class);
	
	/**
	 * Verify Home Page Features
	 */
	@FrameworkAnnotation(author = "Niraj", category = {CategoryType.SANITY, CategoryType.SMOKE, CategoryType.REGRESSION}, description = "Test Home Page Features")
	@Test(dataProvider = "HomePageFeatures", dataProviderClass = dataProvider.class,groups= {"TestHomePageFeatures","Regression","Smoke"},retryAnalyzer = RetryAnalyzer.class)
	public void verifyHomePageFeatures(String userType,String testCaseID,String loginType,String expHomePageHeader,String expHomePageDesc,String manualTCIDs) {
		ReportLogs.addLog(Status.INFO,"TestScript : Running -> Verify Home Page Features");

		//Open Application
		LoginPage loginPage = openApplication(System.getProperty("url"));
		ReportLogs.addLog(Status.INFO,"URL opened: Navigated to Pluralsight Login page");

		//Login to Pluralsight Application
		SearchPage searchPage = archUtil.loginToPluralsightApplication(loginPage, userType,loginType);
		ReportLogs.addLog(Status.INFO,"Successfully logged in Pluralsight Application");
		
		//move to home page
		HomePage homePage =searchPage.moveToHomePage();
		ReportLogs.addLogWithScreenshot(Status.INFO,"Successfully moved to Course Page");
	    
		//assert validations
		String actHomePageHeader = homePage.getHomePageHeader();
		ReportLogs.addLogForStringComparison(BasePage.removeEscapeSequences(actHomePageHeader), BasePage.removeEscapeSequences(expHomePageHeader),"Home Page Header Text");
		Assert.assertEquals(BasePage.removeEscapeSequences(actHomePageHeader), BasePage.removeEscapeSequences(expHomePageHeader),"Home Page Header Text");
		
	    String actHomePageDesc = homePage.getHomePageDescription();
		ReportLogs.addLogForStringComparison(BasePage.removeEscapeSequences(actHomePageDesc), BasePage.removeEscapeSequences(expHomePageDesc),"Home Page Description Text");
	    Assert.assertEquals(BasePage.removeEscapeSequences(actHomePageDesc), BasePage.removeEscapeSequences(expHomePageDesc),"Home Page Description Text is not equal");
	    
		ReportLogs.addLogWithScreenshot(Status.INFO, "Home Page Verification");
				
		//Logout from Pluralsight Application
		searchPage.logoutFromPluralsightApplication();
		ReportLogs.addLog(Status.INFO,"Successfully logged out of Pluralsight Application");
	}
}
