package testsuit.appv1.assessments;


import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.Tools;

import common.appv1.AssessmentStatus;


public class TestAppv1ImageFileAssessment extends TestAppv1Assessment {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test image file questions for App v1");
		setAssessmentLocation(4);
		doFileQuestions = true;
	}

	@Test(description = "test image file questions for App v1", groups = "practera_appv1_assessment_quiz_imagequestions")
	public void main() {
		super.main();
	}

	@Override
	protected void checkStatus() {
		String status = Tools.getElementTextContent(sw.waitForElement(String
				.format(".jsmbp-detail-items > div:nth-of-type(%s) > .item p[ng-if=status]", getAssessmentLocation() + 2))).toLowerCase();
		Assert.assertEquals(status, AssessmentStatus.DONE.getText());
	}
	
	
}
