package testsuit;


import model.Event;
import model.User;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.testng.ITest;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import respositry.LocalDataBase;
import respositry.LocalStore;
import respositry.Settings;
import service.AssignmentDataService;
import service.ShareWebDriver;
import service.TestLogger;
import service.TestRailService;
import common.BuildConfig;
import common.ShareConfig;


public class InitTest implements ITest {

	private String testname;
	
	@BeforeClass
	public void setup() {
		testname = "test initialisation";
	}
	
	@Test(description = "test initialisation")
	public void main() {
		LocalStore ls = LocalStore.getInstance();
		if ("true".equals(ls.getValue("InitTest"))) {
			return;
		}
		ls.addValue("InitTest", "true");
		
		init();
		initUserData();
	}

	protected void init() {
		LocalDataBase ldb = LocalDataBase.getInstance();
		LocalStore ls = LocalStore.getInstance();
		ldb.createTable();
		Settings.getInstance();
		ShareWebDriver.getInstance();
		AssignmentDataService.getInstance();
		
		if (BuildConfig.testrail) {
			TestRailService trs = TestRailService.getInstance();
			JSONArray cases = trs.getCaeseForProject(BuildConfig.projectId);
			if (cases != null) {
				int total = cases.size();
				for (int i = 0; i < total; i++) {
					JSONObject c = (JSONObject) cases.get(i);
					String className = (String) c.get("custom_code_location");
					Long id = (Long) c.get("id");
					ls.addValue(className, id.toString());
				}
			}
			JSONObject result = trs.addTestRun(BuildConfig.projectId, BuildConfig.userIdInTestRail, null);
			if (result != null) {
				ShareConfig.runId = String.valueOf(((Long) result.get("id")).intValue());
			}
		}
	}
	
	protected void initUserData() {
		LocalDataBase ldb = LocalDataBase.getInstance();
		LocalStore ls = LocalStore.getInstance();
		User t;
		if ((t = ldb.getCurrnetUser(BuildConfig.peId)) != null) {
			BuildConfig.userName = t.getUserName();
		}
		Event event;
		if ((event = ldb.getCurrentEvent()) != null) {// in case of rerunning the events test cases
			ShareConfig.createdEventId = event.getId();
		}
		ls.setUserPointsKey(String.format("%s-points", BuildConfig.userName));
		ls.setUserSpinChancesKey(String.format("%s-spinChances", BuildConfig.userName));
	}
	
	@AfterMethod
	public void after(ITestResult result) {
		TestLogger.trace("initialisation completed");
	}
	
	@Override
	public String getTestName() {
		return testname;
	}

}
