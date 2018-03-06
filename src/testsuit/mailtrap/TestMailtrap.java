package testsuit.mailtrap;


import org.openqa.selenium.WebElement;
import org.testng.Assert;

import service.Tools;
import testsuit.TestTemplate;

import common.ElementType;


public abstract class TestMailtrap extends TestTemplate {

	@Override
	protected void waitForLoadFinished() {
		Tools.forceToWaitInMilli(500);
		while (findElement("#nprogress") != null) {
			Tools.forceToWaitInMilli(100);
		}
	}
	
	protected void searchEmail(String keyword, String filter) {
		sw.waitForElement("#main .quick_filter").clear();
		sw.waitForElement("#main .quick_filter").sendKeys(new String[] { keyword });
		waitForLoadFinished();
		Assert.assertNull(sw.waitForElement("//*[contains(@class, 'messages_list')]/li/p[text()='Nothing has been found for the filter']"));
		WebElement email = sw.waitForElement(String.format(
				"//*[contains(concat(' ', @class, ' '), 'messages_list')]/*[contains(concat(' ', @class, ' '), 'email')]/*[contains(text(),'%s')]", filter),
				ElementType.XPATH);
		Assert.assertNotNull(email);
		email.click();
		waitForLoadFinished();
		Tools.forceToWait(2);
	}
	
	protected void waitForBackdropRemoved() {
		WebElement backdrop = sw.waitForElement(".backdrop");
		while (backdrop.getAttribute("class").contains("visible")) {
			Tools.forceToWait(1);
			backdrop = sw.waitForElement("backdrop");
		}
	}
	
}
