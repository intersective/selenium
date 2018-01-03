package testsuit.practera;


import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.Tools;
import testsuit.Appv1TestTemplate;
import common.BuildConfig;


public class Appv1TestRegistration extends Appv1TestTemplate {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test a new student registration for App v1");
	}
	
	@Test(description = "test a new student enrolment for App v1", groups = "practera_appv1_registration")
	public void main() {
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
