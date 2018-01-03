package testsuit.appv1.assessments;


import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.Tools;

import common.appv1.AssessmentStatus;


public class TestAppv1AssessmentReflection extends TestAppv1Assessment {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test appv1 reflection questions");
		setAssessmentLocation(1);
		doFileQuestions = true;
	}

	@Test(description = "test reflection questions for App v1", groups = "practera_appv1_assessment_reflection")
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
