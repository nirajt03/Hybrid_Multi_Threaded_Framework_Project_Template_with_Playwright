package helperTestUtility;

import java.util.Arrays;
import java.util.Base64;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import com.microsoft.playwright.Page;

import enums.CategoryType;
import reportUtilities.MultiThreadedReportingUtility;



/**
 * Report Logs : Adds custom logs to extent reports and prints it in console and application log file. 
 * @author Niraj.Tiwari
 *  Apr 22, 2024 : 11:56:17 AM
 */
public class ReportLogs extends MultiThreadedReportingUtility {

	public static final Logger logger = LogManager.getLogger(ReportLogs.class);

	private static ThreadLocal<ExtentTest> extentNode = MultiThreadedReportingUtility.dataProviderTest;
	private static ThreadLocal<ExtentTest> extentTest = MultiThreadedReportingUtility.methodTest;

	/**
	 * Get Extent Node
	 * @return ExtentTest
	 */
	private static ExtentTest getExtentNode() {
		return extentNode.get();
	}

	/**
	 * Get Extent Test 
	 * @return ExtentTest
	 */
	private static ExtentTest getExtentTest() {
		return extentTest.get();
	}

	/**
	 * Unload Extent: Unloads extent objects from ThreadLocal pool
	 */
	public static void unloadExtent() {
		extentNode.remove();
		extentTest.remove();
	}

	/**
	 * Add Authors : Adds authors to extent test
	 * @param authors
	 */
	public static void addAuthors(String[] authors) {
		Arrays.stream(authors).forEach(getExtentTest()::assignAuthor);
	}

	/**
	 * Add Categories : Adds categories to extent test
	 * @param categories
	 */
	public static void addCategories(CategoryType[] categories) {
		Arrays.stream(categories).forEach(category -> 
		getExtentTest().assignCategory(category.getCategoryType()));
	}

	/**
	 * Add Log
	 * @param status
	 * @param message
	 */
	public static void addLog(Status status, String message) {
		getExtentNode().log(status, message);
		logger.info("Log Message: " + status + " - " + message);
	}

	/**
	 * Add Log With MarkUp (Background)
	 * @param status
	 * @param message
	 */
	public static void addLogWithMarkUp(Status status, String message) {
		ExtentColor color = getExtentColor(status);
		getExtentNode().log(status, MarkupHelper.createLabel(message, color));
		logger.info("Log Message: " + status + " - " + message);
	}

	/**
	 * Add Log With Error
	 * @param status
	 * @param throwable
	 */
	public static void addLogWithError(Status status, Throwable throwable) {
		getExtentNode().log(status, throwable);
		logger.error("Log Message: " + status + " : ", throwable);
	}

	/**
	 * Add Log With Screenshot
	 * @param status
	 * @param message
	 */
	public static void addLogWithScreenshot(Status status, String message) {
		getExtentNode().log(status, message, 
				MediaEntityBuilder.createScreenCaptureFromBase64String(getBase64Screenshot()).build());
		logger.info("Log Message: " + status + " - " + message);
	}

	/**
	 * Add Log With Error And Screenshot
	 * @param status
	 * @param throwable
	 */
	public static void addLogWithErrorAndScreenshot(Status status, Throwable throwable) {
		getExtentNode().log(status, throwable, 
				MediaEntityBuilder.createScreenCaptureFromBase64String(getBase64Screenshot()).build());
		logger.error("Log Message: " + status + " : ", throwable);
	}

	/**
	 * Add Log For String Comparison
	 * @param actual
	 * @param expected
	 * @param message
	 */
	public static void addLogForStringComparison(String actual, String expected, String message) {
		Status status = actual.contains(expected) ? Status.PASS : Status.FAIL;
		addLogWithMarkUp(status, message + ": actual - " + "<b><i>" + actual + "</i></b>" 
				+ " & expected - " + "<b><i>" + expected + "</i></b>");
	}

	/**
	 * Get Base64 Image : Screenshot in the form of base64 string
	 * @return String
	 * @throws WebDriverException
	 */    
	public static String getBase64Screenshot() {
		Page page = PlaywrightDriverFactory.getPage();
		byte[] screenshotBytes = page.screenshot(new Page.ScreenshotOptions().setFullPage(true));
		return Base64.getEncoder().encodeToString(screenshotBytes);
	}

	/**
	 * Get ExtentColor based on status
	 * @param status
	 * @return ExtentColor
	 */
	private static ExtentColor getExtentColor(Status status) {
		switch (status) {
		case PASS:
			return ExtentColor.GREEN;
		case FAIL:
			return ExtentColor.RED;
		case INFO:
			return ExtentColor.BLUE;
		case WARNING:
			return ExtentColor.YELLOW;
		case SKIP:
			return ExtentColor.ORANGE;
		default:
			return ExtentColor.WHITE;
		}
	}
}
