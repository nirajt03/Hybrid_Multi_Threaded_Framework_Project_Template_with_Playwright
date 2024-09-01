package pageObjectModels;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Page;

import playwrightUtilities.actions.PlaywrightElementActions;



public class SearchPage  extends BasePage{

	public static final Logger logger = LogManager.getLogger(SearchPage.class);
	
	public SearchPage(Page page) {
		super(page);
	}

	//Filters option
	//private String filtersTab = "//div[@id='Filters']";	

	//Courses tab
	//private String coursesTab = "//div[@id='courses_tabs']";

	private String clearActiveAllTabs = "//a[text()='All']//parent::li[contains(@class,'ui-tabs-active')]";
	private String clearAllTabs = "//a[text()='All']//parent::li[not(contains(@class,'ui-tabs-active'))]";
	
	//private String activeCourseTab = "//a[text()='Courses']//parent::li[contains(@class,'ui-tabs-active')]";
	//private String courseTabBlockVisible = "//div[@id='tabs']//div[@id='tabs-2' and @style='display: block;']";
	//private String courseTabBlockInvisible = "//div[@id='tabs']//div[@id='tabs-2' and @style='display: none;']";


	/**
	 * Search Page Filter Tags
	 * @author Niraj.Tiwari
	 */
	public enum SearchPageFilterTags{
		SkillLevels("Skill Levels"),Authors("Authors"),Roles("Roles"),Certifications("Certifications");

		private String searchPageFilterTags; 

		SearchPageFilterTags(String searchPageFilterTags) {
			this.searchPageFilterTags = searchPageFilterTags;
		}

		public String getSearchPageFilterTagName() {
			return searchPageFilterTags;
		}
	}

	/**
	 * Search Page NavBar List Tabs
	 * @author Niraj.Tiwari
	 */
	public enum SearchPageNavBarListTabs{
		All("All"),Courses("Courses"),Blog("Blog"),Resources("Resources"),Authors("Authors");

		private String searchPageNavBarListTab; 

		SearchPageNavBarListTabs(String searchPageNavBarListTab) {
			this.searchPageNavBarListTab = searchPageNavBarListTab;
		}

		public String getSearchPageNavBarListTabName() {
			return searchPageNavBarListTab;
		}
	}

	//---------------------------Filter tabs methods------------------------------------

	/**
	 * select Required Filter Tab
	 * @param filterTag
	 */
	public void selectRequiredFilterTab(SearchPageFilterTags filterTag) {
		customWaitInSec(1);
		String filterTagValue = filterTag.getSearchPageFilterTagName();
		String filterTagDiv = "//div[text()='"+filterTagValue+"']";
		PlaywrightElementActions.waitForVisibility(page, filterTagDiv);
		PlaywrightElementActions.click(page, filterTagDiv);
	}

	/**
	 * check Selected Filter Header Active
	 * @param filterTagName
	 * @return
	 */
	public boolean checkSelectedFilterHeaderActive(String filterTagName) {
		String activeFilterHeader ="//div[.='"+filterTagName+"']//parent::h3[contains(@class,'accordion-header-active')]";
		if(!PlaywrightElementActions.isVisible(page, activeFilterHeader)) {
			throw new RuntimeException("Element not found");
		}
		return true;
	}

	/**
	 * get List Of Selected Filter Options
	 * @param filterTag
	 * @return
	 */
	public List<String> getListOfSelectedFilterOptions(SearchPageFilterTags filterTag) {
		customWaitInSec(1);
		String filterTagValue = filterTag.getSearchPageFilterTagName();
		checkSelectedFilterHeaderActive(filterTagValue);		
		String filterTagDiv = "//div[text()='"+filterTagValue+"']//ancestor::h3[contains(@class,'ui-accordion-header-active')]//following-sibling::div[contains(@class,'ui-accordion-content-active')]//span";
		return PlaywrightElementActions.getAllInnerText(page, filterTagDiv);
	}

	/**
	 * close Selected Filter Active Div
	 * @param filterTagName
	 * @return
	 */
	public boolean closeSelectedFilterActiveDiv(String filterTagName) {
		customWaitInSec(1);
		checkSelectedFilterHeaderActive(filterTagName);
		String activeFilterHeader = "//div[.='"+filterTagName+"']//parent::h3[contains(@class,'accordion-header-active')]";
		PlaywrightElementActions.waitForVisibility(page, activeFilterHeader);
		PlaywrightElementActions.click(page, activeFilterHeader);
		String collapsedFilterHeader = "//div[.='"+filterTagName+"']//parent::h3[contains(@class,'accordion-header-collapsed')]";
		PlaywrightElementActions.waitForVisibility(page, collapsedFilterHeader);
		if(!PlaywrightElementActions.isVisible(page, collapsedFilterHeader)) {
			throw new RuntimeException("Element not found");
		}
		return true;
	}

	//---------------------------Nav Bar tabs methods------------------------------------

	/**
	 * select Required Tab In Nav Bar
	 * @param requiredTab
	 */
	public void selectRequiredTabInNavBar(SearchPageNavBarListTabs requiredTab) {
		String navBartabValue = requiredTab.getSearchPageNavBarListTabName();
		String navBarTabDiv = "//div[@id='courses_tabs']//a[text()='"+navBartabValue+"']";
		PlaywrightElementActions.click(page, navBarTabDiv);
	}

	/**
	 * click Course Tab Details
	 */
	public void clickCourseTabDetails() {
		selectRequiredTabInNavBar(SearchPageNavBarListTabs.Courses);
	}

	/**
	 * get Courses List Details Count
	 * @param xpath
	 * @return
	 */
	public int getCoursesListDetailsCount(String xpath) {
		return PlaywrightElementActions.getElementCount(page, xpath);
	}

	/**
	 * get Courses List Details
	 * @return
	 */
	public List<String> getCoursesListDetails() {
		List<String> listOfCourseDetails = new ArrayList<String>();
		String coursesList = "(//div[contains(@class,'search-results-rows')]//div[contains(@class,'columns')])";
		int totalCourses = getCoursesListDetailsCount(coursesList);
		for (int i = 1; i < totalCourses; i++) {
			String courseTitleBy = "(//div[contains(@class,'columns')]//div[contains(@class,'title')])["+i+"]";
			PlaywrightElementActions.hoverOverElement(page, courseTitleBy);
			String courseTitle = PlaywrightElementActions.getText(page, courseTitleBy).trim();
		    listOfCourseDetails.add(courseTitle);			
		}
		return listOfCourseDetails;
	}

	/**
	 * validate Other Tabs Details
	 * @param navBarTabs
	 * @return
	 */
	public List<String> validateOtherTabsDetails(List<SearchPageNavBarListTabs> navBarTabs) {
		List<String> listOfTabDetails = new ArrayList<String>();
		String detailsText = null;
		for (SearchPageNavBarListTabs tab : navBarTabs) {
			clearAllTabs();
			selectRequiredTabInNavBar(tab);
			String tabDetailstext = "//div[contains(@id,'tabs') and @style='display: block;']//p";
			detailsText = PlaywrightElementActions.getText(page, tabDetailstext).trim();
			listOfTabDetails.add(detailsText);
		}	
		return listOfTabDetails;
	}

	/**
	 * clear All Tabs
	 */
	public void clearAllTabs() {	
		customWaitInSec(2);
		if(PlaywrightElementActions.isVisible(page, clearActiveAllTabs)) {
			PlaywrightElementActions.waitForVisibility(page, clearActiveAllTabs);
			PlaywrightElementActions.hoverOverElement(page, clearActiveAllTabs);
			PlaywrightElementActions.click(page, clearActiveAllTabs);
		}else {
			PlaywrightElementActions.waitForVisibility(page, clearAllTabs);
			PlaywrightElementActions.hoverOverElement(page, clearAllTabs);
			PlaywrightElementActions.click(page, clearAllTabs);
		}
	}
	
	/**
	 * click On Required Course
	 * @param courseName
	 */
	public void clickOnRequiredCourse(String courseName) {
		String courseTitleBy = "(//div[contains(@class,'columns')]//div[contains(@class,'title')]//a[text()='"+courseName+"'])";
		PlaywrightElementActions.click(page, courseTitleBy);
	}
	
	/**
	 * move To Course Page
	 * @param courseName
	 * @return
	 */
	public CoursePage moveToCoursePage(String courseName) {
		clickOnRequiredCourse(courseName);
		return new CoursePage(page);
	}
	
	
}
