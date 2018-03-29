package testsuit.appv1.assessments;


import model.Questionnaire;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.AssignmentDataService;
import service.Tools;

import common.appv1.AssessmentStatus;


public class TestAppv1TeamEvaluation extends TestAppv1Assessment {

	@BeforeClass
	public void setup() {
		super.setup();
		questionare = AssignmentDataService.getInstance().loadListDataFromJsonFiles("assessments_appv1_teamstudent", 1, Questionnaire.class).get(0);
		setAssessmentLocation(0);
		setname("test evaluation questions for App v1");
	}

	@Test(description = "test evaluation questions for App v1", groups = "practera_appv1_assessment_evaluation")
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
	}

	@Override
	protected void checkStatus() {
		String status = Tools.getElementTextContent(sw.waitForElement(String
				.format(assessmentStatusLocator, getAssessmentLocation() + numberOfTopics + 1))).toLowerCase();
		Assert.assertEquals(status, AssessmentStatus.DONE.getText());

	}

}
