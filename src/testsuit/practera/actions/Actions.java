package testsuit.practera.actions;


import java.awt.AWTException;
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

import com.google.common.base.Throwables;
import common.BuildConfig;
import common.ElementType;


public class Actions implements PageAction {

	public void logout(SeleniumWaiter sw, String userMenuLocation) {
		UIAction.waitForElementVisible(sw, String.format("div.navbar-buttons > ul.nav > li:nth-of-type(%s) > a.dropdown-toggle", userMenuLocation)).click();
		UIAction.waitForElementVisible(sw, String.format("div.navbar-buttons > ul.nav > li:nth-of-type(%s) > ul.user-menu > li:nth-of-type(4) > a", userMenuLocation)).click();
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
		sw.waitForElement("#select2-drop > .select2-search > input")
			.sendKeys(new String[] { searchItem });
		sw.waitForElement(".select2-results> li > div > span").click();
	}
	
	public String enrolStudentViaCSV(SeleniumWaiter sw, String enrolmentFile) {
		Calendar current = Calendar.getInstance();
		String participantId = String.format("%s%s%s%s%s", current.get(Calendar.YEAR), Tools.prependZero(current.get(Calendar.MONTH) + 1), 
				Tools.prependZero(current.get(Calendar.DAY_OF_MONTH)), Tools.prependZero(current.get(Calendar.HOUR_OF_DAY)),
				Tools.prependZero(current.get(Calendar.MINUTE)));
		String studentUserName = String.format("selenium.%s", participantId);
		String[] participantIds = new String[1];
		participantIds[0] = participantId;
		String fileName = String.format("%s%sdata%s%s", System.getProperty("user.dir"), File.separator, File.separator, enrolmentFile);
		AssignmentDataService.getInstance().buildStudentEnrolmentCSV(participantIds, fileName);
		Tools.setContentToSystemClipboard(fileName);
		WebElement uplaodCSV = sw.waitForElement("input#fileupload[name='data[Enrolment][upload]']");
		Assert.assertNotNull(uplaodCSV);
		uplaodCSV.click();
		Tools.forceToWait(2);// wait the file to write to the disk
		
		try {
			UIAction.pasteAndEnter();
		} catch (AWTException e) {
			TestLogger.error(Throwables.getStackTraceAsString(e));
		}
		return studentUserName;
	}
	
	public String enrolStudentViaTextBox(SeleniumWaiter sw) {
		Calendar current = Calendar.getInstance();
		String participantId = String.format("%s%s%s%s%s", current.get(Calendar.YEAR), Tools.prependZero(current.get(Calendar.MONTH) + 1),
				Tools.prependZero(current.get(Calendar.DAY_OF_MONTH)), Tools.prependZero(current.get(Calendar.HOUR_OF_DAY)),
				Tools.prependZero(current.get(Calendar.MINUTE)));
		String studentUserName = String.format("selenium.%s", participantId);
		sw.waitForElement("textArea[name='data[Enrolment][csvtext]']")
				.sendKeys(new String[] { String.format("%s@%s,%s,%s,fullaccess", studentUserName, BuildConfig.testDomain, participantId, studentUserName) });
		return studentUserName;
	}
	
}
