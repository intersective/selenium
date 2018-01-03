package testsuit.dashboard;


import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.TestLogger;
import service.Tools;

import common.BuildConfig;


public class TestStudentRedoAssignment extends TestStudentAssignment {

	@BeforeClass
	public void setup() {
		super.setup();
		setname("test of re doing assignments for failed assessments");
	}

	@Test(description = "test of re doing assignments for failed assessments", groups = "dashboard_student_redoassginment")
	public void main() {
		
		int i = 0;
		
		i = doAssignment(i, false);
		
		i = checkTicks(i);
		
		i = checkTicks(i);
		
		i = checkTicks(i);
		
		i = doAssignment(i, false);
		
		i = checkTicks(i);
		
		Tools.forceToWait(BuildConfig.jsWaitTime);
	}

	/**
	 * 
	 * @param i
	 * @param failed - whether to use a failed answer
	 * @return
	 */
	private int doAssignment(int i, boolean failed) {
		++i;
		List<WebElement> ticks = dashboardPage.getActTicks(sw, i);
		Assert.assertTrue(ticks.get(0).getAttribute("class").contains("fa-check-circle-o"));
		Assert.assertTrue(ticks.get(1).getAttribute("class").contains("fa-check-circle-o"));
		Assert.assertTrue(ticks.get(2).getAttribute("class").contains("fa-check-circle-o"));
		Assert.assertFalse(ticks.get(3).getAttribute("class").contains("fa-check-circle-o"));
		
		addOneSubmission(i , failed);
		
		TestLogger.trace(String.format("completed assignment %s", i));
		return i;
	}

	private int checkTicks(int i) {
		++i;
		List<WebElement> ticks = dashboardPage.getActTicks(sw, i);
		for (WebElement t : ticks) {
			Assert.assertTrue(t.getAttribute("class").contains("fa-check-circle-o"));// ticked
		}
		return i;
	}
	
}
