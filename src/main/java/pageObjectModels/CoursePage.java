package pageObjectModels;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Page;

import playwrightUtilities.actions.PlaywrightElementActions;

public class CoursePage extends BasePage {

	public static final Logger logger = LogManager.getLogger(CoursePage.class);

	public CoursePage(Page page) {
		super(page);
	}

	private String titleHeader = "//div[contains(@class,'title section')]//h1";
	private String courseDescription = "//div[@class='text-component']";
	private String authorLink = "//span[@class='course-hero-rating']//following-sibling::a";
	private String freeTrailBtn = "//a[@id='free_trial']";
	private String playCourseOverviewBtn = "//div[@id='course_overview']//a";

	//methods 
	/**
	 * get Course PageHeader
	 * @return
	 */
	public String getCoursePageHeader() {
		customWaitInSec(1);
		return PlaywrightElementActions.getText(page, titleHeader);
	}


	/**
	 * get Course Description
	 * @return
	 */
	public String getCourseDescription() {
		//WebElementUtility.explicitWaitForElementToBeVisible(driver, courseDescription, 15);

		return removeEscapeSequences(PlaywrightElementActions.getText(page,courseDescription));
	}

	/** 
	 * validate Author Link Visible
	 * @return
	 */
	public boolean validateAuthorLinkVisible() {
		//WebElementUtility.explicitWaitForElementToBeVisible(driver, authorLink, 10);
		return PlaywrightElementActions.isVisible(page, authorLink);
	}

	/**
	 * get Free Trail Button Text
	 * @return
	 */
	public String getFreeTrailButtonText() {
		//WebElementUtility.explicitWaitForElementToBeVisible(driver, freeTrailBtn, 15);
		return PlaywrightElementActions.getText(page, freeTrailBtn);
	}

	/**
	 * get Course Overview Button Text
	 * @return
	 */
	public String getCourseOverviewButtonText() {
		//WebElementUtility.explicitWaitForElementToBeVisible(driver, playCourseOverviewBtn, 15);
		return PlaywrightElementActions.getText(page, playCourseOverviewBtn);
	}

	/**
	 * move To Search page
	 * @return
	 */
	public SearchPage moveToSearchpage() {
		//WebElementUtility.explicitWaitForElementToBeVisible(driver, coursesLink, 10);
		PlaywrightElementActions.click(page, coursesLink);
		customWaitInSec(2);
		return new SearchPage(page);
	}

}
