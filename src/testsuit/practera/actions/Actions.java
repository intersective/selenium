package testsuit.practera.actions;


import java.io.File;
import java.util.Calendar;
import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;

import service.AssignmentDataService;
import service.PageAction;
import service.SeleniumWaiter;
import service.TestLogger;
import service.Tools;
import service.UIAction;

import common.BuildConfig;
import common.ElementType;


public class Actions implements PageAction {

	public void logout(SeleniumWaiter sw, String userMenuLocation) {
		UIAction.waitForElementVisible(sw, String.format("div.navbar-buttons > ul.nav > li:nth-of-type(%s) > a.dropdown-toggle", userMenuLocation)).click();
		UIAction.waitForElementVisible(sw, String.format("div.navbar-buttons > ul.nav > li:nth-of-type(%s) > ul.user-menu > li:nth-of-type(4) > a", userMenuLocation)).click();
		TestLogger.trace("we logout");
	}
	
	public void login(SeleniumWaiter sw, String userName, String password) {
		WebElement loginBtn = UIAction.waitForElementVisible(sw, "#main-nav > ul.nav > li:nth-of-type(2) > a");
		this.waitToastMessageDisappear(sw);
		if (loginBtn != null) {// we did not login
			loginBtn.click();
			WebElement loginForm = sw.waitForElement("form#login");
			Assert.assertNotNull(loginForm);
			WebElement loginSubmitBtn = loginForm.findElement(Tools.getBy( "button"));
			WebElement userEmail = loginForm.findElement(Tools.getBy("input#UserEmail"));
			WebElement userPassword = loginForm.findElement(Tools.getBy("input#UserPassword"));
			userEmail.sendKeys(new String[] { userName });
			userPassword.sendKeys(new String[] { password });
			loginSubmitBtn.click();
			sw.waitForDocumentReady(BuildConfig.pageWaitTime);
			this.waitToastMessageDisappear(sw);
		} else {
			TestLogger.trace("we have login");
		}
	}
	
	public WebElement getSidebar(SeleniumWaiter sw) {
		return UIAction.waitForElementVisible(sw, "#sidebar");
	}
	
	public void enterToEnrollmentPage(SeleniumWaiter sw, WebDriver driver) {
		WebElement firstRow = sw.waitForElement(".content-container .row:nth-of-type(1)");
		Assert.assertNotNull(firstRow);
		firstRow.findElement(Tools.getBy("a:nth-of-type(4)")).click();
	}
	
	public void waitToastMessageDisappear(SeleniumWaiter sw) {
		WebElement toastMessage = null;
		do {
			Tools.forceToWaitInMilli(100);
			toastMessage = sw.waitForElement(".toast-message", ElementType.CSSSELECTOR, BuildConfig.consecutiveCheckTime);
		} while (toastMessage != null);
	}
	
	public int waitForReviewContent(SeleniumWaiter sw) {
		List<WebElement> assignedSubmissions = sw.waitForListContent("div.page-content > div.content-container > div#assessments > div.tab-content > div#toreview > div.row");
		if (assignedSubmissions == null) {
			return 0;
		}
		return assignedSubmissions.size();
	}
	
	public void getResultFromDropBoxList(SeleniumWaiter sw, String searchItem) {
		sw.waitForElement("#select2-drop > .select2-search > input").sendKeys(new String[] { searchItem });
		sw.waitForElement(".select2-results> li > div > span").click();
	}
	
	public String enrolStudentViaCSV(SeleniumWaiter sw, String enrolmentFile) {
		String participantId = generateStudentId();
		String studentUserName = String.format("selenium.%s", participantId);
		String[] participantIds = new String[1];
		participantIds[0] = participantId;
		String fileName = String.format("%s%sdata%s%s", System.getProperty("user.dir"), File.separator, File.separator, enrolmentFile);
		AssignmentDataService.getInstance().buildStudentEnrolmentCSV(participantIds, fileName);
		// wait the file to write to the disk
		Tools.forceToWait(2);
		WebElement uplaodCSV = sw.waitForElement("input#fileupload[name='data[Enrolment][upload]']");
		Assert.assertNotNull(uplaodCSV);
		uplaodCSV.sendKeys(new String[] { fileName });
		Tools.forceToWait(2);
		
		return studentUserName;
	}
	
	public String enrolStudentViaTextBox(SeleniumWaiter sw) {
		String participantId = generateStudentId();
		String studentUserName = String.format("selenium.%s", participantId);
		sw.waitForElement("textArea[name='data[Enrolment][csvtext]']")
				.sendKeys(new String[] { String.format("%s@%s,%s,%s,fullaccess", studentUserName, BuildConfig.testDomain, participantId, studentUserName) });
		return studentUserName;
	}
	
	private String generateStudentId() {
		Calendar current = Calendar.getInstance();
		return String.format("%s%s%s%s%s%s", current.get(Calendar.YEAR), Tools.prependZero(current.get(Calendar.MONTH) + 1), 
				Tools.prependZero(current.get(Calendar.DAY_OF_MONTH)), Tools.prependZero(current.get(Calendar.HOUR_OF_DAY)),
				Tools.prependZero(current.get(Calendar.MINUTE)), Tools.prependZero(current.get(Calendar.SECOND)));
	}
	
	public String createNewTimeLine(SeleniumWaiter sw) {
		sw.waitForElement("//span[text()='Project']", ElementType.XPATH).click();
		sw.waitForElement("//a[@href='/admin/project/projects']", ElementType.XPATH).click();
		sw.waitForElement("#projectTab > li:nth-of-type(2) > a").click();
		sw.waitForElement("#project-list .thumbnail:nth-of-type(1) .buttons a i").click();// to create time line page
		
		Calendar now = Calendar.getInstance();
		String currentTimelineName = String.format("selenium-timeline.%s%s%s%s%s%s", now.get(Calendar.YEAR), Tools.prependZero(now.get(Calendar.MONTH) + 1),
				Tools.prependZero(now.get(Calendar.DAY_OF_MONTH)), Tools.prependZero(now.get(Calendar.HOUR_OF_DAY)), Tools.prependZero(now.get(Calendar.MINUTE)),
				Tools.prependZero(now.get(Calendar.SECOND)));
		sw.waitForElement("input#TimelineTitle").sendKeys(new String[] { currentTimelineName });
		sw.waitForElement("textArea#TimelineDescription").sendKeys(new String[] { String.format("test automation job smart time line %s", currentTimelineName) });
		sw.waitForElement("input#TimelineStartDate").sendKeys(
				new String[] { String.format("%s-%s-%s", now.get(Calendar.DAY_OF_MONTH), now.get(Calendar.MONTH) + 1, now.get(Calendar.YEAR)) });
		sw.waitForElement("textArea#TimelineDescription").click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		sw.waitForElement("button[type='submit']").click();
		return currentTimelineName;
	}
	
	public WebElement switchProgram(SeleniumWaiter sw, String programName) {
		sw.waitForElement("#programmenu .dropdown-toggle > i:nth-of-type(2)").click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		List<WebElement> programs = sw.waitForListContent("#programmenu .dropdown-menu > .dropdown-content > div:nth-of-type(2) li");
		for (WebElement p : programs) {
			if (programName.equals(Tools.getElementTextContent(p).substring(2))) {// we eliminate the starting character 'J'
				return p;
			}
		}
		return null;
	}
	
}
