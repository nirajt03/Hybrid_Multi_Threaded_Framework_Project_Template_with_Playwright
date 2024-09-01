package helperTestUtility;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

public class PlaywrightDriverFactory {

	// Private constructor to prevent instantiation
	private PlaywrightDriverFactory() {
	}

	// Singleton instance
	private static PlaywrightDriverFactory instance;

	public static PlaywrightDriverFactory getInstance() {
		if (instance == null) {
			synchronized (PlaywrightDriverFactory.class) {
				if (instance == null) {
					instance = new PlaywrightDriverFactory();
				}
			}
		}
		return instance;
	}

	// ThreadLocal for Playwright, BrowserContext, and Page
	private static ThreadLocal<Playwright> tlPlaywright = new ThreadLocal<>();
	private static ThreadLocal<Browser> tlBrowser = new ThreadLocal<>();
	private static ThreadLocal<BrowserContext> tlBrowserContext = new ThreadLocal<>();
	private static ThreadLocal<Page> tlPage = new ThreadLocal<>();

	// Method to initialize Playwright, Browser, and Page
	public void setDriver(Browser browser) {
		if (tlPlaywright.get() == null) {
			Playwright playwright = Playwright.create();
			tlPlaywright.set(playwright);
			tlBrowser.set(browser);
			BrowserContext context = browser.newContext();
//			BrowserContext  context = browser.newContext(new Browser.NewContextOptions()
//			  .setRecordVideoDir(Paths.get("videos/"))
//			  .setRecordVideoSize(1536, 730));

			tlBrowserContext.set(context);
			Page page = context.newPage();
			tlPage.set(page);
		}
	}

	// Getter for the Playwright instance
	public static Playwright getPlaywright() {
		return tlPlaywright.get();
	}

	public static Browser getBrowser() {
		return tlBrowser.get();
	}

	public static BrowserContext getBrowserContext() {
		return tlBrowserContext.get();
	}

	public static Page getPage() {
		return tlPage.get();
	}

	// Method to close Playwright, Browser, and Page
	public void closeDriver() {
		if (tlPage.get() != null) {
			tlPage.get().close();
			tlPage.remove();
		}
		if (tlBrowserContext.get() != null) {
			tlBrowserContext.get().close();
			tlBrowserContext.remove();
		}
		if (tlBrowser.get() != null) {
			tlBrowser.get().close();
			tlBrowser.remove();
		}
		if (tlPlaywright.get() != null) {
			tlPlaywright.remove(); // No close() method for Playwright, just remove the reference
		}
	}
}
