package testsuit.dashboard;


import java.io.File;

import model.Questionnaire;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.AssignmentDataService;
import service.Tools;

import common.BuildConfig;
import common.ElementType;


public class TestAssignment extends AssignmentTestTemplate {
	
	private Questionnaire qn;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("a test for entering some information for an assignment");
		qn = AssignmentDataService.getInstance().loadListDataFromJsonFiles("assessments_student", 7, Questionnaire.class).get(0);
	}

	@Test(description = "start a test for entering some information for an assignment", groups = "assginment", dependsOnGroups = "first")
	public void main() {
	}

	@Test(description = "enter the dashboard page", groups = "assginment", 
			dependsOnMethods = {"testsuit.dashboard.TestAssignment.main"})
	public void step2() {
		waitForLoadFinished();
		
		WebElement dashboardTab = sw.waitForElement("tab-t0-0", ElementType.ID);
		Assert.assertNotNull(dashboardTab);
		dashboardTab.click();
		waitForLoadFinished();
		Assert.assertNotNull(sw.waitForElement("#tabpanel-t0-0[aria-hidden=false]"));
	}
	
	@Test(description = "choose an activity", groups = "assginment", 
			dependsOnMethods = {"testsuit.dashboard.TestAssignment.step2"})
	public void step3() {
		Assert.assertNotNull(sw.waitForElement("activities-list-page"));
		WebElement fa = sw.waitForElement(".activity-list div:nth-of-type(2) .assessment-score button");
		Assert.assertNotNull(fa);
		fa.click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
	}
	
	@Test(description = "choose a submission or add a new submission", groups = "assginment", 
			dependsOnMethods = {"testsuit.dashboard.TestAssignment.step3"})
	public void step4() {
		startSubmission();
	}
	
	@Test(description = "choose upload documents buttons, click the upload button and upload files", groups = "assginment", 
			dependsOnMethods = {"testsuit.dashboard.TestAssignment.step4"})
	public void step5() {
		checkAssessmentInformation(qn);
		doQuesitons(0, 2, qn, false, true, false);
		String[] evidencesFiles = new String[1];
		evidencesFiles[0] = String.format("%s%s%s", BuildConfig.evidenceFolder, File.separator, BuildConfig.evidenceFile);
		uploadFile("3", evidencesFiles);
	}
	
	@Test(description = "back to the dashboard page", groups = "assginment", 
			dependsOnMethods = {"testsuit.dashboard.TestAssignment.step5"})
	public void step6() {
		WebElement saveBtn = sw.waitForElement("assessments-group-page.show-page button.btn-save");
		Assert.assertNotNull(saveBtn);
		saveBtn.click();
		waitForLoadFinished();
		waitForLoadFinished();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		WebElement backBtn = sw.waitForElement("assessments-page.show-page .back-button");
		backBtn.click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		WebElement backBtn2 = sw.waitForElement(".activity-detail .back-button");
		backBtn2.click();
		waitForLoadFinished();
	}
	
}
