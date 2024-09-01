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
import pageObjectModels.CoursePage;
import pageObjectModels.LoginPage;
import pageObjectModels.SearchPage;

/**
 * Test Login Page
 * 
 * @author Niraj.Tiwari
 */
public class TestCoursePageFeatures extends BaseTest{
	public static final Logger logger = LogManager.getLogger(TestCoursePageFeatures.class);
	
	/**
	 * Verify Course Page Features
	 */
	@FrameworkAnnotation(author = "Niraj", category = {CategoryType.SANITY, CategoryType.SMOKE}, description = "Test Course Page Features")
	@Test(dataProvider = "CoursePageFeatures",dataProviderClass = dataProvider.class,groups= {"TestCoursePageFeatures","Regression","Smoke"},retryAnalyzer = RetryAnalyzer.class)
	public void verifyCoursePageFeatures(String userType,String testCaseID,String loginType,String courseName,String expCourseHeaderText, 
			String expCourseDescriptionText,String expFreeTrailButtonText, String expCourseOverviewText,String manualTCIDs) {
		
		ReportLogs.addLog(Status.INFO,"TestScript : Running -> Verify Course Page Features");

		//Open Application
		LoginPage loginPage = openApplication(System.getProperty("url"));
		ReportLogs.addLog(Status.INFO,"URL opened: Navigated to Pluralsight Login page");

		//Login to Pluralsight Application
		SearchPage searchPage = archUtil.loginToPluralsightApplication(loginPage, userType,loginType);
		ReportLogs.addLog(Status.INFO,"Successfully logged in Pluralsight Application");

		//page.pause();
		searchPage.clearAllTabs();

		searchPage.clickCourseTabDetails();
		
		CoursePage coursePage= searchPage.moveToCoursePage(courseName);
		ReportLogs.addLog(Status.INFO,"Successfully moved to Course Page");
		
		//Validate course page details
		String courseHeaderText = coursePage.getCoursePageHeader();
		ReportLogs.addLogForStringComparison(courseHeaderText, expCourseHeaderText,"Course Header Text");
		Assert.assertEquals(courseHeaderText, expCourseHeaderText, "Course Header Text is not equal");
		
		String courseDescriptionText = coursePage.getCourseDescription();
		ReportLogs.addLogForStringComparison(courseDescriptionText, expCourseDescriptionText,"Course Description Text");
		Assert.assertEquals(courseDescriptionText, expCourseDescriptionText,"Course Description Text is not equal");
		ReportLogs.addLogWithScreenshot(Status.INFO,"Course Page verification");
		
		boolean isAuthorLinkVisible = coursePage.validateAuthorLinkVisible();
		Assert.assertTrue(isAuthorLinkVisible, "Author Link is not visible");
		
		String freeTrailButtonText = coursePage.getFreeTrailButtonText();
		ReportLogs.addLogForStringComparison(freeTrailButtonText, expFreeTrailButtonText,"Free Trail Button Text");
		Assert.assertEquals(freeTrailButtonText, expFreeTrailButtonText,"Free Trail Button Text is not equal");
		
		String courseOverviewText = coursePage.getCourseOverviewButtonText();
		ReportLogs.addLogForStringComparison(courseOverviewText, expCourseOverviewText,"Course Header Text");
		Assert.assertEquals(courseOverviewText, expCourseOverviewText,"Course Overview Text is not equal");
		ReportLogs.addLogWithScreenshot(Status.INFO,"Course Overview verification");
				
		//Logout from Pluralsight Application
		searchPage.logoutFromPluralsightApplication();
		ReportLogs.addLog(Status.INFO,"Successfully logged out of Pluralsight Application");
	}
}
