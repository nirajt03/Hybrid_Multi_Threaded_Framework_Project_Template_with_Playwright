package helperTestUtility;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Arrays;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class BrowserFactory {

	 /**
     * Creates and launches a browser instance based on the specified browser type.
     *
     * @param browser  The type of browser to launch (e.g., CHROMIUM, WEBKIT, FIREFOX, CHROME, EDGE).
     * @param headless A boolean flag indicating whether to run the browser in headless mode.
     * @param args     An array of additional arguments to pass to the browser instance.
     * @return         A Browser instance corresponding to the requested browser type.
     */
	public Browser createBrowserInstance(SupportedBrowser browser, boolean headless, String[] args) {
		Playwright playwright = Playwright.create();
		BrowserType.LaunchOptions options = new BrowserType.LaunchOptions()
				.setHeadless(headless)
				.setArgs(Arrays.asList(args)); // Convert String[] to List<String>

		Browser browserInstance = null;

		switch (browser) {
		case CHROMIUM:
			browserInstance = playwright.chromium().launch(options);
			break;
		case WEBKIT:
			browserInstance = playwright.webkit().launch(options);
			break;
		case FIREFOX:
			browserInstance = playwright.firefox().launch(options);
			break;
		case CHROME:
			browserInstance = playwright.chromium().launch(options);
			break;
		case EDGE:
			browserInstance = playwright.chromium().launch(options); // Playwright uses Chromium for Edge
			break;
		default:
			System.out.println("Please pass a valid browser value. Browser passed was: " + browser);
		}
		
		return browserInstance;
	}

//	public Page createMaximizedPage(SupportedBrowser browserType, boolean headless, String[] args) {
//        // Create Playwright instance
//        Playwright playwright = Playwright.create();
//
//        // Set browser launch options
//        LaunchOptions options = new LaunchOptions()
//                .setHeadless(headless)
//                .setArgs(Arrays.asList(args)); // Convert String[] to List<String>
//
//        // Create Browser instance
//        Browser browserInstance = null;
//        switch (browserType) {
//            case CHROMIUM:
//                browserInstance = playwright.chromium().launch(options);
//                break;
//            case WEBKIT:
//                browserInstance = playwright.webkit().launch(options);
//                break;
//            case FIREFOX:
//                browserInstance = playwright.firefox().launch(options);
//                break;
//            case CHROME:
//                browserInstance = playwright.chromium().launch(options);
//                break;
//            case EDGE:
//                browserInstance = playwright.chromium().launch(options); // Playwright uses Chromium for Edge
//                break;
//            default:
//                System.out.println("Please pass a valid browser value. Browser passed was: " + browserType);
//                return null; // Return null if invalid browser type
//        }
//
//        // Maximize Page
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
//        int width = (int)screenSize.getWidth();
//        int height = (int)screenSize.getHeight();
//        BrowserContext context = browserInstance.newContext(new Browser.NewContextOptions().setViewportSize(width, height));
//        return context.newPage();
//    }
	
	/**
	 * Create a new browser context and page with maximized window and cleared cookies.
	 * @param browser Browser instance
	 * @return Page instance
	 */
	public Page createMaximizedPage(Browser browser) {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int width = (int)screenSize.getWidth();
		int height = (int)screenSize.getHeight();
		BrowserContext context = browser.newContext(new Browser.NewContextOptions().setViewportSize(width, height));// Starts maximized
		return context.newPage();
	}


}
