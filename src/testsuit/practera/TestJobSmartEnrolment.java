package testsuit.practera;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import respositry.LocalDataBase;
import service.Tools;

import common.BuildConfig;
import common.ShareConfig;


public class TestJobSmartEnrolment extends TestEnrolment {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test a new student enrol to Jobsmart");
	}

	@Test(description = "test a new student enrolment for Jobsmart", groups = "practera_jobsmart")
	public void main() {
		Tools.forceToWait(5);
		super.enroll("4", "5", "4", ShareConfig.currentTimeline, BuildConfig.jobsmartEnrolmentFile);
		
		BuildConfig.jobsmartStudent = String.format("%s@%s", studentUserName, BuildConfig.testDomain);
		LocalDataBase.getInstance().addUser(BuildConfig.jobsmartId, BuildConfig.jobsmartStudent, regUrl);
	}

	@Override
	protected String supplyStudentInfo() {
		return actions.enrolStudentViaTextBox(sw);
	}
	
}
