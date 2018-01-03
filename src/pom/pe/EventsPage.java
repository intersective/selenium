package pom.pe;


import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import service.PageActionFactory;
import service.SeleniumWaiter;
import testsuit.events.actions.Actions;


public class EventsPage extends TabPage {

	private Actions actions;
	private EventDetailPage eventDetailPage;
	
	private EventsPage(String tabId, String tabPanelId) {
		super(tabId, tabPanelId);
	}
	
	public EventsPage() {
		this("tab-t0-1", "tabpanel-t0-1");
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.events.actions.Actions");
		actions.setEventListLocator("events-list-page ion-list event");
	}

	public WebElement getEventListPage(SeleniumWaiter sw) {
		return sw.waitForElement("events-list-page");
	}
	
	public WebElement getEventsCategory(SeleniumWaiter sw) {
		return sw.waitForElement("events-list-page ion-grid ion-row:nth-of-type(1)");
	}
	
	public WebElement getEvent(SeleniumWaiter sw, int index) {
		return sw.waitForElement(String.format("events-list-page ion-list event:nth-of-type(%s)", index));
	}
	
	public List<WebElement> getEvents(SeleniumWaiter sw) {
		return sw.waitForElements("events-list-page ion-list event");
	}

	public Actions getActions() {
		return actions;
	}
	
	public int getEventPosition(SeleniumWaiter sw, String eventName) {
		return actions.getEventPosition(sw, eventName);
	}
	
	/**
	 * enter into the event detail page
	 * @param driver
	 * @param sw
	 * @param eventPosition
	 * @return
	 */
	public String selectAnEvent(WebDriver driver, SeleniumWaiter sw, int eventPosition) {
		String eventIdentifier = actions.selectAnEvent(driver, sw, eventPosition);
		eventDetailPage = new EventDetailPage();
		return eventIdentifier;
	}

	public String getEventIdentifier(WebDriver driver, int eventPosition) {
		return actions.getEventIdentifier(driver, eventPosition);
	}
	
	public EventDetailPage getEventDetailPage() {
		return eventDetailPage;
	}
	
}
