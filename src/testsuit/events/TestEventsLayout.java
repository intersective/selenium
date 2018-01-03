package testsuit.events;


import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import testsuit.TestTemplate;

import common.ElementType;


public class TestEventsLayout extends TestTemplate {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test events board layout");
	}
	
	@Test(description = "test events board layout", groups = "eventboard_laytout", dependsOnGroups = "first")
	public void main() {
		waitForLoadFinished();
		
		WebElement tab = sw.waitForElement("tab-t0-1", ElementType.ID);
		Assert.assertNotNull(tab);
		tab.click();
		waitForLoadFinished();
		Assert.assertNotNull(sw.waitForElement("#tabpanel-t0-1[aria-hidden=false]"));
		
		List<WebElement> buttons = sw.waitForElements("events-list-page ion-grid ion-row:nth-of-type(1) ion-segment-button");
		Assert.assertNotNull(buttons);
		Assert.assertEquals(buttons.size(), 3);
	}

}
