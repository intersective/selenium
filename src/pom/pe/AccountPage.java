package pom.pe;


import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import service.SeleniumWaiter;
import service.Tools;
import service.UIAction;

import common.ElementType;


public class AccountPage extends TabPage {

	private AccountPage(String tabId, String tabPanelId) {
		super(tabId, tabPanelId);
	}
	
	public AccountPage() {
		this("tab-t0-4", "tabpanel-t0-4");
	}

	public WebElement getTutorialSlides(SeleniumWaiter sw) {
		return sw.waitForElement(".tutorial-slides", ElementType.CSSSELECTOR, 5);
	}
	
	public WebElement getSkipBtn(SeleniumWaiter sw) {
		return sw.waitForElement(".skip-button", ElementType.CSSSELECTOR, 5);
	}
	
	public WebElement getVideo(SeleniumWaiter sw) {
		return UIAction.waitForElementVisible(sw, "video", 5);
	}
	
	public WebElement getVideoDoneBtn(SeleniumWaiter sw) {
		return UIAction.waitForElementVisible(sw, "ion-list.video-tutorial button", 5);
	}
	
	public WebElement getExitBtn(SeleniumWaiter sw) {
		return sw.waitForElement("ion-icon[name=exit]");
	}
	
	public WebElement getEmailBtn(SeleniumWaiter sw) {
		return sw.waitForElement("email", ElementType.NAME).findElement(By.name("email"));
	}
	
	public void logout(WebDriver driver, SeleniumWaiter sw) {
		Tools.waitForLoadFinished(driver);
		
		WebElement exit = this.getExitBtn(sw);
		Assert.assertNotNull(exit);
		Assert.assertTrue(exit.isEnabled());
		exit.click();
		Tools.waitForLoadFinished(driver);
		Assert.assertNotNull(this.getEmailBtn(sw));
	}
	
}
