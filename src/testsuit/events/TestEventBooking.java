package testsuit.events;


import java.util.List;

import model.Event;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pom.pe.EventDetailPage;
import pom.pe.EventsPage;
import respositry.LocalDataBase;
import service.Tools;
import testsuit.TestTemplate;

import common.BuildConfig;
import common.ShareConfig;


public class TestEventBooking extends TestTemplate {

	private EventsPage eventsPage;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test event booking");
		eventsPage = new EventsPage();
	}
	
	@Test(description = "test event booking", groups = "events_action", dependsOnGroups = "eventboard_laytout")
	public void main() {
		waitForLoadFinished();
		Assert.assertNotNull(sw.waitForElement("#tabpanel-t0-1[aria-hidden=false]"));
		
		WebElement category1 = eventsPage.getEventsCategory(sw).findElement(Tools.getBy("ion-segment-button:nth-of-type(1)"));// select Browse category
		Assert.assertNotNull(category1);
		category1.click();
		Tools.forceToWait(BuildConfig.jsWaitTime);// explicit wait for JS execution completed
		Event event = LocalDataBase.getInstance().getUserEvent(ShareConfig.createdEventId);
		String eventName = event.getName();
		String isOriginal = event.getIsOriginal();
		int eventPosition = eventsPage.getEventPosition(sw, eventName);
		if ("0".equals(isOriginal)) {// is not original then check the specific logo
			WebElement eventLogo = eventsPage.getEvent(sw, eventPosition).findElement(Tools.getBy(".event-logo > img"));
			Assert.assertTrue(eventLogo.getAttribute("src").contains("recommended"));
		}
		String eventIdentifier = eventsPage.selectAnEvent(driver, sw, eventPosition);// enter into the event detail page
		EventDetailPage eventDetailPage = eventsPage.getEventDetailPage();
		
		WebElement bookBtn = eventDetailPage.getBookBtn(sw);
		if (bookBtn != null) {
			bookBtn.click();
			Tools.forceToWait(BuildConfig.jsWaitTime);// explicit wait for JS execution completed
			
			Assert.assertNotNull(eventDetailPage.getActionSheetContainer(driver));
			
			WebElement cancel = eventDetailPage.getCancelBtn(driver);
			cancel.click();
			Tools.forceToWait(BuildConfig.jsWaitTime);// explicit wait for JS execution completed
			backpress();
			Assert.assertEquals(eventsPage.getEventIdentifier(driver, eventPosition), eventIdentifier);
			eventsPage.selectAnEvent(driver, sw, eventPosition);
			eventDetailPage = eventsPage.getEventDetailPage();
			eventDetailPage.getBookBtn(sw).click();
			
			Tools.forceToWait(BuildConfig.jsWaitTime);// explicit wait for JS execution completed
			
			WebElement confirm = eventDetailPage.getConfirmBtn(driver);
			confirm.click();
			waitForLoadFinished();
			Tools.forceToWait(BuildConfig.jsWaitTime);// explicit wait for JS execution completed
			waitForLoadFinished();
			
			WebElement category12 = eventsPage.getEventsCategory(sw).findElement(Tools.getBy("ion-segment-button:nth-of-type(2)"));// select the Booking category
			category12.click();
			Tools.forceToWait(BuildConfig.jsWaitTime);// explicit wait for JS execution completed
			List<WebElement> bookedEvents = eventsPage.getEvents(sw);
			Assert.assertNotNull(bookedEvents);
			boolean found = false;
			int total = bookedEvents.size();
			for (int i = 1; i <= total && !found; i++) {
				String bookedEvent = eventsPage.getEventIdentifier(driver, i);
				if (eventIdentifier.equals(bookedEvent)) {
					found = true;
					eventPosition = i;
				}
			}
			Assert.assertTrue(found);
			
			eventsPage.selectAnEvent(driver, sw, eventPosition);
			eventDetailPage = eventsPage.getEventDetailPage();
			Tools.forceToWait(1);
			waitForLoadFinished();
			Assert.assertFalse(new Boolean(eventDetailPage.getCheckInBtn(sw).getAttribute("disabled")));// the event does not pass the end time
		}
		backpress();
	}

	private void backpress() {
		eventsPage.getEventDetailPage().back(sw);
		Tools.forceToWait(BuildConfig.jsWaitTime);// explicit wait for JS execution completed
		waitForLoadFinished();
		Tools.forceToWait(BuildConfig.jsWaitTime);
	}
	
}
