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


public class Appv1TestRegistration extends TestMailtrap {

	private Actions actions;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test a new student registration for App v1");
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.mailtrap.actions.Actions");
	}
	
	@Test(description = "test a new student enrolment for App v1", groups = "practera_appv1_registration")
	public void main() {
		driver.get(BuildConfig.mailtrapUrl);
		actions.login(sw, BuildConfig.mailtrapUser, BuildConfig.mailtrapPassword);
		searchEmail(BuildConfig.appv1UserName, "Welcome and Register");
		
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
		WebElement itemCheckBox = waitForVisibleWithScroll(".item-checkbox");
		itemCheckBox.click();
		
		waitForVisibleWithScroll(".item-checkbox + button").click();
		Tools.forceToWait(2);
		
		sw.waitForElement("input[name='uPassword']").sendKeys(new String[] { BuildConfig.appv1UserPassword });
		sw.waitForElement("input[name='uVerifyPassword']").sendKeys(new String[] { BuildConfig.appv1UserPassword });
		sw.waitForElement("button[type='submit']").click();
		
		sw.waitForElement(".popup-buttons");
		findElement(".popup-buttons > button:nth-of-type(1)").click();
	}

}
