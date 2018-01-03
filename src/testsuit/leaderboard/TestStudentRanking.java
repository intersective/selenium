package testsuit.leaderboard;


import java.util.ArrayList;

import model.Achievement;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import respositry.LocalStore;
import service.AssignmentDataService;
import service.Tools;
import testsuit.TestTemplate;

import common.BuildConfig;
import common.ElementType;


public class TestStudentRanking extends TestTemplate {

	private ArrayList<String> achievements;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test a student ranking data");
		
		achievements = AssignmentDataService.getInstance().loadListDataFromJsonFiles("achievement_student", 1, Achievement.class).get(0).getAchievements();
	}
	
	@Test(description = "test a student ranking data", groups = "studentranking", dependsOnGroups = "first")
	public void main() {
		LocalStore ls = LocalStore.getInstance();
		waitForLoadFinished();
		
		WebElement tab = sw.waitForElement("tab-t0-2", ElementType.ID);
		Assert.assertNotNull(tab);
		tab.click();
		waitForLoadFinished();
		Assert.assertNotNull(sw.waitForElement("#tabpanel-t0-2[aria-hidden=false]"));
		
		WebElement myranking = sw.waitForElement("my-ranking", ElementType.CLASSNAME);
		Assert.assertNotNull(myranking);
		WebElement iPoints = sw.waitForElement(".my-ranking .item-inner > div:nth-of-type(2)");
		Assert.assertNotNull(iPoints);
		if (ls.getUserPotins() != null) {
			Assert.assertEquals(Tools.getElementTextContent(iPoints), ls.getUserPotins());
		}
		iPoints.click();
		sw.waitForListContent(".ranking-details .list .item-block h2").forEach(
				(reward) -> Assert.assertTrue(achievements.contains(Tools.getElementTextContent(reward))));
		sw.waitForElement("rankings-details-page > .header > .toolbar > .back-button").click();
		Tools.forceToWait(BuildConfig.jsWaitTime);
		waitForLoadFinished();
	}

}
