package testsuit.appv1;


import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.Tools;
import testsuit.Appv1TestTemplate;


public class TestAppv1MentorLogout extends Appv1TestTemplate {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test appv1 mentor logout");
	}

	@Test(description = "test a mentor log out for App v1", groups = "practera_appv1_logout_mentor")
	public void main() {
		List<WebElement> tabs = sw.waitForElements(".tab-nav > a:nth-of-type(4)");
		tabs.get(tabs.size() - 1).click();
		
		sw.waitForElement(".jsmbp-settings-container ion-item[ng-click='logout()']").click();
		sw.waitForElement(".jsmbp-login-form");
		Tools.forceToWait(5);
	}

}
