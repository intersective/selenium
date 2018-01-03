package testsuit.dashboard;


import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pom.pe.DashboardPage;
import respositry.LocalStore;
import testsuit.TestTemplate;


public class TestDashBoardData extends TestTemplate {

	private DashboardPage dashboardPage;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test dashboard data");
	}

	@Test(description = "start test dashboard data", groups = "dashboard_data", dependsOnGroups = "first")
	public void main() {
		dashboardPage = new DashboardPage();
	}
	
	@Test(description = "enter the dashboard data", groups = "dashboard_data", 
			dependsOnMethods = {"testsuit.dashboard.TestDashBoardData.main"})
	public void step2() {
		waitForLoadFinished();
		dashboardPage.go(sw);
		waitForLoadFinished();
		Assert.assertNotNull(sw.waitForElement("#tabpanel-t0-0[aria-hidden=false]"));
	}

	@Test(description = "check the dashboard layout", groups = "dashboard_data", 
			dependsOnMethods = {"testsuit.dashboard.TestDashBoardData.step2"})
	public void step3() {
		Assert.assertNotNull(sw.waitForElement("activities-list-page"));
		WebElement dbd = dashboardPage.getDashboardDataContainer(sw);
		Assert.assertNotNull(dbd);
		List<WebElement> lis = dbd.findElements(By.tagName("li"));
		Assert.assertNotNull(lis);
		Assert.assertEquals(lis.size(), 3);
	}
	
	@Test(description = "check user points display correctly", groups = "dashboard_data", 
			dependsOnMethods = {"testsuit.dashboard.TestDashBoardData.step3"})
	public void step4() {
		LocalStore ls = LocalStore.getInstance();
		String tp;
		String display = dashboardPage.getUserPoints(sw);
		if ((tp = ls.getUserPotins()) != null) {
			Assert.assertEquals(display, tp);
		}
	}
	
}
