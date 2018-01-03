package testsuit.dashboard;


import java.util.ArrayList;

import model.Questionnaire;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import respositry.LocalStore;
import service.AssignmentDataService;
import service.Tools;
import common.BuildConfig;


public class TestNewbieAssignment extends AssignmentTestTemplate {

	private ArrayList<Questionnaire> qns;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test of doing an assignment for a new student");
		qns = AssignmentDataService.getInstance().loadListDataFromJsonFiles("assessments_newbie", 1, Questionnaire.class);
	}

	@Test(description = "do an assignment for Newbie", groups = "dashboard_newbie_assginment", dependsOnGroups = "dashboard_newbie")
	public void main() {
		Questionnaire qn = qns.get(0);
		
		startActivity("1");
		startSubmission();
		int numberOfAssessments = checkAssessmentInformation(qn);
		doQuesitons(0, numberOfAssessments, qn, false, true, false);
		
		assessmentsPage.back(sw);
		waitForLoadFinished();
		Tools.forceToWait(1);
		activityDetailPage.back(sw);
		Tools.forceToWait(BuildConfig.jsWaitTime);
		waitForLoadFinished();
		
		startActivity("1");
		startSubmission();
		numberOfAssessments = checkAssessmentInformation(qn);
		doQuesitons(0, numberOfAssessments, qn, true, true, false);
		submitAssessment();
		congratulations();
		
		String spinChances = Tools.getElementTextContent(sw.waitForElement("#tab-t0-3 ion-badge"));
		Assert.assertEquals(spinChances, "1");
		LocalStore.getInstance().addSpinChances(spinChances);
	}
	
	private void submitAssessment() {
		assessmentsPage.getSubmitBtn(sw).click();
		WebElement dialog = sw.waitForElement("ion-alert[role=dialog]");
		Tools.forceToWait(BuildConfig.jsWaitTime);
		dialog.findElement(Tools.getBy(".alert-wrapper .alert-button-group button:nth-of-type(1)")).click();
		waitForLoadFinished();
	}

	private void congratulations() {
		int i = 5;// sometimes the button covered by other elements, therefore we wait a bit of time to retry
		do {
			try {
				WebElement doneBtn = sw.waitForElement("div.modal-wrapper ion-list.items-popup-data button");
				Assert.assertNotNull(doneBtn);
				doneBtn.click();
				i = 0;
			} catch (Exception e) {
				i--;
				Tools.forceToWait(BuildConfig.jsWaitTime);
			}
		} while (i > 0);
		waitForLoadFinished();
		Tools.forceToWait(BuildConfig.jsWaitTime);
	}
	
}
