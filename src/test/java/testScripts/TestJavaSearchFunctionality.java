package testScripts;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

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
import pageObjectModels.CoursePage;
import pageObjectModels.LoginPage;
import pageObjectModels.SearchPage;

/**
 * Test Login Page
 * 
 * @author Niraj.Tiwari
 */
public class TestJavaSearchFunctionality extends BaseTest{
	public static final Logger logger = LogManager.getLogger(TestJavaSearchFunctionality.class);
	
	/**
	 * Verify Java Search Functionality
	 */
	@FrameworkAnnotation(author = "Niraj", category = {CategoryType.SANITY, CategoryType.NAVIGATION}, description = "Test Java Search Functionality")
	@Test(dataProvider = "JavaSearchFunctionality", dataProviderClass = dataProvider.class,groups= {"TestJavaSearchFunctionality","Regression","Smoke"},retryAnalyzer = RetryAnalyzer.class)
	public void verifyJavaSearchFunctionality(String userType,String testCaseID,String loginType,String courseName,String expCourseHeaderText,String expCourseDescriptionText,String manualTCIDs) {
		
		ReportLogs.addLog(Status.INFO,"TestScript : Running -> Verify Java Search Functionality");

		//Open Application
		LoginPage loginPage = openApplication(System.getProperty("url"));
		ReportLogs.addLog(Status.INFO,"URL opened: Navigated to Pluralsight Login page");

		//Login to Pluralsight Application
		SearchPage searchPage = archUtil.loginToPluralsightApplication(loginPage, userType,loginType);
		ReportLogs.addLog(Status.INFO,"Successfully logged in Pluralsight Application");
		
		searchPage.searchRequiredCourseInSearchBox("Java");
		ReportLogs.addLog(Status.INFO,"Searched Java Course in search box");

		searchPage.clearAllTabs();
		ReportLogs.addLog(Status.INFO,"Cleared All tabs");

		//Validate Java Course Search Functionality
		searchPage.clickCourseTabDetails();
		List<String> listOfCourseDetails = searchPage.getCoursesListDetails();		
		assertThat(listOfCourseDetails)
			.hasSize(24)
			.containsOnlyOnce("Java Fundamentals: The Java Language")
			.doesNotContain("Python");
		ReportLogs.addLogWithScreenshot(Status.INFO,"Verified Search Java Course");

		CoursePage coursePage= searchPage.moveToCoursePage(courseName);
		ReportLogs.addLogWithScreenshot(Status.INFO,"Successfully moved to Course Page");

		String courseHeaderText = coursePage.getCoursePageHeader();
		ReportLogs.addLogForStringComparison(courseHeaderText, expCourseHeaderText,"Course Header Text");
		Assert.assertEquals(courseHeaderText, expCourseHeaderText,"Course Header Text is not equal");

		String courseDescriptionText = coursePage.getCourseDescription();
		ReportLogs.addLogForStringComparison(courseDescriptionText, expCourseDescriptionText,"Course Description Text");
		Assert.assertEquals(courseDescriptionText, expCourseDescriptionText,"Course Description Text is not equal");

		//Logout from Pluralsight Application
		searchPage.logoutFromPluralsightApplication();
		ReportLogs.addLog(Status.INFO,"Successfully logged out of Pluralsight Application");
	}
}
