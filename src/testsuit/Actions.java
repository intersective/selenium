package testsuit;


import org.openqa.selenium.WebElement;
import org.testng.Assert;

import service.PageAction;
import service.SeleniumWaiter;


public class Actions implements PageAction {

	public void login(SeleniumWaiter sw, String userName, String upassword) {
		WebElement loginForm = sw.waitForElement("form.general-form");
		Assert.assertNotNull(loginForm);
		WebElement btn = sw.waitForElement("form.general-form > button");
		WebElement email = sw.waitForElement("form.general-form ion-input[name=email] input");
		WebElement password = sw.waitForElement("form.general-form ion-input[name=password] input");
		Assert.assertNotNull(btn);
		Assert.assertNotNull(email);
		Assert.assertNotNull(password);
		email.sendKeys(new String[] { userName });
		password.sendKeys(new String[] { upassword });
		btn.click();
	}
	
}
