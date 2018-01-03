package testsuit.account;


import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.Tools;
import testsuit.TestTemplate;

import common.BuildConfig;
import common.ElementType;


public class TestTutorialButton extends TestTemplate {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test totorial button");
	}

	@Test(description = "test totorial button", groups = "account_button", dependsOnGroups = "account")
	public void main() {
		waitForLoadFinished();
		Assert.assertNotNull(sw.waitForElement("#tabpanel-t0-4[aria-hidden=false]"));
		
		WebElement medkit = sw.waitForElement("medkit", ElementType.NAME);
		Assert.assertNotNull(medkit);
		Assert.assertTrue(medkit.isEnabled());
		runJSScript("document.getElementsByName('medkit')[0].parentElement.click();");
		Tools.forceToWait(BuildConfig.pageWaitTime);
		
		WebElement tutorialPage = sw.waitForElement("tutorial-page");
		Assert.assertNotNull(tutorialPage);
		WebElement doneBtn = tutorialPage.findElement(Tools.getBy(ElementType.CLASSNAME, "button"));
		Assert.assertNotNull(doneBtn);
		doneBtn.click();
		waitForLoadFinished();
		
		WebElement tab0 = sw.waitForElement("tab-t0-0", ElementType.ID);
		Assert.assertNotNull(tab0);
		tab0.click();
		waitForLoadFinished();
		Assert.assertNotNull(sw.waitForElement("#tabpanel-t0-0[aria-hidden=false]"));
	}

}
