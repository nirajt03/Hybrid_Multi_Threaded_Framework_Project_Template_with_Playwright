package runner;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Sheet;
import org.testng.ITestNGListener;
import org.testng.TestNG;
import org.testng.xml.XmlClass;
import org.testng.xml.XmlSuite;
import org.testng.xml.XmlTest;

import excelUtilities.ExcelUtility;
import exceptions.FileDoesNotExistsException;
import exceptions.InCorrectConfigConfigParameters;
import exceptions.NoRowFoundException;
import exceptions.ObjectLengthNotCorrectException;
import helperTestUtility.RetryListerner;
import reportUtilities.MultiThreadedReportingUtility;

/**
 * Test NG Suite
 * 
 * @author Niraj.Tiwari
 */
public class TestNGSuite {

	public static final Logger logger = LogManager.getLogger(TestNGSuite.class);

	public static void main(String[] args)  {

		//Test driver excel sheet path
		String  path = System.getProperty("user.dir") + "\\src\\test\\resources\\testdata\\hybridFrameworkTestDriver.xlsx";
		logger.info("Excel sheet Path : " +path);
		try {
			validateInputFile(path);
		} catch (FileDoesNotExistsException e) {
			e.printStackTrace();
		}
		System.setProperty("driverFilePath", path);
		String testExeType= getTestExecutionType().trim();
		try {
			System.setProperty("url",geturl());
		} catch (InCorrectConfigConfigParameters e) {
			e.printStackTrace();
		}

		//Creating SuiteList and ClassList to be executed
		List<XmlSuite> suiteList = new ArrayList<XmlSuite>();
		List<XmlClass> classList = new ArrayList<XmlClass>();

		//Creating ListnersList for adding listners 
		List<Class<? extends ITestNGListener>> listenerList = new ArrayList<Class<? extends ITestNGListener>>();

		//Setting name for XML Suite
		XmlSuite suiteName = new XmlSuite();
		suiteName.setName("Pluralsight Suite");

		////Setting XML suite parallel execution mode as classes/methods
		//suiteName.setParallel(XmlSuite.ParallelMode.METHODS);
		//		
		////Setting execution thread count for parallel execution
		//suiteName.setThreadCount(2);
		
		//Setting name for XML tests
		XmlTest testName = new XmlTest(suiteName);
		testName.setName("Pluralsight Test");

		////Creating hashmap for setting Parameters for the XML Test
		//HashMap<String , String> testNgParams = new HashMap<String, String>();
		//testNgParams.put("browserName", "Chrome");
		//testNgParams.put("browserVersion", "115");
		//testName.setParameters(testNgParams);		

		//Login test scripts
		classList.add(new XmlClass("testScripts.TestLoginPage"));
		classList.add(new XmlClass("testScripts.TestLoginPageNegativeScenarios"));

		//Pages test scripts
		classList.add(new XmlClass("testScripts.TestCoursePageFeatures"));
		classList.add(new XmlClass("testScripts.TestHomePageFeatures"));
		classList.add(new XmlClass("testScripts.TestSearchPageFeatures"));
		classList.add(new XmlClass("testScripts.TestJavaSearchFunctionality"));

		//Creating TestNG object
		TestNG TestNGRun = new TestNG();

		//Creating ArrayList of Methods to be executed based on group
		List<String> methods = new ArrayList<String>();
		if(testExeType.equals("Custom") || testExeType.equals("Regression") || testExeType.equals("Smoke") || testExeType.equals("Login")) {
			methods = getTestScriptToExecute(testExeType);
		}else {
			methods.add(testExeType);
		}

		//Creating XML Include in form of ArrayList to add multiple methods of class which need to be run
		testName.setIncludedGroups(methods);
		testName.setXmlClasses(classList) ;

		//Adding listners classes
		listenerList.add(MultiThreadedReportingUtility.class);
		listenerList.add(RetryListerner.class);
		suiteList.add(suiteName);

		//Adding XMLSuites selected to TestNG defined
		TestNGRun.setXmlSuites(suiteList);
		TestNGRun.setListenerClasses(listenerList);

		logger.info("Running Test Suite for "+testExeType+" group");
		//Executing TestNG created dynamic suite
		TestNGRun.run();
	}

	/**
	 * Validate Input File
	 * @param path
	 * @throws FileDoesNotExistsException
	 */
	public static void validateInputFile(String path) throws FileDoesNotExistsException {
		File f = new File(path);
		if(!f.exists()) {
			throw new FileDoesNotExistsException("File dmdaTestDriver.xlsx not found under path: "+f.getParentFile());
		}else {
			if (f.length()==0) {
				throw new FileDoesNotExistsException("File dmdaTestDriver.xlsx found under path: "+f.getParentFile() + " is empty");
			}
		}
	}

	/**
	 * Get URL
	 * @return
	 * @throws InCorrectConfigConfigParameters
	 */
	public static String geturl() throws InCorrectConfigConfigParameters  {
		ExcelUtility xlsUtil= new ExcelUtility(System.getProperty("driverFilePath"));
		Sheet sheetObj = xlsUtil.getSheetObject("Config");
		ArrayList<ArrayList<String>> urlList = xlsUtil.getMultipleColumnDataBasedOnOneColumnValue(sheetObj,"Attribute","Env-URL","Value");
		if(urlList.size() == 0) {
			throw new InCorrectConfigConfigParameters("No url value found for url-uat");
		}
		if (urlList.size() > 1){
			throw new InCorrectConfigConfigParameters("Multiple values found for url-uat");
		}
		return urlList.get(0).get(0);
	}

	/**
	 * Get Client Code
	 * @return
	 */
	public static String getClientCode() {
		ExcelUtility xlsUtil= new ExcelUtility(System.getProperty("driverFilePath"));
		ArrayList<String> testTypeList=xlsUtil.getRowData("Driver",1);
		return testTypeList.get(1);
	}

	/**
	 * Get Test Script To Execute
	 * @param groupName
	 * @return
	 */
	private static ArrayList<String> getTestScriptToExecute(String groupName) {
		ExcelUtility xlsUtil= new ExcelUtility(System.getProperty("driverFilePath"));
		Sheet sheetObj = xlsUtil.getSheetObject("TestScripts");
		ArrayList<ArrayList<String>> test2Execute= new ArrayList<ArrayList<String>>();
		if (groupName.equals("Custom")||groupName.equals("Smoke")||groupName.equals("Regression"))
			test2Execute = xlsUtil.getMultipleColumnDataBasedOnOneColumnValue(sheetObj,"Execute","Y","TestScriptName");
		else
			test2Execute = getTestScriptNamesBasedOnGroupsColumn(groupName,"TestScriptName");
		ArrayList<String> test2ExecuteList = new ArrayList<String>();
		for(ArrayList<String>obj:test2Execute) {
			for(String val:obj) {
				test2ExecuteList.add(val.trim());
			}
		}
		return test2ExecuteList;
	}

	/**
	 * Get Test Execution Type
	 * @return
	 */
	private static String getTestExecutionType() {
		ExcelUtility xlsUtil = new ExcelUtility(System.getProperty("driverFilePath"));
		ArrayList<String> testTypeList = xlsUtil.getRowData("Driver",0);
		String executionType = testTypeList.get(1);
		if (!(executionType.equals("Custom") || executionType.equals("Regression") || executionType.equals("Smoke") || executionType.equals("Login"))){
			throw new IllegalArgumentException("Test Execution: "+executionType+" is invalid");
		}

		return executionType;
	}

	//Group wise run via TestNgSuite
	/**
	 * Get Test Script Names Based On Groups Column
	 * @param groupName
	 * @param extColumnName
	 * @return
	 */
	public static  ArrayList<ArrayList<String>> getTestScriptNamesBasedOnGroupsColumn(String groupName, String... extColumnName) {
		ExcelUtility xlsUtil= new ExcelUtility(System.getProperty("driverFilePath"));
		Sheet sheetObj = xlsUtil.getSheetObject("TestScripts");
		ArrayList<ArrayList<String>> columData = new ArrayList<ArrayList<String>>();
		String[][]srchCriteria={{"Groups",groupName}};
		ArrayList<HashMap<String, String>> rowAllData = null;
		try {
			rowAllData = ExcelUtility.getAllRowsData(sheetObj,srchCriteria);
		} catch (NoRowFoundException | ObjectLengthNotCorrectException e) {
			e.printStackTrace();
		}
		for(HashMap<String, String> map:rowAllData) {
			ArrayList<String> temp = new ArrayList<String>();
			String value=map.get("Execute");
			if (value.equals("Y")) {
				for(String colname:extColumnName) {
					temp.add(map.get(colname));
				}
			}
			columData.add(temp);
		}
		ArrayList<String> stringCollection = new ArrayList<String>();
		for (int i = 0; i < columData.size(); ++i) {
			stringCollection.addAll(columData.get(i));
		}
		if (stringCollection.isEmpty()) {
			throw new IllegalArgumentException("No TestScript To Execute for group : "+groupName);
		}
		return columData;
	}

	/** This method is used to covert String value to boolean
	 * @param value
	 * @return
	 */
	public static boolean stringToBoolean(String value) {
		boolean flag = false;
		value = value.toUpperCase();
		if (value.equalsIgnoreCase("Y") || value.equalsIgnoreCase("YES")) {
			flag = true;
		} else if (value.equals("N") || value.equalsIgnoreCase("NO")) {
			flag = false;
		}
		return flag;
	}	
}
