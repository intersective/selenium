package testsuit.jobsmart;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.PageActionFactory;
import service.Tools;
import testsuit.TestTemplate;
import testsuit.jobsmart.assessments.actions.Actions;


public class TestLogout extends TestTemplate {

	private Actions actions;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test job smart logout");
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.jobsmart.assessments.actions.Actions");
	}

	@Test(description = "test new student log out for Job smart", groups = "practera_jobsmart_logout")
	public void main() {
		actions.logout(sw);
		Tools.forceToWait(5);
	}

}
