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
import pageObjectModels.LoginPage;
import pageObjectModels.SearchPage;

/**
 * Test Login Page
 * 
 * @author Niraj.Tiwari
 */
public class TestLoginPage extends BaseTest{
	public static final Logger logger = LogManager.getLogger(TestLoginPage.class);
	
	/**
	 * Verify Login for all roles
	 */
	@FrameworkAnnotation(author = "Niraj", category = {CategoryType.SMOKE, CategoryType.NAVIGATION}, description = "Test Login Page")
	@Test(dataProvider = "LoginData",dataProviderClass = dataProvider.class,groups= {"TestLoginPage","Regression","Smoke"},retryAnalyzer = RetryAnalyzer.class)
	public void verifyLoginFunctionality(String userType,String testCaseID,String loginType,String expSearchText,String manualTCIDs) {
		ReportLogs.addLog(Status.INFO,"TestScript : Running -> verify Login Functionality");

		//Open Application
		LoginPage loginPage = openApplication(System.getProperty("url"));
		ReportLogs.addLog(Status.INFO,"URL opened: Navigated to Pluralsight Login page");

		//Login to Pluralsight Application
		SearchPage searchPage = archUtil.loginToPluralsightApplication(loginPage, userType,loginType);
		ReportLogs.addLog(Status.INFO,"Successfully logged in Pluralsight Application");
		
		//assert validation
		String actSearchBoxText = searchPage.getSearchPlaceholderText();
		ReportLogs.addLogForStringComparison(actSearchBoxText, expSearchText,"Search Text Box Text");
		Assert.assertEquals(actSearchBoxText, expSearchText,"Search Text Box Text is not equal");
		ReportLogs.addLogWithScreenshot(Status.INFO,"Verified login functionality");
		//Assert.assertFalse(true);
		
		//Logout from Pluralsight Application
		searchPage.logoutFromPluralsightApplication();
		ReportLogs.addLog(Status.INFO,"Successfully logged out of Pluralsight Application");	
	}
}
