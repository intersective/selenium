package testsuit.dashboard;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import model.Activity;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import pom.pe.DashboardPage;
import pom.pe.PortfolioPage;
import service.AssignmentDataService;
import service.Tools;
import testsuit.TestTemplate;

import common.BuildConfig;
import common.ElementType;


public class TestStudentPortfolioContent extends TestTemplate {

	private ArrayList<Activity> activities;
	
	@BeforeClass
	public void setup() {
		super.setup();
		setname("test student portfolio content");
		activities = AssignmentDataService.getInstance().loadListDataFromJsonFiles("activities_student", 6, Activity.class);
	}

	@Test(description = "test student portfolio content", groups = "dashboard_student_portfolio_content")
	public void main() {
		waitForLoadFinished();
		DashboardPage dashboardPage = new DashboardPage();
		WebElement actListPage = dashboardPage.getActListPage(sw);
		Assert.assertNotNull(actListPage);
		
		List<WebElement> lis = dashboardPage.getDashboardDataContainer(sw).findElements(Tools.getBy(ElementType.TAGNAME, "li"));
		WebElement portfolio = lis.get(2).findElement(Tools.getBy("p.number + div p"));
		Assert.assertEquals(Tools.getElementTextContent(portfolio), "View Portfolio");
		String mainWindowHandle = driver.getWindowHandle();
		portfolio.click();
		
		Iterator<String> it = driver.getWindowHandles().iterator();
		it.next();
		driver.switchTo().window(it.next());
		
		PortfolioPage portfolioPage = new PortfolioPage();
		List<WebElement> contents = portfolioPage.getContents(sw);
		List<WebElement> headers = portfolioPage.getHeaders(contents);
		Assert.assertEquals(Tools.getElementTextContent(headers.get(0)), "Digital Portfolio");
		Assert.assertEquals(Tools.getElementTextContent(headers.get(1)), BuildConfig.userName.split("@")[0]);
		ArrayList<String> activitiesName = new ArrayList<String>();
		for (Activity a : activities) {
			activitiesName.add(a.getTitle());
		}
		
		int total = contents.size();
		Assert.assertEquals(total - 2, activitiesName.size());
		for (int i = 2; i < total; i++) {
			activitiesName.contains(Tools.getElementTextContent(contents.get(i).findElement(Tools.getBy(".header > div:nth-of-type(2)"))));
		}
		
		driver.switchTo().window(mainWindowHandle);
	}

}
