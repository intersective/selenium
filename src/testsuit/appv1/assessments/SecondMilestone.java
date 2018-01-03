package testsuit.appv1.assessments;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class SecondMilestone extends TestAppv1Assessment {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test appv1 second milestone questions");
		setAssessmentLocation(0);
	}

	@Test(description = "test appv1 second milestone quiz questions for App v1", groups = "practera_appv1_assessment_second_milestone_quiz")
	public void main() {
	}

	@Override
	protected void checkStatus() {
	}
	
}
