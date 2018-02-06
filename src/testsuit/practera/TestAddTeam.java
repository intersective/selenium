package testsuit.practera;


import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.PageActionFactory;
import service.Tools;
import testsuit.TestTemplate;
import testsuit.practera.actions.Actions;

import common.BuildConfig;
import common.ElementType;


public class TestAddTeam extends TestTemplate {

	private Actions actions;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test a new student enrol to a team");
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.practera.actions.Actions");
	}
	
	@Test(description = "test a new student enrolment to a team", groups = "practera_add_team")
	public void main() {
		driver.get(BuildConfig.practeraUrl);
		actions.login(sw, BuildConfig.jobsmartAdmin, BuildConfig.jobsmartAdminPassword);
		actions.waitToastMessageDisappear(sw);
		Tools.forceToWait(3);
		
		if (!"Phase 2 2017 s2".equals(Tools.getPurifyString(sw.waitForElement("#programmenu .dropdown-toggle > .user-info")))) {
			actions.switchProgram(sw, "Phase 2 2017 s2").click();
			Tools.forceToWait(BuildConfig.jsWaitTime);
			actions.waitToastMessageDisappear(sw);
		}
		
		WebElement sideBar = actions.getSidebar(sw);
		WebElement parti = sideBar.findElement(Tools.getBy("ul.nav li.hsub:nth-of-type(4)"));
		parti.findElement(Tools.getBy(ElementType.TAGNAME, "a")).click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		parti.findElement(Tools.getBy("ul.submenu li:nth-of-type(2)")).click();// 'Teams' button within the Participants menu
		
		sw.waitForListContent("#indextbl > tbody > tr", ElementType.CSSSELECTOR, 60);// wait the team list display
		sw.waitForElement(".page-header a").click();
		
		String studentName = BuildConfig.jobsmartStudent.split("@")[0];
		sw.waitForElement("#TeamAdminAddForm", ElementType.CSSSELECTOR, 60);
		sw.waitForElement("#TeamAdminAddForm > .form-group:nth-of-type(4) input[type='text']").sendKeys(new String[] { studentName });
		sw.waitForElement(String.format("//*[@class='select2-match'][text()='%s']", studentName), ElementType.XPATH, 60).click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		sw.waitForElement("#TeamAdminAddForm > .form-actions button:nth-of-type(1)").click();
		actions.waitToastMessageDisappear(sw);
		
		actions.logout(sw, "4");
	}

}
