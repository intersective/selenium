package testsuit.account;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import respositry.LocalStore;
import testsuit.TestTemplate;

import common.ElementType;


public class TestUserTab extends TestTemplate {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test click the user tab");
	}
	
	/**
	 * click the user tab
	 */
	@Test(description = "test click the user tab", groups = "account", dependsOnGroups = "first")
	public void main() {
		LocalStore ls = LocalStore.getInstance();
		
		WebElement personTab = sw.waitForElement("tab-t0-4", ElementType.ID);
		Assert.assertNotNull(personTab);
		personTab.click();
		waitForLoadFinished();
		Assert.assertNotNull(sw.waitForElement("#tabpanel-t0-4[aria-hidden=false]"));
		
		WebElement profileInfo = sw.waitForElement("settings-page", ElementType.TAGNAME).findElement(By.tagName("span"));
		Assert.assertNotNull(profileInfo);
		Assert.assertEquals(String.format("(%s)", ls.getValue("userName")), profileInfo.getText().toLowerCase());// user name is upper case for the iPhone mode
	}

}
