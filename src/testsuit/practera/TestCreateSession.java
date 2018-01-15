package testsuit.practera;


import java.time.LocalDate;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.PageActionFactory;
import service.Tools;
import testsuit.TestTemplate;
import testsuit.practera.actions.Actions;

import common.BuildConfig;
import common.ElementType;
import common.ShareConfig;


public class TestCreateSession extends TestTemplate {

	private Actions actions;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test creating a session");
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.practera.actions.Actions");
	}
	
	@Test(description = "ttest creating a session", groups = "practera_session_create")
	public void main() {
		driver.get(BuildConfig.practeraUrl);
		actions.login(sw, BuildConfig.jobsmartAdmin, BuildConfig.jobsmartAdminPassword);
		actions.waitToastMessageDisappear(sw);
		Tools.forceToWait(3);
		
		ShareConfig.currentTimeline = actions.createNewTimeLine(sw);
		actions.waitToastMessageDisappear(sw);
		Tools.forceToWait(3);
		
		WebElement calendarBtn = null;
		List<WebElement> timelines = sw.waitForListContent("#tblTimeline > tbody > tr");
		for (WebElement t : timelines) {
			scrollIfNotVisible(t);
			String title = Tools.getElementTextContent(t.findElement(Tools.getBy("td:nth-of-type(1)")));
			if (ShareConfig.currentTimeline.equals(title)) {
				calendarBtn = findElement(t, ".td-actions > div > a[data-original-title='Calendar'] i");
				break;
			}
		}
		Assert.assertNotNull(calendarBtn);
		calendarBtn.click();
		Tools.forceToWait(5);
		
		String[] activities = new String[] {
				"How Job Smart helps you get job ready",
				"Job chat",
				"Job Smart Volunteer activity",
				"Final Session - Next steps to become more Job Smart" };
		for (String act : activities) {
			LocalDate date = LocalDate.now();
			String today = String.format("%s-%s-%s", date.getYear(), Tools.prependZero(date.getMonthValue()), Tools.prependZero(date.getDayOfMonth()));
			sw.waitForElements(String.format("div#calendar tbody td[data-date='%s']", today)).get(1).click();
			
			Select activityType = new Select(sw.waitForElement("div.modal[role=dialog] > .modal-dialog select#className"));
			activityType.selectByVisibleText("Activity Session");
			
			sw.waitForElement("//a[@class='select2-choice select2-default']", ElementType.XPATH);
			Tools.forceToWait(1);
			sw.waitForElement("//a[@class='select2-choice select2-default']", ElementType.XPATH).click();
			actions.getResultFromDropBoxList(sw, act);
			
			sw.waitForElement("(//input[@id='EventVisibilityParticipant'])[2]", ElementType.XPATH).click();
			sw.waitForElement("(//input[@id='location'])[2]", ElementType.XPATH).sendKeys(new String[] { "test location" });
			sw.waitForElement("(//input[@id='capacity'])[2]", ElementType.XPATH).sendKeys(new String[] { "50" });
			sw.waitForElement("//button[@class='btn btn btn-sm btn-success']", ElementType.XPATH).click();
			Tools.forceToWait(3);
		}
	}

}