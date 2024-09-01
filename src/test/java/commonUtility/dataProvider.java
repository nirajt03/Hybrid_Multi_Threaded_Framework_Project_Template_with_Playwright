package commonUtility;

import java.util.ArrayList;
import java.util.HashMap;


import org.testng.annotations.DataProvider;


/**
 * Data Provider
 * 
 * @author Niraj.Tiwari
 */
public class dataProvider {

	private ArchUtilities archUtl= new ArchUtilities();

	public dataProvider() {

	}

	/**
	 * LoginData
	 * @return
	 */
	@DataProvider(name = "LoginData")
	public Object[][] getLoginTestData() {

		ArrayList<HashMap<String, String>> testData = archUtl.getTestData("TestLoginPage");
		
		int row = testData.size();
        int col = testData.get(0).size();
        Object[][] obj = new Object[row][col-3];

		int i=0;
		for(HashMap<String, String> map:testData) {
			obj[i][0]=map.get("UserType");
			obj[i][1]=map.get("TestCaseID");
			obj[i][2]=map.get("LoginType");
			obj[i][3]=map.get("ExpSearchText");
			obj[i][4]=map.get("ManualTCIDs");
			
			i++;
		}
		return obj;
	}
	
	/**
	 * Negative Login Scenarios
	 * @return
	 */
	@DataProvider(name = "NegativeLoginScenarios")
	public Object[][] getNegativeLoginScenariosData() {

		ArrayList<HashMap<String, String>> testData = archUtl.getTestData("TestLoginPageNegativeScenarios");
		
		int row = testData.size();
        int col = testData.get(0).size();
        Object[][] obj = new Object[row][col-1];

		int i=0;
		for(HashMap<String, String> map:testData) {
			obj[i][0]=map.get("UserType");
			obj[i][1]=map.get("TestCaseID");
			obj[i][2]=map.get("Username");
			obj[i][3]=map.get("Password");
			obj[i][4]=map.get("ExpErrorMessage");
			obj[i][5]=map.get("ManualTCIDs");
			
			i++;
		}
		return obj;
	}
	
	/**
	 * Course Page Features
	 * @return
	 */
	@DataProvider(name = "CoursePageFeatures")
	public Object[][] getCoursePageFeaturesData() {

		ArrayList<HashMap<String, String>> testData = archUtl.getTestData("TestCoursePageFeatures");
		
		int row = testData.size();
        int col = testData.get(0).size();
        Object[][] obj = new Object[row][col-1];

		int i=0;
		for(HashMap<String, String> map:testData) {
			obj[i][0]=map.get("UserType");
			obj[i][1]=map.get("TestCaseID");
			obj[i][2]=map.get("LoginType");
			obj[i][3]=map.get("CourseName");
			obj[i][4]=map.get("ExpCourseHeaderText");
			obj[i][5]=map.get("ExpCourseDescriptionText");
			obj[i][6]=map.get("ExpFreeTrailText");
			obj[i][7]=map.get("ExpCourseOverviewText");
			obj[i][8]=map.get("ManualTCIDs");
			
			i++;
		}
		return obj;
	}
	
	/**
	 * Home Page Features
	 * @return
	 */
	@DataProvider(name = "HomePageFeatures")
	public Object[][] getHomePageFeaturesData() {

		ArrayList<HashMap<String, String>> testData = archUtl.getTestData("TestHomePageFeatures");
		
		int row = testData.size();
        int col = testData.get(0).size();
        Object[][] obj = new Object[row][col-1];

		int i=0;
		for(HashMap<String, String> map:testData) {
			obj[i][0]=map.get("UserType");
			obj[i][1]=map.get("TestCaseID");
			obj[i][2]=map.get("LoginType");
			obj[i][3]=map.get("ExpHomePageHeader");
			obj[i][4]=map.get("ExpHomePageDesc");
			obj[i][5]=map.get("ManualTCIDs");
			
			i++;
		}
		return obj;
	}
	
	/**
	 * Java Search Functionality
	 * @return
	 */
	@DataProvider(name = "JavaSearchFunctionality")
	public Object[][] getJavaSearchFunctionalityData() {

		ArrayList<HashMap<String, String>> testData = archUtl.getTestData("TestJavaSearchFunctionality");
		
		int row = testData.size();
        int col = testData.get(0).size();
        Object[][] obj = new Object[row][col-1];

		int i=0;
		for(HashMap<String, String> map:testData) {
			obj[i][0]=map.get("UserType");
			obj[i][1]=map.get("TestCaseID");
			obj[i][2]=map.get("LoginType");
			obj[i][3]=map.get("CourseName");
			obj[i][4]=map.get("ExpCourseHeaderText");
			obj[i][5]=map.get("ExpCourseDescriptionText");
			obj[i][6]=map.get("ManualTCIDs");
			
			i++;
		}
		return obj;
	}
	
	/**
	 * Search Page Features
	 * @return
	 */
	@DataProvider(name = "SearchPageFeatures")
	public Object[][] getSearchPageFeaturesData() {

		ArrayList<HashMap<String, String>> testData = archUtl.getTestData("TestSearchPageFeatures");
		
		int row = testData.size();
        int col = testData.get(0).size();
        Object[][] obj = new Object[row][col-1];

		int i=0;
		for(HashMap<String, String> map:testData) {
			obj[i][0]=map.get("UserType");
			obj[i][1]=map.get("TestCaseID");
			obj[i][2]=map.get("LoginType");
			obj[i][3]=map.get("CourseName");
			obj[i][4]=map.get("ManualTCIDs");
			
			i++;
		}
		return obj;
	}
	
	
}