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

/**
 * we use the registration link from the email 
 * @author Barry
 *
 */
public class TestJobSmartRegistration extends TestMailtrap {

	private Actions actions;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test a new student registration for Job smart");
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.mailtrap.actions.Actions");
	}
	
	@Test(description = "test a new student enrolment for Job smart", groups = "practera_jobsmart_registration")
	public void main() {
		driver.get(BuildConfig.mailtrapUrl);
		actions.login(sw, BuildConfig.mailtrapUser, BuildConfig.mailtrapPassword);
		searchEmail(BuildConfig.jobsmartStudent, "Welcome and Register");
		
		String mainWindowHandle = driver.getWindowHandle();
		TestLogger.trace(String.format("main window %s", mainWindowHandle));
		driver.switchTo().frame(sw.waitForElement("#main .tab_content:nth-of-type(1) .flex-item"));
		WebElement btnReg = sw.waitForElement("a[title='REGISTER NOW']");
		String turl = btnReg.getAttribute("href").split("(//)")[1];
		driver.switchTo().window(mainWindowHandle);
		Tools.forceToWait(2);
		actions.logout(sw);
		
		driver.get(String.format("https://develop.%s", turl));
		Tools.forceToWait(2);
		
		WebElement itemCheckBox = waitForVisibleWithScroll(".item-checkbox");
		itemCheckBox.click();
		
		waitForVisibleWithScroll(".item-checkbox + button").click();
		Tools.forceToWait(2);
		
		sw.waitForElement("input[name='uPassword']").sendKeys(new String[] { BuildConfig.jobsmartStudentPassword });
		sw.waitForElement("input[name='uVerifyPassword']").sendKeys(new String[] { BuildConfig.jobsmartStudentPassword });
		sw.waitForElement("button[type='submit']").click();
		
		sw.waitForElement(".popup-buttons");
		findElement(".popup-buttons > button:nth-of-type(1)").click();
	}

}
