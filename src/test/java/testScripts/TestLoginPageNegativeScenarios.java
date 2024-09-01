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

/**
 * Test Login Page
 * 
 * @author Niraj.Tiwari
 */
public class TestLoginPageNegativeScenarios extends BaseTest{
	public static final Logger logger = LogManager.getLogger(TestLoginPageNegativeScenarios.class);
	
	/**
	 * Verify Login Page Negative Scenarios
	 */
	@FrameworkAnnotation(author = "Niraj", category = {CategoryType.SANITY, CategoryType.SMOKE, CategoryType.NAVIGATION}, description = "Test Login Page Negative Scenarios")
	@Test(dataProvider = "NegativeLoginScenarios", retryAnalyzer = RetryAnalyzer.class, dataProviderClass = dataProvider.class,groups= {"TestLoginPageNegativeScenarios","Regression","Smoke"})
	public void verifyLoginPageNegativeScenarios(String userType,String testCaseID,String username,String password,String expErrorMessage,String manualTCIDs) {
		ReportLogs.addLog(Status.INFO,"TestScript : Running -> verify Login Page Negative Scenarios");

		//Open Application
		LoginPage loginPage = openApplication(System.getProperty("url"));
		ReportLogs.addLog(Status.INFO,"URL opened: Navigated to Pluralsight Login page");

		//Login to Pluralsight Application
		String ribbonText = archUtil.checkNegativeLoginScenarios(loginPage, username, password);
		ReportLogs.addLogForStringComparison(ribbonText, expErrorMessage,"Error Message Text");
	    Assert.assertEquals(ribbonText, expErrorMessage,"Error Message Text is not equal");
	    
	    ReportLogs.addLogWithScreenshot(Status.INFO,"Verified Negative Login Scenarios");
	}
}
