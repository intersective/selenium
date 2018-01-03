package pom.pe;


import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import service.PageActionFactory;
import service.SeleniumWaiter;
import testsuit.dashboard.action.Actions;

import common.BuildConfig;
import common.ElementType;


public class DashboardPage extends TabPage {

	private Actions actions;
	
	private DashboardPage(String tabId, String tabPanelId) {
		super(tabId, tabPanelId);
	}
	
	public DashboardPage() {
		this("tab-t0-0", "tabpanel-t0-0");
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.dashboard.action.Actions");
	}

	@Override
	public void go(WebDriver driver) {
		driver.get(BuildConfig.peUrl);
	}

	public WebElement getDashboardDataContainer(SeleniumWaiter sw) {
		return sw.waitForElement("dashboard-data", ElementType.CLASSNAME);
	}
	
	public WebElement getActListPage(SeleniumWaiter sw) {
		return sw.waitForElement("activities-list-page");
	}

	public List<WebElement> getActList(SeleniumWaiter sw) {
		return sw.waitForListContent(".activities .activity-list");
	}
	
	public WebElement getActTitle(SeleniumWaiter sw, int index) {
		return sw.waitForElement(String.format(".activities .activity-list:nth-of-type(%s) p.activity-title", index));
	}
	
	public List<WebElement> getActTicks(SeleniumWaiter sw, int index) {
		return sw.waitForElements(String.format(".activities .activity-list:nth-of-type(%s) p.activity-title + .ticks i", index));
	}
	
	public WebElement getActScore(SeleniumWaiter sw, int index) {
		return sw.waitForElement(String.format(".activities .activity-list:nth-of-type(%s) p.activity-title + .ticks + p.assessment-score", index));
	}
	
	public String getUserPoints(SeleniumWaiter sw) {
		return actions.getUserPoints(sw);
	}
	
	public Actions getActions() {
		return actions;
	}
	
}
