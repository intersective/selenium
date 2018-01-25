package testsuit.jobsmart;


import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.PageActionFactory;
import service.Tools;
import testsuit.TestTemplate;
import testsuit.jobsmart.assessments.actions.Actions;

import common.BuildConfig;
import common.ElementType;


public class TestJobsmartLogin extends TestTemplate {

	protected String userName;
	protected String password;
	private Actions actions;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test job smart login");
		userName = BuildConfig.jobsmartStudent;
		password = BuildConfig.jobsmartStudentPassword;
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.jobsmart.assessments.actions.Actions");
	}
	
	@Test(description = "test a new student login for Job smart", groups = "practera_jobsmart_login")
	public void main() {
		driver.get(BuildConfig.jobsmartUrl);	
		
		Tools.forceToWait(10);
		actions.login(sw, userName, userName);
		
		Assert.assertNotNull(sw.waitForElement("//*[@class='popup-title'][text()='Invalid Login Details']", ElementType.XPATH));
		sw.waitForElement(".popup > .popup-buttons > button").click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		
		actions.login(sw, userName, password);
		Assert.assertNull(sw.waitForElement("//*[@class='popup-title'][text()='Invalid Login Details']", ElementType.XPATH));
		
	}

}
