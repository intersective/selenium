package testsuit.practera;


import java.awt.AWTException;
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
import service.UIAction;
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
		project.findElement(Tools.getBy("ul.submenu li:nth-of-type(3)")).click();
		
		List<WebElement> assessmentsElements = sw.waitForListContent(".content-container > div#assessments > .tab-content > #moderated > div> table > tbody > tr");
		Assert.assertNotNull(assessmentsElements);
		Assert.assertEquals(assessmentsElements.size(), assessments.size());
		int total = assessments.size() + 1;
		
		for (int i = 1; i < total; i++) {
			WebElement assessmentElement = sw.waitForElement(String.format(".content-container > div#assessments > .tab-content > #moderated > div> table > tbody > tr:nth-of-type(%s)", i));
			assessmentElement.findElement(Tools.getBy("td:nth-of-type(4) > a")).click();
			Tools.forceToWait(1);
			
			List<WebElement> completed = sw.waitForListContent("#reviewContainer > div#assessments > .tab-content > div#complete > div > table > tbody > tr");
			Assert.assertNotNull(completed);
			int ctotal = completed.size() + 1;
			for (int j = 1; j < ctotal; j++) {
				String one = String.format(String.format("#reviewContainer > div#assessments > .tab-content > div#complete > div > table > tbody > tr:nth-of-type(%s)", j));
				if (sw.waitForElement(one) != null && userName.equals(Tools.getElementTextContent(sw.waitForElement(String.format("%s td:nth-of-type(1) > span", one))))) {
					WebElement publish = null;
					try {
						publish = sw.waitForElement(String.format("%s td:nth-of-type(5) > span:nth-of-type(2) > a", one));
					} catch (org.openqa.selenium.NoSuchElementException e) {
						TestLogger.error(Throwables.getStackTraceAsString(e));
					}
					if (publish != null) {
						if (BuildConfig.headless) {// automatically accepts the window confirm window since headless mode discards the alert window
							runJSScript("window.confirm = function(){return true;}");
							Tools.forceToWait(2);
						}
						publish.click();
						if (!BuildConfig.headless) {
							try {
								UIAction.hitEnter();// hit enter to confirm the alert window
							} catch (AWTException e) {
								e.printStackTrace();
							}
						}
						Tools.forceToWait(3);
						actions.waitToastMessageDisappear(sw);
					}
				}
			}
			
			Tools.runJSScript(driver, "window.scrollTo(0, -document.body.scrollHeight);");
			Tools.forceToWait(2);// implicit wait for no reason
			sw.waitForElement(".page-header span > a").click();
		}
		
		logout();
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
