package testsuit.practera;


import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.PageActionFactory;
import service.TestLogger;
import service.Tools;
import testsuit.mailtrap.TestMailtrap;
import testsuit.mailtrap.actions.Actions;

import common.BuildConfig;


public class TestPERegistration extends TestMailtrap {

	private Actions actions;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test a new student registration for Personal Edge");
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.mailtrap.actions.Actions");
	}
	
	@Test(description = "test a new student registration for Personal Edge", groups = "practera_pe_registration")
	public void main() {
		driver.get(BuildConfig.mailtrapUrl);
		actions.login(sw, BuildConfig.mailtrapUser, BuildConfig.mailtrapPassword);
		searchEmail(BuildConfig.userName, "Welcome and Register");
		
		String mainWindowHandle = driver.getWindowHandle();
		TestLogger.trace(String.format("main window %s", mainWindowHandle));
		driver.switchTo().frame(sw.waitForElement("#main .tab_content:nth-of-type(1) .flex-item"));
		WebElement btnReg = sw.waitForElement("#btn_registration");
		String turl = btnReg.getAttribute("href");
		driver.switchTo().window(mainWindowHandle);
		Tools.forceToWait(2);
		actions.logout(sw);
		
		driver.get(turl);
		Tools.forceToWait(5);
	}

}
