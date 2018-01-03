package testsuit.appv1.assessments;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import common.appv1.AssessmentStatus;


public class TestAppv1AssessmentModeratedStatus2 extends TestAppv1AssessmentModeratedStatus {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test appv1 moderated assessment status after review published");
		setEstatus(AssessmentStatus.PUBLISHED.getText());
	}

	@Test(description = "test appv1 moderated assessment status after review published", groups = "practera_appv1_assessment_moderated_publish_status")
	public void main() {
		super.main();
	}

}
