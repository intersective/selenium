package testsuit.mailtrap;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.PageActionFactory;
import testsuit.mailtrap.actions.Actions;

import common.BuildConfig;


public class TestMentorEmail extends TestMailtrap {

	private Actions actions;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test mail trap to see if there are mentor emails");
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.mailtrap.actions.Actions");
	}

	@Test(description = "test mail trap to see if there are mentor emails", groups = "mailtrap_mentor")
	public void main() {
		driver.get(BuildConfig.mailtrapUrl);
		actions.login(sw, BuildConfig.mailtrapUser, BuildConfig.mailtrapPassword);
		
		searchEmail(BuildConfig.appV1Mentor, String.format("been assigned to review %s", BuildConfig.appv1UserName.split("@")[0]));
	}
	
}
