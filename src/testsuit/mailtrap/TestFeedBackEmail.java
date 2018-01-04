package testsuit.mailtrap;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.Tools;

import common.BuildConfig;


public class TestFeedBackEmail extends TestMailtrap {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test mail trap to see if there is a feedback available email");
	}

	@Test(description = "test mail trap to see if there is a feedback available email", groups = "mailtrap_student_feedback")
	public void main() {
		Tools.forceToWait(2);
		searchEmail(BuildConfig.appv1UserName, "Feedback available for Comprehensive Moderated");
	}
	
}
