package testsuit.appv1.assessments;


import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.Tools;

import common.appv1.AssessmentStatus;


public class TestAppv1FileVideoAssessment extends TestAppv1Assessment {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test file video questions for App v1");
		setAssessmentLocation(5);
		doFileQuestions = true;
	}

	@Test(description = "test file video questions for App v1", groups = "practera_appv1_assessment_quiz_filevideoquestions")
	public void main() {
		super.main();
	}

	@Override
	protected void checkStatus() {
		String status = Tools.getElementTextContent(sw.waitForElement(String
				.format(assessmentStatusLocator, getAssessmentLocation() + numberOfTopics + 1))).toLowerCase();
		Assert.assertEquals(status, AssessmentStatus.DONE.getText());
	}
	
	
}
