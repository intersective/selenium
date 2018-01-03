package testsuit.appv1;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.Tools;
import testsuit.Appv1TestTemplate;


public class TestLogout extends Appv1TestTemplate {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test appv1 logout");
	}

	@Test(description = "test new student log out for App v1", groups = "practera_appv1_logout")
	public void main() {
		sw.waitForElement(".tab-nav > a:nth-of-type(4)").click();
		
		sw.waitForElement(".jsmbp-settings-container ion-item[ng-click='logout()']").click();
		sw.waitForElement(".jsmbp-login-form");
		Tools.forceToWait(5);
	}

}
