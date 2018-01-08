package testsuit.appv1;


import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.Tools;

import common.BuildConfig;


public class TestAppv1MentorLogin extends TestAppv1Login {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test appv1 mentor login");
		userName = BuildConfig.appV1Mentor;
		password = BuildConfig.appV1MentorPassword;
	}

	@Test(description = "test a mentor login for App v1", groups = "practera_appv1_login_mentor")
	public void main() {
		super.main();
		
		List<WebElement> tabs = sw.waitForElements(".tab-nav > a:nth-of-type(1)");
		tabs.get(tabs.size() - 1).click();
		
		Tools.forceToWait(2);
	}

}
