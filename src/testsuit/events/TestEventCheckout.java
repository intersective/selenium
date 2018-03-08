package testsuit.events;


import model.Event;
import model.Questionnaire;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pom.pe.AssessmentsPage;
import pom.pe.EventDetailPage;
import pom.pe.EventsPage;
import respositry.LocalDataBase;
import service.AssignmentDataService;
import service.Tools;
import testsuit.dashboard.AssignmentTestTemplate;

import common.BuildConfig;
import common.ShareConfig;


public class TestEventCheckout extends AssignmentTestTemplate {

	private EventsPage eventsPage;
	private Questionnaire qn;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test checkout a booked event");
		qn = AssignmentDataService.getInstance().loadListDataFromJsonFiles("event_assessment", 1, Questionnaire.class).get(0);
		eventsPage = new EventsPage();
	}
	
	@Test(description = "test checkout a booked event", groups = "events_action_checkout")
	public void main() {
		eventsPage.go(sw);
		waitForLoadFinished();
		Assert.assertNotNull(sw.waitForElement("#tabpanel-t0-1[aria-hidden=false]"));
		
		WebElement category13 = eventsPage.getEventsCategory(sw).findElement(Tools.getBy("ion-segment-button:nth-of-type(3)"));// select the Attended category
		Assert.assertNotNull(category13);
		category13.click();
		Tools.forceToWait(BuildConfig.jsWaitTime);// explicit wait for JS execution completed
		Event event = LocalDataBase.getInstance().getUserEvent(ShareConfig.createdEventId);
		String eventName = event.getName();
		int eventPosition = eventsPage.getEventPosition(sw, eventName);
		eventsPage.selectAnEvent(driver, sw, eventPosition);// enter into the event detail page
		EventDetailPage eventDetailPage = eventsPage.getEventDetailPage();
		
		WebElement checkInBtn = eventDetailPage.getCheckInBtn(sw);
		Assert.assertNotNull(checkInBtn);
		Assert.assertFalse(new Boolean(checkInBtn.getAttribute("disabled")));
		checkInBtn.click();
		
		WebElement clickBlock = sw.waitForElement(".click-block");
		while (clickBlock != null && clickBlock.getAttribute("class").contains("click-block-active")) {
			Tools.forceToWait(2);
			clickBlock = sw.waitForElement(".click-block");
		}
		assessmentsPage = new AssessmentsPage();
		checkAssessmentInformation(qn);
		doQuesitons(0, 1, qn, false, true, false);
		studentSubmitAssessment();
		assessmentsPage.back(sw);
		Tools.forceToWait(3);
		eventDetailPage.getCheckInBtn(sw).click();
		Tools.forceToWait(1);
		waitForLoadFinished();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		checkAssessmentInformation(qn);
		doQuesitons(0, 1, qn, true, false, false);
		assessmentsPage.back(sw);
		Tools.forceToWait(1);
		backpress();
		LocalDataBase.getInstance().updateEventStatus(ShareConfig.createdEventId);
	}

	private void backpress() {
		eventsPage.getEventDetailPage().back(sw);
		Tools.forceToWait(BuildConfig.jsWaitTime);// explicit wait for JS execution completed
		waitForLoadFinished();
		Tools.forceToWait(BuildConfig.jsWaitTime);
	}
	
	@Override
	protected void studentSubmitAssessment() {
		super.studentSubmitAssessment();
		WebElement dialog = sw.waitForElement("ion-alert[role=dialog]");
		dialog.findElement(Tools.getBy(".alert-wrapper .alert-button-group button:nth-of-type(1)")).click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		waitForLoadFinished();
	}
	
}
