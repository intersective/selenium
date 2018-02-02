package testsuit.practera;


import java.util.ArrayList;
import java.util.List;

import model.Questionnaire;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.AssignmentDataService;
import service.PageActionFactory;
import service.TestLogger;
import service.Tools;
import testsuit.TestTemplate;
import testsuit.practera.actions.Actions;

import com.google.common.base.Throwables;
import common.BuildConfig;
import common.ElementType;


public class TestPublishReview extends TestTemplate {

	protected ArrayList<String> assessments;
	protected Actions actions;
	private String userName;
	private String loginUserName;
	private String loginPassword;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setUserName(BuildConfig.userName.split("@")[0]);
		setLoginUserName(BuildConfig.practeraUser);
		setLoginPassword(BuildConfig.practeraPassword);
		setname("test publish reviews for student submissions");
		initAssessmentData();
		
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.practera.actions.Actions");
	}

	@Test(description = "test publish reviews for student submissions", groups = "practera_review_publish",
			dependsOnGroups = "practera_review_mentor")
	public void main() {
		driver.get(BuildConfig.practeraUrl);
		actions.login(sw, loginUserName, loginPassword);
		
		WebElement sideBar = actions.getSidebar(sw);
		WebElement project = sideBar.findElement(Tools.getBy("ul.nav li:nth-of-type(2)"));
		project.findElement(Tools.getBy(ElementType.TAGNAME, "a")).click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		project.findElement(Tools.getBy("ul.submenu li:nth-of-type(3)")).click();
		
		List<WebElement> assessmentsElements = sw.waitForListContent(".content-container > div#assessments > .tab-content > #moderated > div> table > tbody > tr");
		Assert.assertNotNull(assessmentsElements);
		Assert.assertEquals(assessmentsElements.size(), assessments.size());
		int total = assessments.size() + 1;
		
		for (int i = 1; i < total; i++) {
			WebElement assessmentElement = sw.waitForElement(String.format(".content-container > div#assessments > .tab-content > #moderated > div> table > tbody > tr:nth-of-type(%s)", i));
			assessmentElement.findElement(Tools.getBy("td:nth-of-type(5) > a")).click();// ready to publish page
			Tools.forceToWait(BuildConfig.pageWaitTime);
			
			int j = 1;
			int ctotal = getNumberOfReadytopublish();
			while (j < ctotal) {// we start to deal with each submissions and the list would update after each publish
				String one = String.format("#reviewContainer > div#assessments > .tab-content > div#readytopublish > div > table > tbody > tr:nth-of-type(%s)", j);
				if (sw.waitForElement(one) != null && userName.equals(Tools.getElementTextContent(sw.waitForElement(String.format("%s td:nth-of-type(1) > span", one))))) {
					WebElement publish = null;
					try {
						publish = sw.waitForElement(String.format("%s td:nth-of-type(5) > span:nth-of-type(2) > a", one));
					} catch (org.openqa.selenium.NoSuchElementException e) {
						TestLogger.error(Throwables.getStackTraceAsString(e));
					}
					if (publish != null) {
						// automatically accepts the window confirm window since headless mode discards the alert window
						// also we need no keyboard and mouse action here when we are running on the server platform, such as windows desktop server
						Tools.disableConfirmWindow(driver);
						Tools.forceToWait(BuildConfig.jsWaitTime);
						publish.click();
						Tools.forceToWait(3);
						j = 1;
						actions.waitToastMessageDisappear(sw);
						ctotal = getNumberOfReadytopublish();
					}
				} else {
					j++;
				}
			}
			
			Tools.runJSScript(driver, "window.scrollTo(0, -document.body.scrollHeight);");
			Tools.forceToWait(BuildConfig.jsWaitTime);// implicit wait for no reason
			sw.waitForElement(".page-header span > a").click();
		}
		
		logout();
	}

	private int getNumberOfReadytopublish() {
		List<WebElement> readytopublish = sw.waitForListContent("#reviewContainer > div#assessments > .tab-content > div#readytopublish > div > table > tbody > tr");
		int ctotal;
		if (readytopublish == null) {
			ctotal = 1;
		} else {
			ctotal = readytopublish.size() + 1;
		}
		return ctotal;
	}
	
	public void setUserName(String userName) {
		this.userName = userName;
	}

	protected void initAssessmentData() {
		assessments = new ArrayList<String>();
		ArrayList<Questionnaire> qns = AssignmentDataService.getInstance().loadListDataFromJsonFiles("assessments_student", 6, Questionnaire.class);
		for (Questionnaire q : qns) {
			assessments.add(q.getName());
		}
	}

	protected void logout() {
		actions.logout(sw, "3");
		driver.get(BuildConfig.peUrl);
	}
	
	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}
	
}
