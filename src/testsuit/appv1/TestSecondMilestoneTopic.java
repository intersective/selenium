package testsuit.appv1;


import java.util.List;

import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import service.Tools;


public class TestSecondMilestoneTopic extends TestTopic {

	@BeforeClass
	public void setup() {
		super.setup();
		setTopicFile("topics_appv1student");
		setname("test appv1 second milestone topic");
	}

	@Test(description = "test second milestone topic for App v1", groups = "practera_appv1_assessment_second_milestone_topics")
	public void main() {
		List<WebElement> mileStones = sw.waitForListContent(".view-container[nav-view='active'] .card");
		Assert.assertNotNull(mileStones);
		Tools.forceToWait(2);
		Assert.assertEquals(Tools.getElementTextContent(findElement(mileStones.get(1),"h3")), "UNLOCKED");
		mileStones.get(1).click();
		Tools.forceToWait(2);
		
		super.checkTopics();
	}
	
}
