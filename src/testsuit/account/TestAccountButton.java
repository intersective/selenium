package testsuit.account;


import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import testsuit.TestTemplate;

import common.ElementType;


public class TestAccountButton extends TestTemplate {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test account page button");
	}
	
	@Test(description = "start test button in the account page", groups = "account_button", dependsOnGroups = "account")
	public void main() {
	}
	
	@Test(description = "start test account page button", groups = "account_button", dependsOnGroups = "account", dependsOnMethods = "main")
	public void step2() {
		WebElement personTab = sw.waitForElement("tab-t0-4", ElementType.ID);
		Assert.assertNotNull(personTab);
		personTab.click();
		waitForLoadFinished();
		Assert.assertNotNull(sw.waitForElement("#tabpanel-t0-4[aria-hidden=false]"));
	}

	@Test(description = "check mail box button", groups = "account_button", dependsOnGroups = "account", dependsOnMethods = "step2")
	public void step3() {
		WebElement mail = sw.waitForElement("mail", ElementType.NAME);
		Assert.assertNotNull(mail);
		Assert.assertTrue(mail.isEnabled());
	}
	
	@Test(description = "check clipboard button", groups = "account_button", dependsOnGroups = "account", dependsOnMethods = "step3")
	public void step4() {
		WebElement clipboard = sw.waitForElement("clipboard", ElementType.NAME);
		Assert.assertNotNull(clipboard);
		Assert.assertTrue(clipboard.isEnabled());
	}
	
	@Test(description = "check medkit button", groups = "account_button", dependsOnGroups = "account", dependsOnMethods = "step4")
	public void step5() {
		WebElement medkit = sw.waitForElement("medkit", ElementType.NAME);
		Assert.assertNotNull(medkit);
		Assert.assertTrue(medkit.isEnabled());
	}
	
	@Test(description = "check log out button", groups = "account_button", dependsOnGroups = "account", dependsOnMethods = "step5")
	public void step6() {
		WebElement exit = sw.waitForElement("exit", ElementType.NAME);
		Assert.assertNotNull(exit);
		Assert.assertTrue(exit.isEnabled());
	}
	
	@Test(description = "check user name display correctly", groups = "account_button", dependsOnGroups = "account", dependsOnMethods = "step6")
	public void step7() {
		WebElement nameOnLeaderboard = sw.waitForElement("item-toggle", ElementType.CLASSNAME);
		Assert.assertNotNull(nameOnLeaderboard);
		WebElement itemInner = nameOnLeaderboard.findElement(By.className("item-inner"));
		Assert.assertNotNull(itemInner);
	}
	
}
