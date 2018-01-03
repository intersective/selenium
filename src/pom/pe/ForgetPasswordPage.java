package pom.pe;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pom.Page;
import service.SeleniumWaiter;

public class ForgetPasswordPage implements Page {

	@Override
	public void go(WebDriver driver) {
	}

	@Override
	public void go(SeleniumWaiter sw) {
		sw.waitForElement("form.general-form .forget-password-container button").click();
	}

	@Override
	public void back(SeleniumWaiter sw) {
		sw.waitForElement("page-forget-password .back-button + ion-buttons button").click();
	}
	
	public WebElement getForgetForm(SeleniumWaiter sw) {
		return sw.waitForElement("page-forget-password form");
	}
	
	public WebElement getContactEmail(SeleniumWaiter sw) {
		return sw.waitForElement("page-forget-password form ion-input[name=email] input");
	}
	
	public WebElement getSendEmailBtn(SeleniumWaiter sw) {
		return sw.waitForElement("page-forget-password form button");
	}

}
