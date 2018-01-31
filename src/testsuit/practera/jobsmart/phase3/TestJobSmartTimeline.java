package testsuit.practera.jobsmart.phase3;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.PageActionFactory;
import service.Tools;
import testsuit.TestTemplate;
import testsuit.practera.actions.Actions;

import common.BuildConfig;
import common.ShareConfig;


public class TestJobSmartTimeline extends TestTemplate {

	private Actions actions;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test creating a timeline for phase 3");
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.practera.actions.Actions");
	}

	@Test(description = "test creating a timeline for phase 3", groups = "practera_timelinep3_create")
	public void main() {
		driver.get(BuildConfig.practeraUrl);
		actions.login(sw, BuildConfig.jobsmartAdmin, BuildConfig.jobsmartAdminPassword);
		actions.waitToastMessageDisappear(sw);
		Tools.forceToWait(3);
		
		if (!"Phase 3 2017 s2".equals(Tools.getPurifyString(sw.waitForElement("#programmenu .dropdown-toggle > .user-info")))) {
			actions.switchProgram(sw, "Phase 3 2017 s2").click();
			Tools.forceToWait(BuildConfig.jsWaitTime);
			actions.waitToastMessageDisappear(sw);
		}
		
		ShareConfig.currentTimeline = actions.createNewTimeLine(sw);
		actions.waitToastMessageDisappear(sw);
		Tools.forceToWait(3);
	}

	
}
