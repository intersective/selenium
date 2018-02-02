package testsuit.practera;


import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import respositry.LocalDataBase;
import service.PageActionFactory;
import service.Tools;
import service.UIAction;
import testsuit.TestTemplate;
import testsuit.practera.actions.Actions;

import common.BuildConfig;
import common.ElementType;
import common.ShareConfig;


public class TestCreateEvent extends TestTemplate {

	private Actions actions;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test creating a new event");
		
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.practera.actions.Actions");
	}

	@Test(description = "test creating a new event", groups = "practera_calendar_event")
	public void main() {
		driver.get(BuildConfig.practeraUrl);
		actions.login(sw, BuildConfig.practeraUser, BuildConfig.practeraPassword);
		
		WebElement sideBar = actions.getSidebar(sw);
		WebElement project = sideBar.findElement(Tools.getBy("ul.nav li.hsub:nth-of-type(2)"));
		project.findElement(Tools.getBy(ElementType.TAGNAME, "a")).click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		project.findElement(Tools.getBy("ul.submenu li:nth-of-type(1)")).click();
		
		List<WebElement> projects = sw.waitForListContent("table#tblTimeline tbody tr");
		projects.get(0).findElement(Tools.getBy("td.td-actions > div > a:nth-of-type(2)")).click();
		Calendar current = Calendar.getInstance();
		int month = current.get(Calendar.MONTH)+1;
		int day = current.get(Calendar.DAY_OF_MONTH);
		String today = String.format("%s-%s-%s", current.get(Calendar.YEAR), Tools.prependZero(month), Tools.prependZero(day));
		sw.waitForElement("div#calendar button.fc-today-button").click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		sw.waitForElements(String.format("div#calendar tbody td[data-date='%s']", today)).get(1).click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		Select activityType = new Select(sw.waitForElement("div.modal[role=dialog] > .modal-dialog select#className"));
		activityType.selectByIndex(1);
		Select isOriginalSelect = new Select(sw.waitForElement("select#is_original"));
		if ("1".equals(BuildConfig.originalEvent)) {
			isOriginalSelect.selectByIndex(0);
		} else {
			isOriginalSelect.selectByIndex(1);// set to no
		}
		
		Tools.forceToWait(1);
		WebElement fkey = UIAction.waitForElementVisible(sw, "div.modal[role=dialog] > .modal-dialog #fkey");
		fkey.findElement(Tools.getBy("#s2id_foreign_key")).click();
		actions.getResultFromDropBoxList(sw, "newbie");
		
		WebElement dialog = getModalDialog();
		dialog.findElement(Tools.getBy("#fhas_assessment input[type=checkbox]")).click();
		sw.waitForElement("div.modal[role=dialog] > .modal-dialog #fassessment #s2id_assessment").click();
		actions.getResultFromDropBoxList(sw, "generic barry checkin assessment");
		
		dialog = getModalDialog();
		dialog.findElement(Tools.getBy("input#EventVisibilityParticipant")).click();
		dialog.findElement(Tools.getBy("input#EventVisibilityMentor")).click();
		sw.waitForElement("div.modal[role=dialog] > .modal-dialog #typeall #EventAllDay").click();// set the event to be all day
		sw.waitForElement("div.modal[role=dialog] > .modal-dialog #typeall #EventAllDay").click();// set the event not to be all day
		sw.waitForElement("div.modal[role=dialog] > .modal-dialog #fend .datetime-calendar").click();
		sw.waitForElement("div.modal[role=dialog] > .modal-dialog #fend .datetime-calendar").click();
		WebElement calendarDays = sw.waitForElement("div.modal[role=dialog] > .modal-dialog #fend .bootstrap-datetimepicker-widget .datepicker > .datepicker-days");
		calendarDays.findElement(Tools.getBy(".today")).click();// select the same day
		
		Tools.forceToWait(BuildConfig.jsWaitTime);
		Select reminder = new Select(dialog.findElement(Tools.getBy("select#EventAddNotify")));
		reminder.selectByIndex(0);
		
		String identifier = String.format("%s%s%s%s%s%s", current.get(Calendar.YEAR), current.get(Calendar.MONTH)+1, current.get(Calendar.DAY_OF_MONTH), 
	    		current.get(Calendar.HOUR_OF_DAY), current.get(Calendar.MINUTE), current.get(Calendar.SECOND));
		String eventName = String.format("Selenium Event - %s", identifier);
		dialog.findElement(Tools.getBy("input#title")).sendKeys(new String[] { eventName });
		dialog.findElement(Tools.getBy("input#location")).sendKeys(new String[] { "Sydney City" });
		dialog.findElement(Tools.getBy("textarea#description")).sendKeys(new String[] { String.format("%s description", eventName) });
		dialog.findElement(Tools.getBy("input#capacity")).sendKeys(new String[] { "100" } );
		dialog.findElement(Tools.getBy(".modal-footer > button:nth-of-type(1)")).click();
		ShareConfig.createdEventId = identifier;
		LocalDataBase.getInstance().addEvent(identifier, eventName, BuildConfig.originalEvent);
		Tools.forceToWait(5);
		
		sw.waitForElement(String.format("//*[@class='fc-title'][text()='%s']", eventName), ElementType.XPATH).click();
		Tools.forceToWait(2);
		dialog = getModalDialog();
//		String enentEndTime = Tools.getElementTextContent(dialog.findElement(Tools.getBy("#eventend div:nth-of-type(2)")));
		dialog.findElement(Tools.getBy(".modal-footer > button[data-bb-handler='close']")).click();
	}

	private WebElement getModalDialog() {
		return sw.waitForElement("div.modal[role=dialog] > .modal-dialog");
	}
	
}
