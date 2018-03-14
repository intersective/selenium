package testsuit.practera;


import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import respositry.LocalDataBase;
import service.Tools;
import common.BuildConfig;


public class TestAppV1Enrolment extends TestEnrolment {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test a new student enrol to App v1");
	}

	@Test(description = "test a new student enrolment for App v1", groups = "practera_appv1")
	public void main() {
		driver.get(BuildConfig.practeraUrl);
		actions.login(sw, BuildConfig.appv1AdminUser, BuildConfig.appv1AdminPassword);
		super.enroll("4", "5", "2", "App V1 timeline 1", BuildConfig.appv1EnrolmentFile);
		
		BuildConfig.appv1UserName = String.format("%s@%s", studentUserName, BuildConfig.testDomain);
		LocalDataBase.getInstance().addUser(BuildConfig.appv1Id, BuildConfig.appv1UserName, regUrl);
		
		driver.get(regUrl);
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

	@Override
	protected String supplyStudentInfo() {
		return actions.enrolStudentViaTextBox(sw);
	}
	
}
