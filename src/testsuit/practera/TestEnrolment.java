package testsuit.practera;


import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import respositry.LocalDataBase;
import respositry.LocalStore;
import service.PageActionFactory;
import service.Tools;
import testsuit.TestTemplate;
import testsuit.practera.actions.Actions;

import common.BuildConfig;
import common.ElementType;


public class TestEnrolment extends TestTemplate {

	protected Actions actions;
	protected String regUrl;
	protected String studentUserName;
	private String enrolmentFile;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test a new student enrolment");
		
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.practera.actions.Actions");
	}

	@Test(description = "test a new student enrolment", groups = "practera")
	public void main() {
		enroll("5", "5", "3", "Timeline", BuildConfig.enrolmentFile);
		
		BuildConfig.userName = String.format("%s@%s", studentUserName, BuildConfig.testDomain);
		LocalDataBase.getInstance().addUser(BuildConfig.peId, BuildConfig.userName, regUrl);
		LocalStore.getInstance().addValue("userName", BuildConfig.userName);
	}
	
	/**
	 * enroll a student to a program
	 */
	protected void enroll(String partiLocation, String regUrlLocation, String userMenuLocation, String enrolmentTimeline, String enrolmentFile) {
		Tools.forceToWait(5);
		WebElement sideBar = actions.getSidebar(sw);
		WebElement parti = sideBar.findElement(Tools.getBy(String.format("ul.nav li.hsub:nth-of-type(%s)", partiLocation)));
		parti.findElement(Tools.getBy(ElementType.TAGNAME, "a")).click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		parti.findElement(Tools.getBy("ul.submenu li:nth-of-type(1)")).click();
		
		actions.enterToEnrollmentPage(sw, driver);
		
		WebElement enrolmentAddForm = sw.waitForElement("#EnrolmentAddForm");
		Assert.assertNotNull(enrolmentAddForm);
		WebElement timelineSelect = enrolmentAddForm.findElement(Tools.getBy(".timeline-select"));
		Assert.assertNotNull(timelineSelect);
		Select timelineOptions = new Select(timelineSelect.findElement(Tools.getBy("#EnrolmentTimelineId")));
		timelineOptions.selectByVisibleText(enrolmentTimeline);
		this.enrolmentFile = enrolmentFile;
		this.studentUserName = this.supplyStudentInfo();
		Tools.forceToWait(1);
		WebElement addBtn = enrolmentAddForm.findElement(Tools.getBy(".form-actions button"));
		Assert.assertNotNull(addBtn);
		addBtn.click();
		
		WebElement progressBar = sw.waitForElement("div.progress > #process-percentage");
		while (!"100% Complete".equals(progressBar.getAttribute("innerText"))) {
			Tools.forceToWait(1);
			progressBar = sw.waitForElement("div.progress > #process-percentage");
		}
		Tools.forceToWait(1);
		sw.waitForElement(".page-header a").click();
		
		WebElement recordSearch = sw.waitForElement("#indextbl_filter");
		recordSearch.findElement(Tools.getBy(ElementType.TAGNAME, "input")).sendKeys(new String[] { studentUserName });
		Tools.forceToWait(BuildConfig.jsWaitTime);
		List<WebElement> searchResults = sw.waitForListContent("table#indextbl tbody tr");
		while (searchResults.size() != 1) {
			Tools.forceToWait(1);
			searchResults = sw.waitForListContent("table#indextbl tbody tr");
		}
		WebElement verifyLink = sw.waitForElement(String.format("table#indextbl tbody tr:nth-of-type(1) td:nth-of-type(%s) a", regUrlLocation));
		regUrl = verifyLink.getAttribute("href");
		findElement(String.format("table#indextbl tbody tr:nth-of-type(1) td:nth-of-type(%s) > div > a:nth-of-type(3)", Integer.parseInt(regUrlLocation) + 2)).click();
		actions.waitToastMessageDisappear(sw);
		Tools.forceToWait(2);
		actions.logout(sw, userMenuLocation);
	}
	
	protected String supplyStudentInfo() {
		return actions.enrolStudentViaCSV(sw, this.enrolmentFile);
	}

}
