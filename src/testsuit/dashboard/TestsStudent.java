package testsuit.dashboard;


import java.util.ArrayList;
import java.util.List;

import model.Activity;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pom.pe.DashboardPage;
import respositry.LocalStore;
import service.AssignmentDataService;
import service.Tools;
import testsuit.TestTemplate;

import common.ElementType;


public class TestsStudent extends TestTemplate {

	private ArrayList<Activity> activities;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test student");
		activities = AssignmentDataService.getInstance().loadListDataFromJsonFiles("activities_student", 7, Activity.class);
	}

	@Test(description = "check activity and assignment in the dashboard for a student", groups = "dashboard_student", dependsOnGroups = "first")
	public void main() {
		DashboardPage dashboardPage = new DashboardPage();
		dashboardPage.go(sw);
		waitForLoadFinished();
		Tools.forceToWait(2);
		WebElement actListPage = dashboardPage.getActListPage(sw);
		Assert.assertNotNull(actListPage);
		
		List<WebElement> lis = dashboardPage.getDashboardDataContainer(sw).findElements(Tools.getBy(ElementType.TAGNAME, "li"));
		LocalStore ls = LocalStore.getInstance();
		WebElement pointsField = lis.get(1).findElement(Tools.getBy("p.number"));
		Assert.assertNotNull(pointsField);
		String tp;
		String points = Tools.getElementTextContent(pointsField);
		if ((tp = ls.getUserPotins()) != null) {
			Assert.assertEquals(points, tp);
		} else {
			ls.addUserPoints(points);
		}
		
		List<WebElement> actList = dashboardPage.getActList(sw);
		Assert.assertNotNull(actList);
		Assert.assertEquals(actList.size(), activities.size());
		int total = actList.size() - 1;
		for (int i = 0; i < total; i++) {
			int position = i + 1;
			String activityTitle = Tools.getElementTextContent(dashboardPage.getActTitle(sw, position));
			Assert.assertEquals(activityTitle, activities.get(i).getTitle());
			
			List<WebElement> ticks = dashboardPage.getActTicks(sw, position);
			Assert.assertNotNull(ticks);
			Assert.assertEquals(ticks.size(), 4);
			for (WebElement t : ticks) {// initially zero mark with no ticks
				Assert.assertFalse(t.getAttribute("class").contains("fa-check-circle-o"));
			}
			
			WebElement assessmentScore = dashboardPage.getActScore(sw, position);
			Assert.assertNotNull(assessmentScore);
			Assert.assertEquals(assessmentScore.findElement(Tools.getBy(ElementType.TAGNAME, "i")).getAttribute("aria-hidden"), "true");
		}
		
		total++;
		List<WebElement> ticks = dashboardPage.getActTicks(sw, total);
		Assert.assertNotNull(ticks);
		Assert.assertEquals(ticks.size(), 4);
		for (WebElement t : ticks) {
			Assert.assertTrue(t.getAttribute("class").contains("fa-check-circle-o"));
		}
		WebElement score = dashboardPage.getActScore(sw, total).findElement(Tools.getBy("ion-badge"));
		Assert.assertEquals(Tools.getElementTextContent(score), "4");
	}

}
