package testsuit.appv1.assessments;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class SecondMilestoneModerated extends TestAppv1Assessment {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test appv1 second milestone moderated questions");
		setAssessmentLocation(2);
	}

	@Test(description = "test appv1 second milestone moderated questions for App v1", groups = "practera_appv1_assessment_second_milestone_moderated")
	public void main() {
	}

	@Override
	protected void checkStatus() {
	}
	
}
