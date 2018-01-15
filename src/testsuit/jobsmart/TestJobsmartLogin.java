package testsuit.jobsmart;


import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.Tools;
import service.UIAction;
import testsuit.TestTemplate;
import common.BuildConfig;
import common.ElementType;


public class TestJobsmartLogin extends TestTemplate {

	protected String userName;
	protected String password;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test job smart login");
		userName = BuildConfig.jobsmartStudent;
		password = BuildConfig.jobsmartStudentPassword;
	}
	
	@Test(description = "test a new student login for Job smart", groups = "practera_jobsmart_login")
	public void main() {
		driver.get(BuildConfig.jobsmartUrl);	
		
		Tools.forceToWait(10);
		WebElement loginForm = UIAction.waitForElementVisible(sw, "form[name='jobsmart']");
		Assert.assertNotNull(loginForm);
		loginForm.findElement(Tools.getBy("input[name=uEmail]")).clear();
		loginForm.findElement(Tools.getBy("input[name=uEmail]")).sendKeys(new String [] { userName });
		loginForm.findElement(Tools.getBy("input[name=password]")).clear();
		loginForm.findElement(Tools.getBy("input[name=password]")).sendKeys(new String [] { userName });
		loginForm.findElement(Tools.getBy("input[type='submit']")).click();
		Assert.assertNotNull(sw.waitForElement("//*[@class='popup-title'][text()='Invalid Login Details']", ElementType.XPATH));
		sw.waitForElement(".popup > .popup-buttons > button").click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		
		loginForm = UIAction.waitForElementVisible(sw, "form[name='jobsmart']");
		Assert.assertNotNull(loginForm);
		loginForm.findElement(Tools.getBy("input[name=password]")).clear();
		loginForm.findElement(Tools.getBy("input[name=password]")).sendKeys(new String [] { password });
		loginForm.findElement(Tools.getBy("input[type='submit']")).click();
		Assert.assertNull(sw.waitForElement("//*[@class='popup-title'][text()='Invalid Login Details']", ElementType.XPATH));
		
	}

}
