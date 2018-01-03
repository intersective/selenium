package pom.pe;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import common.ElementType;

import pom.Page;
import service.SeleniumWaiter;
import service.Tools;


public class EventDetailPage implements Page {

	@Override
	public void go(WebDriver driver) {
	}

	@Override
	public void go(SeleniumWaiter sw) {
	}

	@Override
	public void back(SeleniumWaiter sw) {
		sw.waitForElement("show-back-button", ElementType.CLASSNAME).click();
	}
	
	public WebElement getActionSheetContainer(WebDriver driver) {
		return driver.findElement(Tools.getBy(ElementType.CLASSNAME, "action-sheet-container")); 
	}
	
	public WebElement getCancelBtn(WebDriver driver) {
		return driver.findElement(Tools.getBy(".action-sheet-container .action-sheet-cancel"));
	}

	public WebElement getConfirmBtn(WebDriver driver) {
		return driver.findElement(Tools.getBy(".action-sheet-container .action-sheet-title + .action-sheet-button"));
	}
	
	public WebElement getBookBtn(SeleniumWaiter sw) {
		return sw.waitForElement("button.book-btn");
	}
	
	public WebElement getCheckInBtn(SeleniumWaiter sw) {
		return sw.waitForElement(".footer button.checkin-btn");
	}
	
}
