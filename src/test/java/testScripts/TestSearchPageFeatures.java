package testScripts;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
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
import pageObjectModels.LoginPage;
import pageObjectModels.SearchPage;
import pageObjectModels.SearchPage.SearchPageFilterTags;
import pageObjectModels.SearchPage.SearchPageNavBarListTabs;

/**
 * Test Login Page
 * 
 * @author Niraj.Tiwari
 */
public class TestSearchPageFeatures extends BaseTest{
	public static final Logger logger = LogManager.getLogger(TestSearchPageFeatures.class);
	
	/**
	 * Verify Search Page Features
	 */
	@FrameworkAnnotation(author = "Niraj", category = {CategoryType.SANITY, CategoryType.SMOKE, CategoryType.NAVIGATION}, description = "Test Search Page Features")
	@Test(dataProvider = "SearchPageFeatures", dataProviderClass = dataProvider.class,groups= {"TestSearchPageFeatures","Regression","Smoke"},retryAnalyzer = RetryAnalyzer.class)
	public void verifySearchPageFeatures(String userType,String testCaseID,String loginType,String courseName,String manualTCIDs) {
		ReportLogs.addLog(Status.INFO,"TestScript : Running -> Verify Search Page Features");

		//Open Application
		LoginPage loginPage = openApplication(System.getProperty("url"));
		ReportLogs.addLog(Status.INFO,"URL opened: Navigated to Pluralsight Login page");

		//Login to Pluralsight Application
		SearchPage searchPage = archUtil.loginToPluralsightApplication(loginPage, userType,loginType);
		ReportLogs.addLog(Status.INFO,"Successfully logged in Pluralsight Application");
		
		searchPage.searchRequiredCourseInSearchBox(courseName);
		ReportLogs.addLog(Status.INFO,"Searched required course in Search box");

		//Check Filter options
		searchPage.selectRequiredFilterTab(SearchPageFilterTags.SkillLevels);		
		boolean isSkillLevelDivVisible = searchPage.checkSelectedFilterHeaderActive(SearchPageFilterTags.SkillLevels.getSearchPageFilterTagName());
		Assert.assertTrue(isSkillLevelDivVisible, "Failed to assert Skill Level filter options");
		List<String> listOfSkillLevelFilters = searchPage.getListOfSelectedFilterOptions(SearchPageFilterTags.SkillLevels);
		assertThat(listOfSkillLevelFilters)
			.hasSize(3)
			.contains("Advanced","Beginner","Intermediate");
		Assert.assertTrue(searchPage.closeSelectedFilterActiveDiv(SearchPageFilterTags.SkillLevels.getSearchPageFilterTagName()), "Failed to close Skill Level filter options");
		ReportLogs.addLogWithScreenshot(Status.INFO,"Verified Skills Levels Filter Functionality");
		
		searchPage.selectRequiredFilterTab(SearchPageFilterTags.Roles);		
		boolean isRolesDivVisible = searchPage.checkSelectedFilterHeaderActive(SearchPageFilterTags.Roles.getSearchPageFilterTagName());
		Assert.assertTrue(isRolesDivVisible, "Failed to assert Skill Level filter options");
		List<String> listOfRolesFilters = searchPage.getListOfSelectedFilterOptions(SearchPageFilterTags.Roles);
		assertThat(listOfRolesFilters)
			.hasSize(6)
			.contains("Business Professional","Creative Professional","Data Professional","IT Ops","Information & Cyber Security","Software Development");
		Assert.assertTrue(searchPage.closeSelectedFilterActiveDiv(SearchPageFilterTags.Roles.getSearchPageFilterTagName()), "Failed to close Roles filter options");
		ReportLogs.addLogWithScreenshot(Status.INFO,"Verified Roles Filter Functionality");
		
		//Check Nav Bar Tabs
		searchPage.clearAllTabs();
		ReportLogs.addLog(Status.INFO,"Successfully cleared all tabs");

		searchPage.clickCourseTabDetails();
		List<String> listOfCourseDetails = searchPage.getCoursesListDetails();		
		assertThat(listOfCourseDetails)
			.hasSize(24)
			.containsOnlyOnce("Java Fundamentals: The Java Language")
			.doesNotContain("Python");
		ReportLogs.addLogWithScreenshot(Status.INFO,"Verified Click on Course Functionality");

		List<SearchPageNavBarListTabs> listOfTabOptions = new ArrayList<SearchPage.SearchPageNavBarListTabs>();
		listOfTabOptions.add(SearchPageNavBarListTabs.Blog);
		listOfTabOptions.add(SearchPageNavBarListTabs.Resources);
		listOfTabOptions.add(SearchPageNavBarListTabs.Authors);
		
		List<String> listOfTabDetails = searchPage.validateOtherTabsDetails(listOfTabOptions);
		assertThat(listOfTabDetails)
			.hasSize(3)
			.contains("Blog posts","Resources","Authors");
		ReportLogs.addLogWithScreenshot(Status.INFO,"Verified Tab details");
		
		//Logout from Pluralsight Application
		searchPage.logoutFromPluralsightApplication();
		ReportLogs.addLog(Status.INFO,"Successfully logged out of Pluralsight Application");
	}
}
