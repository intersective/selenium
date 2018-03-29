package testsuit.appv1.assessments;


import model.Questionnaire;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import respositry.LocalDataBase;
import service.AssignmentDataService;
import service.Tools;
import service.UIAction;

import common.BuildConfig;
import common.ElementType;
import common.appv1.AssessmentStatus;


public class TestAppv1TeamAssessment extends TestAppv1Assessment {

	@BeforeClass
	public void setup() {
		super.setup();
		questionare = AssignmentDataService.getInstance().loadListDataFromJsonFiles("assessments_appv1_teamstudent", 1, Questionnaire.class).get(0);
		setAssessmentLocation(1);
		setname("test team assessment questions for App v1");
		setTeam(true);
	}

	@Test(description = "test team assessment questions for App v1", groups = "practera_appv1_team_assessment")
	public void main() {
		waitForLoadFinished();
		waitForClickBlock();
		findElement(".tab-nav > a:nth-of-type(2)").click();// a button can only click once in this app
		waitForLoadFinished();//we go to the activities page
		waitForClickBlock();
		Tools.forceToWait(2);
		
		sw.waitForListContent(".view-container[nav-view='active'] .card").get(1).click();
		waitForLoadFinished();
		waitForClickBlock();
		Tools.forceToWait(2);
		
		super.main();
		
		sw.waitForElement(".tab-nav > a:nth-of-type(4)").click();
		sw.waitForElement(".jsmbp-settings-container ion-item[ng-click='logout()']").click();
		sw.waitForElement(".jsmbp-login-form");
		Tools.forceToWait(120);// sometimes must wait team assessment data being actually there and assessment api can it
		
		LocalDataBase ldb = LocalDataBase.getInstance();
		String teamName = ldb.getActiveTeam();
		String[] studentNames = ldb.getTeamStudents(teamName).split(";");
		String userName = String.format("%s@%s", studentNames[1], BuildConfig.testDomain);// get first student because we login as second just now
		
		driver.get(BuildConfig.appv1Url);
		Tools.forceToWait(10);
		WebElement loginForm = UIAction.waitForElementVisible(sw, ".jsmbp-login-form");
		loginForm.findElement(Tools.getBy("input[name=uEmail]")).clear();
		loginForm.findElement(Tools.getBy("input[name=uEmail]")).sendKeys(new String [] { userName });
		loginForm.findElement(Tools.getBy("input[name=password]")).clear();
		loginForm.findElement(Tools.getBy("input[name=password]")).sendKeys(new String [] { BuildConfig.appv1UserPassword });
		loginForm.findElement(Tools.getBy("#jsmbpLoginBtn")).click();
		Assert.assertNull(sw.waitForElement("//*[@class='popup-title'][text()='Invalid Login Details']", ElementType.XPATH));
		Assert.assertEquals(Tools.getElementTextContent(sw.waitForElement(".item-content:nth-of-type(1)")), BuildConfig.appv1Program);
		findElement(".jsmbp-switch-item:nth-of-type(1)").click();
		waitForLoadFinished();
		sw.waitForElement("ion-nav-view[name='home']");
		
		waitForLoadFinished();
		waitForClickBlock();
		findElement(".tab-nav > a:nth-of-type(2)").click();// a button can only click once in this app
		waitForLoadFinished();//we go to the activities page
		waitForClickBlock();
		Tools.forceToWait(2);
		
		sw.waitForListContent(".view-container[nav-view='active'] .card").get(1).click();
		waitForLoadFinished();
		waitForClickBlock();
		Tools.forceToWait(2);
		
		setCheckAnswers(true);
		super.main();
		
		ldb.invalidateATeam(teamName);
	}

	@Override
	protected void checkStatus() {
		String status = Tools.getElementTextContent(sw.waitForElement(String
				.format(assessmentStatusLocator, getAssessmentLocation() + numberOfTopics + 1))).toLowerCase();
		Assert.assertEquals(status, AssessmentStatus.DONE.getText());

	}

}
