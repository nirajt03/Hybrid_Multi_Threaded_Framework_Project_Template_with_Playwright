package pageObjectModels;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Page;

import playwrightUtilities.actions.PlaywrightElementActions;


/**
 * Base Page
 * @author Niraj.Tiwari
 */
public abstract class BasePage {

	protected Page page;

	public static final Logger logger = LogManager.getLogger(BasePage.class);

	public BasePage(Page page) {
		this.page = page;
	}

	public static String originalHandle;
	public static String newhandle; 
	public static boolean isAlertAccepted;

	protected String highlightedOption = "//li[contains(@class,'highlighted')]";

	//Common Header elements	
	protected String logoutIcon = "//div[@id='logout']";
	protected String getStartedIcon = "//div[@id='getStarted']";
	protected String pluralsightIcon = "//div[@id='sitename']";
	protected String coursesLink = "//div[@id='courses']";
	protected String headerSearchBox = "//input[contains(@class,'header_search')]";


	/**
	 * move To Home Page
	 * @return
	 */
	public HomePage moveToHomePage() {
		navigateToDifferentPage("https://nirajt03.github.io/sample-website/HomePage.html");
		return new HomePage(page);
	}

	/**
	 * Logout From Pluralsight Application
	 * @return
	 */
	public LoginPage logoutFromPluralsightApplication() {
		page.onceDialog(dailog -> {
			logger.info("Dailog message: "+ dailog.message());
			dailog.accept();
		});
		PlaywrightElementActions.click(page, logoutIcon);
		logger.info("Logout button clicked");
	
	
		logger.info("Successfully Logged Out from Pluralsight application");
		return (new LoginPage(page));
	}

	/**
	 * Search Required Course In Search Box
	 * @param courseToBeSearched
	 */
	public void searchRequiredCourseInSearchBox(String courseToBeSearched) {
		PlaywrightElementActions.typeText(page, headerSearchBox, courseToBeSearched);
		PlaywrightElementActions.pressKey(page, headerSearchBox, "Enter");
	}

	/**
	 * Get Site Header Text
	 * @return
	 */
	public String getSiteHeaderText() {
		return PlaywrightElementActions.getText(page, pluralsightIcon);
	}

	/**
	 * get Course Link Text
	 * @return
	 */
	public String getCourseLinkText() {
		return PlaywrightElementActions.getText(page, coursesLink);
	}

	/**
	 * get Search Placeholder Text
	 * @return
	 */
	public String getSearchPlaceholderText() {
		return PlaywrightElementActions.getAttributeValue(page, headerSearchBox,"placeholder");
	}

	/**
	 * get Started Icon Text
	 * @return
	 */
	public String getStartedIconText() {
		return PlaywrightElementActions.getText(page, headerSearchBox);
	}


	/**
	 * Custom Wait In Sec
	 * @param timeOut
	 */
	public void customWaitInSec(long timeOut)  {
		try {
			TimeUnit.SECONDS.sleep(timeOut);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void refreshPage() {
		page.reload();
	}

	public void navigateToDifferentPage(String url) {
		page.navigate(url);
	}

	/**
	 * Get Title
	 * @return
	 */
	public String getTitle() {
		return page.title();
	}

	/**
	 * Get Random Operation Element
	 * @param list
	 * @param totalOperation
	 * @return
	 */
	public List<String> getRandomOperationElements(List <String> list, int totalOperation) {
		Random random = new Random();
		List<String> newList = new ArrayList<>();
		for (int i = 0; i < totalOperation; i++) {
			int randomIndex = random.nextInt(list.size());
			newList.add(list.get(randomIndex));
		}
		return newList;
	}
	
	public static String removeEscapeSequences(String input) {
		return input.replaceAll("\\s+", " ").trim();
	}

}
