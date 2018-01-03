package testsuit.dashboard;


import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.Tools;
import testsuit.TestTemplate;

import common.BuildConfig;
import common.ElementType;


public class TestDashBoardWithEvents extends TestTemplate {

	private List<WebElement> rows;
	private String selected;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test tab selecting status within the events board while switching between dashboard and events tab");
	}

	@Test(description = "start the test", groups = "dashboard_with_events", dependsOnGroups = "first")
	public void main() {
	}
	
	@Test(description = "switch between the dashboard and the events page", groups = "dashboard_with_events", dependsOnGroups = "first", 
			dependsOnMethods = {"testsuit.dashboard.TestDashBoardWithEvents.main"})
	public void step2() {
		waitForLoadFinished();
		
		WebElement dashboardTab = sw.waitForElement("tab-t0-0", ElementType.ID);
		Assert.assertNotNull(dashboardTab);
		dashboardTab.click();
		waitForLoadFinished();
		Assert.assertNotNull(sw.waitForElement("#tabpanel-t0-0[aria-hidden=false]"));
		WebElement tab = sw.waitForElement("tab-t0-1", ElementType.ID);
		Assert.assertNotNull(tab);
		tab.click();
		waitForLoadFinished();
		Assert.assertNotNull(sw.waitForElement("#tabpanel-t0-1[aria-hidden=false]"));
	}
	
	@Test(description = "change to the bookings category", groups = "dashboard_with_events", dependsOnGroups = "first", 
			dependsOnMethods = {"testsuit.dashboard.TestDashBoardWithEvents.step2"})
	public void step3() {
		rows = sw.waitForElements("events-list-page ion-grid ion-row");
		rows.get(0).findElements(Tools.getBy("ion-segment-button")).get(1).click();
		Tools.forceToWait(BuildConfig.jsWaitTime);// explicit wait for JS execution completed
	}
	
	@Test(description = "switch between the dashboard and the events page again", groups = "dashboard_with_events", dependsOnGroups = "first", 
			dependsOnMethods = {"testsuit.dashboard.TestDashBoardWithEvents.step3"})
	public void step4() {
		selected = rows.get(0).findElement(Tools.getBy("ion-segment-button[aria-pressed=true]")).getAttribute("value");
		runJSScript("document.getElementById('tab-t0-0').click()");
		waitForLoadFinished();
		Assert.assertNotNull(sw.waitForElement("#tabpanel-t0-0[aria-hidden=false]"));
		runJSScript("document.getElementById('tab-t0-1').click()");
		waitForLoadFinished();
		Assert.assertNotNull(sw.waitForElement("#tabpanel-t0-1[aria-hidden=false]"));
	}
	
	@Test(description = "ensure it stays on the bookings category", groups = "dashboard_with_events", dependsOnGroups = "first", 
			dependsOnMethods = {"testsuit.dashboard.TestDashBoardWithEvents.step4"})
	public void step5() {
		List<WebElement> rows1 = sw.waitForElements("events-list-page ion-grid ion-row");
		Assert.assertEquals(rows1.get(0).findElement(Tools.getBy("ion-segment-button[aria-pressed=true]"))
				.getAttribute("value"), selected);
	}

}
