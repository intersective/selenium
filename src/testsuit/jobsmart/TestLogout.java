package testsuit.jobsmart;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.Tools;
import testsuit.TestTemplate;


public class TestLogout extends TestTemplate {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test job smart logout");
	}

	@Test(description = "test new student log out for Job smart", groups = "practera_jobsmart_logout")
	public void main() {
		sw.waitForElement(".tab-nav > a:nth-of-type(4)").click();
		
		sw.waitForElement(".pane[nav-view='active'] ion-item[ng-click='logout()']").click();
		Tools.forceToWait(5);
	}

}
