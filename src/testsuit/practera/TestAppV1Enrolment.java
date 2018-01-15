package testsuit.practera;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import respositry.LocalDataBase;

import common.BuildConfig;


public class TestAppV1Enrolment extends TestEnrolment {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test a new student enrol to App v1");
	}

	@Test(description = "test a new student enrolment for App v1", groups = "practera_appv1")
	public void main() {
		driver.get(BuildConfig.practeraUrl);
		actions.login(sw, BuildConfig.appv1AdminUser, BuildConfig.appv1AdminPassword);
		super.enroll("4", "5", "2", "App V1 timeline 1", BuildConfig.appv1EnrolmentFile);
		
		BuildConfig.appv1UserName = String.format("%s@%s", studentUserName, BuildConfig.testDomain);
		LocalDataBase.getInstance().addUser(BuildConfig.appv1Id, BuildConfig.appv1UserName, regUrl);
	}

	@Override
	protected String supplyStudentInfo() {
		return actions.enrolStudentViaTextBox(sw);
	}
	
}
