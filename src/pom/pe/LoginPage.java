package pom.pe;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pom.Page;
import service.PageActionFactory;
import service.SeleniumWaiter;
import testsuit.Actions;

import common.BuildConfig;


public class LoginPage implements Page {

	private Actions actions;
	
	public LoginPage() {
		super();
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.Actions");
	}

	@Override
	public void go(WebDriver driver) {
		driver.get(BuildConfig.peUrl);
	}

	@Override
	public void go(SeleniumWaiter sw) {
	}

	@Override
	public void back(SeleniumWaiter sw) {
	}
	
	public WebElement getLoginForm(SeleniumWaiter sw) {
		return sw.waitForElement("form.general-form");
	}

	public WebElement getForgetPasswordBtn(SeleniumWaiter sw) {
		return sw.waitForElement("form.general-form .forget-password-container button");
	}

	public void login(SeleniumWaiter sw, String userName, String password) {
		actions.login(sw, userName, password);
	}
	
	public WebElement getPasswordField(SeleniumWaiter sw) {
		return sw.waitForElement("form.general-form ion-input[name=password] input");
	}
	
	public WebElement getLoginBtn(SeleniumWaiter sw) {
		return sw.waitForElement("form.general-form > button");
	}

}
