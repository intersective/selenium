package testsuit.events.actions;


import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import service.PageAction;
import service.SeleniumWaiter;
import service.Tools;

import common.BuildConfig;


public class Actions implements PageAction {

	protected String eventListLocator;
	
	public String selectAnEvent(WebDriver driver, SeleniumWaiter sw, int eventPosition) {
		String eventIdentifier = getEventIdentifier(driver , eventPosition);
		
		List<WebElement> events = sw.waitForElements(eventListLocator);
		events.get(--eventPosition).findElement(Tools.getBy("ion-card")).click();
		Tools.forceToWait(BuildConfig.pageWaitTime);
		return eventIdentifier;
	}
	
	public int getEventPosition(SeleniumWaiter sw, String eventName) {
		List<WebElement> eventElements = sw.waitForListContent(eventListLocator);
		Assert.assertNotNull(eventElements);
		int eventPosition = 1;
		for (WebElement event : eventElements) {
			if (Tools.getElementTextContent(event.findElement(Tools.getBy("ion-card > ion-list > .title")))
					.equals(eventName)) {
				break;
			}
			eventPosition++;
		}
		Assert.assertTrue(eventPosition <= eventElements.size());// we found a specific event
		return eventPosition;
	}
	
	public String getEventIdentifier(WebDriver driver, int eventPosition) {
		String jsScript = String.format("var t = document.querySelector('%s:nth-of-type(%s) ion-card > ion-list').style['background-image'].split('cdn.filestackcontent.com/')[1]; return t.slice(0,-2);", eventListLocator, eventPosition);
		return (String) Tools.runJSScript(driver, jsScript);
	}

	public void setEventListLocator(String eventListLocator) {
		this.eventListLocator = eventListLocator;
	}
	
}
