package testsuit.spinner;


import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.Tools;
import testsuit.TestTemplate;

import common.BuildConfig;
import common.ElementType;


public class TestSpinnerLayout extends TestTemplate {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test spinner page layout");
	}
	
	@Test(description = "test spinner page layout", groups = "spinnerboard_laytout", dependsOnGroups = "first")
	public void main() {
		waitForLoadFinished();
		
		WebElement tab = sw.waitForElement("tab-t0-3", ElementType.ID);
		Assert.assertNotNull(tab);
		tab.click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		waitForLoadFinished();
		WebElement tabpanel = sw.waitForElement("#tabpanel-t0-3[aria-hidden=false]");
		Assert.assertNotNull(tabpanel);
		
		if (sw.waitForElement("ion-popover", ElementType.TAGNAME) != null) {
			Tools.runJSScript(driver, "document.querySelector('ion-backdrop').click();");
			Tools.forceToWait(BuildConfig.jsWaitTime);
		}
		
		List<WebElement> lis = tabpanel.findElements(Tools.getBy("ion-grid[_ngcontent-c1] ul li"));
		Assert.assertEquals(lis.size(), 2);
	}

}
