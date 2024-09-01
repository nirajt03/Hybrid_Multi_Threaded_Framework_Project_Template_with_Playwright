package commonUtility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import excelUtilities.ExcelUtility;
import exceptions.ColumnNameNotFoundException;
import exceptions.NoRowFoundException;
import exceptions.ObjectLengthNotCorrectException;
import exceptions.SheetNotFoundException;
import helperUtility.EncryptDecrypt;
import pageObjectModels.LoginPage;
import pageObjectModels.SearchPage;

/**
 * Arch Utilities
 * 
 *@author Niraj.Tiwari
 */
public class ArchUtilities  {

	public static final Logger logger = LogManager.getLogger(ArchUtilities.class);

	public ArchUtilities() {
		
	}

	/**
	 * Get User Credential
	 * @param userType
	 * @return
	 */
	public HashMap<String, String> getUserCredential(String userType,String loginType) {

		String path = System.getProperty("driverFilePath");
		ExcelUtility excelUtil = new ExcelUtility(path);
		HashMap<String, String> map = new HashMap<String, String>();

		// Get Sheet Object
		Sheet sheetObject = excelUtil.getSheetObject("LoginTestData");

		String[][] searchData={{"UserType",userType}, {"LoginType",loginType}};
		ArrayList<HashMap<String, String>> rowData = null;
		try {
			rowData = ExcelUtility.getAllRowsData(sheetObject,searchData);
		} catch (NoRowFoundException | ObjectLengthNotCorrectException e) {
			e.printStackTrace();
		}

		// Get the Username & Password and map encrypted details  		
		map.put("Username", EncryptDecrypt.encryptString(rowData.get(0).get("Username")));
		map.put("Password", EncryptDecrypt.encryptString(rowData.get(0).get("Password")));
		return map;
	}

	/**
	 * Login To Pluralsight application
	 * @param <T>
	 * @param loginPage
	 * @param userType
	 * @return
	 */
	public SearchPage loginToPluralsightApplication(LoginPage loginPage,String userType,String loginType) {
		// Get User Credential
		HashMap<String, String> usercredential = getUserCredential(userType,loginType);
	
		//Login pluralsight application
		SearchPage searchpage = loginPage.pluralsightApplicationLogin( usercredential.get("Username"), usercredential.get("Password"));
		logger.info("Successfully Login To Pluralsight Application");
		return searchpage;
	}

	/**
	 * Check Negative Login Scenarios
	 * @param loginPage
	 * @param userType
	 * @param loginType
	 * @return
	 */
	public String checkNegativeLoginScenarios(LoginPage loginPage, String username, String password) {
		
		//Login pluralsight application
		return loginPage.pluralsightApplicationNegativeLoginScenarios(username, password);
	}

	/**
	 * Get Test Data
	 * @param TestScriptName
	 * @return
	 */
	public ArrayList<HashMap<String, String>> getTestData(String TestScriptName) {

		ExcelUtility ExcelUtility = new ExcelUtility();

		// Get Workbook
		Workbook book = ExcelUtility.getWorkbook(System.getProperty("driverFilePath"));

		Sheet testScriptSheetObj = null;
		try {
			testScriptSheetObj = ExcelUtility.getSheetObject(book,"TestScripts");
		} catch (SheetNotFoundException e) {		
			e.printStackTrace();
		}
		ArrayList<ArrayList<String>> testDataSheetNameList = ExcelUtility.getMultipleColumnDataBasedOnOneColumnValue(testScriptSheetObj,"TestScriptName",TestScriptName,"TestDataSheetName");
		String testDataSheetName=(testDataSheetNameList.get(0)).get(0);

		Sheet testDataSheetObj = null;
		try {
			testDataSheetObj = ExcelUtility.getSheetObject(book, testDataSheetName);
		} catch (SheetNotFoundException e) {
			e.printStackTrace();
		}

		ArrayList<HashMap<String, String>> allTestData=new ArrayList<HashMap<String, String>>();

		String[][] srchData= {{"TestCaseID","Not To Run"}};
		try {
			allTestData = getRowDataBasedOnTestcaseID(testDataSheetObj,srchData);
		} catch (ObjectLengthNotCorrectException | NoRowFoundException e) {
			e.printStackTrace();
		}
		return allTestData;
	}

	/**
	 * Get Row Data Based On Test Case ID
	 * @param sheetObject
	 * @param srchCriteriaArray
	 * @return
	 * @throws ObjectLengthNotCorrectException 
	 * @throws NoRowFoundException 
	 */
	public ArrayList<HashMap<String, String>> getRowDataBasedOnTestcaseID(Sheet sheetObject,String[][] srchCriteriaArray) throws ObjectLengthNotCorrectException, NoRowFoundException {    
		for(String [] arr:srchCriteriaArray) {
			if(arr.length !=2) {
				throw new ObjectLengthNotCorrectException("Sreach Object inner Array Should have 2 elements, "
						+ "while "+arr.length+" was given");
			}
		}
		DataFormatter formatter = new DataFormatter();
		ArrayList<HashMap<String,String>> allRowData = new ArrayList<HashMap<String,String>>();    

		List<String> allColmnNames = ExcelUtility.getAllColumnNames(sheetObject);        
		int rowCount=ExcelUtility.getRowCount(sheetObject);
		boolean oneRowFound=false;

		for(int i=1;i<rowCount;i++) {
			boolean srchFlag=true;
			for(String [] arr:srchCriteriaArray) {
				int column2CheckIndx = 0;
				try {
					column2CheckIndx = ExcelUtility.getColumnIndexFrmColumnName(sheetObject,arr[0]);
				} catch (ColumnNameNotFoundException e) {
					e.printStackTrace();
				}
				Row rowObj=sheetObject.getRow(i);
				Cell cellObj=rowObj.getCell(column2CheckIndx);
				String cellValue=formatter.formatCellValue(cellObj);
				srchFlag = srchFlag && (!(cellValue.trim().toUpperCase()).equals(arr[1].trim().toUpperCase()));            
			}
			if (srchFlag){
				oneRowFound=true;
				HashMap<String,String> rowData=new HashMap<String,String>();
				for(String colName:allColmnNames) {
					int columnIndex = 0;
					try {
						columnIndex = ExcelUtility.getColumnIndexFrmColumnName(sheetObject, colName);
					} catch (ColumnNameNotFoundException e) {
						e.printStackTrace();
					}
					rowData.put(colName.trim(),formatter.formatCellValue(sheetObject.getRow(i).getCell(columnIndex)).trim());
				}
				allRowData.add(rowData);
			}
		}
		if(!oneRowFound) {    
			String message = "";
			for(String [] arr:srchCriteriaArray) {
				message = message + arr[0]+"="+arr[1]+" ";
			}
			throw new NoRowFoundException("No Rows found in sheet="+sheetObject.getSheetName()+" for search criteria "+ message);
		}
		return allRowData; 
	}

	/**
	 * Get Current Date in MMDDYYYY
	 * @return
	 */
	public static String getCurrentDateinMMDDYYYY() {
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
	    LocalDate date = LocalDate.now();
	    return date.format(formatter);
	}
}