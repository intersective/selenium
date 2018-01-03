package pom.pe;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import pom.Page;
import service.SeleniumWaiter;

import common.ElementType;


public class TabPage implements Page {

	private String tabId;
	private String tabPanelId;
	private WebElement tab;
	
	public TabPage(String tabId, String tabPanelId) {
		super();
		this.tabId = tabId;
		this.tabPanelId = tabPanelId;
	}

	@Override
	public void go(WebDriver driver) {
	}

	@Override
	public void go(SeleniumWaiter sw) {
		tab = sw.waitForElement(tabId, ElementType.ID);
		tab.click();
	}

	@Override
	public void back(SeleniumWaiter sw) {
	}

	public WebElement getTab(SeleniumWaiter sw) {
		return tab = sw.waitForElement(tabId, ElementType.ID);
	}

	public WebElement getTabPanel(SeleniumWaiter sw) {
		return sw.waitForElement(tabPanelId, ElementType.ID);
	}

	public String getTabId() {
		return tabId;
	}

	public String getTabPanelId() {
		return tabPanelId;
	}
	
}
