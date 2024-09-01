package pageObjectModels;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Page;

import playwrightUtilities.actions.PlaywrightElementActions;


public class HomePage extends BasePage{

	public static final Logger logger = LogManager.getLogger(HomePage.class);
	
	public HomePage(Page page) {
		super(page);
	}

	private String homePageHeader = "//div[@id='content']//h1";
	private String homePageDescription = "//div[@id='content']//p";
	
	
	/**
	 * get Home Page Header
	 * @return
	 */
	public String getHomePageHeader() {
		customWaitInSec(1);
		return PlaywrightElementActions.getText(page, homePageHeader).trim();
	}
	
	/**
	 * get Home Page Description
	 * @return
	 */
	public String getHomePageDescription() {
		return removeEscapeSequences(PlaywrightElementActions.getText(page, homePageDescription));
	}
	
}
