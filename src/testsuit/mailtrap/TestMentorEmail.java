package testsuit.mailtrap;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.Tools;

import common.BuildConfig;
import common.ElementType;


public class TestMentorEmail extends TestMailtrap {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test mail trap to see if there are mentor emails");
	}

	@Test(description = "test mail trap to see if there are mentor emails", groups = "mailtrap_mentor")
	public void main() {
		driver.get(BuildConfig.mailtrapUrl);
		sw.waitForElement(".signin_block > a:nth-of-type(1)").click();
		
		sw.waitForElement("#new_user #user_email").sendKeys(new String[] { BuildConfig.mailtrapUser });
		sw.waitForElement("#new_user #user_password").sendKeys(new String[] { BuildConfig.mailtrapPassword });
		sw.waitForElement("#new_user input[type=submit]").click();
		
		sw.waitForElement("//*[@class='initial']/strong/a/span[text()='practera']/..", ElementType.XPATH).click();
		Tools.forceToWait(2);
		
		searchEmail(BuildConfig.appV1Mentor, String.format("been assigned to review %s", BuildConfig.appv1UserName.split("@")[0]));
	}
	
}
