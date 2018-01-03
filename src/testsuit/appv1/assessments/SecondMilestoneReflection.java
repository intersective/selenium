package testsuit.appv1.assessments;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class SecondMilestoneReflection extends TestAppv1Assessment {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test appv1 second milestone reflection questions");
		setAssessmentLocation(1);
	}

	@Test(description = "test appv1 second milestone reflection questions for App v1", groups = "practera_appv1_assessment_second_milestone_reflection")
	public void main() {
	}

	@Override
	protected void checkStatus() {
	}
	
}
