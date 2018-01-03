package testsuit.dashboard;


import java.util.List;

import model.Questionnaire;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pom.pe.AssessmentsPage;
import pom.pe.LoginPage;
import service.AssignmentDataService;
import service.TestLogger;
import service.Tools;
import service.UIAction;
import common.BuildConfig;
import common.ElementType;


public class TestStudentPortfolio extends AssignmentTestTemplate {

	private Questionnaire qn;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test student portfolio");
		qn = AssignmentDataService.getInstance().loadListDataFromJsonFiles("assessments_portfolio", 1, Questionnaire.class).get(0);
	}

	@Test(description = "test student portfolio", groups = "dashboard_student_portfolio")
	public void main() {
		LoginPage loginPage = new LoginPage();
		if (loginPage.getLoginForm(sw) != null) {
			loginPage.login(sw, BuildConfig.userName, BuildConfig.password);
			sw.waitForDocumentReady(BuildConfig.pageWaitTime);
		}
		waitForLoadFinished();
		
		WebElement actPage = dashboardPage.getActListPage(sw);
		Assert.assertNotNull(actPage);
		
		List<WebElement> lis = dashboardPage.getDashboardDataContainer(sw).findElements(Tools.getBy(ElementType.TAGNAME, "li"));
		
		WebElement gradeField = lis.get(2).findElement(Tools.getBy("p.number"));
		Assert.assertNotNull(gradeField);
		WebElement badge = gradeField.findElement(Tools.getBy(".badge"));
		Assert.assertNotNull(badge);
		String grade = Tools.getElementTextContent(badge);
		TestLogger.info(String.format("user grade %s", grade));
		Assert.assertTrue(Double.valueOf(grade) > 0);
		badge.click();
		WebElement actionSheet = sw.waitForElement("ion-action-sheet .action-sheet-container");
		Assert.assertNotNull(actionSheet);
		WebElement cancel = UIAction.waitForElementVisible(sw, ".action-sheet-group:nth-of-type(2) > button");
		Assert.assertNotNull(cancel);
		Tools.forceToWait(3);
		cancel.click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		
		badge.click();
		actionSheet = sw.waitForElement("ion-action-sheet .action-sheet-container");
		WebElement confirm = UIAction.waitForElementVisible(sw, ".action-sheet-group:nth-of-type(1) > button");
		Assert.assertNotNull(confirm);
		Tools.forceToWait(3);
		confirm.click();
		waitForLoadFinished();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		
		assessmentsPage = new AssessmentsPage();
		checkAssessmentInformation(qn);
		doQuesitons(0, 2, qn, false, true, false);
		studentSubmitAssessment();
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
