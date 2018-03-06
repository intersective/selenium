package testsuit.practera;


import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pom.pe.AccountPage;
import pom.pe.DashboardPage;
import respositry.LocalStore;
import service.Tools;
import testsuit.TestTemplate;
import common.BuildConfig;
import common.ElementType;


public class TestPERegistrationProcess extends TestTemplate {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test a new student registration process for Personal Edge");
	}
	
	@Test(description = "test a new student registration process for Personal Edge", groups = "practera_pe_registration_process")
	public void main() {
		WebElement agreeCheckBox = sw.waitForElement("ion-checkbox[value=Agree]");
		Assert.assertNotNull(agreeCheckBox);
		agreeCheckBox.click();
		findElement(".register-next button").click();
		
		WebElement generalForm = sw.waitForElement(".general-form");
		Assert.assertNotNull(generalForm);
		generalForm.findElement(Tools.getBy("ion-input[type=password] input")).sendKeys(new String[] { BuildConfig.password });
		Tools.forceToWait(BuildConfig.inputWaitTime);
		generalForm.findElement(Tools.getBy("ion-input[name=verify_password] input")).sendKeys(new String[] { BuildConfig.password });
		Tools.forceToWait(BuildConfig.inputWaitTime);
		generalForm.findElement(Tools.getBy(ElementType.TAGNAME, "button")).click();
		
		DashboardPage dashboardPage = new DashboardPage();
		LocalStore ls = LocalStore.getInstance();
		ls.addUserPoints(dashboardPage.getUserPoints(sw));
		
		AccountPage accountPage = new AccountPage();
		accountPage.getVideo(sw);
		WebElement doneBtn = accountPage.getVideoDoneBtn(sw);
		if (doneBtn != null) {
			Tools.forceToWait(5);
			doneBtn.click();
		}
		waitForLoadFinished();
		waitForClickBlock();
		
		accountPage.go(sw);
		waitForLoadFinished();
		waitForClickBlock();
		WebElement exit = accountPage.getExitBtn(sw);
		Assert.assertNotNull(exit);
		Assert.assertTrue(exit.isEnabled());
		exit.click();
	}

}
