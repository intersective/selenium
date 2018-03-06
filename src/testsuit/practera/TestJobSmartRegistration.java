package testsuit.practera;


import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.PageActionFactory;
import service.TestLogger;
import service.Tools;
import service.UIAction;
import testsuit.mailtrap.TestMailtrap;
import testsuit.mailtrap.actions.Actions;

import com.google.common.base.Throwables;
import common.BuildConfig;
import common.ElementType;

/**
 * we use the registration link from the email 
 * @author Barry
 *
 */
public class TestJobSmartRegistration extends TestMailtrap {

	private Actions actions;
	private testsuit.jobsmart.assessments.actions.Actions jobsmaartActions;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test a new student registration for Job smart");
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.mailtrap.actions.Actions");
		jobsmaartActions = (testsuit.jobsmart.assessments.actions.Actions) PageActionFactory.getInstance().build("testsuit.jobsmart.assessments.actions.Actions");
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
		waitForBackdropRemoved();
		Tools.forceToWait(5);
		
		WebElement itemCheckBox = waitForVisibleWithScroll(".item-checkbox > .checkbox > i");
		org.openqa.selenium.interactions.Actions oneAction = new org.openqa.selenium.interactions.Actions(driver);
		oneAction.moveToElement(itemCheckBox).click().build().perform();
		
		sw.waitForElement("input[name='uPassword']").sendKeys(new String[] { BuildConfig.jobsmartStudentPassword });
		sw.waitForElement("input[name='uVerifyPassword']").sendKeys(new String[] { BuildConfig.jobsmartStudentPassword });
		sw.waitForElement("button[type='submit']").click();
		
		sw.waitForElement(".popup-buttons");
		findElement(".popup-buttons > button:nth-of-type(1)").click();
		
		WebElement getStarted = UIAction.waitForElementVisible(sw, "//button[text()='Click here to get started!']", ElementType.XPATH, 40);
		if (getStarted != null) {
			boolean clicked = false;
			while (!clicked) {
				try {
					getStarted.click();
					clicked = true;
				} catch (Exception e) {
					TestLogger.error(Throwables.getStackTraceAsString(e));
					clicked = false;
				}
				if (!clicked) {
					Tools.forceToWait(3);
					getStarted = UIAction.waitForElementVisible(sw, "//button[text()='Click here to get started!']", ElementType.XPATH, 40);
				}
			}
		}
		
		while (!driver.getCurrentUrl().equals(String.format("%s/#/app/home", BuildConfig.jobsmartUrl))) {// automatically login after registration
			Tools.forceToWait(BuildConfig.pageWaitTime);
		}
		jobsmaartActions.logout(sw);
	}

}
