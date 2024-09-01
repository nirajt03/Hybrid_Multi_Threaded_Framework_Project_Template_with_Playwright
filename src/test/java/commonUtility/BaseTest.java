package commonUtility;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Tracing;

import annotations.FrameworkAnnotation;
import excelUtilities.ExcelUtility;
import exceptions.FileDoesNotExistsException;
import exceptions.InCorrectConfigConfigParameters;
import helperTestUtility.BrowserFactory;
import helperTestUtility.PlaywrightDriverFactory;
import helperTestUtility.ReportLogs;
import helperTestUtility.RetryListerner;
import helperTestUtility.SupportedBrowser;
import pageObjectModels.LoginPage;
import playwrightUtilities.browser.PlaywrightBrowserUtil;
import reportUtilities.MultiThreadedReportingUtility;
import runner.TestNGSuite;
import screenRecorderUtilities.ScreenRecorderUtility;
import screenRecorderUtilities.ScreenRecorderUtility.TypeOfScreen;

/**
 * Base Test
 * 
 * @author Niraj.Tiwari
 */
@Listeners({RetryListerner.class, MultiThreadedReportingUtility.class})
public abstract class BaseTest {

	public static final Logger logger = LogManager.getLogger(BaseTest.class);

	private static final int LINE_LENGTH = 100;

	protected BrowserFactory bf = new BrowserFactory();
	static private Set<String> manualTCIDsSet = new TreeSet<String>();
	static private Set<String> passedTCIDsSet = new TreeSet<String>();
	static private Set<String> failedTCIDsSet = new TreeSet<String>();

	protected Browser browser;
	protected Page page;	

	String path= System.getProperty("testScriptName");
	PlaywrightBrowserUtil playwrightBrowserUtil = new PlaywrightBrowserUtil();
	public ArchUtilities archUtil= new ArchUtilities();
	public ExcelUtility excelUtil = new ExcelUtility();

	String testScriptName;
	String className;
	private int passedTests=0;
	private int failedTests=0;
	private int skipedTests=0;

	public BaseTest() {

	}

	/**
	 * Get Class Name
	 * @return
	 */
	public String getClassName() {
		String packageName = this.getClass().getPackage().getName().trim();
		logger.info("Qualified Test Package name : " + packageName);
		return packageName;
	}

	@BeforeSuite(alwaysRun=true)
	public void beforeSuite()  {
		int thresholdDays = 10;
		String testClassName = getClassName();
		try {
			ScreenRecorderUtility.deleteOlderFilesAndDirectories(thresholdDays, TimeUnit.DAYS,".avi");	
			ScreenRecorderUtility.startRecord(TypeOfScreen.RegularScreen,testClassName);	
			logger.info("Screen Recording Started ..!!");
		} catch (Exception e) {
			e.printStackTrace();	
		}
		//from test ng suite
		//TestNg class's part - must set below properties
		String path = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\hybridFrameworkTestDriver.xlsx";
		try {
			TestNGSuite.validateInputFile(path);
		} catch (FileDoesNotExistsException e) {
			e.printStackTrace();
		}
		System.setProperty("driverFilePath", path);
		try {
			System.setProperty("url",TestNGSuite.geturl());
		} catch (InCorrectConfigConfigParameters e) {
			e.printStackTrace();
		}
		System.setProperty("AppName Details","Pluralsight");
		System.setProperty("Environment Details","QA");
		String applicationTitle = "Pluralsight";
		System.setProperty("Application Title", applicationTitle +" Automation");
		System.setProperty("Report Name",applicationTitle+" Automation Test Report");
	}

	@BeforeClass(alwaysRun = true)
	public void beforeClass(ITestContext testcontext) {

		PlaywrightDriverFactory.getInstance().setDriver(bf.createBrowserInstance(SupportedBrowser.CHROMIUM,false,new String[]{"--start-maximized"}));
		PlaywrightDriverFactory.getInstance();
		browser = PlaywrightDriverFactory.getBrowser();
		//playwrightBrowserUtil.createMaximizedPage(browser);
		startTracing(PlaywrightDriverFactory.getBrowserContext());
		page = PlaywrightDriverFactory.getPage();
		testcontext.setAttribute("webPage", page);
		System.setProperty("Browser Name", SupportedBrowser.CHROMIUM.toString());
		System.setProperty("Browser Version", PlaywrightBrowserUtil.getBrowserVersion(browser));
		logger.info("Chromium Browser Initiated successfully");
	}

	@BeforeMethod(alwaysRun=true)
	public void beforeMethod(Object[] data) {
		List<Object> myModel = Arrays.asList(data);
		int lastIndex = myModel.size()-1;
		Object object = myModel.get(lastIndex);
		if (object.toString().contains("TCs") ) {
			String[] objectArr = object.toString().split(",");
			for (String obj : objectArr) {
				manualTCIDsSet.add(obj.trim());
			}
		}
		logger.info("Manual TC IDs for current test : "+manualTCIDsSet);
	}

	@AfterMethod(alwaysRun=true)
	public void afterMethod(ITestResult result) {

		ReportLogs.addAuthors(result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(FrameworkAnnotation.class).author());
		ReportLogs.addCategories(result.getMethod().getConstructorOrMethod().getMethod().getAnnotation(FrameworkAnnotation.class).category());

		//testScriptName = result.getMethod().getMethodName();
		testScriptName = result.getMethod().getRealClass().getSimpleName();
		System.setProperty("testScriptName", testScriptName);
		try {
			List<Object> myModel = Arrays.asList(result.getParameters());
			int lastIndex = myModel.size() - 1;
			Object object = myModel.get(lastIndex);
			if (result.getStatus() == ITestResult.SUCCESS) {
				++passedTests;	
				String[] objectArr = object.toString().split(",");
				for (String obj : objectArr) {
					passedTCIDsSet.add(obj.trim());
				}
			}
			else if (result.getStatus() == ITestResult.FAILURE) {
				++failedTests;
				String[] objectArr = object.toString().split(",");
				for (String obj : objectArr) {
					failedTCIDsSet.add(obj.trim());
				}
			}				
			else if (result.getStatus() == ITestResult.SKIP)
				++skipedTests;
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e);
		}
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		ReportLogs.unloadExtent();

		int manualTCIDs = manualTCIDsSet.size();
		System.setProperty("Manual TC IDs Count",String.valueOf(manualTCIDs));
		logger.info("Manual TC IDs Count for current test : "+manualTCIDs);
		try {
			int totalTestCases = ExcelUtility.getTotalTestCases(passedTests,failedTests,skipedTests);
			double passPercentage = ExcelUtility.calculatePercentage(passedTests,totalTestCases);
			logger.info("Pass Percentage : "+passPercentage+" %");
			String sheetName = "TestScripts";
			String filePath = System.getProperty("driverFilePath");
			String testScriptName= System.getProperty("testScriptName");
			excelUtil.setPassPercentage(passPercentage, filePath,testScriptName,sheetName);
		} catch (Exception e) {
			System.out.println(e.getCause());
		}finally {
			//driver.close();//driver.quit();
			stopTracing(PlaywrightDriverFactory.getBrowserContext());
			PlaywrightDriverFactory.getInstance().closeDriver();
			logger.info("Chrome Browser Closed");
		}
	}

	@AfterSuite(alwaysRun=true)
	public void afterSuite() {
		try {
			logTestExecutionStatusInTextFile(passedTCIDsSet,failedTCIDsSet);
			ScreenRecorderUtility.stopRecord();
			logger.info("Screen Recording Stopped ..!!");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(e);
		}
	}

	/**
	 * Open Application : URL
	 * @param url
	 * @return
	 */
	protected LoginPage openApplication(String url) {
		this.page.navigate(url);
		logger.info("Login Page Displayed");
		return (new LoginPage(page));
	}

	/** 
	 * This method is used to covert String value to boolean
	 * @param value
	 * @return
	 */
	public boolean stringToBoolean(String value) {
		boolean flag = false;
		value = value.toUpperCase();
		if (value.equalsIgnoreCase("Y") || value.equalsIgnoreCase("YES")) {
			flag = true;
		} else if (value.equals("N") || value.equalsIgnoreCase("NO")) {
			flag = false;
		}
		return flag;
	}

	/**
	 * Log Test Execution Status In Text File
	 * @param passedTCIDsSet
	 * @param failedTCIDsSet
	 */
	public static void logTestExecutionStatusInTextFile(Set<String> passedTCIDsSet, Set<String> failedTCIDsSet) {
		// Specify the file path
		failedTCIDsSet.removeAll(passedTCIDsSet);
		String  textFilePath = System.getProperty("user.dir") + "\\src\\main\\resources\\Execution Status.txt";

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(textFilePath, false))) {
			// Write the passed test cases
			writer.write("=====================================================================\n");
			writer.write("Passed Manual TC IDs : \n" + formatString(passedTCIDsSet.toString()) + "\n");
			writer.write("=====================================================================\n" + "\n");

			// Write the failed test cases
			writer.write("=====================================================================\n");
			writer.write("Failed Manual TC IDs : \n" + formatString(failedTCIDsSet.toString()) + "\n");
			writer.write("=====================================================================\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Start tracing
	public void startTracing(BrowserContext context) {
		// Start tracing before creating / navigating a page.
		context.tracing().start(new Tracing.StartOptions()
				.setScreenshots(true)
				.setSnapshots(true)
				.setSources(true));
	}

	// Stop tracing
	public void stopTracing(BrowserContext context) {
		// Stop tracing and export it into a zip archive.
		context.tracing().stop(new Tracing.StopOptions()
				.setPath(Paths.get("trace.zip")));

	}

	/**
	 * Format String
	 * @param input
	 * @return
	 */
	private static String formatString(String input) {
		StringBuilder formattedString = new StringBuilder();
		String[] words = input.split(" ");

		int currentLength = 0;
		for (String word : words) {
			if (currentLength + word.length() > LINE_LENGTH) {
				formattedString.append("\n");
				currentLength = 0;
			}
			formattedString.append(word).append(" ");
			currentLength += word.length() + 1;
		}
		return formattedString.toString().trim();
	}
}
