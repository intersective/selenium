package testsuit.mailtrap;


import java.util.Iterator;

import model.User;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import respositry.LocalDataBase;
import service.TestLogger;
import service.Tools;

import common.BuildConfig;
import common.ElementType;


public class TestStudentRegistrationEmail extends TestMailtrap {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test mail trap to see if there is a student registration email");
	}

	@Test(description = "test mail trap to see if there is a student registration email", groups = "mailtrap_student_registration")
	public void main() {
		Tools.forceToWait(2);
		searchEmail(BuildConfig.appv1UserName, "Welcome and Register");
		
		User user = LocalDataBase.getInstance().getCurrnetUser(BuildConfig.appv1Id);
		String regurl = user.getRegurl().split("(//)")[1];
		String mainWindowHandle = driver.getWindowHandle();
		TestLogger.trace(String.format("main window %s", mainWindowHandle));
		driver.switchTo().frame(sw.waitForElement("#main .tab_content:nth-of-type(1) .flex-item"));
		WebElement btnReg = sw.waitForElement("#btn_registration");
		String turl = btnReg.getAttribute("href").split("(//)")[1];
		btnReg.click();
		Iterator<String> it = driver.getWindowHandles().iterator();
		it.next();
		driver.switchTo().window(it.next());
		Tools.forceToWait(2);
		Assert.assertNotNull(sw.waitForElement("//*[@class='popup-title'][text()='Account already registered']", ElementType.XPATH));
		Assert.assertEquals(turl, regurl);
	}
	
}
