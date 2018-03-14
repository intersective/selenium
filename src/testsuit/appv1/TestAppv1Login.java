package testsuit.appv1;


import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.Tools;
import service.UIAction;
import testsuit.Appv1TestTemplate;

import common.BuildConfig;
import common.ElementType;


public class TestAppv1Login extends Appv1TestTemplate {

	protected String userName;
	protected String password;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test appv1 login");
		userName = BuildConfig.appv1UserName;
		password = BuildConfig.appv1UserPassword;
	}

	@Test(description = "test a new student login for App v1", groups = "practera_appv1_login")
	public void main() {
		driver.get(BuildConfig.appv1Url);

		Tools.forceToWait(10);
		WebElement loginForm = UIAction.waitForElementVisible(sw, ".jsmbp-login-form");
		Assert.assertNotNull(loginForm);
		
		loginForm.findElement(Tools.getBy("input[name=uEmail]")).clear();
		loginForm.findElement(Tools.getBy("input[name=uEmail]")).sendKeys(new String [] { userName });
		loginForm.findElement(Tools.getBy("input[name=password]")).clear();
		loginForm.findElement(Tools.getBy("input[name=password]")).sendKeys(new String [] { userName });
		loginForm.findElement(Tools.getBy("#jsmbpLoginBtn")).click();
		Assert.assertNotNull(sw.waitForElement("//*[@class='popup-title'][text()='Invalid Login Details']", ElementType.XPATH));
		sw.waitForElement(".popup > .popup-buttons > button").click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		
		loginForm = UIAction.waitForElementVisible(sw, ".jsmbp-login-form");
		loginForm.findElement(Tools.getBy("input[name=password]")).clear();
		loginForm.findElement(Tools.getBy("input[name=password]")).sendKeys(new String[] { password });
		loginForm.findElement(Tools.getBy("#jsmbpLoginBtn")).click();
		Assert.assertNull(sw.waitForElement("//*[@class='popup-title'][text()='Invalid Login Details']", ElementType.XPATH));
		
		Assert.assertEquals(Tools.getElementTextContent(sw.waitForElement(".item-content:nth-of-type(1)")), BuildConfig.appv1Program);
		findElement(".jsmbp-switch-item:nth-of-type(1)").click();
		
		waitForLoadFinished();
		WebElement home = sw.waitForElement("ion-nav-view[name='home']");
		Assert.assertNotNull(home);
		Assert.assertEquals(home.getAttribute("nav-view"), "active");
	}

}
