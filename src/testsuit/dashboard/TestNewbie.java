package testsuit.dashboard;


import java.util.ArrayList;
import java.util.List;

import model.Activity;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pom.pe.AccountPage;
import pom.pe.DashboardPage;
import respositry.LocalStore;
import service.AssignmentDataService;
import service.Tools;
import testsuit.TestTemplate;

import common.ElementType;


public class TestNewbie extends TestTemplate {

	private ArrayList<Activity> activities;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test newbie");
		activities = AssignmentDataService.getInstance().loadListDataFromJsonFiles("activities_newbie", 1, Activity.class);
	}

	@Test(description = "check activity and assignment in the dashboard for Newbie", groups = "dashboard_newbie")
	public void main() {
		AccountPage accountPage = new AccountPage();
		waitForLoadFinished();
		accountPage.getVideo(sw);
		WebElement doneBtn = accountPage.getVideoDoneBtn(sw);
		if (doneBtn != null) {
			Tools.forceToWait(5);// wait until the video loaded
			doneBtn.click();
		}
		waitForLoadFinished();
		
		DashboardPage dashboardPage = new DashboardPage();
		WebElement actPage = dashboardPage.getActListPage(sw);
		Assert.assertNotNull(actPage);
		
		List<WebElement> lis = dashboardPage.getDashboardDataContainer(sw).findElements(Tools.getBy(ElementType.TAGNAME, "li"));
		LocalStore ls = LocalStore.getInstance();
		WebElement pointsField = lis.get(1).findElement(Tools.getBy("p.number"));
		Assert.assertNotNull(pointsField);
		String points = Tools.getElementTextContent(pointsField);
		Assert.assertEquals(points, "0");
		ls.addSpinChances(points);
		
		List<WebElement> actList = dashboardPage.getActList(sw);
		Assert.assertNotNull(actList);
		Assert.assertEquals(actList.size(), activities.size());
		int total = actList.size();
		for (int i = 0; i < total; i++) {
			String activityTitle = Tools.getElementTextContent(dashboardPage.getActTitle(sw, i + 1));
			Assert.assertEquals(activityTitle, activities.get(i).getTitle());
			
			List<WebElement> ticks = dashboardPage.getActTicks(sw, i + 1);
			Assert.assertNotNull(ticks);
			Assert.assertEquals(ticks.size(), 4);
			for (WebElement t : ticks) {// initially zero mark with no ticks
				Assert.assertFalse(t.getAttribute("class").contains("fa-check-circle-o"));
			}
			
			WebElement assessmentScore = dashboardPage.getActScore(sw, i + 1);
			Assert.assertNotNull(assessmentScore);
			Assert.assertEquals(assessmentScore.findElement(Tools.getBy(ElementType.TAGNAME, "i")).getAttribute("aria-hidden"), "true");
		}
	}

}
