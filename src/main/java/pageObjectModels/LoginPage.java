package pageObjectModels;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.microsoft.playwright.Page;

import helperUtility.EncryptDecrypt;
import playwrightUtilities.actions.PlaywrightElementActions;

/**
 * Login Page
 * 
 * @author Niraj.Tiwari
 */
public class LoginPage extends BasePage{

	public static final Logger logger = LogManager.getLogger(LoginPage.class);

	public LoginPage(Page page) {
		super(page);
	}

	private String loginForm = "//div[@id='login_left' and @class='inputForm']";
	private String username = "//input[@id='Username']";
	private String password = "//input[@id='Password']";
	private String loginBtn = "//span[@id='login_on_login_page']";
	private String headerRibbonTextVisible = "//div[@id='sign_in_fail' and @style='display: block;']//p";
	//private String headerRibbonTextInvisible = "//div[@id='sign_in_fail' and @style='display: none;']//p";

	/**
	 * Pluralsight Application Login
	 * @param userName
	 * @param password
	 * @return
	 */
	public SearchPage pluralsightApplicationLogin(String username, String password) { //<T extends BasePage>T

		// Wait till the login page is visible
		PlaywrightElementActions.waitForVisibility(page, loginForm);

		//Decrpyt & Enter Username 
		enterUsername(EncryptDecrypt.decryptString(username));

		//Decrpyt & Enter Password
		enterPassword(EncryptDecrypt.decryptString(password));

		// Click on Login
		clickLogin();
		logger.info("Successfully logged in Puralsight application");
		return new SearchPage(page);
	}

	/**
	 * Pluralsight Application Negative Login Scenarios
	 * @param username
	 * @param password
	 * @return
	 */
	public String pluralsightApplicationNegativeLoginScenarios(String username, String password) {

		refreshPage();

		// Wait till the login page is visible
		PlaywrightElementActions.waitForVisibility(page, loginForm);

		// Enter User name
		enterUsername(username);

		// Enter Password
		enterPassword(password);

		// Click on Login
		clickLogin();

		//Header ribbon text
		String ribbonText = getHeaderRibbonText();

		//wait till ribbon is invisible
		checkHeaderRibbonIsInvisible();

		return ribbonText;
	}

	/**
	 * check Header Ribbon Is Visible
	 * @return boolean
	 */
	public boolean checkHeaderRibbonIsVisible() {
		boolean flag = false;
		if(!PlaywrightElementActions.isVisible(page, headerRibbonTextVisible)) {
			throw new RuntimeException("Header message is not visible");	
		}
		flag=true;		
		return flag;
	}

	/**
	 * get Header Ribbon Text
	 * @return String
	 */
	public String getHeaderRibbonText() {
		checkHeaderRibbonIsVisible();		
		return PlaywrightElementActions.getText(page, headerRibbonTextVisible);
	}

	/**
	 * Check Header Ribbon Is Invisible
	 * @return
	 */
	public boolean checkHeaderRibbonIsInvisible() {
		boolean flag = false;
		if(getHeaderRibbonText().equalsIgnoreCase("Invalid user name or password")) {
			flag=true;
		}
		return flag;
	}

	/**
	 * Set Username
	 * @param userName
	 */
	public void enterUsername(String userName) {
		PlaywrightElementActions.clearText(page, username);
		PlaywrightElementActions.typeText(page,username,userName);
	}

	/**
	 * set Password
	 * @param password
	 */
	public void enterPassword(String pwd) {
		PlaywrightElementActions.clearText(page, password);
		PlaywrightElementActions.typeText(page, password, pwd);
	}

	/**
	 * Click on Login
	 */
	public void clickLogin() {
		PlaywrightElementActions.click(page, loginBtn);
	}

}
