package testsuit.practera;


import org.openqa.selenium.WebElement;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import respositry.LocalDataBase;
import service.PageActionFactory;
import service.Tools;
import service.UIAction;
import testsuit.TestTemplate;
import testsuit.practera.actions.Actions;
import common.BuildConfig;
import common.ElementType;


public class TestAppv1AddTeam extends TestTemplate {

	private Actions actions;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test adding students to a new team for Appv1");
		actions = (Actions) PageActionFactory.getInstance().build("testsuit.practera.actions.Actions");
	}

	@Test(description = "test adding students to a new team for Appv1", groups = "practera_addteam_appv1")
	public void main() {
		driver.get(BuildConfig.practeraUrl);
		actions.login(sw, BuildConfig.appv1AdminUser, BuildConfig.appv1AdminPassword);
		
		Tools.forceToWait(5);
		WebElement sideBar = actions.getSidebar(sw);
		WebElement parti = sideBar.findElement(Tools.getBy(String.format("ul.nav li.hsub:nth-of-type(%s)", 4)));
		parti.findElement(Tools.getBy(ElementType.TAGNAME, "a")).click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		parti.findElement(Tools.getBy("ul.submenu li:nth-of-type(2)")).click();
		
		sw.waitForListContent("#indextbl > tbody > tr", ElementType.CSSSELECTOR, 60);// wait the team list display
		sw.waitForElement(".page-header a").click();
		
		UIAction.waitForElementVisible(sw, "#TeamAdminAddForm", 120);
		LocalDataBase ldb = LocalDataBase.getInstance();
		String teamName = ldb.getActiveTeam();
		String[] studentNames = ldb.getTeamStudents(teamName).split(";");
		for (int i = 1; i < studentNames.length; i++) {
			Tools.forceToWait(BuildConfig.jsWaitTime);
			sw.waitForElement("#TeamAdminAddForm", ElementType.CSSSELECTOR, 60);
			sw.waitForElement("#TeamAdminAddForm > .form-group:nth-of-type(4) input[type='text']").sendKeys(new String[] { studentNames[i] });
			sw.waitForElement(String.format("//*[@class='select2-match'][text()='%s']", studentNames[i]), ElementType.XPATH, 60).click();
		}
		
		Tools.forceToWait(BuildConfig.jsWaitTime);
		sw.waitForElement("#TeamAdminAddForm > .form-actions button:nth-of-type(1)").click();
		actions.waitToastMessageDisappear(sw);
		
		actions.logout(sw, "3");
		Tools.forceToWait(180);// sometimes must wait team data being actually there and get team member api can get it  
	}

}
