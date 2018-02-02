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
import service.Tools;
import testsuit.TestTemplate;
import testsuit.practera.actions.Actions;

import common.BuildConfig;
import common.ElementType;


public class TestReview extends TestTemplate {

	protected ArrayList<String> assessments;
	protected Actions actions;
	private String userName;
	private String mentorName;
	private String loginUserName;
	private String loginPassword;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setUserName(BuildConfig.userName.split("@")[0]);
		setMentorName(BuildConfig.practerMentor.split("@")[0]);
		setLoginUserName(BuildConfig.practeraUser);
		setLoginPassword(BuildConfig.practeraPassword);
		setname("test assigning a mentor for reviewing student submissions");
		initAssessmentData();
		
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.practera.actions.Actions");
	}

	@Test(description = "test assigning a mentor for reviewing student submissions", groups = "practera_review_assign")
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
			WebElement assessmentElement = sw.waitForElement(String.format(".content-container > div#assessments > .tab-content > #moderated > div > table > tbody > tr:nth-of-type(%s)", i));
			assessmentElement.findElement(Tools.getBy("td:nth-of-type(3) > a")).click();// unassigned
			sw.waitForElement("#reviewContainer > div#assessments > ul#reviewTab > li:nth-of-type(2)").click();
			Tools.forceToWait(1);
			
			boolean found = false;
			do {
				found = false;
				List<WebElement> unassigned = sw.waitForListContent("#reviewContainer > div#assessments > div > div#unassigned > div > table > tbody > tr");
				if (unassigned == null) {
					unassigned = new ArrayList<WebElement>();
				}
				WebElement popover = null;
				
				int index = 0;
				
				for (WebElement one: unassigned) {
					if (userName.equals(Tools.getElementTextContent(one.findElement(Tools.getBy("td:nth-of-type(1) > span"))))) {
						found = true;
						one.findElement(Tools.getBy("td:nth-of-type(3) > span a")).click();
						popover = sw.waitForElement(one, "td:nth-of-type(3) > span > div.popover", ElementType.CSSSELECTOR, BuildConfig.jsWaitTime);
						break;
					}
					index++;
				}
				
				if (found) {
					Assert.assertNotNull(popover);
				
					popover.findElement(Tools.getBy(".popover-content input")).sendKeys(new String[] { mentorName });
					sw.waitForElement("ul.select2-results > li > div").click();
					popover.findElement(Tools.getBy(".popover-content button.editable-submit")).click();
					
					do {
						popover = sw.waitForElement(String.format("#reviewContainer > div#assessments > div > div#unassigned > div > table > tbody > tr:nth-of-type(%s) td:nth-of-type(3) > span > div.popover", ++index),
								ElementType.CSSSELECTOR, BuildConfig.consecutiveCheckTime);
					} while (popover != null);
					actions.waitToastMessageDisappear(sw);
				}
			} while (found);
			
			Tools.runJSScript(driver, "window.scrollTo(0, -document.body.scrollHeight);");
			Tools.forceToWait(2);
			sw.waitForElement(".page-header span > a").click();
		}
		
		logout();
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public void setMentorName(String mentorName) {
		this.mentorName = mentorName;
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
	}
	
	public void setLoginUserName(String loginUserName) {
		this.loginUserName = loginUserName;
	}

	public void setLoginPassword(String loginPassword) {
		this.loginPassword = loginPassword;
	}

}
